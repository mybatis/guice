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

import org.apache.commons.dbcp.BasicDataSource;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Properties;

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
   * @param driver The JDBC driver class.
   * @param url the database URL of the form <code>jdbc:subprotocol:subname</code>.
   * @param driverClassLoader ClassLoader to use to load JDBC driver class.
   */
  @Inject
  public BasicDataSourceProvider(@Named("JDBC.driver") final String driver, @Named("JDBC.url") final String url,
      @Named("JDBC.driverClassLoader") final ClassLoader driverClassLoader) {
    dataSource.setDriverClassLoader(driverClassLoader);
    dataSource.setDriverClassName(driver);
    dataSource.setUrl(url);
  }

  /**
   *
   * @param username
   * @since 3.3
   */
  @com.google.inject.Inject(optional = true)
  public void setUser(@Named("JDBC.username") final String username) {
    dataSource.setUsername(username);
  }

  /**
   *
   * @param password
   * @since 3.3
   */
  @com.google.inject.Inject(optional = true)
  public void setPassword(@Named("JDBC.password") final String password) {
    dataSource.setPassword(password);
  }

  /**
   *
   *
   * @param autoCommit
   */
  @com.google.inject.Inject(optional = true)
  public void setAutoCommit(@Named("JDBC.autoCommit") final boolean autoCommit) {
    dataSource.setDefaultAutoCommit(autoCommit);
  }

  /**
   *
   *
   * @param loginTimeout
   */
  @com.google.inject.Inject(optional = true)
  public void setLoginTimeout(@Named("JDBC.loginTimeout") final int loginTimeout) {
    try {
      dataSource.setLoginTimeout(loginTimeout);
    } catch (SQLException e) {
      throw new RuntimeException("Impossible to set DBCP login timeout '" + loginTimeout + "', see nested exceptions",
          e);
    }
  }

  /**
   *
   * @param driverProperties
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
   *
   *
   * @param allow
   */
  @com.google.inject.Inject(optional = true)
  public void setAccessToUnderlyingConnectionAllowed(
      @Named("DBCP.accessToUnderlyingConnectionAllowed") final boolean allow) {
    dataSource.setAccessToUnderlyingConnectionAllowed(allow);
  }

  /**
   *
   *
   * @param defaultCatalog
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultCatalog(@Named("DBCP.defaultCatalog") final String defaultCatalog) {
    dataSource.setDefaultCatalog(defaultCatalog);
  }

  /**
   *
   *
   * @param defaultReadOnly
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultReadOnly(@Named("DBCP.defaultReadOnly") final boolean defaultReadOnly) {
    dataSource.setDefaultReadOnly(defaultReadOnly);
  }

  /**
   *
   *
   * @param defaultTransactionIsolation
   */
  @com.google.inject.Inject(optional = true)
  public void setDefaultTransactionIsolation(
      @Named("DBCP.defaultTransactionIsolation") final int defaultTransactionIsolation) {
    dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
  }

  /**
   *
   *
   * @param initialSize
   */
  @com.google.inject.Inject(optional = true)
  public void setInitialSize(@Named("DBCP.initialSize") final int initialSize) {
    dataSource.setInitialSize(initialSize);
  }

  /**
   *
   *
   * @param maxActive
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxActive(@Named("DBCP.maxActive") final int maxActive) {
    dataSource.setMaxActive(maxActive);
  }

  /**
   *
   *
   * @param maxIdle
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxIdle(@Named("DBCP.maxIdle") final int maxIdle) {
    dataSource.setMaxIdle(maxIdle);
  }

  /**
   *
   *
   * @param maxOpenPreparedStatements
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxOpenPreparedStatements(
      @Named("DBCP.maxOpenPreparedStatements") final int maxOpenPreparedStatements) {
    dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
  }

  /**
   *
   *
   * @param maxWait
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxWait(@Named("DBCP.maxWait") final long maxWait) {
    dataSource.setMaxWait(maxWait);
  }

  /**
   *
   *
   * @param minEvictableIdleTimeMillis
   */
  @com.google.inject.Inject(optional = true)
  public void setMinEvictableIdleTimeMillis(
      @Named("DBCP.minEvictableIdleTimeMillis") final long minEvictableIdleTimeMillis) {
    dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
  }

  /**
   *
   *
   * @param minIdle
   */
  @com.google.inject.Inject(optional = true)
  public void setMinIdle(@Named("DBCP.minIdle") final int minIdle) {
    dataSource.setMinIdle(minIdle);
  }

  /**
   *
   *
   * @param numTestsPerEvictionRun
   */
  @com.google.inject.Inject(optional = true)
  public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") final int numTestsPerEvictionRun) {
    dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
  }

  /**
   *
   *
   * @param poolPreparedStatements
   */
  @com.google.inject.Inject(optional = true)
  public void setPoolPreparedStatements(@Named("DBCP.poolPreparedStatements") final boolean poolPreparedStatements) {
    dataSource.setPoolPreparedStatements(poolPreparedStatements);
  }

  /**
   *
   *
   * @param testOnBorrow
   */
  @com.google.inject.Inject(optional = true)
  public void setTestOnBorrow(@Named("DBCP.testOnBorrow") final boolean testOnBorrow) {
    dataSource.setTestOnBorrow(testOnBorrow);
  }

  /**
   *
   *
   * @param testOnReturn
   */
  @com.google.inject.Inject(optional = true)
  public void setTestOnReturn(@Named("DBCP.testOnReturn") final boolean testOnReturn) {
    dataSource.setTestOnReturn(testOnReturn);
  }

  /**
   *
   *
   * @param testWhileIdle
   */
  @com.google.inject.Inject(optional = true)
  public void setTestWhileIdle(@Named("DBCP.testWhileIdle") final boolean testWhileIdle) {
    dataSource.setTestWhileIdle(testWhileIdle);
  }

  /**
   *
   *
   * @param timeBetweenEvictionRunsMillis
   */
  @com.google.inject.Inject(optional = true)
  public void setTimeBetweenEvictionRunsMillis(
      @Named("DBCP.timeBetweenEvictionRunsMillis") int timeBetweenEvictionRunsMillis) {
    dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
  }

  /**
   *
   *
   * @param validationQuery
   */
  @com.google.inject.Inject(optional = true)
  public void setValidationQuery(@Named("DBCP.validationQuery") final String validationQuery) {
    dataSource.setValidationQuery(validationQuery);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataSource get() {
    return dataSource;
  }

}
