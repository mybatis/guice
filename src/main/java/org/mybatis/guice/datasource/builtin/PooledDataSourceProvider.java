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
package org.mybatis.guice.datasource.builtin;

import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;

/**
 * Provides the myBatis built-in PooledDataSource.
 */
public final class PooledDataSourceProvider implements Provider<DataSource> {

  /**
   * The PooledDataSource reference.
   */
  private final PooledDataSource dataSource;

  /**
   * Creates a new PooledDataSource using the needed parameter.
   *
   * @param driver
   *          The JDBC driver class.
   * @param url
   *          the database URL of the form <code>jdbc:subprotocol:subname</code>.
   * @param driverClassLoader
   *          ClassLoader to use to load JDBC driver class.
   */
  @Inject
  public PooledDataSourceProvider(@Named("JDBC.driver") final String driver, @Named("JDBC.url") final String url,
      @Named("JDBC.driverClassLoader") final ClassLoader driverClassLoader) {
    dataSource = new PooledDataSource(driverClassLoader, driver, url, null, null);
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
   * Sets the login timeout.
   *
   * @param loginTimeout
   *          the new login timeout
   */
  @com.google.inject.Inject(optional = true)
  public void setLoginTimeout(@Named("JDBC.loginTimeout") final int loginTimeout) {
    dataSource.setLoginTimeout(loginTimeout);
  }

  @com.google.inject.Inject(optional = true)
  public void setDriverProperties(@Named("JDBC.driverProperties") final Properties driverProperties) {
    dataSource.setDriverProperties(driverProperties);
  }

  /**
   * Sets the maximum active connections.
   *
   * @param maximumActiveConnections
   *          the new maximum active connections
   */
  @com.google.inject.Inject(optional = true)
  public void setMaximumActiveConnections(
      @Named("mybatis.pooled.maximumActiveConnections") final int maximumActiveConnections) {
    dataSource.setPoolMaximumActiveConnections(maximumActiveConnections);
  }

  /**
   * Sets the maximum checkout time.
   *
   * @param maximumCheckoutTime
   *          the new maximum checkout time
   */
  @com.google.inject.Inject(optional = true)
  public void setMaximumCheckoutTime(@Named("mybatis.pooled.maximumCheckoutTime") final int maximumCheckoutTime) {
    dataSource.setPoolMaximumCheckoutTime(maximumCheckoutTime);
  }

  /**
   * Sets the maximum idle connections.
   *
   * @param maximumIdleConnections
   *          the new maximum idle connections
   */
  @com.google.inject.Inject(optional = true)
  public void setMaximumIdleConnections(
      @Named("mybatis.pooled.maximumIdleConnections") final int maximumIdleConnections) {
    dataSource.setPoolMaximumIdleConnections(maximumIdleConnections);
  }

  /**
   * Sets the ping connections not used for.
   *
   * @param pingConnectionsNotUsedFor
   *          the new ping connections not used for
   */
  @com.google.inject.Inject(optional = true)
  public void setPingConnectionsNotUsedFor(
      @Named("mybatis.pooled.pingConnectionsNotUsedFor") final int pingConnectionsNotUsedFor) {
    dataSource.setPoolPingConnectionsNotUsedFor(pingConnectionsNotUsedFor);
  }

  /**
   * Sets the ping enabled.
   *
   * @param pingEnabled
   *          the new ping enabled
   */
  @com.google.inject.Inject(optional = true)
  public void setPingEnabled(@Named("mybatis.pooled.pingEnabled") final boolean pingEnabled) {
    dataSource.setPoolPingEnabled(pingEnabled);
  }

  /**
   * Sets the ping enabled.
   *
   * @param pingQuery
   *          the new ping enabled
   */
  @com.google.inject.Inject(optional = true)
  public void setPingEnabled(@Named("mybatis.pooled.pingQuery") final String pingQuery) {
    dataSource.setPoolPingQuery(pingQuery);
  }

  /**
   * Sets the time to wait.
   *
   * @param timeToWait
   *          the new time to wait
   */
  @com.google.inject.Inject(optional = true)
  public void setTimeToWait(@Named("mybatis.pooled.timeToWait") final int timeToWait) {
    dataSource.setPoolTimeToWait(timeToWait);
  }

  @Override
  public DataSource get() {
    return dataSource;
  }

}
