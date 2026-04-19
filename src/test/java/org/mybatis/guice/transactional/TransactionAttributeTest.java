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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.transaction.NotSupportedException;
import jakarta.transaction.Status;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionAttributeTest {
  @Mock
  private TransactionManager manager;
  @Mock
  private Transaction transaction;
  @Mock
  private Transaction suspended;

  @Test
  void fromValue() {
    assertEquals(TransactionAttribute.REQUIRED, TransactionAttribute.fromValue("required"));
  }

  @Test
  void mandatory() throws Exception {
    when(manager.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION);
    assertThrows(IllegalStateException.class, () -> TransactionAttribute.MANDATORY.begin(manager));

    when(manager.getStatus()).thenReturn(Status.STATUS_ACTIVE);
    when(manager.getTransaction()).thenReturn(transaction);
    TransactionToken token = TransactionAttribute.MANDATORY.begin(manager);
    assertSame(transaction, token.getActiveTransaction());
    assertEquals(TransactionAttribute.MANDATORY, token.getTransactionAttribute());
    TransactionAttribute.MANDATORY.finish(manager, token);
  }

  @Test
  void never() throws Exception {
    when(manager.getStatus()).thenReturn(Status.STATUS_ACTIVE);
    assertThrows(IllegalStateException.class, () -> TransactionAttribute.NEVER.begin(manager));

    when(manager.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION);
    TransactionToken token = TransactionAttribute.NEVER.begin(manager);
    assertNull(token.getActiveTransaction());
    assertEquals(TransactionAttribute.NEVER, token.getTransactionAttribute());
    TransactionAttribute.NEVER.finish(manager, token);
  }

  @Test
  void notSupported() throws Exception {
    when(manager.getStatus()).thenReturn(Status.STATUS_ACTIVE);
    when(manager.suspend()).thenReturn(suspended);
    TransactionToken token = TransactionAttribute.NOTSUPPORTED.begin(manager);
    assertSame(suspended, token.getSuspendedTransaction());
    TransactionAttribute.NOTSUPPORTED.finish(manager, token);
    verify(manager).resume(suspended);

    when(manager.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION);
    TransactionToken noTx = TransactionAttribute.NOTSUPPORTED.begin(manager);
    assertNull(noTx.getSuspendedTransaction());
    TransactionAttribute.NOTSUPPORTED.finish(manager, noTx);
  }

  @Test
  void required() throws Exception {
    when(manager.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION);
    when(manager.getTransaction()).thenReturn(transaction);
    TransactionToken token = TransactionAttribute.REQUIRED.begin(manager);
    assertTrue(token.isCompletionAllowed());
    TransactionAttribute.REQUIRED.finish(manager, token);
    verify(manager).begin();
    verify(manager).commit();

    when(manager.getStatus()).thenReturn(Status.STATUS_ACTIVE);
    TransactionToken inherited = TransactionAttribute.REQUIRED.begin(manager);
    assertFalse(inherited.isCompletionAllowed());
    TransactionAttribute.REQUIRED.finish(manager, inherited);

    when(manager.getStatus()).thenReturn(Status.STATUS_MARKED_ROLLBACK);
    TransactionAttribute.REQUIRED.finish(manager, token);
    verify(manager).rollback();
  }

  @Test
  void requiresNew() throws Exception {
    when(manager.getStatus()).thenReturn(Status.STATUS_ACTIVE);
    when(manager.suspend()).thenReturn(suspended);
    when(manager.getTransaction()).thenReturn(transaction);
    TransactionToken token = TransactionAttribute.REQUIRESNEW.begin(manager);
    assertSame(transaction, token.getActiveTransaction());
    assertSame(suspended, token.getSuspendedTransaction());
    assertTrue(token.isCompletionAllowed());

    when(manager.getStatus()).thenReturn(Status.STATUS_MARKED_ROLLBACK);
    TransactionAttribute.REQUIRESNEW.finish(manager, token);
    verify(manager).rollback();
    verify(manager).resume(suspended);

    when(manager.getStatus()).thenReturn(Status.STATUS_ACTIVE);
    doThrow(new NotSupportedException("nope")).when(manager).begin();
    assertThrows(NotSupportedException.class, () -> TransactionAttribute.REQUIRESNEW.begin(manager));
  }

  @Test
  void supports() throws Exception {
    when(manager.getStatus()).thenReturn(Status.STATUS_ACTIVE);
    when(manager.getTransaction()).thenReturn(transaction);
    TransactionToken active = TransactionAttribute.SUPPORTS.begin(manager);
    assertSame(transaction, active.getActiveTransaction());

    when(manager.getStatus()).thenReturn(Status.STATUS_NO_TRANSACTION);
    TransactionToken noTx = TransactionAttribute.SUPPORTS.begin(manager);
    assertNull(noTx.getActiveTransaction());
    TransactionAttribute.SUPPORTS.finish(manager, noTx);
  }

  @Test
  void requiresNewFinish_WithoutCompletionAllowed() throws Exception {
    TransactionToken token = new TransactionToken(transaction, suspended, TransactionAttribute.REQUIRESNEW, false);
    TransactionAttribute.REQUIRESNEW.finish(manager, token);
    verify(manager).resume(suspended);
  }

  @Test
  void strategyImplementations() {
    assertEquals(TransactionAttribute.MANDATORY, new MandatoryTransactionAttributeStrategy().getTransactionAttribute());
    assertEquals(TransactionAttribute.NEVER, new NeverTransactionAttributeStrategy().getTransactionAttribute());
    assertEquals(TransactionAttribute.SUPPORTS, new SupportsTransactionAttributeStrategy().getTransactionAttribute());
  }
}
