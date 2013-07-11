/*
 *    Copyright 2010-2012 The MyBatis Team
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
package org.mybatis.guice.environment;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * Provides the myBatis Environment.
 *
 * @version $Id$
 */
@Singleton
public final class EnvironmentProvider implements Provider<Environment> {

    /**
     * The environment id.
     */
    @Inject
    @Named("mybatis.environment.id")
    private String id;

    @Inject
    private TransactionFactory transactionFactory;

    @Inject
    private DataSource dataSource;

    public void setId(String id) {
        this.id = id;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    public Environment get() {
        return new Environment(id, transactionFactory, dataSource);
    }

}
