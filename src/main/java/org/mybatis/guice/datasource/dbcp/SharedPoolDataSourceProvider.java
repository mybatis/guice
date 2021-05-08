/*
 *    Copyright 2009-2021 the original author or authors.
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

import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;

/**
 * Provides the Apache commons-dbcp {@code SharedPoolDataSource}.
 */
public final class SharedPoolDataSourceProvider implements Provider<DataSource> {

  private final SharedPoolDataSource dataSource = new SharedPoolDataSource();

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
    dataSource.setLoginTimeout(loginTimeout);
  }

  @com.google.inject.Inject(optional = true)
  public void setDefaultMinEvictableIdleTimeMillis(
      @Named("DBCP.defaultMinEvictableIdleTimeMillis") int defaultMinEvictableIdleTimeMillis) {
    dataSource.setDefaultMinEvictableIdleTimeMillis(defaultMinEvictableIdleTimeMillis);
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
    dataSource.setDefaultTimeBetweenEvictionRunsMillis(defaultTimeBetweenEvictionRunsMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setValidationQuery(@Named("DBCP.validationQuery") String validationQuery) {
    dataSource.setValidationQuery(validationQuery);
  }

  /**
   * Sets the default max total.
   *
   * @param defaultMaxTotal
   *          the new default max total
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxTotal(@Named("DBCP.defaultMaxTotal") final int defaultMaxTotal) {
    dataSource.setDefaultMaxTotal(defaultMaxTotal);
  }

  /**
   * Sets the default max idle.
   *
   * @param defaultMaxIdle
   *          the new default max idle
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxIdle(@Named("DBCP.defaultMaxIdle") final int defaultMaxIdle) {
    dataSource.setDefaultMaxIdle(defaultMaxIdle);
  }

  /**
   * Sets the default max wait in milliseconds.
   *
   * @param defaultMaxWaitMillis
   *          the new default max wait in milliseconds
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultMaxWaitMillis(@Named("DBCP.defaultMaxWaitMillis") final int defaultMaxWaitMillis) {
    dataSource.setDefaultMaxWaitMillis(defaultMaxWaitMillis);
  }

  @Override
  public DataSource get() {
    return dataSource;
  }

}
