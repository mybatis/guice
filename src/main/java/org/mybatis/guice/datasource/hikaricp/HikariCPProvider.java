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
package org.mybatis.guice.datasource.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;

import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

/**
 * Provides the HikariCP DataSource.
 * <p>
 * For additional information see configuration here
 * <a href="https://github.com/brettwooldridge/HikariCP">https://github.com/brettwooldridge/HikariCP</a>.
 */
public final class HikariCPProvider implements Provider<DataSource> {
  private final HikariConfig configuration = new HikariConfig();

  private Integer loginTimeout;

  @Override
  public DataSource get() {
    HikariDataSource dataSource = new HikariDataSource(configuration);
    if (loginTimeout != null) {
      try {
        dataSource.setLoginTimeout(loginTimeout);
      } catch (Exception e) {
        throw new RuntimeException("Failed to set login timeout '" + loginTimeout + "' for the HikariCP datasource", e);
      }
    }

    return dataSource;
  }

  @com.google.inject.Inject(optional = true)
  public void setAllowPoolSuspension(@Named("hikaricp.allowPoolSuspension") boolean allowPoolSuspension) {
    configuration.setAllowPoolSuspension(allowPoolSuspension);
  }

  @com.google.inject.Inject(optional = true)
  public void setAutoCommit(@Named("hikaricp.autoCommit") boolean autoCommit) {
    configuration.setAutoCommit(autoCommit);
  }

  @com.google.inject.Inject(optional = true)
  public void setCatalog(@Named("hikaricp.catalog") String catalog) {
    configuration.setCatalog(catalog);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionInitSql(@Named("hikaricp.connectionInitSql") String connectionInitSql) {
    configuration.setConnectionInitSql(connectionInitSql);
  }

  @com.google.inject.Inject(optional = true)
  public void setConnectionTestQuery(@Named("hikaricp.connectionTestQuery") String connectionTestQuery) {
    configuration.setConnectionTestQuery(connectionTestQuery);
  }

  /**
   * Sets the <code>connectionTimeout</code> HikariCP configuration property.
   *
   * @param connectionTimeoutMs
   *          the connection timeout in milliseconds
   */
  @com.google.inject.Inject(optional = true)
  public void setConnectionTimeout(@Named("hikaricp.connectionTimeoutMs") long connectionTimeoutMs) {
    configuration.setConnectionTimeout(connectionTimeoutMs);
  }

  @com.google.inject.Inject(optional = true)
  public void setDriverClassName(@Named("hikaricp.driverClassName") String driverClassName) {
    configuration.setDriverClassName(driverClassName);
  }

  @com.google.inject.Inject(optional = true)
  public void setHealthCheckProperties(@Named("hikaricp.healthCheckProperties") Properties healthCheckProperties) {
    configuration.setHealthCheckProperties(healthCheckProperties);
  }

  @com.google.inject.Inject(optional = true)
  public void setHealthCheckRegistry(@Named("hikaricp.healthCheckRegistry") Object healthCheckRegistry) {
    configuration.setHealthCheckRegistry(healthCheckRegistry);
  }

  @com.google.inject.Inject(optional = true)
  public void setIdleTimeout(@Named("hikaricp.idleTimeoutMs") long idleTimeoutMs) {
    configuration.setIdleTimeout(idleTimeoutMs);
  }

  @com.google.inject.Inject(optional = true)
  public void setInitializationFailTimeout(
      @Named("hikaricp.initializationFailTimeout") long initializationFailTimeout) {
    configuration.setInitializationFailTimeout(initializationFailTimeout);
  }

  @com.google.inject.Inject(optional = true)
  public void setIsolateInternalQueries(@Named("hikaricp.isolateInternalQueries") boolean isolateInternalQueries) {
    configuration.setIsolateInternalQueries(isolateInternalQueries);
  }

  @Inject
  public void setJdbcUrl(@Named("JDBC.url") String jdbcUrl) {
    configuration.setJdbcUrl(jdbcUrl);
  }

  @com.google.inject.Inject(optional = true)
  public void setLeakDetectionThreshold(@Named("hikaricp.leakDetectionThresholdMs") long leakDetectionThresholdMs) {
    configuration.setLeakDetectionThreshold(leakDetectionThresholdMs);
  }

  @com.google.inject.Inject(optional = true)
  public void setLoginTimeout(@Named("JDBC.loginTimeout") int loginTimeout) {
    this.loginTimeout = loginTimeout;
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxLifetime(@Named("hikaricp.maxLifetimeMs") long maxLifetimeMs) {
    configuration.setMaxLifetime(maxLifetimeMs);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaximumPoolSize(@Named("hikaricp.maxPoolSize") int maxPoolSize) {
    configuration.setMaximumPoolSize(maxPoolSize);
  }

  @com.google.inject.Inject(optional = true)
  public void setMetricRegistry(@Named("hikaricp.metricRegistry") Object metricRegistry) {
    configuration.setMetricRegistry(metricRegistry);
  }

  @com.google.inject.Inject(optional = true)
  public void setMetricsTrackerFactory(
      @Named("hikaricp.metricsTrackerFactory") MetricsTrackerFactory metricsTrackerFactory) {
    configuration.setMetricsTrackerFactory(metricsTrackerFactory);
  }

  @com.google.inject.Inject(optional = true)
  public void setMinimumIdle(@Named("hikaricp.minimumIdle") int minimumIdle) {
    configuration.setMinimumIdle(minimumIdle);
  }

  @Inject
  public void setPassword(@Named("JDBC.password") String password) {
    configuration.setPassword(password);
  }

  @com.google.inject.Inject(optional = true)
  public void setPoolName(@Named("hikaricp.poolName") String poolName) {
    configuration.setPoolName(poolName);
  }

  @com.google.inject.Inject(optional = true)
  public void setReadOnly(@Named("hikaricp.readOnly") boolean readOnly) {
    configuration.setReadOnly(readOnly);
  }

  @com.google.inject.Inject(optional = true)
  public void setRegisterMbeans(@Named("hikaricp.registerMbeans") boolean registerMbeans) {
    configuration.setRegisterMbeans(registerMbeans);
  }

  @com.google.inject.Inject(optional = true)
  public void setScheduledExecutor(
      @Named("hikaricp.scheduledExecutorService") ScheduledExecutorService scheduledExecutorService) {
    configuration.setScheduledExecutor(scheduledExecutorService);
  }

  @com.google.inject.Inject(optional = true)
  public void setSchema(@Named("hikaricp.schema") String schema) {
    configuration.setSchema(schema);
  }

  @com.google.inject.Inject(optional = true)
  public void setThreadFactory(@Named("hikaricp.threadFactory") ThreadFactory threadFactory) {
    configuration.setThreadFactory(threadFactory);
  }

  @com.google.inject.Inject(optional = true)
  public void setTransactionIsolation(@Named("hikaricp.transactionIsolation") String transactionIsolation) {
    configuration.setTransactionIsolation(transactionIsolation);
  }

  @Inject
  public void setUsername(@Named("JDBC.username") String username) {
    configuration.setUsername(username);
  }

  @com.google.inject.Inject(optional = true)
  public void setValidationTimeout(@Named("hikaricp.validationTimeoutMs") long validationTimeoutMs) {
    configuration.setValidationTimeout(validationTimeoutMs);
  }
}
