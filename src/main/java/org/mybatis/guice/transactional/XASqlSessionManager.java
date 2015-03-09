/*
 *    Copyright 2010-2015 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.transactional;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.geronimo.transaction.manager.NamedXAResource;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;

public class XASqlSessionManager implements XAResource, NamedXAResource {
    private static final Log log = LogFactory.getLog(XASqlSessionManager.class);

    private SqlSessionManager sqlSessionManager;
	private int transactionTimeout;
	private String id;

	private static HashMap<GlobalKey, GlobalToken> globalTokens = new HashMap<XASqlSessionManager.GlobalKey, XASqlSessionManager.GlobalToken>();

	public XASqlSessionManager(SqlSessionManager sqlSessionManager) {
		this.sqlSessionManager = sqlSessionManager;
		id = sqlSessionManager.getConfiguration().getEnvironment().getId();
	}

	//@Override
	public String getName() {
		return id;
	}

	//@Override
	public void commit(Xid xid, boolean flag) throws XAException {
		if(log.isDebugEnabled()) {
			log.debug(id + " commit flag:" + flag + " " + xid);
		}
		parentResume(xid);
	}

	//@Override
	public void end(Xid xid, int flag) throws XAException {
		if(log.isDebugEnabled()) {
			log.debug(id + " end flag:" + printFlag(flag) + " " + xid);
		}

		// TODO XAResource.TMSUSPEND ?
	}

	//@Override
	public void forget(Xid xid) throws XAException {
		if(log.isDebugEnabled()) {
			log.debug(id + " forget " + xid);
		}
	}

	//@Override
	public int getTransactionTimeout() throws XAException {
		return transactionTimeout;
	}

	//@Override
	public boolean isSameRM(XAResource xares) throws XAException {
		return this == xares;
	}

	//@Override
	public int prepare(Xid xid) throws XAException {
		if(log.isDebugEnabled()) {
			log.debug(id + " prepare " + xid);
		}
		return 0;
	}

	//@Override
	public Xid[] recover(int flag) throws XAException {
		if(log.isDebugEnabled()) {
			log.debug(id + " recover flag:" + printFlag(flag));
		}

		// TODO return empty array or null? or current xid?
		return new Xid[0];
	}

	//@Override
	public void rollback(Xid xid) throws XAException {
		if(log.isDebugEnabled()) {
			log.debug(id + " rollback " + xid);
		}

		parentResume(xid);
	}

	//@Override
	public boolean setTransactionTimeout(int second) throws XAException {
		transactionTimeout = second;
		return true;
	}

	//@Override
	public void start(Xid xid, int flag) throws XAException {
		if(log.isDebugEnabled()) {
			log.debug(id + " start flag:" + printFlag(flag) + " " + xid);
		}

		if(flag == XAResource.TMNOFLAGS) {
			parentSuspend(xid);
		}
		// TODO XAResource.TMRESUME ?
	}

	private String printFlag(int flag) {
		switch(flag) {
		case XAResource.TMENDRSCAN: return "TMENDRSCAN";
		case XAResource.TMFAIL: return "TMFAIL";
		case XAResource.TMJOIN: return "TMJOIN";
		case XAResource.TMNOFLAGS: return "TMNOFLAGS";
		case XAResource.TMONEPHASE: return "TMONEPHASE";
		case XAResource.TMRESUME: return "TMRESUME";
		case XAResource.TMSTARTRSCAN: return "TMSTARTRSCAN";
		case XAResource.TMSUCCESS: return "TMSUCCESS";
		case XAResource.TMSUSPEND: return "TMSUSPEND";
		default: return "" + flag;
		}
	}

	private void parentSuspend(Xid xid) {
		if(log.isDebugEnabled()) {
			log.debug(id + " suspend parent session " + xid);
		}

		byte[] trId = xid.getGlobalTransactionId();
		GlobalKey key = new GlobalKey(trId);
		GlobalToken globalToken = globalTokens.get(key);

		if(globalToken == null) {
			if(log.isDebugEnabled()) {
				log.debug(id + " add GlobalToken " + key);
			}

			globalTokens.put(key, globalToken = new GlobalToken());
		} else {
			if(log.isDebugEnabled()) {
				log.debug(id + " present GlobalToken " + key);
			}
		}
		globalToken.parentSuspend(id, sqlSessionManager);
	}

	private void parentResume(Xid xid) {
		if(log.isDebugEnabled()) {
			log.debug(id + " resume parent session " + xid);
		}

		byte[] trId = xid.getGlobalTransactionId();
		GlobalKey key = new GlobalKey(trId);
		GlobalToken globalToken = globalTokens.get(key);

		if(globalToken != null) {
			globalToken.parentResume(id, sqlSessionManager);

			if(globalToken.isEmpty()) {
				if(log.isDebugEnabled()) {
					log.debug(id + " remove GlobalToken " + key);
				}

				globalTokens.remove(key);
			} else {
				if(log.isDebugEnabled()) {
					log.debug(id + " not remove GlobalToken " + key);
				}
			}
		} else {
			if(log.isDebugEnabled()) {
				log.debug(id + " not find GlobalToken " + key);
			}
		}
	}

	static class GlobalKey {
		final byte[] globalId;
		final int arrayHash;

		public GlobalKey(byte[] globalId) {
			this.globalId = globalId;
			this.arrayHash = Arrays.hashCode(globalId);
		}

		@Override
		public int hashCode() {
			return arrayHash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GlobalKey other = (GlobalKey) obj;
			if (!Arrays.equals(globalId, other.globalId))
				return false;
			return true;
		}

		@Override
		public String toString() {
	        StringBuilder s = new StringBuilder();
	        s.append("[Xid:globalId=");
	        for (int i = 0; i < globalId.length; i++) {
	            s.append(Integer.toHexString(globalId[i]));
	        }
	        s.append(",length=").append(globalId.length);
			return s.toString();
		}
	}

	static class GlobalToken {
	    private final Log log = LogFactory.getLog(getClass());
		IdentityHashMap<SqlSessionManager, Token> tokens = new IdentityHashMap<SqlSessionManager, XASqlSessionManager.Token>();

		public GlobalToken() {
		}

		void parentSuspend(String id, SqlSessionManager sqlSessionManager) {
			Token token = tokens.get(sqlSessionManager);

			if(token == null) {
				if(log.isDebugEnabled()) {
					log.debug(id + " add Token " + sqlSessionManager);
				}

				token = new Token(sqlSessionManager);
				tokens.put(sqlSessionManager, token);
			} else {
				if(log.isDebugEnabled()) {
					log.debug(id + " present Token " + sqlSessionManager);
				}
			}
			token.parentSuspend(id);
		}

		void parentResume(String id, SqlSessionManager sqlSessionManager) {
			Token token = tokens.get(sqlSessionManager);

			if(token != null) {
				token.parentResume(id);

				// remove last
				if(token.isFirst()) {
					if(log.isDebugEnabled()) {
						log.debug(id + " remove parent session " + sqlSessionManager);
					}

					tokens.remove(sqlSessionManager);
				}
			} else {
				if(log.isDebugEnabled()) {
					log.debug(id + " not find parent session " + sqlSessionManager);
				}
			}
		}

		boolean isEmpty() {
			return tokens.isEmpty();
		}
	}

	static class Token {
	    private final Log log = LogFactory.getLog(getClass());
	    final SqlSessionManager sqlSessionManager;
		ThreadLocal<SqlSession> localSqlSession;
		SqlSession suspendedSqlSession;
		int count;

		@SuppressWarnings("unchecked")
		public Token(SqlSessionManager sqlSessionManager) {
			this.sqlSessionManager = sqlSessionManager;
			this.count = 0;
			try {
				Field field = SqlSessionManager.class.getDeclaredField("localSqlSession");
				field.setAccessible(true);
				localSqlSession = (ThreadLocal<SqlSession>) field.get(sqlSessionManager);
			} catch(Exception e) {
			}
		}

		boolean isFirst() {
			return count == 0;
		}

		void parentSuspend(String id) {
			if(isFirst()) {
				if(log.isDebugEnabled()) {
					log.debug(id + " suspend parent session");
				}

				if(localSqlSession != null) {
					suspendedSqlSession = localSqlSession.get();
					localSqlSession.remove();
				}
			} else {
				if(log.isDebugEnabled()) {
					log.debug(id + " skip suspend parent session");
				}
			}
			count++;
		}

		void parentResume(String id) {
			if(count > 0)
				count--;

			if(isFirst()) {
				if(log.isDebugEnabled()) {
					log.debug(id + " resume parent session");
				}

				if(localSqlSession != null) {
					if(suspendedSqlSession == null) {
						localSqlSession.remove();
					} else {
						localSqlSession.set(suspendedSqlSession);
						suspendedSqlSession = null;
					}
				}
			} else {
				if(log.isDebugEnabled()) {
					log.debug(id + " skip resume parent session");
				}
			}
		}
	}
}
