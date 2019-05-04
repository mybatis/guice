/**
 *    Copyright 2009-2019 the original author or authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.dbcp2.datasources.PerUserPoolDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;
import javax.sql.ConnectionPoolDataSource;

@ExtendWith(MockitoExtension.class)
public class PerUserPoolDataSourceModuleTest {
  @Mock
  private ConnectionPoolDataSource connectionPoolDataSource;

  @Test
  public void configure_PerUserDefaultAutoCommit() throws Throwable {
    Injector injector = Guice.createInjector(new PerUserPoolDataSourceModule.Builder()
        .setPerUserDefaultAutoCommitProviderClass(PerUserDefaultAutoCommitProvider.class).create());
    PerUserPoolDataSourceProvider provider = injector.getInstance(PerUserPoolDataSourceProvider.class);

    PerUserPoolDataSource dataSource = (PerUserPoolDataSource) provider.get();

    assertEquals(true, dataSource.getPerUserDefaultAutoCommit("test_user"));
    assertEquals(false, dataSource.getPerUserDefaultAutoCommit("test_user2"));
  }

  @Test
  public void configure_PerUserDefaultReadOnly() throws Throwable {
    Injector injector = Guice.createInjector(new PerUserPoolDataSourceModule.Builder()
        .setPerUserDefaultReadOnlyProviderClass(PerUserDefaultReadOnlyProvider.class).create());
    PerUserPoolDataSourceProvider provider = injector.getInstance(PerUserPoolDataSourceProvider.class);

    PerUserPoolDataSource dataSource = (PerUserPoolDataSource) provider.get();

    assertEquals(false, dataSource.getPerUserDefaultReadOnly("test_user"));
    assertEquals(true, dataSource.getPerUserDefaultReadOnly("test_user2"));
  }

  @Test
  public void configure_PerUserDefaultTransactionIsolation() throws Throwable {
    Injector injector = Guice.createInjector(new PerUserPoolDataSourceModule.Builder()
        .setPerUserDefaultTransactionIsolationProviderClass(PerUserDefaultTransactionIsolationProvider.class).create());
    PerUserPoolDataSourceProvider provider = injector.getInstance(PerUserPoolDataSourceProvider.class);

    PerUserPoolDataSource dataSource = (PerUserPoolDataSource) provider.get();

    assertEquals((Integer) Connection.TRANSACTION_READ_COMMITTED,
        dataSource.getPerUserDefaultTransactionIsolation("test_user"));
    assertEquals((Integer) Connection.TRANSACTION_SERIALIZABLE,
        dataSource.getPerUserDefaultTransactionIsolation("test_user2"));
  }

  @Test
  public void configure_PerUserMaxTotal() throws Throwable {
    Injector injector = Guice.createInjector(new PerUserPoolDataSourceModule.Builder()
        .setPerUserMaxTotalProviderClass(PerUserMaxTotalProvider.class).create());
    PerUserPoolDataSourceProvider provider = injector.getInstance(PerUserPoolDataSourceProvider.class);

    PerUserPoolDataSource dataSource = (PerUserPoolDataSource) provider.get();

    assertEquals(10, dataSource.getPerUserMaxTotal("test_user"));
    assertEquals(20, dataSource.getPerUserMaxTotal("test_user2"));
  }

  @Test
  public void configure_PerUserMaxIdle() throws Throwable {
    Injector injector = Guice.createInjector(new PerUserPoolDataSourceModule.Builder()
        .setPerUserMaxIdleProviderClass(PerUserMaxIdleProvider.class).create());
    PerUserPoolDataSourceProvider provider = injector.getInstance(PerUserPoolDataSourceProvider.class);

    PerUserPoolDataSource dataSource = (PerUserPoolDataSource) provider.get();

    assertEquals(30, dataSource.getPerUserMaxIdle("test_user"));
    assertEquals(40, dataSource.getPerUserMaxIdle("test_user2"));
  }

  @Test
  public void configure_PerUserMaxWaitMillis() throws Throwable {
    Injector injector = Guice.createInjector(new PerUserPoolDataSourceModule.Builder()
        .setPerUserMaxWaitMillisProviderClass(PerUserMaxWaitMillisProvider.class).create());
    PerUserPoolDataSourceProvider provider = injector.getInstance(PerUserPoolDataSourceProvider.class);

    PerUserPoolDataSource dataSource = (PerUserPoolDataSource) provider.get();

    assertEquals(50, dataSource.getPerUserMaxWaitMillis("test_user"));
    assertEquals(60, dataSource.getPerUserMaxWaitMillis("test_user2"));
  }

  public static class PerUserDefaultAutoCommitProvider implements Provider<Map<String, Boolean>> {
    @Override
    public Map<String, Boolean> get() {
      Map<String, Boolean> defaultAutoCommit = new HashMap<String, Boolean>();
      defaultAutoCommit.put("test_user", true);
      defaultAutoCommit.put("test_user2", false);
      return defaultAutoCommit;
    }
  }

  public static class PerUserDefaultReadOnlyProvider implements Provider<Map<String, Boolean>> {
    @Override
    public Map<String, Boolean> get() {
      Map<String, Boolean> defaultAutoCommit = new HashMap<String, Boolean>();
      defaultAutoCommit.put("test_user", false);
      defaultAutoCommit.put("test_user2", true);
      return defaultAutoCommit;
    }
  }

  public static class PerUserDefaultTransactionIsolationProvider implements Provider<Map<String, Integer>> {
    @Override
    public Map<String, Integer> get() {
      Map<String, Integer> defaultTransactionIsolation = new HashMap<String, Integer>();
      defaultTransactionIsolation.put("test_user", Connection.TRANSACTION_READ_COMMITTED);
      defaultTransactionIsolation.put("test_user2", Connection.TRANSACTION_SERIALIZABLE);
      return defaultTransactionIsolation;
    }
  }

  public static class PerUserMaxTotalProvider implements Provider<Map<String, Integer>> {
    @Override
    public Map<String, Integer> get() {
      Map<String, Integer> maxActive = new HashMap<String, Integer>();
      maxActive.put("test_user", 10);
      maxActive.put("test_user2", 20);
      return maxActive;
    }
  }

  public static class PerUserMaxIdleProvider implements Provider<Map<String, Integer>> {
    @Override
    public Map<String, Integer> get() {
      Map<String, Integer> maxIdle = new HashMap<String, Integer>();
      maxIdle.put("test_user", 30);
      maxIdle.put("test_user2", 40);
      return maxIdle;
    }
  }

  public static class PerUserMaxWaitMillisProvider implements Provider<Map<String, Long>> {
    @Override
    public Map<String, Long> get() {
      Map<String, Long> maxWait = new HashMap<String, Long>();
      maxWait.put("test_user", 50l);
      maxWait.put("test_user2", 60l);
      return maxWait;
    }
  }
}
