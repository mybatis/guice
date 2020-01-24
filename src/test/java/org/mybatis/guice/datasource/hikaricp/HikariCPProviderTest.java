/**
 *    Copyright 2009-2020 the original author or authors.
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
package org.mybatis.guice.datasource.hikaricp;

import java.sql.SQLException;
import java.util.Properties;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HikariCPProviderTest {

  @Test
  public void get() throws SQLException {

    // JDBC connection
    // ============================================================================================================
    final String url = "jdbc:h2:mem:testdb";
    final String username = "test_user";
    final String password = "test_password";

    final int loginTimeout = 1; // Seconds

    // Hikari CP configuration
    // ============================================================================================================
    final boolean allowPoolSuspension = true;
    final boolean autoCommit = true;

    final String catalog = "custom";
    final String connectionInitSql = "CREATE SCHEMA IF NOT EXISTS TEST; SET SCHEMA TEST;";
    final String connectionTestQuery = "Commit;";
    final long connectionTimeoutMs = 1000L;

    final String driverClassName = "org.h2.Driver";

    final Properties healthCheckProperties = new Properties();
    healthCheckProperties.put("boom", "goes the dynamite");
    final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    final long idleTimeoutMs = 42L;
    final long initializationFailTimeout = 2L;
    final boolean isolateInternalQueries = true;

    final long leakDetectionThresholdMs = 2000L; // 2000ms

    final long maxLifetimeMs = 30000L;
    final long maxPoolSize = 10L;

    final MetricRegistry metricRegistry = new MetricRegistry();
    final MetricsTrackerFactory metricsTrackerFactory = new CodahaleMetricsTrackerFactory(metricRegistry);

    final long minimumIdle = 10L;

    final String poolName = "MyBatis Pool";

    final boolean readOnly = false;
    final Object registerMbeans = null;

    final Object scheduledExecutorService = null;
    final String schema = "PUBLIC";

    final Object threadFactory = null;
    final Object transactionIsolation = null;

    final long validationTimeoutMs = 250; // Min is 250ms

    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {

        // JDBC connection
        // ============================================================================================================
        bindConstant().annotatedWith(Names.named("JDBC.url")).to(url);
        bindConstant().annotatedWith(Names.named("JDBC.username")).to(username);
        bindConstant().annotatedWith(Names.named("JDBC.password")).to(password);
        bindConstant().annotatedWith(Names.named("JDBC.loginTimeout")).to(loginTimeout);

        // Hikari CP configuration
        // ============================================================================================================

        bindConstant().annotatedWith(Names.named("hikaricp.allowPoolSuspension")).to(allowPoolSuspension);
        bindConstant().annotatedWith(Names.named("hikaricp.autoCommit")).to(autoCommit);

        bindConstant().annotatedWith(Names.named("hikaricp.catalog")).to(catalog);
        bindConstant().annotatedWith(Names.named("hikaricp.connectionInitSql")).to(connectionInitSql);
        bindConstant().annotatedWith(Names.named("hikaricp.connectionTestQuery")).to(connectionTestQuery);
        bindConstant().annotatedWith(Names.named("hikaricp.connectionTimeoutMs")).to(connectionTimeoutMs);

        bindConstant().annotatedWith(Names.named("hikaricp.driverClassName")).to(driverClassName);

        bind(Properties.class).annotatedWith(Names.named("hikaricp.healthCheckProperties"))
            .toInstance(healthCheckProperties);
        bind(Object.class).annotatedWith(Names.named("hikaricp.healthCheckRegistry")).toInstance(healthCheckRegistry);

        bindConstant().annotatedWith(Names.named("hikaricp.idleTimeoutMs")).to(idleTimeoutMs);
        bindConstant().annotatedWith(Names.named("hikaricp.initializationFailTimeout")).to(initializationFailTimeout);
        bindConstant().annotatedWith(Names.named("hikaricp.isolateInternalQueries")).to(isolateInternalQueries);

        bindConstant().annotatedWith(Names.named("hikaricp.leakDetectionThresholdMs")).to(leakDetectionThresholdMs);

        bindConstant().annotatedWith(Names.named("hikaricp.maxLifetimeMs")).to(maxLifetimeMs);
        bindConstant().annotatedWith(Names.named("hikaricp.maxPoolSize")).to(maxPoolSize);

        bind(MetricsTrackerFactory.class).annotatedWith(Names.named("hikaricp.metricsTrackerFactory"))
            .toInstance(metricsTrackerFactory);

        bindConstant().annotatedWith(Names.named("hikaricp.minimumIdle")).to(minimumIdle);
        bindConstant().annotatedWith(Names.named("hikaricp.poolName")).to(poolName);
        bindConstant().annotatedWith(Names.named("hikaricp.readOnly")).to(readOnly);
        // bindConstant().annotatedWith(Names.named("hikaricp.registerMbeans")).to(registerMbeans);

        // bindConstant().annotatedWith(Names.named("hikaricp.scheduledExecutorService")).to(scheduledExecutorService);
        bindConstant().annotatedWith(Names.named("hikaricp.schema")).to(schema);

        // bindConstant().annotatedWith(Names.named("hikaricp.threadFactory")).to(threadFactory);
        // bindConstant().annotatedWith(Names.named("hikaricp.transactionIsolation")).to(transactionIsolation);

        bindConstant().annotatedWith(Names.named("hikaricp.validationTimeoutMs")).to(validationTimeoutMs);
      }
    });

    HikariCPProvider provider = injector.getInstance(HikariCPProvider.class);
    HikariDataSource dataSource = (HikariDataSource) provider.get();

    // JDBC connection
    // ============================================================================================================
    assertEquals(url, dataSource.getJdbcUrl());
    assertEquals(username, dataSource.getUsername());
    assertEquals(password, dataSource.getPassword());
    assertEquals(1, dataSource.getLoginTimeout());

    // Hikari CP configuration
    // ============================================================================================================
    assertEquals(allowPoolSuspension, dataSource.isAllowPoolSuspension());
    assertEquals(autoCommit, dataSource.isAutoCommit());

    assertEquals(catalog, dataSource.getCatalog());
    assertEquals(connectionInitSql, dataSource.getConnectionInitSql());
    assertEquals(connectionTestQuery, dataSource.getConnectionTestQuery());
    assertEquals(connectionTimeoutMs, dataSource.getConnectionTimeout());

    assertEquals(driverClassName, dataSource.getDriverClassName());

    assertEquals(healthCheckProperties, dataSource.getHealthCheckProperties());
    assertEquals(healthCheckRegistry, dataSource.getHealthCheckRegistry());

    assertEquals(idleTimeoutMs, dataSource.getIdleTimeout());
    assertEquals(initializationFailTimeout, dataSource.getInitializationFailTimeout());
    assertEquals(isolateInternalQueries, dataSource.isIsolateInternalQueries());

    assertEquals(leakDetectionThresholdMs, dataSource.getLeakDetectionThreshold());

    assertEquals(maxLifetimeMs, dataSource.getMaxLifetime());
    assertEquals(maxPoolSize, dataSource.getMaximumPoolSize());
    assertEquals(metricsTrackerFactory, dataSource.getMetricsTrackerFactory());
    assertEquals(minimumIdle, dataSource.getMinimumIdle());

    assertEquals(poolName, dataSource.getPoolName());
    assertEquals(readOnly, dataSource.isReadOnly());
    assertEquals(schema, dataSource.getSchema());
    assertEquals(validationTimeoutMs, dataSource.getValidationTimeout());
  }
}
