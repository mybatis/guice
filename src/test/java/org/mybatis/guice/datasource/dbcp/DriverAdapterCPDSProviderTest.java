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

import static org.junit.Assert.assertEquals;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.junit.Test;

public class DriverAdapterCPDSProviderTest {
  @Test
  public void get() throws Throwable {
    final String driver = TestDriver.class.getName();
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";
    final int loginTimeout = 10;
    final String description = "test_description";
    final int maxActive = 20;
    final int maxIdle = 30;
    final int maxOpenPreparedStatements = 40;
    final int minEvictableIdleTimeMillis = 50;
    final int numTestsPerEvictionRun = 60;
    final boolean poolPreparedStatements = true;
    final int timeBetweenEvictionRunsMillis = 70;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);
        bindConstant().annotatedWith(Names.named("DBCP.description")).to(description);
        bindConstant().annotatedWith(Names.named("DBCP.maxActive")).to(maxActive);
        bindConstant().annotatedWith(Names.named("DBCP.maxIdle")).to(maxIdle);
        bindConstant().annotatedWith(Names.named("DBCP.maxOpenPreparedStatements")).to(maxOpenPreparedStatements);
        bindConstant().annotatedWith(Names.named("DBCP.minEvictableIdleTimeMillis")).to(minEvictableIdleTimeMillis);
        bindConstant().annotatedWith(Names.named("DBCP.numTestsPerEvictionRun")).to(numTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("DBCP.poolPreparedStatements")).to(poolPreparedStatements);
        bindConstant().annotatedWith(Names.named("DBCP.timeBetweenEvictionRunsMillis"))
            .to(timeBetweenEvictionRunsMillis);
      }
    });
    DriverAdapterCPDSProvider provider = injector.getInstance(DriverAdapterCPDSProvider.class);

    DriverAdapterCPDS adapter = (DriverAdapterCPDS) provider.get();

    assertEquals(driver, adapter.getDriver());
    assertEquals(url, adapter.getUrl());
    assertEquals(username, adapter.getUser());
    assertEquals(password, adapter.getPassword());
    assertEquals(loginTimeout, adapter.getLoginTimeout());
    assertEquals(description, adapter.getDescription());
    assertEquals(maxActive, adapter.getMaxActive());
    assertEquals(maxIdle, adapter.getMaxIdle());
    assertEquals(maxOpenPreparedStatements, adapter.getMaxPreparedStatements());
    assertEquals(minEvictableIdleTimeMillis, adapter.getMinEvictableIdleTimeMillis());
    assertEquals(numTestsPerEvictionRun, adapter.getNumTestsPerEvictionRun());
    assertEquals(poolPreparedStatements, adapter.isPoolPreparedStatements());
    assertEquals(timeBetweenEvictionRunsMillis, adapter.getTimeBetweenEvictionRunsMillis());
  }

  @Test
  public void getOtherValues() throws Throwable {
    final String driver = TestDriver2.class.getName();
    final String url = "jdbc:h2:mem:testdb2";
    final String username = "test_user2";
    final String password = "test_password2";
    final int loginTimeout = 11;
    final String description = "test_description2";
    final int maxActive = 21;
    final int maxIdle = 31;
    final int maxOpenPreparedStatements = 41;
    final int minEvictableIdleTimeMillis = 51;
    final int numTestsPerEvictionRun = 61;
    final boolean poolPreparedStatements = false;
    final int timeBetweenEvictionRunsMillis = 71;
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("JDBC.driver")).to(driver);
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);
        bindConstant().annotatedWith(Names.named("DBCP.description")).to(description);
        bindConstant().annotatedWith(Names.named("DBCP.maxActive")).to(maxActive);
        bindConstant().annotatedWith(Names.named("DBCP.maxIdle")).to(maxIdle);
        bindConstant().annotatedWith(Names.named("DBCP.maxOpenPreparedStatements")).to(maxOpenPreparedStatements);
        bindConstant().annotatedWith(Names.named("DBCP.minEvictableIdleTimeMillis")).to(minEvictableIdleTimeMillis);
        bindConstant().annotatedWith(Names.named("DBCP.numTestsPerEvictionRun")).to(numTestsPerEvictionRun);
        bindConstant().annotatedWith(Names.named("DBCP.poolPreparedStatements")).to(poolPreparedStatements);
        bindConstant().annotatedWith(Names.named("DBCP.timeBetweenEvictionRunsMillis"))
            .to(timeBetweenEvictionRunsMillis);
      }
    });
    DriverAdapterCPDSProvider provider = injector.getInstance(DriverAdapterCPDSProvider.class);

    DriverAdapterCPDS adapter = (DriverAdapterCPDS) provider.get();

    assertEquals(driver, adapter.getDriver());
    assertEquals(url, adapter.getUrl());
    assertEquals(username, adapter.getUser());
    assertEquals(password, adapter.getPassword());
    assertEquals(loginTimeout, adapter.getLoginTimeout());
    assertEquals(description, adapter.getDescription());
    assertEquals(maxActive, adapter.getMaxActive());
    assertEquals(maxIdle, adapter.getMaxIdle());
    assertEquals(maxOpenPreparedStatements, adapter.getMaxPreparedStatements());
    assertEquals(minEvictableIdleTimeMillis, adapter.getMinEvictableIdleTimeMillis());
    assertEquals(numTestsPerEvictionRun, adapter.getNumTestsPerEvictionRun());
    assertEquals(poolPreparedStatements, adapter.isPoolPreparedStatements());
    assertEquals(timeBetweenEvictionRunsMillis, adapter.getTimeBetweenEvictionRunsMillis());
  }

  public static class TestDriver {
  }

  public static class TestDriver2 {
  }
}
