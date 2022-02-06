/*
 *    Copyright 2009-2022 the original author or authors.
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
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionalMethodInterceptorTest {
  private TransactionalMethodInterceptor transactionalMethodInterceptor;
  @Mock
  private MethodInvocation invocation;
  @Mock
  private SqlSessionManager sqlSessionManager;

  @BeforeEach
  public void beforeTest() {
    transactionalMethodInterceptor = new TransactionalMethodInterceptor();
    transactionalMethodInterceptor.setSqlSessionManager(sqlSessionManager);
  }

  @Test
  void invoke() throws Throwable {
    Method method = MethodAnnotation.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenReturn(true);

    Object returned = transactionalMethodInterceptor.invoke(invocation);

    assertEquals(true, returned);
    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager).commit(transactional.force());
    verify(sqlSessionManager, never()).rollback();
    verify(sqlSessionManager, never()).rollback(anyBoolean());
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_CustomAnnotation() throws Throwable {
    Method method = MethodAnnotationCustom.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenReturn(true);

    Object returned = transactionalMethodInterceptor.invoke(invocation);

    assertEquals(true, returned);
    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager).commit(transactional.force());
    verify(sqlSessionManager, never()).rollback();
    verify(sqlSessionManager, never()).rollback(anyBoolean());
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_RollbackOnly() throws Throwable {
    Method method = MethodAnnotationRollbackOnly.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenReturn(true);

    Object returned = transactionalMethodInterceptor.invoke(invocation);

    assertEquals(true, returned);
    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(transactional.force());
    verify(sqlSessionManager).rollback(true);
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_RuntimeException() throws Throwable {
    Method method = MethodAnnotation.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenThrow(new RuntimeException("test"));

    try {
      transactionalMethodInterceptor.invoke(invocation);
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // Success.
    }

    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(anyBoolean());
    verify(sqlSessionManager).rollback(true);
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_IllegalStateException() throws Throwable {
    Method method = MethodAnnotation.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenThrow(new IllegalStateException("test"));

    try {
      transactionalMethodInterceptor.invoke(invocation);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Success.
    }

    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(anyBoolean());
    verify(sqlSessionManager).rollback(true);
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_IOException_Declared() throws Throwable {
    Method method = MethodAnnotationIOException.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenThrow(new IOException("test"));

    try {
      transactionalMethodInterceptor.invoke(invocation);
      fail("Expected IOException");
    } catch (IOException e) {
      // Success.
    }

    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(anyBoolean());
    verify(sqlSessionManager).rollback(true);
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_IOException_Undeclared() throws Throwable {
    Method method = MethodAnnotation.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenThrow(new IOException("test"));

    try {
      transactionalMethodInterceptor.invoke(invocation);
      fail("Expected IOException");
    } catch (IOException e) {
    }

    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(anyBoolean());
    verify(sqlSessionManager).rollback(true);
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_RuntimeException_Rethrow() throws Throwable {
    Method method = MethodAnnotationRethrow.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    Exception exception = new RuntimeException("test");
    when(invocation.proceed()).thenThrow(exception);

    try {
      transactionalMethodInterceptor.invoke(invocation);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertEquals("test message", e.getMessage());
      assertEquals(exception, e.getCause());
    }

    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(anyBoolean());
    verify(sqlSessionManager).rollback(true);
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_IllegalStateException_Rethrow() throws Throwable {
    Method method = MethodAnnotationRethrow.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    Exception exception = new IllegalStateException("test");
    when(invocation.proceed()).thenThrow(exception);

    try {
      transactionalMethodInterceptor.invoke(invocation);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertEquals("test message", e.getMessage());
      assertEquals(exception, e.getCause());
    }

    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(anyBoolean());
    verify(sqlSessionManager).rollback(true);
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_UnsupportedOperationException_Rethrow() throws Throwable {
    Method method = MethodAnnotationRethrow.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    Exception exception = new UnsupportedOperationException("test");
    when(invocation.proceed()).thenThrow(exception);

    try {
      transactionalMethodInterceptor.invoke(invocation);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertEquals("test", e.getMessage());
      assertEquals(exception, e);
    }

    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(anyBoolean());
    verify(sqlSessionManager).rollback(true);
    verify(sqlSessionManager).close();
  }

  @Test
  void invoke_SessionInherited() throws Throwable {
    Method method = MethodAnnotation.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenReturn(true);
    when(sqlSessionManager.isManagedSessionStarted()).thenReturn(true);

    Object returned = transactionalMethodInterceptor.invoke(invocation);

    assertEquals(true, returned);
    verify(sqlSessionManager, never()).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(transactional.force());
    verify(sqlSessionManager, never()).rollback();
    verify(sqlSessionManager, never()).rollback(anyBoolean());
    verify(sqlSessionManager, never()).close();
  }

  @Test
  void invoke_SessionInherited_Exception() throws Throwable {
    Method method = MethodAnnotation.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = method.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenThrow(new RuntimeException("test"));
    when(sqlSessionManager.isManagedSessionStarted()).thenReturn(true);

    try {
      transactionalMethodInterceptor.invoke(invocation);
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // Success.
    }

    verify(sqlSessionManager, never()).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager, never()).commit();
    verify(sqlSessionManager, never()).commit(transactional.force());
    verify(sqlSessionManager, never()).rollback();
    verify(sqlSessionManager, never()).rollback(anyBoolean());
    verify(sqlSessionManager, never()).close();
  }

  @Test
  void invoke_ClassAnnotation() throws Throwable {
    Method method = ClassAnnotation.class.getMethod("transaction");
    when(invocation.getMethod()).thenReturn(method);
    Transactional transactional = ClassAnnotation.class.getAnnotation(Transactional.class);
    when(invocation.proceed()).thenReturn(true);

    Object returned = transactionalMethodInterceptor.invoke(invocation);

    assertEquals(true, returned);
    verify(sqlSessionManager).startManagedSession(transactional.executorType(),
        transactional.isolation().getTransactionIsolationLevel());
    verify(sqlSessionManager).commit(transactional.force());
    verify(sqlSessionManager, never()).rollback();
    verify(sqlSessionManager, never()).rollback(anyBoolean());
    verify(sqlSessionManager).close();
  }

  private static class MethodAnnotation {
    @Transactional
    public void transaction() {
    }
  }

  private static class MethodAnnotationCustom {
    @Transactional(executorType = ExecutorType.REUSE, isolation = Isolation.REPEATABLE_READ, force = true)
    public void transaction() {
    }
  }

  private static class MethodAnnotationIOException {
    @Transactional()
    public void transaction() throws IOException {
    }
  }

  private static class MethodAnnotationRethrow {
    @Transactional(rethrowExceptionsAs = UnsupportedOperationException.class, exceptionMessage = "test message")
    public void transaction() {
    }
  }

  private static class MethodAnnotationRollbackOnly {
    @Transactional(rollbackOnly = true)
    public void transaction() {
    }
  }

  @Transactional
  private static class ClassAnnotation {
    @SuppressWarnings("unused")
    public void transaction() {
    }
  }
}
