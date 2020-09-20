/**
 *    Copyright 2009-2020 the original author or authors.
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
package org.mybatis.guice.multidstest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.google.inject.name.Names;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.JndiDataSourceProvider;

public class MultiJNDITest {

  @Test
  public void testMultiDSWithJNDIWithProviderType() throws Exception {
    setupJNDI();
    Injector injector = setupInjectorWithProviderType();
    Schema1Service schema1Service = injector.getInstance(Schema1Service.class);
    schema1Service.createSchema1();
    Integer int1 = schema1Service.getNextValueFromSchema1();
    assertEquals(100, int1.intValue());

    Schema2Service schema2Service = injector.getInstance(Schema2Service.class);
    schema2Service.createSchema2();
    Integer int2 = schema2Service.getNextValueFromSchema2();
    assertEquals(200, int2.intValue());
  }

  @Test
  public void testMultiDSWithJNDIWithProvider() throws Exception {
    setupJNDI();
    Injector injector = setupInjectorWithProvider();
    Schema1Service schema1Service = injector.getInstance(Schema1Service.class);
    schema1Service.createSchema1();
    Integer int1 = schema1Service.getNextValueFromSchema1();
    assertEquals(100, int1.intValue());

    Schema2Service schema2Service = injector.getInstance(Schema2Service.class);
    schema2Service.createSchema2();
    Integer int2 = schema2Service.getNextValueFromSchema2();
    assertEquals(200, int2.intValue());
  }

  private void setupJNDI() throws NamingException {
    Properties properties = new Properties();
    properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());
    InitialContext ic = new InitialContext(properties);

    JDBCDataSource ds1 = new JDBCDataSource();
    ds1.setDatabaseName("schema1");
    ds1.setUser("sa");
    ds1.setUrl("jdbc:hsqldb:mem:schema1");

    ic.bind("java:comp/env/jdbc/DS1", ds1);

    JDBCDataSource ds2 = new JDBCDataSource();
    ds2.setDatabaseName("schema2");
    ds2.setUser("sa");
    ds2.setUrl("jdbc:hsqldb:mem:schema2");

    ic.bind("java:comp/env/jdbc/DS2", ds2);
  }

  private Injector setupInjectorWithProvider() {
    Injector injector = Guice.createInjector(new PrivateModule() {
      @Override
      protected void configure() {
        install(new MyBatisModule() {

          @Override
          protected void initialize() {
            bindTransactionFactoryType(JdbcTransactionFactory.class);

            JndiDataSourceProvider provider = new JndiDataSourceProvider("java:comp/env/jdbc/DS1");
            provider.setEnvInitialContextFactory(MockInitialContextFactory.class.getName());
            bindDataSourceProvider(provider);

            Properties connectionProps = new Properties();
            connectionProps.setProperty("mybatis.environment.id", "jndi");

            Names.bindProperties(binder(), connectionProps);

            addMapperClass(Schema1Mapper.class);
            bind(Schema1Service.class);
          }
        });

        expose(Schema1Service.class);
      }
    }, new PrivateModule() {

      @Override
      protected void configure() {
        install(new MyBatisModule() {

          @Override
          protected void initialize() {
            bindTransactionFactoryType(JdbcTransactionFactory.class);

            JndiDataSourceProvider provider = new JndiDataSourceProvider("java:comp/env/jdbc/DS2");
            provider.setEnvInitialContextFactory(MockInitialContextFactory.class.getName());
            bindDataSourceProvider(provider);

            Properties connectionProps = new Properties();
            connectionProps.setProperty("mybatis.environment.id", "jndi");

            Names.bindProperties(binder(), connectionProps);

            addMapperClass(Schema2Mapper.class);
            bind(Schema2Service.class);

          }
        });

        expose(Schema2Service.class);
      }
    });

    return injector;
  }

  /**
   * This method fails because the properties are not injected into the JndiDataSourceProvider.
   *
   * @return
   */
  private Injector setupInjectorWithProviderType() {
    Injector injector = Guice.createInjector(new PrivateModule() {
      @Override
      protected void configure() {
        install(new MyBatisModule() {

          @Override
          protected void initialize() {
            bindTransactionFactoryType(JdbcTransactionFactory.class);
            bindDataSourceProviderType(JndiDataSourceProvider.class);

            Properties connectionProps = new Properties();
            connectionProps.setProperty("mybatis.environment.id", "jndi");
            connectionProps.setProperty("jndi.dataSource", "java:comp/env/jdbc/DS1");
            connectionProps.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());

            Names.bindProperties(binder(), connectionProps);

            addMapperClass(Schema1Mapper.class);
            bind(Schema1Service.class);
          }
        });

        expose(Schema1Service.class);
      }
    }, new PrivateModule() {

      @Override
      protected void configure() {
        install(new MyBatisModule() {

          @Override
          protected void initialize() {
            bindTransactionFactoryType(JdbcTransactionFactory.class);
            bindDataSourceProviderType(JndiDataSourceProvider.class);

            Properties connectionProps = new Properties();
            connectionProps.setProperty("mybatis.environment.id", "jndi");
            connectionProps.setProperty("jndi.dataSource", "java:comp/env/jdbc/DS2");
            connectionProps.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());

            Names.bindProperties(binder(), connectionProps);

            addMapperClass(Schema2Mapper.class);
            bind(Schema2Service.class);

          }
        });

        expose(Schema2Service.class);
      }
    });

    return injector;
  }
}
