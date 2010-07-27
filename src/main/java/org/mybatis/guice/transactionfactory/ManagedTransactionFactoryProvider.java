/*
 *    Copyright 2010 The myBatis Team
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
package org.mybatis.guice.transactionfactory;

import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Provides the default built-in iBatis JDBC Transaction Factory.
 *
 * @version $Id$
 */
@Singleton
public final class ManagedTransactionFactoryProvider implements Provider<TransactionFactory> {

    /**
     * The default built-in iBatis JDBC Transaction Factory reference.
     */
    private final TransactionFactory transactionFactory = new ManagedTransactionFactory();

    /**
     * {@inheritDoc}
     */
    public TransactionFactory get() {
        return this.transactionFactory;
    }

}
