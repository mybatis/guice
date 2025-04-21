/*
 *    Copyright 2009-2025 the original author or authors.
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
package org.mybatis.guice.datasource.dbcp;

import jakarta.inject.Named;
import jakarta.inject.Provider;

import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.datasources.PerUserPoolDataSource;

/**
 * Provides the Apache commons-dbcp {@code PerUserPoolDataSource}.
 */
public final class PerUserPoolDataSourceProvider implements Provider<DataSource> {

  private final PerUserPoolDataSource dataSource = new PerUserPoolDataSource();

  @com.google.inject.Inject(optional = true)
  public void setConnectionPoolDataSource(ConnectionPoolDataSource cpds) {
    dataSource.setConnectionPoolDataSource(cpds);
  }

  @com.google.inject.Inject(optional = true)
  public void setDataSourceName(@Named("DBCP.name") String name) {
    dataSource.setDataSourceName(name);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultAutoCommit(@Named("JDBC.autoCommit") boolean autoCommit) {
    dataSource.setDefaultAutoCommit(autoCommit);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultReadOnly(@Named("DBCP.defaultReadOnly") boolean defaultReadOnly) {
    dataSource.setDefaultReadOnly(defaultReadOnly);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTransactionIsolation(
      @Named("DBCP.defaultTransactionIsolation") int defaultTransactionIsolation) {
    dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
  }

  @com.google.inject.Inject(optional = true)
  public void setDescription(@Named("DBCP.description") String description) {
    dataSource.setDescription(description);
  }

  @com.google.inject.Inject(optional = true)
  public void setJndiEnvironment(@Named("DBCP.jndi.key") String key, @Named("DBCP.jndi.value") String value) {
    dataSource.setJndiEnvironment(key, value);
  }

  @com.google.inject.Inject(optional = true)
  public void setLoginTimeout(@Named("JDBC.loginTimeout") int loginTimeout) {
    dataSource.setLoginTimeout(Duration.ofSeconds(loginTimeout));
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMinEvictableIdleTimeMillis(
      @Named("DBCP.defaultMinEvictableIdleTimeMillis") int defaultMinEvictableIdleTimeMillis) {
    dataSource.setDefaultMinEvictableIdle(Duration.ofMillis(defaultMinEvictableIdleTimeMillis));
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultNumTestsPerEvictionRun(
      @Named("DBCP.defaultNumTestsPerEvictionRun") int defaultNumTestsPerEvictionRun) {
    dataSource.setDefaultNumTestsPerEvictionRun(defaultNumTestsPerEvictionRun);
  }

  @com.google.inject.Inject(optional = true)
  public void setRollbackAfterValidation(@Named("DBCP.rollbackAfterValidation") boolean rollbackAfterValidation) {
    dataSource.setRollbackAfterValidation(rollbackAfterValidation);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTestOnBorrow(@Named("DBCP.defaultTestOnBorrow") boolean defaultTestOnBorrow) {
    dataSource.setDefaultTestOnBorrow(defaultTestOnBorrow);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTestOnReturn(@Named("DBCP.defaultTestOnReturn") boolean defaultTestOnReturn) {
    dataSource.setDefaultTestOnReturn(defaultTestOnReturn);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTestWhileIdle(@Named("DBCP.defaultTestWhileIdle") boolean defaultTestWhileIdle) {
    dataSource.setDefaultTestWhileIdle(defaultTestWhileIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultTimeBetweenEvictionRunsMillis(
      @Named("DBCP.defaultTimeBetweenEvictionRunsMillis") int defaultTimeBetweenEvictionRunsMillis) {
    dataSource.setDefaultDurationBetweenEvictionRuns(Duration.ofMillis(defaultTimeBetweenEvictionRunsMillis));
  }

  @com.google.inject.Inject(optional = true)
  public void setValidationQuery(@Named("DBCP.validationQuery") String validationQuery) {
    dataSource.setValidationQuery(validationQuery);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxTotal(@Named("DBCP.defaultMaxTotal") int defaultMaxTotal) {
    dataSource.setDefaultMaxTotal(defaultMaxTotal);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxIdle(@Named("DBCP.defaultMaxIdle") int defaultMaxIdle) {
    dataSource.setDefaultMaxIdle(defaultMaxIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxWaitMillis(@Named("DBCP.defaultMaxWaitMillis") int defaultMaxWaitMillis) {
    dataSource.setDefaultMaxWait(Duration.ofMillis(defaultMaxWaitMillis));
  }

  /**
   * Sets the per user default auto commit.
   *
   * @param perUserDefaultAutoCommit
   *          the per user default auto commit
   */
  @com.google.inject.Inject(optional = true)
  public void setPerUserDefaultAutoCommit(@PerUserDefaultAutoCommit Map<String, Boolean> perUserDefaultAutoCommit) {
    for (Entry<String, Boolean> entry : perUserDefaultAutoCommit.entrySet()) {
      dataSource.setPerUserDefaultAutoCommit(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Sets the per user default read only.
   *
   * @param perUserDefaultReadOnly
   *          the per user default read only
   */
  @com.google.inject.Inject(optional = true)
  public void setPerUserDefaultReadOnly(@PerUserDefaultReadOnly Map<String, Boolean> perUserDefaultReadOnly) {
    for (Entry<String, Boolean> entry : perUserDefaultReadOnly.entrySet()) {
      dataSource.setPerUserDefaultReadOnly(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Sets the per user default transaction isolation.
   *
   * @param perUserDefaultTransactionIsolation
   *          the per user default transaction isolation
   */
  @com.google.inject.Inject(optional = true)
  public void setPerUserDefaultTransactionIsolation(
      @PerUserDefaultTransactionIsolation Map<String, Integer> perUserDefaultTransactionIsolation) {
    for (Entry<String, Integer> entry : perUserDefaultTransactionIsolation.entrySet()) {
      dataSource.setPerUserDefaultTransactionIsolation(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Sets the per user max total.
   *
   * @param perUserMaxTotal
   *          the per user max total
   */
  @com.google.inject.Inject(optional = true)
  public void setPerUserMaxTotal(@PerUserMaxTotal Map<String, Integer> perUserMaxTotal) {
    for (Entry<String, Integer> entry : perUserMaxTotal.entrySet()) {
      dataSource.setPerUserMaxTotal(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Sets the per user max idle.
   *
   * @param perUserMaxIdle
   *          the per user max idle
   */
  @com.google.inject.Inject(optional = true)
  public void setPerUserMaxIdle(@PerUserMaxIdle Map<String, Integer> perUserMaxIdle) {
    for (Entry<String, Integer> entry : perUserMaxIdle.entrySet()) {
      dataSource.setPerUserMaxIdle(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Sets the per user max wait in milliseconds.
   *
   * @param perUserMaxWaitMillis
   *          the per user max wait in milliseconds
   */
  @com.google.inject.Inject(optional = true)
  public void setPerUserMaxWaitMillis(@PerUserMaxWaitMillis Map<String, Long> perUserMaxWaitMillis) {
    for (Entry<String, Long> entry : perUserMaxWaitMillis.entrySet()) {
      dataSource.setPerUserMaxWait(entry.getKey(), Duration.ofMillis(entry.getValue()));
    }
  }

  @Override
  public DataSource get() {
    return dataSource;
  }

}
