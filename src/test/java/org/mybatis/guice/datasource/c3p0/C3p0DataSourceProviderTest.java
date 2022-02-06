/*
 *    Copyright 2009-2022 the original author or authors.
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
package org.mybatis.guice.datasource.c3p0;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.ConnectionTester;
import com.mchange.v2.sql.SqlUtils;

import java.sql.Connection;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class C3p0DataSourceProviderTest {

  @Test
  public void get() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final int acquireIncrement = 10;
    final int acquireRetryAttempts = 20;
    final int acquireRetryDelay = 30;
    final String automaticTestTable = "auto-table";
    final boolean breakAfterAcquireFailure = true;
    final int checkoutTimeout = 40;
    final String connectionCustomizerClassName = "org.mybatis.guice.ConnectionCustomizer";
    final String connectionTesterClassName = TestConnectionTester.class.getName();
    final int idleConnectionTestPeriod = 50;
    final int initialPoolSize = 60;
    final int maxAdministrativeTaskTime = 70;
    final int maxConnectionAge = 80;
    final int maxIdleTime = 90;
    final int maxIdleTimeExcessConnections = 100;
    final int maxPoolSize = 110;
    final int maxStatements = 120;
    final int maxStatementsPerConnection = 130;
    final int minPoolSize = 140;
    final String preferredTestQuery = "SELECT 1";
    final int propertyCycle = 150;
    final boolean testConnectionOnCheckin = true;
    final boolean testConnectionOnCheckout = true;
    final int unreturnedConnectionTimeout = 160;
    final boolean usesTraditionalReflectiveProxies = true;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("c3p0.acquireIncrement")).to(acquireIncrement);
        bindConstant().annotatedWith(Names.named("c3p0.acquireRetryAttempts")).to(acquireRetryAttempts);
        bindConstant().annotatedWith(Names.named("c3p0.acquireRetryDelay")).to(acquireRetryDelay);
        bindConstant().annotatedWith(Names.named("c3p0.automaticTestTable")).to(automaticTestTable);
        bindConstant().annotatedWith(Names.named("c3p0.breakAfterAcquireFailure")).to(breakAfterAcquireFailure);
        bindConstant().annotatedWith(Names.named("c3p0.checkoutTimeout")).to(checkoutTimeout);
        bindConstant().annotatedWith(Names.named("c3p0.connectionCustomizerClassName"))
            .to(connectionCustomizerClassName);
        bindConstant().annotatedWith(Names.named("c3p0.connectionTesterClassName")).to(connectionTesterClassName);
        bindConstant().annotatedWith(Names.named("c3p0.idleConnectionTestPeriod")).to(idleConnectionTestPeriod);
        bindConstant().annotatedWith(Names.named("c3p0.initialPoolSize")).to(initialPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.maxAdministrativeTaskTime")).to(maxAdministrativeTaskTime);
        bindConstant().annotatedWith(Names.named("c3p0.maxConnectionAge")).to(maxConnectionAge);
        bindConstant().annotatedWith(Names.named("c3p0.maxIdleTime")).to(maxIdleTime);
        bindConstant().annotatedWith(Names.named("c3p0.maxIdleTimeExcessConnections")).to(maxIdleTimeExcessConnections);
        bindConstant().annotatedWith(Names.named("c3p0.maxPoolSize")).to(maxPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.maxStatements")).to(maxStatements);
        bindConstant().annotatedWith(Names.named("c3p0.maxStatementsPerConnection")).to(maxStatementsPerConnection);
        bindConstant().annotatedWith(Names.named("c3p0.minPoolSize")).to(minPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.preferredTestQuery")).to(preferredTestQuery);
        bindConstant().annotatedWith(Names.named("c3p0.propertyCycle")).to(propertyCycle);
        bindConstant().annotatedWith(Names.named("c3p0.testConnectionOnCheckin")).to(testConnectionOnCheckin);
        bindConstant().annotatedWith(Names.named("c3p0.testConnectionOnCheckout")).to(testConnectionOnCheckout);
        bindConstant().annotatedWith(Names.named("c3p0.unreturnedConnectionTimeout")).to(unreturnedConnectionTimeout);
        bindConstant().annotatedWith(Names.named("c3p0.usesTraditionalReflectiveProxies"))
            .to(usesTraditionalReflectiveProxies);
      }
    });
    C3p0DataSourceProvider provider = injector.getInstance(C3p0DataSourceProvider.class);

    ComboPooledDataSource dataSource = (ComboPooledDataSource) provider.get();

    assertEquals(driver, dataSource.getDriverClass());
    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUser());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommitOnClose());
    assertEquals(acquireIncrement, dataSource.getAcquireIncrement());
    assertEquals(acquireRetryAttempts, dataSource.getAcquireRetryAttempts());
    assertEquals(acquireRetryDelay, dataSource.getAcquireRetryDelay());
    assertEquals(automaticTestTable, dataSource.getAutomaticTestTable());
    assertEquals(breakAfterAcquireFailure, dataSource.isBreakAfterAcquireFailure());
    assertEquals(checkoutTimeout, dataSource.getCheckoutTimeout());
    assertEquals(connectionCustomizerClassName, dataSource.getConnectionCustomizerClassName());
    assertEquals(connectionTesterClassName, dataSource.getConnectionTesterClassName());
    assertEquals(idleConnectionTestPeriod, dataSource.getIdleConnectionTestPeriod());
    assertEquals(initialPoolSize, dataSource.getInitialPoolSize());
    assertEquals(maxAdministrativeTaskTime, dataSource.getMaxAdministrativeTaskTime());
    assertEquals(maxConnectionAge, dataSource.getMaxConnectionAge());
    assertEquals(maxIdleTime, dataSource.getMaxIdleTime());
    assertEquals(maxIdleTimeExcessConnections, dataSource.getMaxIdleTimeExcessConnections());
    assertEquals(maxPoolSize, dataSource.getMaxPoolSize());
    assertEquals(maxStatements, dataSource.getMaxStatements());
    assertEquals(maxStatementsPerConnection, dataSource.getMaxStatementsPerConnection());
    assertEquals(minPoolSize, dataSource.getMinPoolSize());
    assertEquals(preferredTestQuery, dataSource.getPreferredTestQuery());
    assertEquals(propertyCycle, dataSource.getPropertyCycle());
    assertEquals(testConnectionOnCheckin, dataSource.isTestConnectionOnCheckin());
    assertEquals(testConnectionOnCheckout, dataSource.isTestConnectionOnCheckout());
    assertEquals(unreturnedConnectionTimeout, dataSource.getUnreturnedConnectionTimeout());
    assertEquals(usesTraditionalReflectiveProxies, dataSource.isUsesTraditionalReflectiveProxies());
  }

  @Test
  public void get_OtherValues() {
    final String driver = "org.mybatis.guice.TestDriver2";
    final String url = "jdbc:h2:mem:testdb2";
    final String username = "test_user2";
    final String password = "test_password2";
    final boolean autoCommit = false;
    final int acquireIncrement = 11;
    final int acquireRetryAttempts = 21;
    final int acquireRetryDelay = 31;
    final String automaticTestTable = "auto-table2";
    final boolean breakAfterAcquireFailure = false;
    final int checkoutTimeout = 41;
    final String connectionCustomizerClassName = "org.mybatis.guice.ConnectionCustomizer2";
    final String connectionTesterClassName = SecondTestConnectionTester.class.getName();
    final int idleConnectionTestPeriod = 51;
    final int initialPoolSize = 61;
    final int maxAdministrativeTaskTime = 71;
    final int maxConnectionAge = 81;
    final int maxIdleTime = 91;
    final int maxIdleTimeExcessConnections = 101;
    final int maxPoolSize = 111;
    final int maxStatements = 121;
    final int maxStatementsPerConnection = 131;
    final int minPoolSize = 141;
    final String preferredTestQuery = "SELECT 2";
    final int propertyCycle = 151;
    final boolean testConnectionOnCheckin = false;
    final boolean testConnectionOnCheckout = false;
    final int unreturnedConnectionTimeout = 161;
    final boolean usesTraditionalReflectiveProxies = false;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("c3p0.acquireIncrement")).to(acquireIncrement);
        bindConstant().annotatedWith(Names.named("c3p0.acquireRetryAttempts")).to(acquireRetryAttempts);
        bindConstant().annotatedWith(Names.named("c3p0.acquireRetryDelay")).to(acquireRetryDelay);
        bindConstant().annotatedWith(Names.named("c3p0.automaticTestTable")).to(automaticTestTable);
        bindConstant().annotatedWith(Names.named("c3p0.breakAfterAcquireFailure")).to(breakAfterAcquireFailure);
        bindConstant().annotatedWith(Names.named("c3p0.checkoutTimeout")).to(checkoutTimeout);
        bindConstant().annotatedWith(Names.named("c3p0.connectionCustomizerClassName"))
            .to(connectionCustomizerClassName);
        bindConstant().annotatedWith(Names.named("c3p0.connectionTesterClassName")).to(connectionTesterClassName);
        bindConstant().annotatedWith(Names.named("c3p0.idleConnectionTestPeriod")).to(idleConnectionTestPeriod);
        bindConstant().annotatedWith(Names.named("c3p0.initialPoolSize")).to(initialPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.maxAdministrativeTaskTime")).to(maxAdministrativeTaskTime);
        bindConstant().annotatedWith(Names.named("c3p0.maxConnectionAge")).to(maxConnectionAge);
        bindConstant().annotatedWith(Names.named("c3p0.maxIdleTime")).to(maxIdleTime);
        bindConstant().annotatedWith(Names.named("c3p0.maxIdleTimeExcessConnections")).to(maxIdleTimeExcessConnections);
        bindConstant().annotatedWith(Names.named("c3p0.maxPoolSize")).to(maxPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.maxStatements")).to(maxStatements);
        bindConstant().annotatedWith(Names.named("c3p0.maxStatementsPerConnection")).to(maxStatementsPerConnection);
        bindConstant().annotatedWith(Names.named("c3p0.minPoolSize")).to(minPoolSize);
        bindConstant().annotatedWith(Names.named("c3p0.preferredTestQuery")).to(preferredTestQuery);
        bindConstant().annotatedWith(Names.named("c3p0.propertyCycle")).to(propertyCycle);
        bindConstant().annotatedWith(Names.named("c3p0.testConnectionOnCheckin")).to(testConnectionOnCheckin);
        bindConstant().annotatedWith(Names.named("c3p0.testConnectionOnCheckout")).to(testConnectionOnCheckout);
        bindConstant().annotatedWith(Names.named("c3p0.unreturnedConnectionTimeout")).to(unreturnedConnectionTimeout);
        bindConstant().annotatedWith(Names.named("c3p0.usesTraditionalReflectiveProxies"))
            .to(usesTraditionalReflectiveProxies);
      }
    });
    C3p0DataSourceProvider provider = injector.getInstance(C3p0DataSourceProvider.class);

    ComboPooledDataSource dataSource = (ComboPooledDataSource) provider.get();

    assertEquals(driver, dataSource.getDriverClass());
    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUser());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommitOnClose());
    assertEquals(acquireIncrement, dataSource.getAcquireIncrement());
    assertEquals(acquireRetryAttempts, dataSource.getAcquireRetryAttempts());
    assertEquals(acquireRetryDelay, dataSource.getAcquireRetryDelay());
    assertEquals(automaticTestTable, dataSource.getAutomaticTestTable());
    assertEquals(breakAfterAcquireFailure, dataSource.isBreakAfterAcquireFailure());
    assertEquals(checkoutTimeout, dataSource.getCheckoutTimeout());
    assertEquals(connectionCustomizerClassName, dataSource.getConnectionCustomizerClassName());
    assertEquals(connectionTesterClassName, dataSource.getConnectionTesterClassName());
    assertEquals(idleConnectionTestPeriod, dataSource.getIdleConnectionTestPeriod());
    assertEquals(initialPoolSize, dataSource.getInitialPoolSize());
    assertEquals(maxAdministrativeTaskTime, dataSource.getMaxAdministrativeTaskTime());
    assertEquals(maxConnectionAge, dataSource.getMaxConnectionAge());
    assertEquals(maxIdleTime, dataSource.getMaxIdleTime());
    assertEquals(maxIdleTimeExcessConnections, dataSource.getMaxIdleTimeExcessConnections());
    assertEquals(maxPoolSize, dataSource.getMaxPoolSize());
    assertEquals(maxStatements, dataSource.getMaxStatements());
    assertEquals(maxStatementsPerConnection, dataSource.getMaxStatementsPerConnection());
    assertEquals(minPoolSize, dataSource.getMinPoolSize());
    assertEquals(preferredTestQuery, dataSource.getPreferredTestQuery());
    assertEquals(propertyCycle, dataSource.getPropertyCycle());
    assertEquals(testConnectionOnCheckin, dataSource.isTestConnectionOnCheckin());
    assertEquals(testConnectionOnCheckout, dataSource.isTestConnectionOnCheckout());
    assertEquals(unreturnedConnectionTimeout, dataSource.getUnreturnedConnectionTimeout());
    assertEquals(usesTraditionalReflectiveProxies, dataSource.isUsesTraditionalReflectiveProxies());
  }

  @Test
  public void get_Properties() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final Properties driverProperties = new Properties();
    driverProperties.setProperty("my_property", "true");
    driverProperties.setProperty(SqlUtils.DRIVER_MANAGER_USER_PROPERTY, username);
    driverProperties.setProperty(SqlUtils.DRIVER_MANAGER_PASSWORD_PROPERTY, password);
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bind(Properties.class).annotatedWith(Names.named("JDBC.driverProperties")).toInstance(driverProperties);
      }
    });
    C3p0DataSourceProvider provider = injector.getInstance(C3p0DataSourceProvider.class);

    ComboPooledDataSource dataSource = (ComboPooledDataSource) provider.get();

    assertEquals(driver, dataSource.getDriverClass());
    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUser());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommitOnClose());
    assertEquals(driverProperties, dataSource.getProperties());
  }

  @Test
  public void get_UserPasswordAndProperties() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final String propUsername = "test_user2";
    final String propPassword = "test_password2";
    final boolean autoCommit = true;
    final Properties driverProperties = new Properties();
    driverProperties.setProperty("my_property", "true");
    driverProperties.setProperty(SqlUtils.DRIVER_MANAGER_USER_PROPERTY, propUsername);
    driverProperties.setProperty(SqlUtils.DRIVER_MANAGER_PASSWORD_PROPERTY, propPassword);
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bind(Properties.class).annotatedWith(Names.named("JDBC.driverProperties")).toInstance(driverProperties);
      }
    });
    C3p0DataSourceProvider provider = injector.getInstance(C3p0DataSourceProvider.class);

    ComboPooledDataSource dataSource = (ComboPooledDataSource) provider.get();

    assertEquals(driver, dataSource.getDriverClass());
    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUser());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommitOnClose());
    final Properties expectedDriverProperties = new Properties();
    expectedDriverProperties.setProperty("my_property", "true");
    expectedDriverProperties.setProperty(SqlUtils.DRIVER_MANAGER_USER_PROPERTY, username);
    expectedDriverProperties.setProperty(SqlUtils.DRIVER_MANAGER_PASSWORD_PROPERTY, password);
    assertEquals(expectedDriverProperties, dataSource.getProperties());
  }

  @SuppressWarnings("serial")
  public static final class TestConnectionTester implements ConnectionTester {
    @Override
    public int activeCheckConnection(Connection c) {
      return 0;
    }

    @Override
    public int statusOnException(Connection c, Throwable t) {
      return 0;
    }
  }

  @SuppressWarnings("serial")
  public static final class SecondTestConnectionTester implements ConnectionTester {
    @Override
    public int activeCheckConnection(Connection c) {
      return 0;
    }

    @Override
    public int statusOnException(Connection c, Throwable t) {
      return 0;
    }
  }
}
