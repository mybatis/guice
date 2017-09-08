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
package org.mybatis.guice.datasource.dbcp;

import static org.junit.Assert.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Connection;

import javax.sql.ConnectionPoolDataSource;

public class SharedPoolDataSourceProviderTest {
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
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
    final int minEvictableIdleTimeMillis = 30;
    final int numTestsPerEvictionRun = 40;
    final boolean rollbackAfterValidation = true;
    final boolean testOnBorrow = true;
    final boolean testOnReturn = true;
    final boolean testWhileIdle = true;
    final int timeBetweenEvictionRunsMillis = 50;
    final String validationQuery = "SELECT 1";
    final int maxActive = 60;
    final int maxIdle = 70;
    final int maxWait = 80;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ConnectionPoolDataSource.class).toInstance(connectionPoolDataSource);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);
        bindConstant().annotatedWith(Names.named("DBCP.defaultReadOnly")).to(defaultReadOnly);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTransactionIsolation")).to(defaultTransactionIsolation);
        bindConstant().annotatedWith(Names.named("DBCP.description")).to(description);
        bindConstant().annotatedWith(Names.named("DBCP.minEvictableIdleTimeMillis")).to(minEvictableIdleTimeMillis);
        bindConstant().annotatedWith(Names.named("DBCP.numTestsPerEvictionRun")).to(numTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("DBCP.rollbackAfterValidation")).to(rollbackAfterValidation);
        bindConstant().annotatedWith(Names.named("DBCP.testOnBorrow")).to(testOnBorrow);
        bindConstant().annotatedWith(Names.named("DBCP.testOnReturn")).to(testOnReturn);
        bindConstant().annotatedWith(Names.named("DBCP.testWhileIdle")).to(testWhileIdle);
        bindConstant().annotatedWith(Names.named("DBCP.timeBetweenEvictionRunsMillis"))
            .to(timeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("DBCP.validationQuery")).to(validationQuery);
        bindConstant().annotatedWith(Names.named("DBCP.maxActive")).to(maxActive);
        bindConstant().annotatedWith(Names.named("DBCP.maxIdle")).to(maxIdle);
        bindConstant().annotatedWith(Names.named("DBCP.maxWait")).to(maxWait);
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
    assertEquals(minEvictableIdleTimeMillis, dataSource.getMinEvictableIdleTimeMillis());
    assertEquals(numTestsPerEvictionRun, dataSource.getNumTestsPerEvictionRun());
    assertEquals(rollbackAfterValidation, dataSource.isRollbackAfterValidation());
    assertEquals(testOnBorrow, dataSource.isTestOnBorrow());
    assertEquals(testOnReturn, dataSource.isTestOnReturn());
    assertEquals(testWhileIdle, dataSource.isTestWhileIdle());
    assertEquals(timeBetweenEvictionRunsMillis, dataSource.getTimeBetweenEvictionRunsMillis());
    assertEquals(validationQuery, dataSource.getValidationQuery());
    assertEquals(maxActive, dataSource.getMaxActive());
    assertEquals(maxIdle, dataSource.getMaxIdle());
    assertEquals(maxWait, dataSource.getMaxWait());
  }

  @Test
  public void get_OtherValues() throws Throwable {
    final boolean autoCommit = false;
    final int loginTimeout = 11;
    final boolean defaultReadOnly = false;
    final int defaultTransactionIsolation = Connection.TRANSACTION_REPEATABLE_READ;
    final String description = "test_description2";
    final int minEvictableIdleTimeMillis = 31;
    final int numTestsPerEvictionRun = 41;
    final boolean rollbackAfterValidation = false;
    final boolean testOnBorrow = false;
    final boolean testOnReturn = false;
    final boolean testWhileIdle = false;
    final int timeBetweenEvictionRunsMillis = 51;
    final String validationQuery = "SELECT 2";
    final int maxActive = 61;
    final int maxIdle = 71;
    final int maxWait = 81;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ConnectionPoolDataSource.class).toInstance(connectionPoolDataSource);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);
        bindConstant().annotatedWith(Names.named("DBCP.defaultReadOnly")).to(defaultReadOnly);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTransactionIsolation")).to(defaultTransactionIsolation);
        bindConstant().annotatedWith(Names.named("DBCP.description")).to(description);
        bindConstant().annotatedWith(Names.named("DBCP.minEvictableIdleTimeMillis")).to(minEvictableIdleTimeMillis);
        bindConstant().annotatedWith(Names.named("DBCP.numTestsPerEvictionRun")).to(numTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("DBCP.rollbackAfterValidation")).to(rollbackAfterValidation);
        bindConstant().annotatedWith(Names.named("DBCP.testOnBorrow")).to(testOnBorrow);
        bindConstant().annotatedWith(Names.named("DBCP.testOnReturn")).to(testOnReturn);
        bindConstant().annotatedWith(Names.named("DBCP.testWhileIdle")).to(testWhileIdle);
        bindConstant().annotatedWith(Names.named("DBCP.timeBetweenEvictionRunsMillis"))
            .to(timeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("DBCP.validationQuery")).to(validationQuery);
        bindConstant().annotatedWith(Names.named("DBCP.maxActive")).to(maxActive);
        bindConstant().annotatedWith(Names.named("DBCP.maxIdle")).to(maxIdle);
        bindConstant().annotatedWith(Names.named("DBCP.maxWait")).to(maxWait);
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
    assertEquals(minEvictableIdleTimeMillis, dataSource.getMinEvictableIdleTimeMillis());
    assertEquals(numTestsPerEvictionRun, dataSource.getNumTestsPerEvictionRun());
    assertEquals(rollbackAfterValidation, dataSource.isRollbackAfterValidation());
    assertEquals(testOnBorrow, dataSource.isTestOnBorrow());
    assertEquals(testOnReturn, dataSource.isTestOnReturn());
    assertEquals(testWhileIdle, dataSource.isTestWhileIdle());
    assertEquals(timeBetweenEvictionRunsMillis, dataSource.getTimeBetweenEvictionRunsMillis());
    assertEquals(validationQuery, dataSource.getValidationQuery());
    assertEquals(maxActive, dataSource.getMaxActive());
    assertEquals(maxIdle, dataSource.getMaxIdle());
    assertEquals(maxWait, dataSource.getMaxWait());
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
