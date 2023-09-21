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
package org.mybatis.guice.jta.simple;

import com.arjuna.ats.arjuna.recovery.RecoveryManager;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.security.NamePrincipal;
import io.agroal.api.security.SimplePassword;
import io.agroal.narayana.NarayanaTransactionIntegration;

import jakarta.transaction.TransactionManager;
import jakarta.transaction.TransactionSynchronizationRegistry;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.mybatis.guice.multidstest.MockInitialContextFactory;

public class Utils {

  /**
   * This method sets up a mock JNDI context with a transaction manager and two XA datasources
   *
   * @throws Exception
   */
  public static void setupMockJNDI() throws Exception {
    Properties properties = new Properties();
    properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());
    InitialContext ic = new InitialContext(properties);

    TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();
    TransactionSynchronizationRegistry transactionSynchronizationRegistry = new com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionSynchronizationRegistryImple();
    // intitialization of recovery manager
    RecoveryManager recoveryManager = com.arjuna.ats.arjuna.recovery.RecoveryManager.manager();
    recoveryManager.initialize();

    ic.bind("javax.transaction.TransactionManager", tm);

    DataSource ads1 = AgroalDataSource
        .from(new AgroalDataSourceConfigurationSupplier().connectionPoolConfiguration(cp -> cp.maxSize(10)
            .connectionFactoryConfiguration(cf -> cf.jdbcUrl("jdbc:hsqldb:mem:schema1")
                .principal(new NamePrincipal("sa")).credential(new SimplePassword("")))
            .transactionIntegration(
                new NarayanaTransactionIntegration(tm, transactionSynchronizationRegistry, "java:/agroalds1", false))));

    ic.bind("java:comp/env/jdbc/DS1", ads1);

    DataSource ads2 = AgroalDataSource
        .from(new AgroalDataSourceConfigurationSupplier().connectionPoolConfiguration(cp -> cp.maxSize(10)
            .connectionFactoryConfiguration(cf -> cf.jdbcUrl("jdbc:hsqldb:mem:schema2")
                .principal(new NamePrincipal("sa")).credential(new SimplePassword("")))
            .transactionIntegration(
                new NarayanaTransactionIntegration(tm, transactionSynchronizationRegistry, "java:/agroalds2", false))));

    ic.bind("java:comp/env/jdbc/DS2", ads2);
  }
}
