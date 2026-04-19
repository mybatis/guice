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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.transaction.Status;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.transaction.xa.XAResource;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TxTransactionalMethodInterceptorTest {

  private TxTransactionalMethodInterceptor interceptor;
  private TransactionManager manager;
  private Transaction transaction;
  private XAResource xaResource;

  @BeforeEach
  void beforeTest() throws Exception {
    interceptor = new TxTransactionalMethodInterceptor();
    manager = mock(TransactionManager.class);
    transaction = mock(Transaction.class);
    xaResource = mock(XAResource.class);

    setField(interceptor, "manager", manager);
    setField(interceptor, "xaResourceProvider", (jakarta.inject.Provider<XAResource>) () -> xaResource);
  }

  @Test
  void invoke_WithRequiredTransaction_Commits() throws Throwable {
    Method method = RequiredMethod.class.getMethod("tx");
    MethodInvocation invocation = mock(MethodInvocation.class);
    when(invocation.getMethod()).thenReturn(method);
    when(invocation.proceed()).thenReturn("ok");
    when(manager.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION, Status.STATUS_ACTIVE);
    when(manager.getTransaction()).thenReturn(transaction);
    when(transaction.enlistResource(xaResource)).thenReturn(true);

    Object result = interceptor.invoke(invocation);

    assertEquals("ok", result);
    verify(manager).begin();
    verify(transaction).enlistResource(xaResource);
    verify(manager).commit();
  }

  @Test
  void invoke_WithRollbackOnlyAnnotation_RollsBack() throws Throwable {
    Method method = RollbackOnlyMethod.class.getMethod("tx");
    MethodInvocation invocation = mock(MethodInvocation.class);
    when(invocation.getMethod()).thenReturn(method);
    when(invocation.proceed()).thenReturn("ok");
    when(manager.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION, Status.STATUS_MARKED_ROLLBACK);
    when(manager.getTransaction()).thenReturn(transaction);
    when(transaction.enlistResource(xaResource)).thenReturn(true);

    interceptor.invoke(invocation);

    verify(manager).setRollbackOnly();
    verify(manager).rollback();
  }

  @Test
  void invoke_WithRuntimeException_MarksRollbackAndRethrows() throws Throwable {
    Method method = RequiredMethod.class.getMethod("tx");
    MethodInvocation invocation = mock(MethodInvocation.class);
    RuntimeException failure = new RuntimeException("failure");
    when(invocation.getMethod()).thenReturn(method);
    when(invocation.proceed()).thenThrow(failure);
    when(manager.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION, Status.STATUS_MARKED_ROLLBACK);
    when(manager.getTransaction()).thenReturn(transaction);
    when(transaction.enlistResource(xaResource)).thenReturn(true);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> interceptor.invoke(invocation));
    assertEquals("failure", thrown.getMessage());
    verify(manager).setRollbackOnly();
    verify(manager).rollback();
  }

  @Test
  void invoke_WithoutManager_SkipsTxHandling() throws Throwable {
    setField(interceptor, "manager", null);

    Method method = RequiredMethod.class.getMethod("tx");
    MethodInvocation invocation = mock(MethodInvocation.class);
    when(invocation.getMethod()).thenReturn(method);
    when(invocation.proceed()).thenReturn("ok");

    assertEquals("ok", interceptor.invoke(invocation));
  }

  @Test
  void invoke_WithoutManager_UsesClassLevelAnnotation() throws Throwable {
    setField(interceptor, "manager", null);

    Method method = ClassLevelRequiredMethod.class.getMethod("tx");
    MethodInvocation invocation = mock(MethodInvocation.class);
    when(invocation.getMethod()).thenReturn(method);
    when(invocation.proceed()).thenReturn("ok");

    assertEquals("ok", interceptor.invoke(invocation));
  }

  @Test
  void invoke_WithoutManager_RethrowsInvocationError() throws Throwable {
    setField(interceptor, "manager", null);

    Method method = RequiredMethod.class.getMethod("tx");
    MethodInvocation invocation = mock(MethodInvocation.class);
    when(invocation.getMethod()).thenReturn(method);
    when(invocation.proceed()).thenThrow(new IllegalStateException("boom"));

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> interceptor.invoke(invocation));
    assertEquals("boom", thrown.getMessage());
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static class RequiredMethod {
    @Transactional(Transactional.TxType.REQUIRED)
    public String tx() {
      return "ok";
    }
  }

  private static class RollbackOnlyMethod {
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOnly = true)
    public String tx() {
      return "ok";
    }
  }

  @Transactional(Transactional.TxType.REQUIRED)
  private static class ClassLevelRequiredMethod {
    public String tx() {
      return "ok";
    }
  }
}
