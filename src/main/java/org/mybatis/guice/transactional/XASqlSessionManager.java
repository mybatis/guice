/*
 *    Copyright 2009-2017 the original author or authors.
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
import java.util.IdentityHashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;

public class XASqlSessionManager implements XAResource {
  private static final Log log = LogFactory.getLog(XASqlSessionManager.class);

  public static final int NO_TX = 0;
  public static final int STARTED = 1;
  public static final int ENDED = 2;
  public static final int PREPARED = 3;

  private SqlSessionManager sqlSessionManager;
  private int transactionTimeout;
  private String id;
  private Xid xid;
  private int state = NO_TX;

  private static ConcurrentHashMap<GlobalKey, GlobalToken> globalTokens = new ConcurrentHashMap<XASqlSessionManager.GlobalKey, XASqlSessionManager.GlobalToken>();

  public XASqlSessionManager(SqlSessionManager sqlSessionManager) {
    this.sqlSessionManager = sqlSessionManager;
    id = sqlSessionManager.getConfiguration().getEnvironment().getId();
  }

  public String getId() {
    return id;
  }

  public int getState() {
    return state;
  }

  private String xlatedState() {
    switch (state) {
      case NO_TX:
        return "NO_TX";
      case STARTED:
        return "STARTED";
      case ENDED:
        return "ENDED";
      case PREPARED:
        return "PREPARED";
      default:
        return "!invalid state (" + state + ")!";
    }
  }

  private String decodeXAResourceFlag(int flag) {
    switch (flag) {
      case XAResource.TMENDRSCAN:
        return "TMENDRSCAN";
      case XAResource.TMFAIL:
        return "TMFAIL";
      case XAResource.TMJOIN:
        return "TMJOIN";
      case XAResource.TMNOFLAGS:
        return "TMNOFLAGS";
      case XAResource.TMONEPHASE:
        return "TMONEPHASE";
      case XAResource.TMRESUME:
        return "TMRESUME";
      case XAResource.TMSTARTRSCAN:
        return "TMSTARTRSCAN";
      case XAResource.TMSUCCESS:
        return "TMSUCCESS";
      case XAResource.TMSUSPEND:
        return "TMSUSPEND";
      default:
        return "" + flag;
    }
  }

  @Override
  public int getTransactionTimeout() throws XAException {
    return transactionTimeout;
  }

  @Override
  public boolean setTransactionTimeout(int second) throws XAException {
    transactionTimeout = second;
    return true;
  }

  @Override
  public void forget(Xid xid) throws XAException {
  }

  @Override
  public Xid[] recover(int flags) throws XAException {
    return new Xid[0];
  }

  @Override
  public boolean isSameRM(XAResource xares) throws XAException {
    return this == xares;
  }

  @Override
  public void start(Xid xid, int flag) throws XAException {
    if (log.isDebugEnabled()) {
      log.debug(
          id + ": call start old state=" + xlatedState() + ", XID=" + xid + ", flag=" + decodeXAResourceFlag(flag));
    }

    if (flag != XAResource.TMNOFLAGS && flag != XAResource.TMJOIN) {
      throw new MyBatisXAException(id + ": unsupported start flag " + decodeXAResourceFlag(flag),
          XAException.XAER_RMERR);
    }

    if (xid == null) {
      throw new MyBatisXAException(id + ": XID cannot be null", XAException.XAER_INVAL);
    }

    if (state == NO_TX) {
      if (this.xid != null) {
        throw new MyBatisXAException(id + ": resource already started on XID " + this.xid, XAException.XAER_PROTO);
      } else {
        if (flag == XAResource.TMJOIN) {
          throw new MyBatisXAException(id + ": resource not yet started", XAException.XAER_PROTO);
        } else {
          if (log.isDebugEnabled()) {
            log.debug(id + ": OK to start, old state=" + xlatedState() + ", XID=" + xid + ", flag="
                + decodeXAResourceFlag(flag));
          }
          this.xid = xid;
        }
      }
    } else if (state == STARTED) {
      throw new MyBatisXAException(id + ": resource already started on XID " + this.xid, XAException.XAER_PROTO);
    } else if (state == ENDED) {
      if (flag == XAResource.TMNOFLAGS) {
        throw new MyBatisXAException(id + ": resource already registered XID " + this.xid, XAException.XAER_DUPID);
      } else {
        if (xid.equals(this.xid)) {
          if (log.isDebugEnabled()) {
            log.debug(id + ": OK to join, old state=" + xlatedState() + ", XID=" + xid + ", flag="
                + decodeXAResourceFlag(flag));
          }
        } else {
          throw new MyBatisXAException(id + ": resource already started on XID " + this.xid
              + " - cannot start it on more than one XID at a time", XAException.XAER_RMERR);
        }
      }
    } else if (state == PREPARED) {
      throw new MyBatisXAException(id + ": resource already prepared on XID " + this.xid, XAException.XAER_PROTO);
    }

    state = STARTED;
    parentSuspend(xid);
  }

  @Override
  public void end(Xid xid, int flag) throws XAException {
    if (log.isDebugEnabled()) {
      log.debug(
          id + ": call end old state=" + xlatedState() + ", XID=" + xid + " and flag " + decodeXAResourceFlag(flag));
    }

    if (flag != XAResource.TMSUCCESS && flag != XAResource.TMFAIL) {
      throw new MyBatisXAException(id + ": unsupported end flag " + decodeXAResourceFlag(flag), XAException.XAER_RMERR);
    }

    if (xid == null) {
      throw new MyBatisXAException(id + ": XID cannot be null", XAException.XAER_INVAL);
    }

    if (state == NO_TX) {
      throw new MyBatisXAException(id + ": resource never started on XID " + xid, XAException.XAER_PROTO);
    } else if (state == STARTED) {
      if (this.xid.equals(xid)) {
        if (log.isDebugEnabled()) {
          log.debug(
              id + ": OK to end, old state=" + xlatedState() + ", XID=" + xid + ", flag=" + decodeXAResourceFlag(flag));
        }
      } else {
        throw new MyBatisXAException(
            id + ": resource already started on XID " + this.xid + " - cannot end it on another XID " + xid,
            XAException.XAER_PROTO);
      }
    } else if (state == ENDED) {
      throw new MyBatisXAException(id + ": resource already ended on XID " + xid, XAException.XAER_PROTO);
    } else if (state == PREPARED) {
      throw new MyBatisXAException(id + ": cannot end, resource already prepared on XID " + xid,
          XAException.XAER_PROTO);
    }

    if (flag == XAResource.TMFAIL) {
      // Rollback transaction. After call method end() call method rollback()
      if (log.isDebugEnabled()) {
        log.debug(id + ": after end TMFAIL reset state to ENDED and roolback");
      }
    }

    this.state = ENDED;
  }

  @Override
  public int prepare(Xid xid) throws XAException {
    if (log.isDebugEnabled()) {
      log.debug(id + ": call prepare old state=" + xlatedState() + ", XID=" + xid);
    }

    if (xid == null) {
      throw new MyBatisXAException(id + ": XID cannot be null", XAException.XAER_INVAL);
    }

    if (state == NO_TX) {
      throw new MyBatisXAException(id + ": resource never started on XID " + xid, XAException.XAER_PROTO);
    } else if (state == STARTED) {
      throw new MyBatisXAException(id + ": resource never ended on XID " + xid, XAException.XAER_PROTO);
    } else if (state == ENDED) {
      if (this.xid.equals(xid)) {
        if (log.isDebugEnabled()) {
          log.debug(id + ": OK to prepare, old state=" + xlatedState() + ", XID=" + xid);
        }
      } else {
        throw new MyBatisXAException(
            id + ": resource already started on XID " + this.xid + " - cannot prepare it on another XID " + xid,
            XAException.XAER_PROTO);
      }
    } else if (state == PREPARED) {
      throw new MyBatisXAException(id + ": resource already prepared on XID " + this.xid, XAException.XAER_PROTO);
    }

    this.state = PREPARED;
    return XAResource.XA_OK;
  }

  @Override
  public void commit(Xid xid, boolean onePhase) throws XAException {
    if (log.isDebugEnabled()) {
      log.debug(id + ": call commit old state=" + xlatedState() + ", XID=" + xid + " onePhase is " + onePhase);
    }

    if (xid == null) {
      throw new MyBatisXAException(id + ": XID cannot be null", XAException.XAER_INVAL);
    }

    if (state == NO_TX) {
      throw new MyBatisXAException(id + ": resource never started on XID " + xid, XAException.XAER_PROTO);
    } else if (state == STARTED) {
      throw new MyBatisXAException(id + ": resource never ended on XID " + xid, XAException.XAER_PROTO);
    } else if (state == ENDED) {
      if (onePhase) {
        if (log.isDebugEnabled()) {
          log.debug(id + ": OK to commit with 1PC, old state=" + xlatedState() + ", XID=" + xid);
        }
      } else {
        throw new MyBatisXAException(id + ": resource never prepared on XID " + xid, XAException.XAER_PROTO);
      }
    } else if (state == PREPARED) {
      if (!onePhase) {
        if (this.xid.equals(xid)) {
          if (log.isDebugEnabled()) {
            log.debug(id + ": OK to commit, old state=" + xlatedState() + ", XID=" + xid);
          }
        } else {
          throw new MyBatisXAException(
              id + ": resource already started on XID " + this.xid + " - cannot commit it on another XID " + xid,
              XAException.XAER_PROTO);
        }
      } else {
        throw new MyBatisXAException(id + ": cannot commit in one phase as resource has been prepared on XID " + xid,
            XAException.XAER_PROTO);
      }
    }

    try {
      parentResume(xid);
    } finally {
      if (log.isDebugEnabled()) {
        log.debug(id + ": after commit reset state to NO_TX");
      }
      this.state = NO_TX;
      this.xid = null;
    }
  }

  @Override
  public void rollback(Xid xid) throws XAException {
    if (log.isDebugEnabled()) {
      log.debug(id + ": call roolback old state=" + xlatedState() + ", XID=" + xid);
    }

    if (xid == null) {
      throw new MyBatisXAException(id + ": XID cannot be null", XAException.XAER_INVAL);
    }

    if (state == NO_TX) {
      throw new MyBatisXAException(id + ": resource never started on XID " + xid, XAException.XAER_PROTO);
    } else if (state == STARTED) {
      throw new MyBatisXAException(id + ": resource never ended on XID " + xid, XAException.XAER_PROTO);
    } else if (state == ENDED) {
      if (this.xid.equals(xid)) {
        if (log.isDebugEnabled()) {
          log.debug(id + ": OK to rollback, old state=" + xlatedState() + ", XID=" + xid);
        }
      } else {
        throw new MyBatisXAException(
            id + ": resource already started on XID " + this.xid + " - cannot roll it back on another XID " + xid,
            XAException.XAER_PROTO);
      }
    } else if (state == PREPARED) {
      if (log.isDebugEnabled()) {
        log.debug(id + ": rollback reset state from PREPARED to NO_TX");
      }
      this.state = NO_TX;
      throw new MyBatisXAException(id + ": resource committed during prepare on XID " + this.xid,
          XAException.XA_HEURCOM);
    }

    try {
      parentResume(xid);
    } finally {
      if (log.isDebugEnabled()) {
        log.debug(id + ": after rollback reset state to NO_TX");
      }
      this.state = NO_TX;
      this.xid = null;
    }
  }

  private void parentSuspend(Xid xid) {
    if (log.isDebugEnabled()) {
      log.debug(id + ": suspend parent session " + xid);
    }

    byte[] trId = xid.getGlobalTransactionId();
    GlobalKey key = new GlobalKey(trId);
    GlobalToken globalToken = globalTokens.get(key);

    if (globalToken == null) {
      if (log.isDebugEnabled()) {
        log.debug(id + ": add GlobalToken " + key);
      }

      globalTokens.put(key, globalToken = new GlobalToken());
    } else {
      if (log.isDebugEnabled()) {
        log.debug(id + ": present GlobalToken " + key);
      }
    }
    globalToken.parentSuspend(id, sqlSessionManager);
  }

  private void parentResume(Xid xid) {
    if (log.isDebugEnabled()) {
      log.debug(id + ": resume parent session " + xid);
    }

    byte[] trId = xid.getGlobalTransactionId();
    GlobalKey key = new GlobalKey(trId);
    GlobalToken globalToken = globalTokens.get(key);

    if (globalToken != null) {
      globalToken.parentResume(id, sqlSessionManager);

      if (globalToken.isEmpty()) {
        if (log.isDebugEnabled()) {
          log.debug(id + ": remove GlobalToken " + key);
        }

        globalTokens.remove(key);
      } else {
        if (log.isDebugEnabled()) {
          log.debug(id + ": not remove GlobalToken " + key);
        }
      }
    } else {
      if (log.isDebugEnabled()) {
        log.debug(id + ": not find GlobalToken " + key);
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
      if (this == obj) {
        return true;
      }

      if (obj == null) {
        return false;
      }

      if (getClass() != obj.getClass()) {
        return false;
      }

      GlobalKey other = (GlobalKey) obj;
      if (!Arrays.equals(globalId, other.globalId)) {
        return false;
      }
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

      if (token == null) {
        if (log.isDebugEnabled()) {
          log.debug(id + ": add Token " + sqlSessionManager);
        }

        token = new Token(sqlSessionManager);
        tokens.put(sqlSessionManager, token);
      } else {
        if (log.isDebugEnabled()) {
          log.debug(id + ": present Token " + sqlSessionManager);
        }
      }
      token.parentSuspend(id);
    }

    void parentResume(String id, SqlSessionManager sqlSessionManager) {
      Token token = tokens.get(sqlSessionManager);

      if (token != null) {
        token.parentResume(id);

        // remove last
        if (token.isFirst()) {
          if (log.isDebugEnabled()) {
            log.debug(id + ": remove parent session " + sqlSessionManager);
          }

          tokens.remove(sqlSessionManager);
        }
      } else {
        if (log.isDebugEnabled()) {
          log.debug(id + ": not find parent session " + sqlSessionManager);
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
      } catch (Exception e) {
      }
    }

    boolean isFirst() {
      return count == 0;
    }

    void parentSuspend(String id) {
      if (isFirst()) {
        if (log.isDebugEnabled()) {
          log.debug(id + " suspend parent session");
        }

        if (localSqlSession != null) {
          suspendedSqlSession = localSqlSession.get();
          localSqlSession.remove();
        }
      } else {
        if (log.isDebugEnabled()) {
          log.debug(id + " skip suspend parent session");
        }
      }
      count++;
    }

    void parentResume(String id) {
      if (count > 0) {
        count--;
      }

      if (isFirst()) {
        if (log.isDebugEnabled()) {
          log.debug(id + " resume parent session");
        }

        if (localSqlSession != null) {
          if (suspendedSqlSession == null) {
            localSqlSession.remove();
          } else {
            localSqlSession.set(suspendedSqlSession);
            suspendedSqlSession = null;
          }
        }
      } else {
        if (log.isDebugEnabled()) {
          log.debug(id + " skip resume parent session");
        }
      }
    }
  }
}
