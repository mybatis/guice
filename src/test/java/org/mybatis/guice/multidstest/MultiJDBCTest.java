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
package org.mybatis.guice.multidstest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.google.inject.name.Names;

import java.util.Properties;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

class MultiJDBCTest {

  @Test
  void testMultiDSWithJDBC() {
    Injector injector = setupInjector();
    Schema1Service schema1Service = injector.getInstance(Schema1Service.class);
    schema1Service.createSchema1();
    Integer int1 = schema1Service.getNextValueFromSchema1();
    assertEquals(100, int1.intValue());

    Schema2Service schema2Service = injector.getInstance(Schema2Service.class);
    schema2Service.createSchema2();
    Integer int2 = schema2Service.getNextValueFromSchema2();
    assertEquals(200, int2.intValue());
  }

  private Injector setupInjector() {
    Injector injector = Guice.createInjector(new PrivateModule() {

      @Override
      protected void configure() {
        install(new MyBatisModule() {

          @Override
          protected void initialize() {
            bindDataSourceProviderType(PooledDataSourceProvider.class);
            bindTransactionFactoryType(JdbcTransactionFactory.class);

            install(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);

            Properties connectionProps = new Properties();
            connectionProps.setProperty("mybatis.environment.id", "jdbc");
            connectionProps.setProperty("JDBC.username", "sa");
            connectionProps.setProperty("JDBC.password", "");
            connectionProps.setProperty("JDBC.schema", "schema1");
            connectionProps.setProperty("JDBC.autoCommit", "false");

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
            bindDataSourceProviderType(PooledDataSourceProvider.class);
            bindTransactionFactoryType(JdbcTransactionFactory.class);

            install(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);

            Properties connectionProps = new Properties();
            connectionProps.setProperty("mybatis.environment.id", "jdbc");
            connectionProps.setProperty("JDBC.username", "sa");
            connectionProps.setProperty("JDBC.password", "");
            connectionProps.setProperty("JDBC.schema", "schema2");
            connectionProps.setProperty("JDBC.autoCommit", "false");

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
