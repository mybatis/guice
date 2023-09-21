/*
 *    Copyright 2009-2023 the original author or authors.
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

import jakarta.transaction.Transaction;

public class TransactionToken {
  private Transaction activeTransaction;
  private Transaction suspendedTransaction;
  private TransactionAttribute transactionAttribute;
  private boolean isCompletionAllowed;

  public TransactionToken(Transaction activeTransaction, Transaction suspendedTransaction,
      TransactionAttribute transactionAttribute) {
    this(activeTransaction, suspendedTransaction, transactionAttribute, false);
  }

  TransactionToken(Transaction activeTransaction, Transaction suspendedTransaction,
      TransactionAttribute transactionAttribute, boolean isCompletionAllowed) {
    this.activeTransaction = activeTransaction;
    this.suspendedTransaction = suspendedTransaction;
    this.transactionAttribute = transactionAttribute;
    this.isCompletionAllowed = isCompletionAllowed;
  }

  public Transaction getActiveTransaction() {
    return activeTransaction;
  }

  public void setActiveTransaction(Transaction activeTransaction) {
    this.activeTransaction = activeTransaction;
  }

  public Transaction getSuspendedTransaction() {
    return suspendedTransaction;
  }

  public void setSuspendedTransaction(Transaction suspendedTransaction) {
    this.suspendedTransaction = suspendedTransaction;
  }

  public TransactionAttribute getTransactionAttribute() {
    return transactionAttribute;
  }

  public void setTransactionStrategy(TransactionAttribute transactionAttribute) {
    this.transactionAttribute = transactionAttribute;
  }

  public boolean isCompletionAllowed() {
    return isCompletionAllowed;
  }

  public void setCompletionAllowed(boolean isCompletionAllowed) {
    this.isCompletionAllowed = isCompletionAllowed;
  }
}
