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
package org.mybatis.guice.datasource.dbcp;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.ConnectionPoolDataSource;

import org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS;

/**
 * Provides the Apache commons-dbcp {@code DriverAdapterCPDS}.
 */
public final class DriverAdapterCPDSProvider implements Provider<ConnectionPoolDataSource> {

  private final DriverAdapterCPDS adapter = new DriverAdapterCPDS();

  /**
   * Instantiates a new driver adapter CPDS provider.
   *
   * @param driver
   *          the driver
   * @param url
   *          the url
   */
  @Inject
  public DriverAdapterCPDSProvider(@Named("JDBC.driver") final String driver, @Named("JDBC.url") final String url) {
    try {
      adapter.setDriver(driver);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Driver '" + driver + "' not found in the classpath", e);
    }
    adapter.setUrl(url);
  }

  /**
   * Sets the user.
   *
   * @param username
   *          the new user
   * @since 3.3
   */
  @com.google.inject.Inject(optional = true)
  public void setUser(@Named("JDBC.username") final String username) {
    adapter.setUser(username);
  }

  /**
   * Sets the password.
   *
   * @param password
   *          the new password
   * @since 3.3
   */
  @com.google.inject.Inject(optional = true)
  public void setPassword(@Named("JDBC.password") final String password) {
    adapter.setPassword(password);
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the new description
   */
  @com.google.inject.Inject(optional = true)
  public void setDescription(@Named("DBCP.description") String description) {
    adapter.setDescription(description);
  }

  @com.google.inject.Inject(optional = true)
  public void setLoginTimeout(@Named("JDBC.loginTimeout") int seconds) {
    adapter.setLoginTimeout(seconds);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxIdle(@Named("DBCP.maxIdle") int maxIdle) {
    adapter.setMaxIdle(maxIdle);
  }

  @com.google.inject.Inject(optional = true)
  public void setMaxPreparedStatements(@Named("DBCP.maxOpenPreparedStatements") int maxPreparedStatements) {
    adapter.setMaxPreparedStatements(maxPreparedStatements);
  }

  @com.google.inject.Inject(optional = true)
  public void setMinEvictableIdleTimeMillis(@Named("DBCP.minEvictableIdleTimeMillis") int minEvictableIdleTimeMillis) {
    adapter.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
  }

  @com.google.inject.Inject(optional = true)
  public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") int numTestsPerEvictionRun) {
    adapter.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
  }

  @com.google.inject.Inject(optional = true)
  public void setPoolPreparedStatements(@Named("DBCP.poolPreparedStatements") boolean poolPreparedStatements) {
    adapter.setPoolPreparedStatements(poolPreparedStatements);
  }

  @com.google.inject.Inject(optional = true)
  public void setTimeBetweenEvictionRunsMillis(
      @Named("DBCP.timeBetweenEvictionRunsMillis") int timeBetweenEvictionRunsMillis) {
    adapter.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
  }

  @Override
  public ConnectionPoolDataSource get() {
    return adapter;
  }

}
