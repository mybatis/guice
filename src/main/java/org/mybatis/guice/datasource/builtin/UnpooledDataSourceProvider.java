/*
 *    Copyright 2009-2026 the original author or authors.
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;

/**
 * Provides the myBatis built-in UnpooledDataSource.
 */
public final class UnpooledDataSourceProvider implements Provider<DataSource> {

  /**
   * The UnpooledDataSource reference.
   */
  private final UnpooledDataSource unpooledDataSource;

  /**
   * Creates a new UnpooledDataSource using the needed parameter.
   *
   * @param driver
   *          The JDBC driver class.
   * @param url
   *          the database URL of the form <code>jdbc:subprotocol:subname</code>.
   * @param driverClassLoader
   *          ClassLoader to use to load JDBC driver class.
   */
  @Inject
  public UnpooledDataSourceProvider(@Named("JDBC.driver") final String driver, @Named("JDBC.url") final String url,
      @Named("JDBC.driverClassLoader") final ClassLoader driverClassLoader) {
    unpooledDataSource = new UnpooledDataSource(driverClassLoader, driver, url, null, null);
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
    unpooledDataSource.setUsername(username);
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
    unpooledDataSource.setPassword(password);
  }

  /**
   * Sets the auto commit.
   *
   * @param autoCommit
   *          the new auto commit
   */
  @com.google.inject.Inject(optional = true)
  public void setAutoCommit(@Named("JDBC.autoCommit") final boolean autoCommit) {
    unpooledDataSource.setAutoCommit(autoCommit);
  }

  /**
   * Sets the login timeout.
   *
   * @param loginTimeout
   *          the new login timeout
   */
  @com.google.inject.Inject(optional = true)
  public void setLoginTimeout(@Named("JDBC.loginTimeout") final int loginTimeout) {
    unpooledDataSource.setLoginTimeout(loginTimeout);
  }

  @com.google.inject.Inject(optional = true)
  public void setDriverProperties(@Named("JDBC.driverProperties") final Properties driverProperties) {
    unpooledDataSource.setDriverProperties(driverProperties);
  }

  @Override
  public DataSource get() {
    return unpooledDataSource;
  }

}
