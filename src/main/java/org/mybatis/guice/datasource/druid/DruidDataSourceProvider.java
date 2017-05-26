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
package org.mybatis.guice.datasource.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Provides the Druid DataSource.
 *
 * @author lazeyliu
 */
public class DruidDataSourceProvider implements Provider<DataSource> {

  DruidDataSource dataSource = new DruidDataSource();

  @Inject
  public void setDriverClassName(@Named("JDBC.driverClassName") final String driverClassName) {
    dataSource.setDriverClassName(driverClassName);
  }

  @Inject
  public void setUrl(@Named("JDBC.url") final String url) {
    dataSource.setUrl(url);
  }

  @Inject
  public void setUsername(@Named("JDBC.username") final String username) {
    dataSource.setUsername(username);
  }

  @Inject
  public void setPassword(@Named("JDBC.password") final String password) {
    dataSource.setPassword(password);
  }

  @Inject(optional = true)
  public void setDefaultAutoCommit(@Named("JDBC.autoCommit") final boolean defaultAutoCommit) {
    dataSource.setDefaultAutoCommit(defaultAutoCommit);
  }

  @Inject(optional = true)
  public void setDefaultReadOnly(@Named("JDBC.readOnly") final boolean defaultReadOnly) {
    dataSource.setDefaultAutoCommit(defaultReadOnly);
  }

  @Inject(optional = true)
  public void setDefaultTransactionIsolation(
      @Named("JDBC.transactionIsolation") final int defaultTransactionIsolation) {
    dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
  }

  @Inject(optional = true)
  public void setDefaultCatalog(@Named("JDBC.catalog") final String defaultCatalog) {
    dataSource.setDefaultCatalog(defaultCatalog);
  }

  @Inject(optional = true)
  public void setMaxActive(@Named("JDBC.maxActive") final int maxActive) {
    dataSource.setMaxActive(maxActive);
  }

  @Inject(optional = true)
  public void setMinIdle(@Named("JDBC.minIdle") final int minIdle) {
    dataSource.setMinIdle(minIdle);
  }

  @Inject(optional = true)
  public void setInitialSize(@Named("JDBC.initialSize") final int initialSize) {
    dataSource.setInitialSize(initialSize);
  }

  @Inject(optional = true)
  public void setMaxWait(@Named("JDBC.maxWait") final long maxWait) {
    dataSource.setMaxWait(maxWait);
  }

  @Inject(optional = true)
  public void setTestOnBorrow(@Named("JDBC.testOnBorrow") final boolean testOnBorrow) {
    dataSource.setTestOnBorrow(testOnBorrow);
  }

  @Inject(optional = true)
  public void setTestOnReturn(@Named("JDBC.testOnReturn") final boolean testOnReturn) {
    dataSource.setTestOnReturn(testOnReturn);
  }

  @Inject(optional = true)
  public void setTimeBetweenEvictionRunsMillis(
      @Named("JDBC.timeBetweenEvictionRunsMillis") final long timeBetweenEvictionRunsMillis) {
    dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
  }

  @Inject(optional = true)
  public void setMinEvictableIdleTimeMillis(
      @Named("JDBC.minEvictableIdleTimeMillis") final long minEvictableIdleTimeMillis) {
    dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
  }

  @Inject(optional = true)
  public void setTestWhileIdle(@Named("JDBC.testWhileIdle") final boolean testWhileIdle) {
    dataSource.setTestWhileIdle(testWhileIdle);
  }

  @Inject(optional = true)
  public void setValidationQuery(@Named("JDBC.validationQuery") final String validationQuery) {
    dataSource.setValidationQuery(validationQuery);
  }

  @Inject(optional = true)
  public void setValidationQueryTimeout(@Named("JDBC.validationQueryTimeout") final int validationQueryTimeout) {
    dataSource.setValidationQueryTimeout(validationQueryTimeout);
  }

  @Inject(optional = true)
  public void setAccessToUnderlyingConnectionAllowed(
      @Named("JDBC.accessToUnderlyingConnectionAllowed") final boolean accessToUnderlyingConnectionAllowed) {
    dataSource.setAccessToUnderlyingConnectionAllowed(accessToUnderlyingConnectionAllowed);
  }

  @Inject(optional = true)
  public void setRemoveAbandoned(@Named("JDBC.removeAbandoned") final boolean removeAbandoned) {
    dataSource.setRemoveAbandoned(removeAbandoned);
  }

  @Inject(optional = true)
  public void setRemoveAbandonedTimeout(@Named("JDBC.removeAbandonedTimeout") final int removeAbandonedTimeout) {
    dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
  }

  @Inject(optional = true)
  public void setLogAbandoned(@Named("JDBC.logAbandoned") final boolean logAbandoned) {
    dataSource.setLogAbandoned(logAbandoned);
  }

  @Inject(optional = true)
  public void setPoolPreparedStatements(@Named("JDBC.poolPreparedStatements") final boolean poolPreparedStatements) {
    dataSource.setPoolPreparedStatements(poolPreparedStatements);
  }

  @Inject(optional = true)
  public void setMaxOpenPreparedStatements(
      @Named("JDBC.maxOpenPreparedStatements") final int maxOpenPreparedStatements) {
    dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
  }

  @Inject(optional = true)
  public void setConnectionProperties(@Named("JDBC.connectionProperties") final String connectionProperties) {
    dataSource.setConnectionProperties(connectionProperties);
  }

  @Inject(optional = true)
  public void setFilters(@Named("JDBC.filters") final String filters) throws SQLException {
    dataSource.setFilters(filters);
  }

  @Inject(optional = true)
  public void setExceptionSorter(@Named("JDBC.exceptionSorter") final String exceptionSorter) throws SQLException {
    dataSource.setExceptionSorter(exceptionSorter);
  }

  @Inject(optional = true)
  public void setExceptionSorterClassName(@Named("JDBC.exceptionSorterClassName") final String exceptionSorterClassName)
      throws Exception {
    dataSource.setExceptionSorterClassName(exceptionSorterClassName);
  }

  @Override
  public DataSource get() {
    return dataSource;
  }

}
