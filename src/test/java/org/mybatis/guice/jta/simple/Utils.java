/**
 *    Copyright 2009-2015 the original author or authors.
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
package org.mybatis.guice.jta.simple;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.aries.transaction.AriesTransactionManager;
import org.apache.aries.transaction.internal.AriesTransactionManagerImpl;
import org.apache.aries.transaction.jdbc.RecoverableDataSource;
import org.hsqldb.jdbc.pool.JDBCXADataSource;
import org.mybatis.guice.multidstest.MockInitialContextFactory;

public class Utils {

    /**
     * This method sets up a mock JNDI context with a transaction manager
     * and two XA datasources
     * 
     * @throws Exception
     */
    public static void setupMockJNDI() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                MockInitialContextFactory.class.getName());
        InitialContext ic = new InitialContext(properties);
        
        AriesTransactionManager tm = new AriesTransactionManagerImpl();
        ic.bind("javax.transaction.TransactionManager", tm);
        
        JDBCXADataSource ds1 = new JDBCXADataSource();
        ds1.setDatabaseName("schema1");
        ds1.setUser("sa");
        ds1.setPassword("");
        ds1.setUrl("jdbc:hsqldb:mem:schema1");
        
        RecoverableDataSource rds1 = new RecoverableDataSource();
        rds1.setDataSource(ds1);
        rds1.setUsername("sa");
        rds1.setPassword("");
        rds1.setTransactionManager(tm);
        rds1.setTransaction("xa");
        rds1.setName("DS1");
        rds1.start();
        
        ic.bind("java:comp/env/jdbc/DS1", rds1);

        JDBCXADataSource ds2 = new JDBCXADataSource();
        ds2.setDatabaseName("schema2");
        ds2.setUser("sa");
        ds2.setPassword("");
        ds2.setUrl("jdbc:hsqldb:mem:schema2");

        RecoverableDataSource rds2 = new RecoverableDataSource();
        rds2.setDataSource(ds2);
        rds2.setUsername("sa");
        rds2.setPassword("");
        rds2.setTransactionManager(tm);
        rds2.setTransaction("xa");
        rds2.setName("DS2");
        rds2.start();

        ic.bind("java:comp/env/jdbc/DS2", rds2);
    }
}
