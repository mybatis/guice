/*
 *    Copyright 2009-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.transactional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XASqlSessionManagerTest {

  @SuppressWarnings("unchecked")
  @BeforeEach
  void beforeTest() throws Exception {
    Field field = XASqlSessionManager.class.getDeclaredField("globalTokens");
    field.setAccessible(true);
    ConcurrentHashMap<XASqlSessionManager.GlobalKey, XASqlSessionManager.GlobalToken> map = (ConcurrentHashMap<XASqlSessionManager.GlobalKey, XASqlSessionManager.GlobalToken>) field
        .get(null);
    map.clear();
  }

  @Test
  void timeoutRecoverSameResource() throws Exception {
    XASqlSessionManager manager = newManager("db1");
    assertTrue(manager.setTransactionTimeout(15));
    assertEquals(15, manager.getTransactionTimeout());
    assertArrayEquals(new Xid[0], manager.recover(XAResource.TMSTARTRSCAN));
    assertTrue(manager.isSameRM(manager));
    assertFalse(manager.isSameRM(newManager("db2")));
    manager.forget(newXid(new byte[] { 1 }, new byte[] { 2 }, 1));
  }

  @Test
  void startEndPrepareCommitCycle() throws Exception {
    XASqlSessionManager manager = newManager("db1");
    Xid xid = newXid(new byte[] { 1, 2, 3 }, new byte[] { 9 }, 10);

    manager.start(xid, XAResource.TMNOFLAGS);
    assertEquals(XASqlSessionManager.STARTED, manager.getState());
    manager.end(xid, XAResource.TMSUCCESS);
    assertEquals(XASqlSessionManager.ENDED, manager.getState());
    assertEquals(XAResource.XA_OK, manager.prepare(xid));
    assertEquals(XASqlSessionManager.PREPARED, manager.getState());
    manager.commit(xid, false);
    assertEquals(XASqlSessionManager.NO_TX, manager.getState());
  }

  @Test
  void rollbackPaths() throws Exception {
    XASqlSessionManager onePhase = newManager("db1");
    Xid xid = newXid(new byte[] { 4, 5 }, new byte[] { 7 }, 11);
    onePhase.start(xid, XAResource.TMNOFLAGS);
    onePhase.end(xid, XAResource.TMFAIL);
    onePhase.rollback(xid);
    assertEquals(XASqlSessionManager.NO_TX, onePhase.getState());

    XASqlSessionManager prepared = newManager("db1");
    prepared.start(xid, XAResource.TMNOFLAGS);
    prepared.end(xid, XAResource.TMSUCCESS);
    prepared.prepare(xid);
    MyBatisXAException e = assertThrows(MyBatisXAException.class, () -> prepared.rollback(xid));
    assertEquals(XAException.XA_HEURCOM, e.errorCode);
    assertEquals(XASqlSessionManager.NO_TX, prepared.getState());
  }

  @Test
  void invalidTransitionsThrowProtocolErrors() throws Exception {
    XASqlSessionManager manager = newManager("db1");
    Xid xid = newXid(new byte[] { 1 }, new byte[] { 2 }, 2);
    Xid other = newXid(new byte[] { 3 }, new byte[] { 4 }, 3);

    assertXaError(XAException.XAER_RMERR, () -> manager.start(xid, XAResource.TMSUCCESS));
    assertXaError(XAException.XAER_INVAL, () -> manager.start(null, XAResource.TMNOFLAGS));

    manager.start(xid, XAResource.TMNOFLAGS);
    assertXaError(XAException.XAER_PROTO, () -> manager.start(xid, XAResource.TMNOFLAGS));
    assertXaError(XAException.XAER_PROTO, () -> manager.end(other, XAResource.TMSUCCESS));

    manager.end(xid, XAResource.TMSUCCESS);
    assertXaError(XAException.XAER_DUPID, () -> manager.start(xid, XAResource.TMNOFLAGS));
    manager.start(xid, XAResource.TMJOIN);
    assertEquals(XASqlSessionManager.STARTED, manager.getState());
    manager.end(xid, XAResource.TMSUCCESS);
    assertXaError(XAException.XAER_RMERR, () -> manager.start(other, XAResource.TMJOIN));

    assertXaError(XAException.XAER_RMERR, () -> manager.end(xid, XAResource.TMNOFLAGS));
    assertXaError(XAException.XAER_INVAL, () -> manager.end(null, XAResource.TMSUCCESS));

    manager.prepare(xid);
    assertXaError(XAException.XAER_PROTO, () -> manager.prepare(xid));
    assertXaError(XAException.XAER_PROTO, () -> manager.commit(xid, true));
    assertXaError(XAException.XAER_PROTO, () -> manager.commit(other, false));
    assertXaError(XAException.XA_HEURCOM, () -> manager.rollback(xid));
  }

  @Test
  void commitAndPrepareValidation() throws Exception {
    XASqlSessionManager manager = newManager("db1");
    Xid xid = newXid(new byte[] { 8 }, new byte[] { 9 }, 8);

    assertXaError(XAException.XAER_PROTO, () -> manager.prepare(xid));
    manager.start(xid, XAResource.TMNOFLAGS);
    assertXaError(XAException.XAER_PROTO, () -> manager.prepare(xid));
    assertXaError(XAException.XAER_PROTO, () -> manager.commit(xid, false));
    manager.end(xid, XAResource.TMSUCCESS);
    assertXaError(XAException.XAER_PROTO, () -> manager.commit(xid, false));
    manager.commit(xid, true);
    assertEquals(XASqlSessionManager.NO_TX, manager.getState());
  }

  @Test
  void nestedTypes() {
    byte[] id = new byte[] { 1, 2, 3 };
    XASqlSessionManager.GlobalKey key = new XASqlSessionManager.GlobalKey(id);
    XASqlSessionManager.GlobalKey same = new XASqlSessionManager.GlobalKey(new byte[] { 1, 2, 3 });
    XASqlSessionManager.GlobalKey other = new XASqlSessionManager.GlobalKey(new byte[] { 1, 2, 4 });

    assertEquals(key, same);
    assertEquals(key.hashCode(), same.hashCode());
    assertNotEquals(key, other);
    assertNotEquals(key, null);
    assertNotEquals(key, "x");
    assertTrue(key.toString().contains("length=3"));

    SqlSessionManager sqlSessionManager = mock(SqlSessionManager.class);
    XASqlSessionManager.GlobalToken globalToken = new XASqlSessionManager.GlobalToken();
    assertTrue(globalToken.isEmpty());

    globalToken.parentResume("db1", sqlSessionManager);
    globalToken.parentSuspend("db1", sqlSessionManager);
    assertFalse(globalToken.isEmpty());
    globalToken.parentSuspend("db1", sqlSessionManager);
    globalToken.parentResume("db1", sqlSessionManager);
    assertFalse(globalToken.isEmpty());
    globalToken.parentResume("db1", sqlSessionManager);
    assertTrue(globalToken.isEmpty());

    XASqlSessionManager.Token token = new XASqlSessionManager.Token(sqlSessionManager);
    assertTrue(token.isFirst());
    token.parentSuspend("db1");
    assertFalse(token.isFirst());
    token.parentResume("db1");
    assertTrue(token.isFirst());
  }

  @Test
  void getId() throws Exception {
    assertEquals("env", newManager("env").getId());
  }

  private XASqlSessionManager newManager(String id) throws Exception {
    SqlSessionManager sqlSessionManager = mock(SqlSessionManager.class);
    Configuration configuration = mock(Configuration.class);
    Environment environment = mock(Environment.class);
    when(sqlSessionManager.getConfiguration()).thenReturn(configuration);
    when(configuration.getEnvironment()).thenReturn(environment);
    when(environment.getId()).thenReturn(id);
    return new XASqlSessionManager(sqlSessionManager);
  }

  private static Xid newXid(byte[] globalId, byte[] branchId, int formatId) {
    return new Xid() {
      @Override
      public int getFormatId() {
        return formatId;
      }

      @Override
      public byte[] getGlobalTransactionId() {
        return globalId;
      }

      @Override
      public byte[] getBranchQualifier() {
        return branchId;
      }
    };
  }

  private static void assertXaError(int expectedErrorCode, XaCallable callable) {
    XAException e = assertThrows(XAException.class, callable::run);
    assertEquals(expectedErrorCode, e.errorCode);
  }

  @FunctionalInterface
  private interface XaCallable {
    void run() throws XAException;
  }
}
