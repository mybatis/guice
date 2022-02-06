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
package org.mybatis.guice.datasource.druid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ExceptionSorter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DruidDataSourceProviderTest {

  @Mock
  private ClassLoader driverClassLoader;

  @Test
  public void get() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final boolean readOnly = false;
    final Integer transactionIsolation = 10;
    final String catalog = "test";
    final int maxActive = 20;
    final int minIdle = 30;
    final int initialSize = 40;
    final long maxWait = 50;
    final boolean testOnBorrow = true;
    final boolean testOnReturn = true;
    final long timeBetweenEvictionRunsMillis = 60;
    final long minEvictableIdleTimeMillis = 70;
    final boolean testWhileIdle = true;
    final String validationQuery = "SELECT 1";
    final int validationQueryTimeout = 80;
    final boolean accessToUnderlyingConnectionAllowed = true;
    final boolean removeAbandoned = true;
    final int removeAbandonedTimeout = 90;
    final boolean logAbandoned = true;
    final boolean poolPreparedStatements = true;
    final int maxOpenPreparedStatements = 100;
    final Properties connectProperties = new Properties();
    connectProperties.put("my_property", "true");
    final String filters = "com.alibaba.druid.filter.logging.Slf4jLogFilter";
    final String exceptionSorterClassName = TestExceptionSorter.class.getName();
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driverClassName")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("JDBC.readOnly")).to(readOnly);
        bindConstant().annotatedWith(Names.named("JDBC.transactionIsolation")).to(transactionIsolation);
        bindConstant().annotatedWith(Names.named("JDBC.catalog")).to(catalog);
        bindConstant().annotatedWith(Names.named("JDBC.maxActive")).to(maxActive);
        bindConstant().annotatedWith(Names.named("JDBC.minIdle")).to(minIdle);
        bindConstant().annotatedWith(Names.named("JDBC.initialSize")).to(initialSize);
        bindConstant().annotatedWith(Names.named("JDBC.maxWait")).to(maxWait);
        bindConstant().annotatedWith(Names.named("JDBC.testOnBorrow")).to(testOnBorrow);
        bindConstant().annotatedWith(Names.named("JDBC.testOnReturn")).to(testOnReturn);
        bindConstant().annotatedWith(Names.named("JDBC.timeBetweenEvictionRunsMillis"))
            .to(timeBetweenEvictionRunsMillis);
        bindConstant().annotatedWith(Names.named("JDBC.minEvictableIdleTimeMillis")).to(minEvictableIdleTimeMillis);
        bindConstant().annotatedWith(Names.named("JDBC.testWhileIdle")).to(testWhileIdle);
        bindConstant().annotatedWith(Names.named("JDBC.validationQuery")).to(validationQuery);
        bindConstant().annotatedWith(Names.named("JDBC.validationQueryTimeout")).to(validationQueryTimeout);
        bindConstant().annotatedWith(Names.named("JDBC.accessToUnderlyingConnectionAllowed"))
            .to(accessToUnderlyingConnectionAllowed);
        bindConstant().annotatedWith(Names.named("JDBC.removeAbandoned")).to(removeAbandoned);
        bindConstant().annotatedWith(Names.named("JDBC.removeAbandonedTimeout")).to(removeAbandonedTimeout);
        bindConstant().annotatedWith(Names.named("JDBC.logAbandoned")).to(logAbandoned);
        bindConstant().annotatedWith(Names.named("JDBC.poolPreparedStatements")).to(poolPreparedStatements);
        bindConstant().annotatedWith(Names.named("JDBC.maxOpenPreparedStatements")).to(maxOpenPreparedStatements);
        bind(Properties.class).annotatedWith(Names.named("JDBC.connectProperties")).toInstance(connectProperties);
        bindConstant().annotatedWith(Names.named("JDBC.filters")).to(filters);
        bindConstant().annotatedWith(Names.named("JDBC.exceptionSorterClassName")).to(exceptionSorterClassName);
      }
    });
    DruidDataSourceProvider provider = injector.getInstance(DruidDataSourceProvider.class);

    DruidDataSource dataSource = (DruidDataSource) provider.get();

    assertEquals(driver, dataSource.getDriverClassName());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isDefaultAutoCommit());
    assertEquals(readOnly, dataSource.getDefaultReadOnly());
    assertEquals(transactionIsolation, dataSource.getDefaultTransactionIsolation());
    assertEquals(catalog, dataSource.getDefaultCatalog());
    assertEquals(maxActive, dataSource.getMaxActive());
    assertEquals(minIdle, dataSource.getMinIdle());
    assertEquals(initialSize, dataSource.getInitialSize());
    assertEquals(maxWait, dataSource.getMaxWait());
    assertEquals(testOnBorrow, dataSource.isTestOnBorrow());
    assertEquals(testOnReturn, dataSource.isTestOnReturn());
    assertEquals(timeBetweenEvictionRunsMillis, dataSource.getTimeBetweenEvictionRunsMillis());
    assertEquals(minEvictableIdleTimeMillis, dataSource.getMinEvictableIdleTimeMillis());
    assertEquals(testWhileIdle, dataSource.isTestWhileIdle());
    assertEquals(validationQuery, dataSource.getValidationQuery());
    assertEquals(validationQueryTimeout, dataSource.getValidationQueryTimeout());
    assertEquals(accessToUnderlyingConnectionAllowed, dataSource.isAccessToUnderlyingConnectionAllowed());
    assertEquals(removeAbandoned, dataSource.isRemoveAbandoned());
    assertEquals(removeAbandonedTimeout, dataSource.getRemoveAbandonedTimeout());
    assertEquals(logAbandoned, dataSource.isLogAbandoned());
    assertEquals(poolPreparedStatements, dataSource.isPoolPreparedStatements());
    assertEquals(maxOpenPreparedStatements, dataSource.getMaxOpenPreparedStatements());
    assertEquals(connectProperties, dataSource.getConnectProperties());
    assertEquals(1, dataSource.getFilterClassNames().size());
    assertEquals(filters, dataSource.getFilterClassNames().get(0));
    assertTrue(dataSource.getExceptionSorter() instanceof TestExceptionSorter);
    assertEquals(exceptionSorterClassName, dataSource.getExceptionSorterClassName());
  }

  @Test
  public void get_ConnectionPropertiesAsString() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final String connectionProperties = "my_property=true;my_property2=false";
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driverClassName")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.connectionProperties")).to(connectionProperties);
      }
    });
    DruidDataSourceProvider provider = injector.getInstance(DruidDataSourceProvider.class);

    DruidDataSource dataSource = (DruidDataSource) provider.get();

    assertEquals(driver, dataSource.getDriverClassName());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(2, dataSource.getConnectProperties().size());
    assertEquals("true", dataSource.getConnectProperties().getProperty("my_property"));
    assertEquals("false", dataSource.getConnectProperties().getProperty("my_property2"));
  }

  @Test
  public void get_ExceptionSorterAsString() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final String exceptionSorter = TestExceptionSorter.class.getName();
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driverClassName")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.exceptionSorter")).to(exceptionSorter);
      }
    });
    DruidDataSourceProvider provider = injector.getInstance(DruidDataSourceProvider.class);

    DruidDataSource dataSource = (DruidDataSource) provider.get();

    assertEquals(driver, dataSource.getDriverClassName());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertTrue(dataSource.getExceptionSorter() instanceof TestExceptionSorter);
    assertEquals(exceptionSorter, dataSource.getExceptionSorterClassName());
  }

  @Test
  public void get_ReadOnly() {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final boolean readOnly = true;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driverClassName")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("JDBC.readOnly")).to(readOnly);
      }
    });
    DruidDataSourceProvider provider = injector.getInstance(DruidDataSourceProvider.class);

    DruidDataSource dataSource = (DruidDataSource) provider.get();

    assertEquals(driver, dataSource.getDriverClassName());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isDefaultAutoCommit());
    assertEquals(readOnly, dataSource.getDefaultReadOnly());
  }

  public static class TestExceptionSorter implements ExceptionSorter {
    @Override
    public boolean isExceptionFatal(SQLException e) {
      return false;
    }

    @Override
    public void configFromProperties(Properties properties) {
    }
  }
}
