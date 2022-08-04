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

import org.apache.ibatis.session.TransactionIsolationLevel;

/**
 * Enum of isolation levels. This enum exists because Java annotations do not support null default values - so we need
 * to add the DEFAULT level which means - do not specify an isolation level.
 *
 * @author Jeff Butler
 *
 * @since 3.1
 */
public enum Isolation {

  DEFAULT(null),

  NONE(TransactionIsolationLevel.NONE),

  READ_COMMITTED(TransactionIsolationLevel.READ_COMMITTED),

  READ_UNCOMMITTED(TransactionIsolationLevel.READ_UNCOMMITTED),

  REPEATABLE_READ(TransactionIsolationLevel.REPEATABLE_READ),

  SERIALIZABLE(TransactionIsolationLevel.SERIALIZABLE);

  private final TransactionIsolationLevel transactionIsolationLevel;

  private Isolation(TransactionIsolationLevel transactionIsolationLevel) {
    this.transactionIsolationLevel = transactionIsolationLevel;
  }

  public TransactionIsolationLevel getTransactionIsolationLevel() {
    return transactionIsolationLevel;
  }

}
