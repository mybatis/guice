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
package org.mybatis.guice.datasource.bonecp;

import com.jolbox.bonecp.BoneCPDataSource;
import com.jolbox.bonecp.hooks.ConnectionHook;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

/**
 * Provides the BoneCP DataSource.
 */
public final class BoneCPProvider implements Provider<DataSource> {

  private final BoneCPDataSource dataSource = new BoneCPDataSource();

  @com.google.inject.Inject(optional = true)
  public void setAcquireIncrement(@Named("bonecp.acquireIncrement") int acquireIncrement) {
    dataSource.setAcquireIncrement(acquireIncrement);
  }

  @com.google.inject.Inject(optional = true)
  public void setAcquireRetryAttempts(@Named("bonecp.acquireRetryAttempts") int acquireRetryAttempts) {
    dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
  }

  @com.google.inject.Inject(optional = true)
  public void setClassLoader(@Named("JDBC.driverClassLoader") ClassLoader classLoader) {
    dataSource.setClassLoader(classLoader);
  }

  @com.google.inject.Inject(optional = true)
  public void setCloseConnectionWatch(@Named("bonecp.closeConnectionWatch") boolean closeConnectionWatch) {
    dataSource.setCloseConnectionWatch(closeConnectionWatch);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionHook(@Named("bonecp.connectionHook") ConnectionHook connectionHook) {
    dataSource.setConnectionHook(connectionHook);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionHookClassName(@Named("bonecp.connectionHookClassName") String connectionHookClassName) {
    dataSource.setConnectionHookClassName(connectionHookClassName);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionTestStatement(@Named("bonecp.connectionTestStatement") String connectionTestStatement) {
    dataSource.setConnectionTestStatement(connectionTestStatement);
  }

  @com.google.inject.Inject(optional = true)
  public void setDisableConnectionTracking(
      @Named("bonecp.disableConnectionTracking") boolean disableConnectionTracking) {
    dataSource.setDisableConnectionTracking(disableConnectionTracking);
  }

  @com.google.inject.Inject(optional = true)
  public void setDisableJMX(@Named("bonecp.disableJMX") boolean disableJMX) {
    dataSource.setDisableJMX(disableJMX);
  }

  @Inject
  public void setDriverClass(@Named("JDBC.driver") final String driverClass) {
    dataSource.setDriverClass(driverClass);
  }

  @com.google.inject.Inject(optional = true)
  public void setDriverProperties(@Named("bonecp.driverProperties") Properties driverProperties) {
    dataSource.setDriverProperties(driverProperties);
  }

  @com.google.inject.Inject(optional = true)
  public void setIdleConnectionTestPeriod(@Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod,
      @Named("bonecp.timeUnit") TimeUnit timeUnit) {
    dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod, timeUnit);
  }

  @com.google.inject.Inject(optional = true)
  public void setIdleConnectionTestPeriodInMinutes(
      @Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod) {
    dataSource.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriod);
  }

  @com.google.inject.Inject(optional = true)
  public void setIdleConnectionTestPeriodInSeconds(
      @Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod) {
    dataSource.setIdleConnectionTestPeriodInSeconds(idleConnectionTestPeriod);
  }

  @com.google.inject.Inject(optional = true)
  public void setIdleMaxAge(@Named("bonecp.idleMaxAge") long idleMaxAge, @Named("bonecp.timeUnit") TimeUnit timeUnit) {
    dataSource.setIdleMaxAge(idleMaxAge, timeUnit);
  }

  @com.google.inject.Inject(optional = true)
  public void setInitSQL(@Named("bonecp.initSQL") String initSQL) {
    dataSource.setInitSQL(initSQL);
  }

  @Inject
  public void setJdbcUrl(@Named("JDBC.url") String jdbcUrl) {
    dataSource.setJdbcUrl(jdbcUrl);
  }

  @com.google.inject.Inject(optional = true)
  public void setLazyInit(@Named("bonecp.lazyInit") boolean lazyInit) {
    dataSource.setLazyInit(lazyInit);
  }

  @com.google.inject.Inject(optional = true)
  public void setLogStatementsEnabled(@Named("bonecp.logStatementsEnabled") boolean logStatementsEnabled) {
    dataSource.setLogStatementsEnabled(logStatementsEnabled);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxConnectionAge(@Named("bonecp.maxConnectionAge") long maxConnectionAge,
      @Named("bonecp.timeUnit") TimeUnit timeUnit) {
    dataSource.setMaxConnectionAge(maxConnectionAge, timeUnit);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxConnectionAgeInSeconds(@Named("bonecp.maxConnectionAgeInSeconds") long maxConnectionAgeInSeconds) {
    dataSource.setMaxConnectionAgeInSeconds(maxConnectionAgeInSeconds);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxConnectionsPerPartition(
      @Named("bonecp.maxConnectionsPerPartition") int maxConnectionsPerPartition) {
    dataSource.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
  }

  @com.google.inject.Inject(optional = true)
  public void setMinConnectionsPerPartition(
      @Named("bonecp.minConnectionsPerPartition") int minConnectionsPerPartition) {
    dataSource.setMinConnectionsPerPartition(minConnectionsPerPartition);
  }

  @com.google.inject.Inject(optional = true)
  public void setPartitionCount(@Named("bonecp.partitionCount") int partitionCount) {
    dataSource.setPartitionCount(partitionCount);
  }

  @Inject
  public void setPassword(@Named("JDBC.password") String password) {
    dataSource.setPassword(password);
  }

  @com.google.inject.Inject(optional = true)
  public void setPoolAvailabilityThreshold(@Named("bonecp.poolAvailabilityThreshold") int poolAvailabilityThreshold) {
    dataSource.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
  }

  @com.google.inject.Inject(optional = true)
  public void setPoolName(@Named("bonecp.poolName") String poolName) {
    dataSource.setPoolName(poolName);
  }

  @com.google.inject.Inject(optional = true)
  public void setQueryExecuteTimeLimit(@Named("bonecp.queryExecuteTimeLimit") int queryExecuteTimeLimit,
      @Named("bonecp.timeUnit") TimeUnit timeUnit) {
    dataSource.setQueryExecuteTimeLimit(queryExecuteTimeLimit, timeUnit);
  }

  @com.google.inject.Inject(optional = true)
  public void setReleaseHelperThreads(@Named("bonecp.releaseHelperThreads") int releaseHelperThreads) {
    dataSource.setReleaseHelperThreads(releaseHelperThreads);
  }

  @com.google.inject.Inject(optional = true)
  public void setServiceOrder(@Named("bonecp.serviceOrder") String serviceOrder) {
    dataSource.setServiceOrder(serviceOrder);
  }

  @com.google.inject.Inject(optional = true)
  public void setStatementReleaseHelperThreads(
      @Named("bonecp.statementReleaseHelperThreads") int statementReleaseHelperThreads) {
    dataSource.setStatementReleaseHelperThreads(statementReleaseHelperThreads);
  }

  @com.google.inject.Inject(optional = true)
  public void setStatementsCacheSize(@Named("bonecp.statementsCacheSize") int statementsCacheSize) {
    dataSource.setStatementsCacheSize(statementsCacheSize);
  }

  @com.google.inject.Inject(optional = true)
  public void setTransactionRecoveryEnabled(
      @Named("bonecp.transactionRecoveryEnabled") boolean transactionRecoveryEnabled) {
    dataSource.setTransactionRecoveryEnabled(transactionRecoveryEnabled);
  }

  @Inject
  public void setUsername(@Named("JDBC.username") String username) {
    dataSource.setUsername(username);
  }

  @Override
  public DataSource get() {
    return dataSource;
  }

}
