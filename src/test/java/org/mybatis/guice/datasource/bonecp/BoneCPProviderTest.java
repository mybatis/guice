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
package org.mybatis.guice.datasource.bonecp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import com.jolbox.bonecp.BoneCPDataSource;
import com.jolbox.bonecp.hooks.ConnectionHook;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

// TODO Tests fail with jdk 11, outstanding to replace this with hikariCp
@Disabled
@ExtendWith(MockitoExtension.class)
public class BoneCPProviderTest {
  @Mock
  private ClassLoader driverClassLoader;
  @Mock
  private ConnectionHook connectionHook;

  @Test
  public void get() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final TimeUnit timeUnit = TimeUnit.SECONDS;
    final int acquireIncrement = 10;
    final int acquireRetryAttempts = 5;
    final boolean closeConnectionWatch = true;
    final String connectionHookClassName = "org.mybatis.guice.TestConnectionHook";
    final String connectionTestStatement = "SELECT 1";
    final boolean disableConnectionTracking = true;
    final boolean disableJMX = true;
    final Properties driverProperties = new Properties();
    driverProperties.setProperty("my_property", "true");
    final long idleConnectionTestPeriod = 10000;
    final long idleMaxAge = 1000;
    final String initSQL = "CREATE TABLE test(id INT NOT NULL);";
    final boolean lazyInit = true;
    final boolean logStatementsEnabled = true;
    final long maxConnectionAge = 20000;
    final int maxConnectionsPerPartition = 20;
    final int minConnectionsPerPartition = 4;
    final int partitionCount = 2;
    final int poolAvailabilityThreshold = 15;
    final String poolName = "testPoolName";
    final long queryExecuteTimeLimit = 30000;
    final int releaseHelperThreads = 1;
    final String serviceOrder = "testServiceOrder";
    final int statementReleaseHelperThreads = 1;
    final int statementsCacheSize = 300;
    final boolean transactionRecoveryEnabled = true;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ClassLoader.class).annotatedWith(Names.named("JDBC.driverClassLoader")).toInstance(driverClassLoader);
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bind(TimeUnit.class).annotatedWith(Names.named("bonecp.timeUnit")).toInstance(timeUnit);
        bindConstant().annotatedWith(Names.named("bonecp.acquireIncrement")).to(acquireIncrement);
        bindConstant().annotatedWith(Names.named("bonecp.acquireRetryAttempts")).to(acquireRetryAttempts);
        bindConstant().annotatedWith(Names.named("bonecp.closeConnectionWatch")).to(closeConnectionWatch);
        bindConstant().annotatedWith(Names.named("bonecp.connectionHookClassName")).to(connectionHookClassName);
        bindConstant().annotatedWith(Names.named("bonecp.connectionTestStatement")).to(connectionTestStatement);
        bindConstant().annotatedWith(Names.named("bonecp.disableConnectionTracking")).to(disableConnectionTracking);
        bindConstant().annotatedWith(Names.named("bonecp.disableJMX")).to(disableJMX);
        bind(Properties.class).annotatedWith(Names.named("bonecp.driverProperties")).toInstance(driverProperties);
        bindConstant().annotatedWith(Names.named("bonecp.idleConnectionTestPeriod")).to(idleConnectionTestPeriod);
        bindConstant().annotatedWith(Names.named("bonecp.idleMaxAge")).to(idleMaxAge);
        bindConstant().annotatedWith(Names.named("bonecp.initSQL")).to(initSQL);
        bindConstant().annotatedWith(Names.named("bonecp.lazyInit")).to(lazyInit);
        bindConstant().annotatedWith(Names.named("bonecp.logStatementsEnabled")).to(logStatementsEnabled);
        bindConstant().annotatedWith(Names.named("bonecp.maxConnectionAge")).to(maxConnectionAge);
        bindConstant().annotatedWith(Names.named("bonecp.maxConnectionsPerPartition")).to(maxConnectionsPerPartition);
        bindConstant().annotatedWith(Names.named("bonecp.minConnectionsPerPartition")).to(minConnectionsPerPartition);
        bindConstant().annotatedWith(Names.named("bonecp.partitionCount")).to(partitionCount);
        bindConstant().annotatedWith(Names.named("bonecp.poolAvailabilityThreshold")).to(poolAvailabilityThreshold);
        bindConstant().annotatedWith(Names.named("bonecp.poolName")).to(poolName);
        bindConstant().annotatedWith(Names.named("bonecp.queryExecuteTimeLimit")).to(queryExecuteTimeLimit);
        bindConstant().annotatedWith(Names.named("bonecp.releaseHelperThreads")).to(releaseHelperThreads);
        bindConstant().annotatedWith(Names.named("bonecp.serviceOrder")).to(serviceOrder);
        bindConstant().annotatedWith(Names.named("bonecp.statementReleaseHelperThreads"))
            .to(statementReleaseHelperThreads);
        bindConstant().annotatedWith(Names.named("bonecp.statementsCacheSize")).to(statementsCacheSize);
        bindConstant().annotatedWith(Names.named("bonecp.transactionRecoveryEnabled")).to(transactionRecoveryEnabled);
      }
    });
    BoneCPProvider provider = injector.getInstance(BoneCPProvider.class);

    BoneCPDataSource dataSource = (BoneCPDataSource) provider.get();

    assertEquals(driverClassLoader, dataSource.getClassLoader());
    assertEquals(driver, dataSource.getDriverClass());
    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(acquireIncrement, dataSource.getAcquireIncrement());
    assertEquals(acquireRetryAttempts, dataSource.getAcquireRetryAttempts());
    assertEquals(closeConnectionWatch, dataSource.isCloseConnectionWatch());
    assertEquals(connectionHookClassName, dataSource.getConnectionHookClassName());
    assertEquals(connectionTestStatement, dataSource.getConnectionTestStatement());
    assertEquals(disableConnectionTracking, dataSource.isDisableConnectionTracking());
    assertEquals(disableJMX, dataSource.isDisableJMX());
    assertEquals(driverProperties, dataSource.getDriverProperties());
    assertEquals(idleConnectionTestPeriod, dataSource.getIdleConnectionTestPeriod(timeUnit));
    assertEquals(TimeUnit.MILLISECONDS.convert(idleConnectionTestPeriod, timeUnit),
        dataSource.getIdleConnectionTestPeriod(TimeUnit.MILLISECONDS));
    assertEquals(idleMaxAge, dataSource.getIdleMaxAge(timeUnit));
    assertEquals(TimeUnit.MILLISECONDS.convert(idleMaxAge, timeUnit), dataSource.getIdleMaxAge(TimeUnit.MILLISECONDS));
    assertEquals(initSQL, dataSource.getInitSQL());
    assertEquals(lazyInit, dataSource.isLazyInit());
    assertEquals(logStatementsEnabled, dataSource.isLogStatementsEnabled());
    assertEquals(maxConnectionAge, dataSource.getMaxConnectionAge(timeUnit));
    assertEquals(TimeUnit.MILLISECONDS.convert(maxConnectionAge, timeUnit),
        dataSource.getMaxConnectionAge(TimeUnit.MILLISECONDS));
    assertEquals(minConnectionsPerPartition, dataSource.getMinConnectionsPerPartition());
    assertEquals(partitionCount, dataSource.getPartitionCount());
    assertEquals(poolAvailabilityThreshold, dataSource.getPoolAvailabilityThreshold());
    assertEquals(poolName, dataSource.getPoolName());
    assertEquals(queryExecuteTimeLimit, dataSource.getQueryExecuteTimeLimit(timeUnit));
    assertEquals(TimeUnit.MILLISECONDS.convert(queryExecuteTimeLimit, timeUnit),
        dataSource.getQueryExecuteTimeLimit(TimeUnit.MILLISECONDS));
    assertEquals(releaseHelperThreads, dataSource.getReleaseHelperThreads());
    assertEquals(serviceOrder, dataSource.getServiceOrder());
    assertEquals(statementReleaseHelperThreads, dataSource.getStatementReleaseHelperThreads());
    assertEquals(statementsCacheSize, dataSource.getStatementsCacheSize());
    assertEquals(transactionRecoveryEnabled, dataSource.isTransactionRecoveryEnabled());
  }

  @Test
  public void get_OtherValues() {
    final String driver = "org.mybatis.guice.TestDriver2";
    final String url = "jdbc:h2:mem:testdb2";
    final String username = "test_user2";
    final String password = "test_password2";
    final TimeUnit timeUnit = TimeUnit.MINUTES;
    final int acquireIncrement = 20;
    final int acquireRetryAttempts = 15;
    final boolean closeConnectionWatch = false;
    final String connectionHookClassName = "org.mybatis.guice.TestConnectionHook2";
    final String connectionTestStatement = "SELECT 2";
    final boolean disableConnectionTracking = false;
    final boolean disableJMX = false;
    final Properties driverProperties = new Properties();
    driverProperties.setProperty("my_property2", "false");
    final long idleConnectionTestPeriod = 10010;
    final long idleMaxAge = 1010;
    final String initSQL = "CREATE TABLE test2(id INT NOT NULL);";
    final boolean lazyInit = false;
    final boolean logStatementsEnabled = false;
    final long maxConnectionAge = 20010;
    final int maxConnectionsPerPartition = 20;
    final int minConnectionsPerPartition = 14;
    final int partitionCount = 12;
    final int poolAvailabilityThreshold = 25;
    final String poolName = "testPoolName2";
    final long queryExecuteTimeLimit = 30010;
    final int releaseHelperThreads = 11;
    final String serviceOrder = "testServiceOrder2";
    final int statementReleaseHelperThreads = 11;
    final int statementsCacheSize = 310;
    final boolean transactionRecoveryEnabled = false;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ClassLoader.class).annotatedWith(Names.named("JDBC.driverClassLoader")).toInstance(driverClassLoader);
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bind(TimeUnit.class).annotatedWith(Names.named("bonecp.timeUnit")).toInstance(timeUnit);
        bindConstant().annotatedWith(Names.named("bonecp.acquireIncrement")).to(acquireIncrement);
        bindConstant().annotatedWith(Names.named("bonecp.acquireRetryAttempts")).to(acquireRetryAttempts);
        bindConstant().annotatedWith(Names.named("bonecp.closeConnectionWatch")).to(closeConnectionWatch);
        bindConstant().annotatedWith(Names.named("bonecp.connectionHookClassName")).to(connectionHookClassName);
        bindConstant().annotatedWith(Names.named("bonecp.connectionTestStatement")).to(connectionTestStatement);
        bindConstant().annotatedWith(Names.named("bonecp.disableConnectionTracking")).to(disableConnectionTracking);
        bindConstant().annotatedWith(Names.named("bonecp.disableJMX")).to(disableJMX);
        bind(Properties.class).annotatedWith(Names.named("bonecp.driverProperties")).toInstance(driverProperties);
        bindConstant().annotatedWith(Names.named("bonecp.idleConnectionTestPeriod")).to(idleConnectionTestPeriod);
        bindConstant().annotatedWith(Names.named("bonecp.idleMaxAge")).to(idleMaxAge);
        bindConstant().annotatedWith(Names.named("bonecp.initSQL")).to(initSQL);
        bindConstant().annotatedWith(Names.named("bonecp.lazyInit")).to(lazyInit);
        bindConstant().annotatedWith(Names.named("bonecp.logStatementsEnabled")).to(logStatementsEnabled);
        bindConstant().annotatedWith(Names.named("bonecp.maxConnectionAge")).to(maxConnectionAge);
        bindConstant().annotatedWith(Names.named("bonecp.maxConnectionsPerPartition")).to(maxConnectionsPerPartition);
        bindConstant().annotatedWith(Names.named("bonecp.minConnectionsPerPartition")).to(minConnectionsPerPartition);
        bindConstant().annotatedWith(Names.named("bonecp.partitionCount")).to(partitionCount);
        bindConstant().annotatedWith(Names.named("bonecp.poolAvailabilityThreshold")).to(poolAvailabilityThreshold);
        bindConstant().annotatedWith(Names.named("bonecp.poolName")).to(poolName);
        bindConstant().annotatedWith(Names.named("bonecp.queryExecuteTimeLimit")).to(queryExecuteTimeLimit);
        bindConstant().annotatedWith(Names.named("bonecp.releaseHelperThreads")).to(releaseHelperThreads);
        bindConstant().annotatedWith(Names.named("bonecp.serviceOrder")).to(serviceOrder);
        bindConstant().annotatedWith(Names.named("bonecp.statementReleaseHelperThreads"))
            .to(statementReleaseHelperThreads);
        bindConstant().annotatedWith(Names.named("bonecp.statementsCacheSize")).to(statementsCacheSize);
        bindConstant().annotatedWith(Names.named("bonecp.transactionRecoveryEnabled")).to(transactionRecoveryEnabled);
      }
    });
    BoneCPProvider provider = injector.getInstance(BoneCPProvider.class);

    BoneCPDataSource dataSource = (BoneCPDataSource) provider.get();

    assertEquals(driverClassLoader, dataSource.getClassLoader());
    assertEquals(driver, dataSource.getDriverClass());
    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(acquireIncrement, dataSource.getAcquireIncrement());
    assertEquals(acquireRetryAttempts, dataSource.getAcquireRetryAttempts());
    assertEquals(closeConnectionWatch, dataSource.isCloseConnectionWatch());
    assertEquals(connectionHookClassName, dataSource.getConnectionHookClassName());
    assertEquals(connectionTestStatement, dataSource.getConnectionTestStatement());
    assertEquals(disableConnectionTracking, dataSource.isDisableConnectionTracking());
    assertEquals(disableJMX, dataSource.isDisableJMX());
    assertEquals(driverProperties, dataSource.getDriverProperties());
    assertEquals(idleConnectionTestPeriod, dataSource.getIdleConnectionTestPeriod(timeUnit));
    assertEquals(TimeUnit.MILLISECONDS.convert(idleConnectionTestPeriod, timeUnit),
        dataSource.getIdleConnectionTestPeriod(TimeUnit.MILLISECONDS));
    assertEquals(idleMaxAge, dataSource.getIdleMaxAge(timeUnit));
    assertEquals(TimeUnit.MILLISECONDS.convert(idleMaxAge, timeUnit), dataSource.getIdleMaxAge(TimeUnit.MILLISECONDS));
    assertEquals(initSQL, dataSource.getInitSQL());
    assertEquals(lazyInit, dataSource.isLazyInit());
    assertEquals(logStatementsEnabled, dataSource.isLogStatementsEnabled());
    assertEquals(maxConnectionAge, dataSource.getMaxConnectionAge(timeUnit));
    assertEquals(TimeUnit.MILLISECONDS.convert(maxConnectionAge, timeUnit),
        dataSource.getMaxConnectionAge(TimeUnit.MILLISECONDS));
    assertEquals(minConnectionsPerPartition, dataSource.getMinConnectionsPerPartition());
    assertEquals(partitionCount, dataSource.getPartitionCount());
    assertEquals(poolAvailabilityThreshold, dataSource.getPoolAvailabilityThreshold());
    assertEquals(poolName, dataSource.getPoolName());
    assertEquals(queryExecuteTimeLimit, dataSource.getQueryExecuteTimeLimit(timeUnit));
    assertEquals(TimeUnit.MILLISECONDS.convert(queryExecuteTimeLimit, timeUnit),
        dataSource.getQueryExecuteTimeLimit(TimeUnit.MILLISECONDS));
    assertEquals(releaseHelperThreads, dataSource.getReleaseHelperThreads());
    assertEquals(serviceOrder, dataSource.getServiceOrder());
    assertEquals(statementReleaseHelperThreads, dataSource.getStatementReleaseHelperThreads());
    assertEquals(statementsCacheSize, dataSource.getStatementsCacheSize());
    assertEquals(transactionRecoveryEnabled, dataSource.isTransactionRecoveryEnabled());
  }

  @Test
  public void get_ConnectionHook() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bind(ConnectionHook.class).annotatedWith(Names.named("bonecp.connectionHook")).toInstance(connectionHook);
      }
    });
    BoneCPProvider provider = injector.getInstance(BoneCPProvider.class);

    BoneCPDataSource dataSource = (BoneCPDataSource) provider.get();

    assertEquals(connectionHook, dataSource.getConnectionHook());
  }

  @Test
  public void get_IdleConnectionTestPeriodInMinutes() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final long idleConnectionTestPeriod = 10;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("bonecp.idleConnectionTestPeriodInMinutes"))
            .to(idleConnectionTestPeriod);
      }
    });
    BoneCPProvider provider = injector.getInstance(BoneCPProvider.class);

    BoneCPDataSource dataSource = (BoneCPDataSource) provider.get();

    assertEquals(idleConnectionTestPeriod, dataSource.getIdleConnectionTestPeriod(TimeUnit.MINUTES));
    assertEquals(TimeUnit.MILLISECONDS.convert(idleConnectionTestPeriod, TimeUnit.MINUTES),
        dataSource.getIdleConnectionTestPeriod(TimeUnit.MILLISECONDS));
  }

  @Test
  public void get_IdleConnectionTestPeriodInSeconds() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final long idleConnectionTestPeriod = 10;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("bonecp.idleConnectionTestPeriodInSeconds"))
            .to(idleConnectionTestPeriod);
      }
    });
    BoneCPProvider provider = injector.getInstance(BoneCPProvider.class);

    BoneCPDataSource dataSource = (BoneCPDataSource) provider.get();

    assertEquals(idleConnectionTestPeriod, dataSource.getIdleConnectionTestPeriod(TimeUnit.SECONDS));
    assertEquals(TimeUnit.MILLISECONDS.convert(idleConnectionTestPeriod, TimeUnit.SECONDS),
        dataSource.getIdleConnectionTestPeriod(TimeUnit.MILLISECONDS));
  }

  @Test
  public void get_MaxConnectionAgeInSeconds() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final long maxConnectionAgeInSeconds = 200;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("bonecp.maxConnectionAgeInSeconds")).to(maxConnectionAgeInSeconds);
      }
    });
    BoneCPProvider provider = injector.getInstance(BoneCPProvider.class);

    BoneCPDataSource dataSource = (BoneCPDataSource) provider.get();

    assertEquals(maxConnectionAgeInSeconds, dataSource.getMaxConnectionAge(TimeUnit.SECONDS));
    assertEquals(TimeUnit.MILLISECONDS.convert(maxConnectionAgeInSeconds, TimeUnit.SECONDS),
        dataSource.getMaxConnectionAge(TimeUnit.MILLISECONDS));
  }
}
