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
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

@ExtendWith(MockitoExtension.class)
public class BasicDataSourceProviderTest {
  @Mock
  private ClassLoader driverClassLoader;

  @Test
  public void get() throws Throwable {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final Properties driverProperties = new Properties();
    driverProperties.put("my_property", "true");
    final boolean accessToUnderlyingConnectionAllowed = true;
    final String defaultCatalog = "test_catalog";
    final boolean defaultReadOnly = true;
    final int defaultTransactionIsolation = 20;
    final int initialSize = 30;
    final int maxTotal = 40;
    final int maxIdle = 50;
    final int maxOpenPreparedStatements = 60;
    final long maxWaitMillis = 70;
    final int minIdle = 80;
    final int numTestsPerEvictionRun = 90;
    final boolean poolPreparedStatements = true;
    final boolean testOnBorrow = true;
    final boolean testOnReturn = true;
    final boolean testWhileIdle = true;
    final int timeBetweenEvictionRunsMillis = 100;
    final String validationQuery = "SELECT 1";
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ClassLoader.class).annotatedWith(Names.named("JDBC.driverClassLoader")).toInstance(driverClassLoader);
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bind(Properties.class).annotatedWith(Names.named("JDBC.driverProperties")).toInstance(driverProperties);
        bindConstant().annotatedWith(Names.named("DBCP.accessToUnderlyingConnectionAllowed"))
            .to(accessToUnderlyingConnectionAllowed);
        bindConstant().annotatedWith(Names.named("DBCP.defaultCatalog")).to(defaultCatalog);
        bindConstant().annotatedWith(Names.named("DBCP.defaultReadOnly")).to(defaultReadOnly);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTransactionIsolation")).to(defaultTransactionIsolation);
        bindConstant().annotatedWith(Names.named("DBCP.initialSize")).to(initialSize);
        bindConstant().annotatedWith(Names.named("DBCP.maxTotal")).to(maxTotal);
        bindConstant().annotatedWith(Names.named("DBCP.maxIdle")).to(maxIdle);
        bindConstant().annotatedWith(Names.named("DBCP.maxOpenPreparedStatements")).to(maxOpenPreparedStatements);
        bindConstant().annotatedWith(Names.named("DBCP.maxWaitMillis")).to(maxWaitMillis);
        bindConstant().annotatedWith(Names.named("DBCP.minIdle")).to(minIdle);
        bindConstant().annotatedWith(Names.named("DBCP.numTestsPerEvictionRun")).to(numTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("DBCP.poolPreparedStatements")).to(poolPreparedStatements);
        bindConstant().annotatedWith(Names.named("DBCP.testOnBorrow")).to(testOnBorrow);
        bindConstant().annotatedWith(Names.named("DBCP.testOnReturn")).to(testOnReturn);
        bindConstant().annotatedWith(Names.named("DBCP.testWhileIdle")).to(testWhileIdle);
        bindConstant().annotatedWith(Names.named("DBCP.timeBetweenEvictionRunsMillis"))
            .to(timeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("DBCP.validationQuery")).to(validationQuery);
      }
    });
    BasicDataSourceProvider provider = injector.getInstance(BasicDataSourceProvider.class);

    BasicDataSource dataSource = (BasicDataSource) provider.get();

    assertEquals(driverClassLoader, dataSource.getDriverClassLoader());
    assertEquals(driver, dataSource.getDriverClassName());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.getDefaultAutoCommit());
    // Cannot test driver properties.
    assertEquals(accessToUnderlyingConnectionAllowed, dataSource.isAccessToUnderlyingConnectionAllowed());
    assertEquals(defaultCatalog, dataSource.getDefaultCatalog());
    assertEquals(defaultReadOnly, dataSource.getDefaultReadOnly());
    assertEquals(defaultTransactionIsolation, dataSource.getDefaultTransactionIsolation());
    assertEquals(initialSize, dataSource.getInitialSize());
    assertEquals(maxTotal, dataSource.getMaxTotal());
    assertEquals(maxIdle, dataSource.getMaxIdle());
    assertEquals(maxOpenPreparedStatements, dataSource.getMaxOpenPreparedStatements());
    assertEquals(maxWaitMillis, dataSource.getMaxWaitMillis());
    assertEquals(minIdle, dataSource.getMinIdle());
    assertEquals(numTestsPerEvictionRun, dataSource.getNumTestsPerEvictionRun());
    assertEquals(poolPreparedStatements, dataSource.isPoolPreparedStatements());
    assertEquals(testOnBorrow, dataSource.getTestOnBorrow());
    assertEquals(testOnReturn, dataSource.getTestOnReturn());
    assertEquals(testWhileIdle, dataSource.getTestWhileIdle());
    assertEquals(timeBetweenEvictionRunsMillis, dataSource.getTimeBetweenEvictionRunsMillis());
    assertEquals(validationQuery, dataSource.getValidationQuery());
  }

  @Test
  public void get_OtherValues() throws Throwable {
    final String driver = "org.mybatis.guice.TestDriver2";
    final String url = "jdbc:h2:mem:testdb2";
    final String username = "test_user2";
    final String password = "test_password2";
    final boolean autoCommit = false;
    final Properties driverProperties = new Properties();
    driverProperties.put("my_property", "false");
    final boolean accessToUnderlyingConnectionAllowed = false;
    final String defaultCatalog = "test_catalog2";
    final boolean defaultReadOnly = false;
    final int defaultTransactionIsolation = 21;
    final int initialSize = 31;
    final int maxTotal = 41;
    final int maxIdle = 51;
    final int maxOpenPreparedStatements = 61;
    final long maxWaitMillis = 71;
    final int minIdle = 81;
    final int numTestsPerEvictionRun = 91;
    final boolean poolPreparedStatements = false;
    final boolean testOnBorrow = false;
    final boolean testOnReturn = false;
    final boolean testWhileIdle = false;
    final int timeBetweenEvictionRunsMillis = 101;
    final String validationQuery = "SELECT 2";
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ClassLoader.class).annotatedWith(Names.named("JDBC.driverClassLoader")).toInstance(driverClassLoader);
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bind(Properties.class).annotatedWith(Names.named("JDBC.driverProperties")).toInstance(driverProperties);
        bindConstant().annotatedWith(Names.named("DBCP.accessToUnderlyingConnectionAllowed"))
            .to(accessToUnderlyingConnectionAllowed);
        bindConstant().annotatedWith(Names.named("DBCP.defaultCatalog")).to(defaultCatalog);
        bindConstant().annotatedWith(Names.named("DBCP.defaultReadOnly")).to(defaultReadOnly);
        bindConstant().annotatedWith(Names.named("DBCP.defaultTransactionIsolation")).to(defaultTransactionIsolation);
        bindConstant().annotatedWith(Names.named("DBCP.initialSize")).to(initialSize);
        bindConstant().annotatedWith(Names.named("DBCP.maxTotal")).to(maxTotal);
        bindConstant().annotatedWith(Names.named("DBCP.maxIdle")).to(maxIdle);
        bindConstant().annotatedWith(Names.named("DBCP.maxOpenPreparedStatements")).to(maxOpenPreparedStatements);
        bindConstant().annotatedWith(Names.named("DBCP.maxWaitMillis")).to(maxWaitMillis);
        bindConstant().annotatedWith(Names.named("DBCP.minIdle")).to(minIdle);
        bindConstant().annotatedWith(Names.named("DBCP.numTestsPerEvictionRun")).to(numTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("DBCP.poolPreparedStatements")).to(poolPreparedStatements);
        bindConstant().annotatedWith(Names.named("DBCP.testOnBorrow")).to(testOnBorrow);
        bindConstant().annotatedWith(Names.named("DBCP.testOnReturn")).to(testOnReturn);
        bindConstant().annotatedWith(Names.named("DBCP.testWhileIdle")).to(testWhileIdle);
        bindConstant().annotatedWith(Names.named("DBCP.timeBetweenEvictionRunsMillis"))
            .to(timeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("DBCP.validationQuery")).to(validationQuery);
      }
    });
    BasicDataSourceProvider provider = injector.getInstance(BasicDataSourceProvider.class);

    BasicDataSource dataSource = (BasicDataSource) provider.get();

    assertEquals(driverClassLoader, dataSource.getDriverClassLoader());
    assertEquals(driver, dataSource.getDriverClassName());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.getDefaultAutoCommit());
    // Cannot test driver properties.
    assertEquals(accessToUnderlyingConnectionAllowed, dataSource.isAccessToUnderlyingConnectionAllowed());
    assertEquals(defaultCatalog, dataSource.getDefaultCatalog());
    assertEquals(defaultReadOnly, dataSource.getDefaultReadOnly());
    assertEquals(defaultTransactionIsolation, dataSource.getDefaultTransactionIsolation());
    assertEquals(initialSize, dataSource.getInitialSize());
    assertEquals(maxTotal, dataSource.getMaxTotal());
    assertEquals(maxIdle, dataSource.getMaxIdle());
    assertEquals(maxOpenPreparedStatements, dataSource.getMaxOpenPreparedStatements());
    assertEquals(maxWaitMillis, dataSource.getMaxWaitMillis());
    assertEquals(minIdle, dataSource.getMinIdle());
    assertEquals(numTestsPerEvictionRun, dataSource.getNumTestsPerEvictionRun());
    assertEquals(poolPreparedStatements, dataSource.isPoolPreparedStatements());
    assertEquals(testOnBorrow, dataSource.getTestOnBorrow());
    assertEquals(testOnReturn, dataSource.getTestOnReturn());
    assertEquals(testWhileIdle, dataSource.getTestWhileIdle());
    assertEquals(timeBetweenEvictionRunsMillis, dataSource.getTimeBetweenEvictionRunsMillis());
    assertEquals(validationQuery, dataSource.getValidationQuery());
  }
}
