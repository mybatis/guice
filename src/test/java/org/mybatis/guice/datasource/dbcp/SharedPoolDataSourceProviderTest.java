/**
 *    Copyright 2009-2019 the original author or authors.
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
package org.mybatis.guice.datasource.dbcp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;

import javax.sql.ConnectionPoolDataSource;

@ExtendWith(MockitoExtension.class)
public class SharedPoolDataSourceProviderTest {
  @Mock
  private ClassLoader driverClassLoader;
  @Mock
  private ConnectionPoolDataSource connectionPoolDataSource;

  @Test
  public void get() throws Throwable {
    final boolean autoCommit = true;
    final int loginTimeout = 10;
    final boolean defaultReadOnly = true;
    final int defaultTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
    final String description = "test_description";
    final int defaultMinEvictableIdleTimeMillis = 30;
    final int defaultNumTestsPerEvictionRun = 40;
    final boolean rollbackAfterValidation = true;
    final boolean defaultTestOnBorrow = true;
    final boolean defaultTestOnReturn = true;
    final boolean defaultTestWhileIdle = true;
    final int defaultTimeBetweenEvictionRunsMillis = 50;
    final String validationQuery = "SELECT 1";
    final int defaultMaxTotal = 60;
    final int defaultMaxIdle = 70;
    final int defaultMaxWaitMillis = 80;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ConnectionPoolDataSource.class).toInstance(connectionPoolDataSource);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);
        bindConstant().annotatedWith(Names.named("DBCP.defaultReadOnly")).to(defaultReadOnly);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTransactionIsolation")).to(defaultTransactionIsolation);
        bindConstant().annotatedWith(Names.named("DBCP.description")).to(description);
        bindConstant().annotatedWith(Names.named("DBCP.defaultMinEvictableIdleTimeMillis"))
            .to(defaultMinEvictableIdleTimeMillis);
        bindConstant().annotatedWith(Names.named("DBCP.defaultNumTestsPerEvictionRun"))
            .to(defaultNumTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("DBCP.rollbackAfterValidation")).to(rollbackAfterValidation);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTestOnBorrow")).to(defaultTestOnBorrow);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTestOnReturn")).to(defaultTestOnReturn);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTestWhileIdle")).to(defaultTestWhileIdle);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTimeBetweenEvictionRunsMillis"))
            .to(defaultTimeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("DBCP.validationQuery")).to(validationQuery);
        bindConstant().annotatedWith(Names.named("DBCP.defaultMaxTotal")).to(defaultMaxTotal);
        bindConstant().annotatedWith(Names.named("DBCP.defaultMaxIdle")).to(defaultMaxIdle);
        bindConstant().annotatedWith(Names.named("DBCP.defaultMaxWaitMillis")).to(defaultMaxWaitMillis);
      }
    });
    SharedPoolDataSourceProvider provider = injector.getInstance(SharedPoolDataSourceProvider.class);

    SharedPoolDataSource dataSource = (SharedPoolDataSource) provider.get();

    assertEquals(connectionPoolDataSource, dataSource.getConnectionPoolDataSource());
    assertEquals(autoCommit, dataSource.isDefaultAutoCommit());
    assertEquals(defaultReadOnly, dataSource.isDefaultReadOnly());
    assertEquals(defaultTransactionIsolation, dataSource.getDefaultTransactionIsolation());
    assertEquals(description, dataSource.getDescription());
    assertEquals(loginTimeout, dataSource.getLoginTimeout());
    assertEquals(defaultMinEvictableIdleTimeMillis, dataSource.getDefaultMinEvictableIdleTimeMillis());
    assertEquals(defaultNumTestsPerEvictionRun, dataSource.getDefaultNumTestsPerEvictionRun());
    assertEquals(rollbackAfterValidation, dataSource.isRollbackAfterValidation());
    assertEquals(defaultTestOnBorrow, dataSource.getDefaultTestOnBorrow());
    assertEquals(defaultTestOnReturn, dataSource.getDefaultTestOnReturn());
    assertEquals(defaultTestWhileIdle, dataSource.getDefaultTestWhileIdle());
    assertEquals(defaultTimeBetweenEvictionRunsMillis, dataSource.getDefaultTimeBetweenEvictionRunsMillis());
    assertEquals(validationQuery, dataSource.getValidationQuery());
    assertEquals(defaultMaxTotal, dataSource.getDefaultMaxTotal());
    assertEquals(defaultMaxIdle, dataSource.getDefaultMaxIdle());
    assertEquals(defaultMaxWaitMillis, dataSource.getDefaultMaxWaitMillis());
  }

  @Test
  public void get_OtherValues() throws Throwable {
    final boolean autoCommit = false;
    final int loginTimeout = 11;
    final boolean defaultReadOnly = false;
    final int defaultTransactionIsolation = Connection.TRANSACTION_REPEATABLE_READ;
    final String description = "test_description2";
    final int defaultMinEvictableIdleTimeMillis = 31;
    final int defaultNumTestsPerEvictionRun = 41;
    final boolean rollbackAfterValidation = false;
    final boolean defaultTestOnBorrow = false;
    final boolean defaultTestOnReturn = false;
    final boolean defaultTestWhileIdle = false;
    final int defaultTimeBetweenEvictionRunsMillis = 51;
    final String validationQuery = "SELECT 2";
    final int defaultMaxTotal = 61;
    final int defaultMaxIdle = 71;
    final int defaultMaxWaitMillis = 81;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ConnectionPoolDataSource.class).toInstance(connectionPoolDataSource);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);
        bindConstant().annotatedWith(Names.named("DBCP.defaultReadOnly")).to(defaultReadOnly);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTransactionIsolation")).to(defaultTransactionIsolation);
        bindConstant().annotatedWith(Names.named("DBCP.description")).to(description);
        bindConstant().annotatedWith(Names.named("DBCP.defaultMinEvictableIdleTimeMillis"))
            .to(defaultMinEvictableIdleTimeMillis);
        bindConstant().annotatedWith(Names.named("DBCP.defaultNumTestsPerEvictionRun"))
            .to(defaultNumTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("DBCP.rollbackAfterValidation")).to(rollbackAfterValidation);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTestOnBorrow")).to(defaultTestOnBorrow);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTestOnReturn")).to(defaultTestOnReturn);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTestWhileIdle")).to(defaultTestWhileIdle);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTimeBetweenEvictionRunsMillis"))
            .to(defaultTimeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("DBCP.validationQuery")).to(validationQuery);
        bindConstant().annotatedWith(Names.named("DBCP.defaultMaxTotal")).to(defaultMaxTotal);
        bindConstant().annotatedWith(Names.named("DBCP.defaultMaxIdle")).to(defaultMaxIdle);
        bindConstant().annotatedWith(Names.named("DBCP.defaultMaxWaitMillis")).to(defaultMaxWaitMillis);
      }
    });
    SharedPoolDataSourceProvider provider = injector.getInstance(SharedPoolDataSourceProvider.class);

    SharedPoolDataSource dataSource = (SharedPoolDataSource) provider.get();

    assertEquals(connectionPoolDataSource, dataSource.getConnectionPoolDataSource());
    assertEquals(autoCommit, dataSource.isDefaultAutoCommit());
    assertEquals(defaultReadOnly, dataSource.isDefaultReadOnly());
    assertEquals(defaultTransactionIsolation, dataSource.getDefaultTransactionIsolation());
    assertEquals(description, dataSource.getDescription());
    assertEquals(loginTimeout, dataSource.getLoginTimeout());
    assertEquals(defaultMinEvictableIdleTimeMillis, dataSource.getDefaultMinEvictableIdleTimeMillis());
    assertEquals(defaultNumTestsPerEvictionRun, dataSource.getDefaultNumTestsPerEvictionRun());
    assertEquals(rollbackAfterValidation, dataSource.isRollbackAfterValidation());
    assertEquals(defaultTestOnBorrow, dataSource.getDefaultTestOnBorrow());
    assertEquals(defaultTestOnReturn, dataSource.getDefaultTestOnReturn());
    assertEquals(defaultTestWhileIdle, dataSource.getDefaultTestWhileIdle());
    assertEquals(defaultTimeBetweenEvictionRunsMillis, dataSource.getDefaultTimeBetweenEvictionRunsMillis());
    assertEquals(validationQuery, dataSource.getValidationQuery());
    assertEquals(defaultMaxTotal, dataSource.getDefaultMaxTotal());
    assertEquals(defaultMaxIdle, dataSource.getDefaultMaxIdle());
    assertEquals(defaultMaxWaitMillis, dataSource.getDefaultMaxWaitMillis());
  }

  @Test
  public void get_Jndi() throws Throwable {
    final String jndiKey = "test_key";
    final String jndiValue = "test_value";
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ConnectionPoolDataSource.class).toInstance(connectionPoolDataSource);
        bindConstant().annotatedWith(Names.named("DBCP.jndi.key")).to(jndiKey);
        bindConstant().annotatedWith(Names.named("DBCP.jndi.value")).to(jndiValue);
      }
    });
    SharedPoolDataSourceProvider provider = injector.getInstance(SharedPoolDataSourceProvider.class);

    SharedPoolDataSource dataSource = (SharedPoolDataSource) provider.get();

    assertEquals(connectionPoolDataSource, dataSource.getConnectionPoolDataSource());
    assertEquals(jndiValue, dataSource.getJndiEnvironment(jndiKey));
  }

  @Test
  public void get_DataSourceName() throws Throwable {
    final String name = "test_name";
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("DBCP.name")).to(name);
      }
    });
    SharedPoolDataSourceProvider provider = injector.getInstance(SharedPoolDataSourceProvider.class);

    SharedPoolDataSource dataSource = (SharedPoolDataSource) provider.get();

    assertEquals(name, dataSource.getDataSourceName());
  }
}
