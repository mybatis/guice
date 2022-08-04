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
package org.mybatis.guice.datasource.c3p0;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

/**
 * Provides the C3P0 DataSource.
 */
public final class C3p0DataSourceProvider implements Provider<DataSource> {

  /**
   * The ComboPooledDataSource reference.
   */
  private final ComboPooledDataSource dataSource = new ComboPooledDataSource();
  private String username;
  private String password;

  /**
   * Creates a new ComboPooledDataSource using the needed parameter.
   *
   * @param driver
   *          The JDBC driver class.
   * @param url
   *          the database URL of the form <code>jdbc:subprotocol:subname</code>.
   */
  @Inject
  public C3p0DataSourceProvider(@Named("JDBC.driver") final String driver, @Named("JDBC.url") final String url) {
    try {
      dataSource.setDriverClass(driver);
    } catch (PropertyVetoException e) {
      throw new RuntimeException(
          "Impossible to initialize C3P0 Data Source with driver class '" + driver + "', see nested exceptions", e);
    }
    dataSource.setJdbcUrl(url);
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
    this.username = username;
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
    this.password = password;
  }

  /**
   * Sets the acquire increment.
   *
   * @param acquireIncrement
   *          the new acquire increment
   */
  @com.google.inject.Inject(optional = true)
  public void setAcquireIncrement(@Named("c3p0.acquireIncrement") final int acquireIncrement) {
    dataSource.setAcquireIncrement(acquireIncrement);
  }

  /**
   * Sets the acquire retry attempts.
   *
   * @param acquireRetryAttempts
   *          the new acquire retry attempts
   */
  @com.google.inject.Inject(optional = true)
  public void setAcquireRetryAttempts(@Named("c3p0.acquireRetryAttempts") final int acquireRetryAttempts) {
    dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
  }

  /**
   * Sets the acquire retry delay.
   *
   * @param acquireRetryDelay
   *          the new acquire retry delay
   */
  @com.google.inject.Inject(optional = true)
  public void setAcquireRetryDelay(@Named("c3p0.acquireRetryDelay") final int acquireRetryDelay) {
    dataSource.setAcquireRetryDelay(acquireRetryDelay);
  }

  /**
   * Sets the auto commit on close.
   *
   * @param autoCommit
   *          the new auto commit on close
   */
  @com.google.inject.Inject(optional = true)
  public void setAutoCommitOnClose(@Named("JDBC.autoCommit") final boolean autoCommit) {
    dataSource.setAutoCommitOnClose(autoCommit);
  }

  /**
   * Sets the driver properties.
   *
   * @param driverProperties
   *          the new driver properties
   */
  @com.google.inject.Inject(optional = true)
  public void setDriverProperties(@Named("JDBC.driverProperties") final Properties driverProperties) {
    dataSource.setProperties(driverProperties);
  }

  /**
   * Sets the aautomatic test table.
   *
   * @param automaticTestTable
   *          the new aautomatic test table
   */
  @com.google.inject.Inject(optional = true)
  public void setAautomaticTestTable(@Named("c3p0.automaticTestTable") final String automaticTestTable) {
    dataSource.setAutomaticTestTable(automaticTestTable);
  }

  /**
   * Sets the break after acquire failure.
   *
   * @param breakAfterAcquireFailure
   *          the new break after acquire failure
   */
  @com.google.inject.Inject(optional = true)
  public void setBreakAfterAcquireFailure(
      @Named("c3p0.breakAfterAcquireFailure") final boolean breakAfterAcquireFailure) {
    dataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
  }

  /**
   * Sets the checkout timeout.
   *
   * @param checkoutTimeout
   *          the new checkout timeout
   */
  @com.google.inject.Inject(optional = true)
  public void setCheckoutTimeout(@Named("c3p0.checkoutTimeout") final int checkoutTimeout) {
    dataSource.setCheckoutTimeout(checkoutTimeout);
  }

  /**
   * Sets the connection customizer class name.
   *
   * @param connectionCustomizerClassName
   *          the new connection customizer class name
   */
  @com.google.inject.Inject(optional = true)
  public void setConnectionCustomizerClassName(
      @Named("c3p0.connectionCustomizerClassName") final String connectionCustomizerClassName) {
    dataSource.setConnectionCustomizerClassName(connectionCustomizerClassName);
  }

  /**
   * Sets the connection tester class name.
   *
   * @param connectionTesterClassName
   *          the new connection tester class name
   */
  @com.google.inject.Inject(optional = true)
  public void setConnectionTesterClassName(
      @Named("c3p0.connectionTesterClassName") final String connectionTesterClassName) {
    try {
      dataSource.setConnectionTesterClassName(connectionTesterClassName);
    } catch (PropertyVetoException e) {
      throw new RuntimeException("Impossible to set C3P0 Data Source connection tester class name '"
          + connectionTesterClassName + "', see nested exceptions", e);
    }
  }

  /**
   * Sets the idle connection test period.
   *
   * @param idleConnectionTestPeriod
   *          the new idle connection test period
   */
  @com.google.inject.Inject(optional = true)
  public void setIdleConnectionTestPeriod(@Named("c3p0.idleConnectionTestPeriod") final int idleConnectionTestPeriod) {
    dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
  }

  /**
   * Sets the initial pool size.
   *
   * @param initialPoolSize
   *          the new initial pool size
   */
  @com.google.inject.Inject(optional = true)
  public void setInitialPoolSize(@Named("c3p0.initialPoolSize") final int initialPoolSize) {
    dataSource.setInitialPoolSize(initialPoolSize);
  }

  /**
   * Sets the max administrative task time.
   *
   * @param maxAdministrativeTaskTime
   *          the new max administrative task time
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxAdministrativeTaskTime(
      @Named("c3p0.maxAdministrativeTaskTime") final int maxAdministrativeTaskTime) {
    dataSource.setMaxAdministrativeTaskTime(maxAdministrativeTaskTime);
  }

  /**
   * Sets the max connection age.
   *
   * @param maxConnectionAge
   *          the new max connection age
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxConnectionAge(@Named("c3p0.maxConnectionAge") final int maxConnectionAge) {
    dataSource.setMaxConnectionAge(maxConnectionAge);
  }

  /**
   * Sets the max idle time.
   *
   * @param maxIdleTime
   *          the new max idle time
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxIdleTime(@Named("c3p0.maxIdleTime") final int maxIdleTime) {
    dataSource.setMaxIdleTime(maxIdleTime);
  }

  /**
   * Sets the max idle time excess connections.
   *
   * @param maxIdleTimeExcessConnections
   *          the new max idle time excess connections
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxIdleTimeExcessConnections(
      @Named("c3p0.maxIdleTimeExcessConnections") final int maxIdleTimeExcessConnections) {
    dataSource.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
  }

  /**
   * Sets the max pool size.
   *
   * @param maxPoolSize
   *          the new max pool size
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxPoolSize(@Named("c3p0.maxPoolSize") final int maxPoolSize) {
    dataSource.setMaxPoolSize(maxPoolSize);
  }

  /**
   * Sets the max statements.
   *
   * @param maxStatements
   *          the new max statements
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxStatements(@Named("c3p0.maxStatements") final int maxStatements) {
    dataSource.setMaxStatements(maxStatements);
  }

  /**
   * Sets the max statements per connection.
   *
   * @param maxStatementsPerConnection
   *          the new max statements per connection
   */
  @com.google.inject.Inject(optional = true)
  public void setMaxStatementsPerConnection(
      @Named("c3p0.maxStatementsPerConnection") final int maxStatementsPerConnection) {
    dataSource.setMaxStatementsPerConnection(maxStatementsPerConnection);
  }

  /**
   * Sets the min pool size.
   *
   * @param minPoolSize
   *          the new min pool size
   */
  @com.google.inject.Inject(optional = true)
  public void setMinPoolSize(@Named("c3p0.minPoolSize") final int minPoolSize) {
    dataSource.setMinPoolSize(minPoolSize);
  }

  /**
   * Sets the preferred test query.
   *
   * @param preferredTestQuery
   *          the new preferred test query
   */
  @com.google.inject.Inject(optional = true)
  public void setPreferredTestQuery(@Named("c3p0.preferredTestQuery") final String preferredTestQuery) {
    dataSource.setPreferredTestQuery(preferredTestQuery);
  }

  /**
   * Sets the property cycle.
   *
   * @param propertyCycle
   *          the new property cycle
   */
  @com.google.inject.Inject(optional = true)
  public void setPropertyCycle(@Named("c3p0.propertyCycle") final int propertyCycle) {
    dataSource.setPropertyCycle(propertyCycle);
  }

  /**
   * Sets the test connection on checkin.
   *
   * @param testConnectionOnCheckin
   *          the new test connection on checkin
   */
  @com.google.inject.Inject(optional = true)
  public void setTestConnectionOnCheckin(@Named("c3p0.testConnectionOnCheckin") final boolean testConnectionOnCheckin) {
    dataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
  }

  /**
   * Sets the test connection on checkout.
   *
   * @param testConnectionOnCheckout
   *          the new test connection on checkout
   */
  @com.google.inject.Inject(optional = true)
  public void setTestConnectionOnCheckout(
      @Named("c3p0.testConnectionOnCheckout") final boolean testConnectionOnCheckout) {
    dataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);
  }

  /**
   * Sets the unreturned connection timeout.
   *
   * @param unreturnedConnectionTimeout
   *          the new unreturned connection timeout
   */
  @com.google.inject.Inject(optional = true)
  public void setUnreturnedConnectionTimeout(
      @Named("c3p0.unreturnedConnectionTimeout") final int unreturnedConnectionTimeout) {
    dataSource.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
  }

  /**
   * Sets the uses traditional reflective proxies.
   *
   * @param usesTraditionalReflectiveProxies
   *          the new uses traditional reflective proxies
   */
  @com.google.inject.Inject(optional = true)
  public void setUsesTraditionalReflectiveProxies(
      @Named("c3p0.usesTraditionalReflectiveProxies") final boolean usesTraditionalReflectiveProxies) {
    dataSource.setUsesTraditionalReflectiveProxies(usesTraditionalReflectiveProxies);
  }

  @Override
  public DataSource get() {
    if (username != null) {
      dataSource.setUser(username);
    }
    if (password != null) {
      dataSource.setPassword(password);
    }
    return dataSource;
  }

}
