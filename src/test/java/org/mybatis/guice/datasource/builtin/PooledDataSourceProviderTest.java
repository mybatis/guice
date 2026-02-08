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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import java.util.Properties;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PooledDataSourceProviderTest {
  @Mock
  private ClassLoader driverClassLoader;

  @Test
  void get() throws Throwable {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final int loginTimeout = 10;
    final Properties driverProperties = new Properties();
    driverProperties.setProperty("my_property", "true");
    final int maximumActiveConnections = 20;
    final int maximumCheckoutTime = 30;
    final int maximumIdleConnections = 40;
    final int pingConnectionsNotUsedFor = 50;
    final boolean pingEnabled = true;
    final String pingQuery = "SELECT 1";
    final int timeToWait = 60;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ClassLoader.class).annotatedWith(Names.named("JDBC.driverClassLoader")).toInstance(driverClassLoader);
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);
        bind(Properties.class).annotatedWith(Names.named("JDBC.driverProperties")).toInstance(driverProperties);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.maximumActiveConnections"))
            .to(maximumActiveConnections);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.maximumCheckoutTime")).to(maximumCheckoutTime);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.maximumIdleConnections")).to(maximumIdleConnections);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.pingConnectionsNotUsedFor"))
            .to(pingConnectionsNotUsedFor);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.pingEnabled")).to(pingEnabled);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.pingQuery")).to(pingQuery);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.timeToWait")).to(timeToWait);
      }
    });
    PooledDataSourceProvider provider = injector.getInstance(PooledDataSourceProvider.class);

    PooledDataSource dataSource = (PooledDataSource) provider.get();

    assertEquals(driver, dataSource.getDriver());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommit());
    assertEquals(loginTimeout, dataSource.getLoginTimeout());
    assertEquals(driverProperties, dataSource.getDriverProperties());
    assertEquals(maximumActiveConnections, dataSource.getPoolMaximumActiveConnections());
    assertEquals(maximumCheckoutTime, dataSource.getPoolMaximumCheckoutTime());
    assertEquals(maximumIdleConnections, dataSource.getPoolMaximumIdleConnections());
    assertEquals(pingConnectionsNotUsedFor, dataSource.getPoolPingConnectionsNotUsedFor());
    assertEquals(pingEnabled, dataSource.isPoolPingEnabled());
    assertEquals(pingQuery, dataSource.getPoolPingQuery());
    assertEquals(timeToWait, dataSource.getPoolTimeToWait());
  }

  @Test
  void get_OtherValues() throws Throwable {
    final String driver = "org.mybatis.guice.TestDriver2";
    final String url = "jdbc:h2:mem:testdb2";
    final String username = "test_user2";
    final String password = "test_password2";
    final boolean autoCommit = false;
    final int loginTimeout = 11;
    final Properties driverProperties = new Properties();
    driverProperties.setProperty("my_property", "false");
    final int maximumActiveConnections = 21;
    final int maximumCheckoutTime = 31;
    final int maximumIdleConnections = 41;
    final int pingConnectionsNotUsedFor = 51;
    final boolean pingEnabled = false;
    final String pingQuery = "SELECT 1";
    final int timeToWait = 61;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(ClassLoader.class).annotatedWith(Names.named("JDBC.driverClassLoader")).toInstance(driverClassLoader);
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.autoCommit")).to(autoCommit);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);
        bind(Properties.class).annotatedWith(Names.named("JDBC.driverProperties")).toInstance(driverProperties);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.maximumActiveConnections"))
            .to(maximumActiveConnections);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.maximumCheckoutTime")).to(maximumCheckoutTime);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.maximumIdleConnections")).to(maximumIdleConnections);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.pingConnectionsNotUsedFor"))
            .to(pingConnectionsNotUsedFor);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.pingEnabled")).to(pingEnabled);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.pingQuery")).to(pingQuery);
        bindConstant().annotatedWith(Names.named("mybatis.pooled.timeToWait")).to(timeToWait);
      }
    });
    PooledDataSourceProvider provider = injector.getInstance(PooledDataSourceProvider.class);

    PooledDataSource dataSource = (PooledDataSource) provider.get();

    assertEquals(driver, dataSource.getDriver());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommit());
    assertEquals(loginTimeout, dataSource.getLoginTimeout());
    assertEquals(driverProperties, dataSource.getDriverProperties());
    assertEquals(maximumActiveConnections, dataSource.getPoolMaximumActiveConnections());
    assertEquals(maximumCheckoutTime, dataSource.getPoolMaximumCheckoutTime());
    assertEquals(maximumIdleConnections, dataSource.getPoolMaximumIdleConnections());
    assertEquals(pingConnectionsNotUsedFor, dataSource.getPoolPingConnectionsNotUsedFor());
    assertEquals(pingEnabled, dataSource.isPoolPingEnabled());
    assertEquals(pingQuery, dataSource.getPoolPingQuery());
    assertEquals(timeToWait, dataSource.getPoolTimeToWait());
  }
}
