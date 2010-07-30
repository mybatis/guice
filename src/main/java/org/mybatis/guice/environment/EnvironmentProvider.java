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
package org.mybatis.guice.environment;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.transaction.TransactionFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Provides the myBatis Environment.
 *
 * @version $Id$
 */
@Singleton
public final class EnvironmentProvider implements Provider<Environment> {

    /**
     * The Environment reference.
     */
    private final Environment environment;

    /**
     * Creates a new myBatis Environment Provider.
     *
     * @param id the environment id.
     * @param transactionFactory the myBatis TransactionFactory.
     * @param dataSource the DataSource.
     */
    @Inject
    public EnvironmentProvider(@Named("mybatis.environment.id") final String id,
            final TransactionFactory transactionFactory,
            final DataSource dataSource) {
        this.environment = new Environment(id, transactionFactory, dataSource);
    }

    /**
     * {@inheritDoc}
     */
    public Environment get() {
        return this.environment;
    }

}
