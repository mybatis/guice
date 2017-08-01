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

import com.google.inject.Provider;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

/**
 * Provides the Apache commons-dbcp {@code SharedPoolDataSource}.
 */
public final class SharedPoolDataSourceProvider implements Provider<DataSource> {

  private final SharedPoolDataSource dataSource = new SharedPoolDataSource();

  @Inject
  public SharedPoolDataSourceProvider(ConnectionPoolDataSource cpds) {
    dataSource.setConnectionPoolDataSource(cpds);
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
  public void setMinEvictableIdleTimeMillis(@Named("DBCP.minEvictableIdleTimeMillis") int minEvictableIdleTimeMillis) {
    dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") int numTestsPerEvictionRun) {
    dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
  }

  @com.google.inject.Inject(optional = true)
  public void setRollbackAfterValidation(@Named("DBCP.rollbackAfterValidation") boolean rollbackAfterValidation) {
    dataSource.setRollbackAfterValidation(rollbackAfterValidation);
  }

  @com.google.inject.Inject(optional = true)
  public void setTestOnBorrow(@Named("DBCP.testOnBorrow") boolean testOnBorrow) {
    dataSource.setTestOnBorrow(testOnBorrow);
  }

  @com.google.inject.Inject(optional = true)
  public void setTestOnReturn(@Named("DBCP.testOnReturn") boolean testOnReturn) {
    dataSource.setTestOnReturn(testOnReturn);
  }

  @com.google.inject.Inject(optional = true)
  public void setTestWhileIdle(@Named("DBCP.testWhileIdle") boolean testWhileIdle) {
    dataSource.setTestWhileIdle(testWhileIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setTimeBetweenEvictionRunsMillis(
      @Named("DBCP.timeBetweenEvictionRunsMillis") int timeBetweenEvictionRunsMillis) {
    dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setValidationQuery(@Named("DBCP.validationQuery") String validationQuery) {
    dataSource.setValidationQuery(validationQuery);
  }

  /**
   * Sets the max active.
   *
   * @param maxActive
   *          the new max active
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxActive(@Named("DBCP.maxActive") final int maxActive) {
    dataSource.setMaxActive(maxActive);
  }

  /**
   * Sets the max idle.
   *
   * @param maxIdle
   *          the new max idle
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxIdle(@Named("DBCP.maxIdle") final int maxIdle) {
    dataSource.setMaxIdle(maxIdle);
  }

  /**
   * Sets the max wait.
   *
   * @param maxWait
   *          the new max wait
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxWait(@Named("DBCP.maxWait") final int maxWait) {
    dataSource.setMaxWait(maxWait);
  }

  @Override
  public DataSource get() {
    return dataSource;
  }

}
