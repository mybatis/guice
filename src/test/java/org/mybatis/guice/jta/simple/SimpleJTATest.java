/**
 *    Copyright 2009-2017 the original author or authors.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.TransactionManager;

import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.guice.MyBatisJtaModule;
import org.mybatis.guice.datasource.builtin.JndiDataSourceProvider;
import org.mybatis.guice.multidstest.MockInitialContextFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.google.inject.name.Names;

public class SimpleJTATest {

  private CombinedService combinedService;

  @Before
  public void setup() throws Exception {
    // this sets up a mocked JNDI environment.  In JEE containers all this would be
    // configured in the container
    Utils.setupMockJNDI();

    Injector injector = getInjector();
    combinedService = injector.getInstance(CombinedService.class);

    // create the schemas in the memory databases
    Schema1Service schema1Service = injector.getInstance(Schema1Service.class);
    Schema2Service schema2Service = injector.getInstance(Schema2Service.class);
    schema1Service.createSchema1();
    schema2Service.createSchema2();
  }

  @Test
  public void testRollBack() {
    try {
      combinedService.insert2RecordsIntoSchema1And1RecordIntoSchema2AndRollbackAll();
      fail("Expected an exception to force rollback");
    } catch (Exception e) {
      // ignore - expected
    }

    assertEquals(0, combinedService.getAllNamesFromSchema1().size());
    assertEquals(0, combinedService.getAllNamesFromSchema2().size());
  }

  @Test
  public void testInserts() {
    combinedService.insert2RecordsIntoSchema1And1RecordIntoSchema2();
    assertEquals(2, combinedService.getAllNamesFromSchema1().size());
    assertEquals(1, combinedService.getAllNamesFromSchema2().size());
  }

  @Test
  public void testPartialRollBack() {
    try {
      combinedService.insert2RecordsIntoSchema1And1RecordIntoSchema2AndRollbackSchema2();
      fail("Expected an exception to force rollback");
    } catch (Exception e) {
      // ignore - expected
    }

    assertEquals(2, combinedService.getAllNamesFromSchema1().size());
    assertEquals(0, combinedService.getAllNamesFromSchema2().size());
  }

  private Injector getInjector() throws Exception {
    Properties properties = new Properties();

    // Use the mock JNDI environment (not required in a JEE container)
    properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());

    InitialContext ic = new InitialContext(properties);
    final TransactionManager tm = (TransactionManager) ic.lookup("javax.transaction.TransactionManager");

    return Guice.createInjector(new PrivateModule() {
      @Override
      protected void configure() {
        install(new MyBatisJtaModule(tm) {
          @Override
          protected void initialize() {
            environmentId("DS1");
            bindTransactionFactoryType(ManagedTransactionFactory.class);
            bindDataSourceProviderType(JndiDataSourceProvider.class);
            Properties jndiProperties = new Properties();
            jndiProperties.put("jndi.dataSource", "java:comp/env/jdbc/DS1");

            // Use the mock JNDI environment (not required in a JEE container)
            jndiProperties.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());

            Names.bindProperties(binder(), jndiProperties);

            addMapperClass(Schema1Mapper.class);
            bind(Schema1Service.class);
          }
        });

        expose(Schema1Service.class);
      }
    }, new PrivateModule() {
      @Override
      protected void configure() {
        install(new MyBatisJtaModule(tm) {
          @Override
          protected void initialize() {
            environmentId("DS2");
            bindTransactionFactoryType(ManagedTransactionFactory.class);
            bindDataSourceProviderType(JndiDataSourceProvider.class);
            Properties jndiProperties = new Properties();
            jndiProperties.put("jndi.dataSource", "java:comp/env/jdbc/DS2");

            // Use the mock JNDI environment (not required in a JEE container)
            jndiProperties.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());

            Names.bindProperties(binder(), jndiProperties);

            addMapperClass(Schema2Mapper.class);
            bind(Schema2Service.class);
            bind(CombinedService.class);
          }
        });

        expose(Schema2Service.class);
        expose(CombinedService.class);
      }
    });
  }
}
