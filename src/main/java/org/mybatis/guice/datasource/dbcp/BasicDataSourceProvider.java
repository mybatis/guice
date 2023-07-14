/*
 *    Copyright 2009-2023 the original author or authors.
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;

import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Provides the Apache commons-dbcp {@code BasicDataSource}.
 */
public final class BasicDataSourceProvider implements Provider<DataSource> {

  /**
   * The BasicDataSource reference.
   */
  private final BasicDataSource dataSource = new BasicDataSource();

  /**
   * Creates a new BasicDataSource using the needed parameter.
   *
   * @param driver
   *          The JDBC driver class.
   * @param url
   *          the database URL of the form <code>jdbc:subprotocol:subname</code>.
   * @param driverClassLoader
   *          ClassLoader to use to load JDBC driver class.
   */
  @Inject
  public BasicDataSourceProvider(@Named("JDBC.driver") final String driver, @Named("JDBC.url") final String url,
      @Named("JDBC.driverClassLoader") final ClassLoader driverClassLoader) {
    dataSource.setDriverClassLoader(driverClassLoader);
    dataSource.setDriverClassName(driver);
    dataSource.setUrl(url);
  }

  /**
   * Sets the user.
   *
   * @param username
   *          the new user
   *
   * @since 3.3
   */
  @com.google.inject.Inject(optional = true)
  public void setUser(@Named("JDBC.username") final String username) {
    dataSource.setUsername(username);
  }

  /**
   * Sets the password.
   *
   * @param password
   *          the new password
   *
   * @since 3.3
   */
  @com.google.inject.Inject(optional = true)
  public void setPassword(@Named("JDBC.password") final String password) {
    dataSource.setPassword(password);
  }

  /**
   * Sets the auto commit.
   *
   * @param autoCommit
   *          the new auto commit
   */
  @com.google.inject.Inject(optional = true)
  public void setAutoCommit(@Named("JDBC.autoCommit") final boolean autoCommit) {
    dataSource.setDefaultAutoCommit(autoCommit);
  }

  /**
   * Sets the driver properties.
   *
   * @param driverProperties
   *          the new driver properties
   */
  @com.google.inject.Inject(optional = true)
  public void setDriverProperties(@Named("JDBC.driverProperties") final Properties driverProperties) {
    for (Entry<Object, Object> property : driverProperties.entrySet()) {
      String name = property.getKey().toString();
      String value = property.getValue().toString();
      dataSource.addConnectionProperty(name, value);
    }
  }

  /**
   * Sets the access to underlying connection allowed.
   *
   * @param allow
   *          the new access to underlying connection allowed
   */
  @com.google.inject.Inject(optional = true)
  public void setAccessToUnderlyingConnectionAllowed(
      @Named("DBCP.accessToUnderlyingConnectionAllowed") final boolean allow) {
    dataSource.setAccessToUnderlyingConnectionAllowed(allow);
  }

  /**
   * Sets the default catalog.
   *
   * @param defaultCatalog
   *          the new default catalog
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultCatalog(@Named("DBCP.defaultCatalog") final String defaultCatalog) {
    dataSource.setDefaultCatalog(defaultCatalog);
  }

  /**
   * Sets the default read only.
   *
   * @param defaultReadOnly
   *          the new default read only
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultReadOnly(@Named("DBCP.defaultReadOnly") final boolean defaultReadOnly) {
    dataSource.setDefaultReadOnly(defaultReadOnly);
  }

  /**
   * Sets the default transaction isolation.
   *
   * @param defaultTransactionIsolation
   *          the new default transaction isolation
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultTransactionIsolation(
      @Named("DBCP.defaultTransactionIsolation") final int defaultTransactionIsolation) {
    dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
  }

  /**
   * Sets the initial size.
   *
   * @param initialSize
   *          the new initial size
   */
  @com.google.inject.Inject(optional = true)
  public void setInitialSize(@Named("DBCP.initialSize") final int initialSize) {
    dataSource.setInitialSize(initialSize);
  }

  /**
   * Sets the max total.
   *
   * @param maxTotal
   *          the new max total
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxTotal(@Named("DBCP.maxTotal") final int maxTotal) {
    dataSource.setMaxTotal(maxTotal);
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
   * Sets the max open prepared statements.
   *
   * @param maxOpenPreparedStatements
   *          the new max open prepared statements
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxOpenPreparedStatements(
      @Named("DBCP.maxOpenPreparedStatements") final int maxOpenPreparedStatements) {
    dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
  }

  /**
   * Sets the max wait in milliseconds.
   *
   * @param maxWaitMillis
   *          the new max wait in milliseconds
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxWaitMillis(@Named("DBCP.maxWaitMillis") final long maxWaitMillis) {
    dataSource.setMaxWaitMillis(maxWaitMillis);
  }

  /**
   * Sets the min evictable idle time millis.
   *
   * @param minEvictableIdleTimeMillis
   *          the new min evictable idle time millis
   */
  @com.google.inject.Inject(optional = true)
  public void setMinEvictableIdleTimeMillis(
      @Named("DBCP.minEvictableIdleTimeMillis") final long minEvictableIdleTimeMillis) {
    dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
  }

  /**
   * Sets the min idle.
   *
   * @param minIdle
   *          the new min idle
   */
  @com.google.inject.Inject(optional = true)
  public void setMinIdle(@Named("DBCP.minIdle") final int minIdle) {
    dataSource.setMinIdle(minIdle);
  }

  /**
   * Sets the num tests per eviction run.
   *
   * @param numTestsPerEvictionRun
   *          the new num tests per eviction run
   */
  @com.google.inject.Inject(optional = true)
  public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") final int numTestsPerEvictionRun) {
    dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
  }

  /**
   * Sets the pool prepared statements.
   *
   * @param poolPreparedStatements
   *          the new pool prepared statements
   */
  @com.google.inject.Inject(optional = true)
  public void setPoolPreparedStatements(@Named("DBCP.poolPreparedStatements") final boolean poolPreparedStatements) {
    dataSource.setPoolPreparedStatements(poolPreparedStatements);
  }

  /**
   * Sets the test on borrow.
   *
   * @param testOnBorrow
   *          the new test on borrow
   */
  @com.google.inject.Inject(optional = true)
  public void setTestOnBorrow(@Named("DBCP.testOnBorrow") final boolean testOnBorrow) {
    dataSource.setTestOnBorrow(testOnBorrow);
  }

  /**
   * Sets the test on return.
   *
   * @param testOnReturn
   *          the new test on return
   */
  @com.google.inject.Inject(optional = true)
  public void setTestOnReturn(@Named("DBCP.testOnReturn") final boolean testOnReturn) {
    dataSource.setTestOnReturn(testOnReturn);
  }

  /**
   * Sets the test while idle.
   *
   * @param testWhileIdle
   *          the new test while idle
   */
  @com.google.inject.Inject(optional = true)
  public void setTestWhileIdle(@Named("DBCP.testWhileIdle") final boolean testWhileIdle) {
    dataSource.setTestWhileIdle(testWhileIdle);
  }

  /**
   * Sets the time between eviction runs millis.
   *
   * @param timeBetweenEvictionRunsMillis
   *          the new time between eviction runs millis
   */
  @com.google.inject.Inject(optional = true)
  public void setTimeBetweenEvictionRunsMillis(
      @Named("DBCP.timeBetweenEvictionRunsMillis") int timeBetweenEvictionRunsMillis) {
    dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
  }

  /**
   * Sets the validation query.
   *
   * @param validationQuery
   *          the new validation query
   */
  @com.google.inject.Inject(optional = true)
  public void setValidationQuery(@Named("DBCP.validationQuery") final String validationQuery) {
    dataSource.setValidationQuery(validationQuery);
  }

  @Override
  public DataSource get() {
    return dataSource;
  }

}
