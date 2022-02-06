/*
 *    Copyright 2009-2021 the original author or authors.
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
package org.mybatis.guice.customsqlsessionfactory;

import static com.google.inject.name.Names.bindProperties;
import static org.junit.jupiter.api.Assertions.*;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

public class CustomSqlSessionFactoryTest {
  protected Properties createTestProperties() {
    final Properties myBatisProperties = new Properties();
    myBatisProperties.setProperty("mybatis.environment.id", "test");
    myBatisProperties.setProperty("JDBC.username", "sa");
    myBatisProperties.setProperty("JDBC.password", "");
    myBatisProperties.setProperty("JDBC.autoCommit", "false");
    return myBatisProperties;
  }

  @Test
  public void customSqlSessionFactoryProvider() throws Exception {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        install(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);
        bindProperties(binder(), createTestProperties());

        useSqlSessionFactoryProvider(MySqlSessionFactoryProvider.class);
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
      }
    });
    SqlSessionFactory sqlSessionFactory = injector.getInstance(SqlSessionFactory.class);
    assertTrue(MySqlSessionFactory.class.isAssignableFrom(sqlSessionFactory.getClass()),
        "SqlSessionFactory not an instanceof MySqlSessionFactory");
  }
}
