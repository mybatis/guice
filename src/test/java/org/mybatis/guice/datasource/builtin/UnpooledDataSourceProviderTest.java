/*
 *    Copyright 2009-2021 the original author or authors.
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

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnpooledDataSourceProviderTest {
  @Mock
  private ClassLoader driverClassLoader;

  @Test
  public void get() throws Throwable {
    final String driver = "org.mybatis.guice.TestDriver";
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final boolean autoCommit = true;
    final int loginTimeout = 10;
    final Properties driverProperties = new Properties();
    driverProperties.setProperty("my_property", "true");
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
      }
    });
    UnpooledDataSourceProvider provider = injector.getInstance(UnpooledDataSourceProvider.class);

    UnpooledDataSource dataSource = (UnpooledDataSource) provider.get();

    assertEquals(driver, dataSource.getDriver());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommit());
    assertEquals(loginTimeout, dataSource.getLoginTimeout());
    assertEquals(driverProperties, dataSource.getDriverProperties());
  }

  @Test
  public void get_OtherValues() throws Throwable {
    final String driver = "org.mybatis.guice.TestDriver2";
    final String url = "jdbc:h2:mem:testdb2";
    final String username = "test_user2";
    final String password = "test_password2";
    final boolean autoCommit = false;
    final int loginTimeout = 11;
    final Properties driverProperties = new Properties();
    driverProperties.setProperty("my_property", "false");
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
      }
    });
    UnpooledDataSourceProvider provider = injector.getInstance(UnpooledDataSourceProvider.class);

    UnpooledDataSource dataSource = (UnpooledDataSource) provider.get();

    assertEquals(driver, dataSource.getDriver());
    assertEquals(url, dataSource.getUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(autoCommit, dataSource.isAutoCommit());
    assertEquals(loginTimeout, dataSource.getLoginTimeout());
    assertEquals(driverProperties, dataSource.getDriverProperties());
  }
}
