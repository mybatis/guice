/*
 *    Copyright 2010-2011 The myBatis Team
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
package org.mybatis.guice.datasource.bonecp;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPDataSource;
import com.jolbox.bonecp.hooks.ConnectionHook;

/**
 * Provides the BoneCP DataSource.
 *
 * @version $Id$
 */
public final class BoneCPProvider implements Provider<DataSource> {

    private final BoneCPDataSource dataSource = new BoneCPDataSource();

    @com.google.inject.Inject(optional = true)
    public void setAcquireIncrement(@Named("bonecp.acquireIncrement") int acquireIncrement) {
        this.dataSource.setAcquireIncrement(acquireIncrement);
    }

    @com.google.inject.Inject(optional = true)
    public void setAcquireRetryAttempts(@Named("bonecp.acquireRetryAttempts") int acquireRetryAttempts) {
        this.dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
    }

    @com.google.inject.Inject(optional = true)
    public void setClassLoader(@Named("bonecp.classLoader") ClassLoader classLoader) {
        this.dataSource.setClassLoader(classLoader);
    }

    @com.google.inject.Inject(optional = true)
    public void setCloseConnectionWatch(@Named("bonecp.closeConnectionWatch") boolean closeConnectionWatch) {
        this.dataSource.setCloseConnectionWatch(closeConnectionWatch);
    }

    @com.google.inject.Inject(optional = true)
    public void setConnectionHook(@Named("bonecp.connectionHook") ConnectionHook connectionHook) {
        this.dataSource.setConnectionHook(connectionHook);
    }

    @com.google.inject.Inject(optional = true)
    public void setConnectionHookClassName(@Named("bonecp.connectionHookClassName") String connectionHookClassName) {
        this.dataSource.setConnectionHookClassName(connectionHookClassName);
    }

    @com.google.inject.Inject(optional = true)
    public void setConnectionTestStatement(@Named("bonecp.connectionTestStatement") String connectionTestStatement) {
        this.dataSource.setConnectionTestStatement(connectionTestStatement);
    }

    @com.google.inject.Inject(optional = true)
    public void setDisableConnectionTracking(@Named("bonecp.disableConnectionTracking") boolean disableConnectionTracking) {
        this.dataSource.setDisableConnectionTracking(disableConnectionTracking);
    }

    @com.google.inject.Inject(optional = true)
    public void setDisableJMX(@Named("bonecp.disableJMX") boolean disableJMX) {
        this.dataSource.setDisableJMX(disableJMX);
    }

    @Inject
    public void setDriverClass(@Named("JDBC.driver") final String driverClass) {
        this.dataSource.setDriverClass(driverClass);
    }

    @com.google.inject.Inject(optional = true)
    public void setDriverProperties(@Named("bonecp.driverProperties") Properties driverProperties) {
        this.dataSource.setDriverProperties(driverProperties);
    }

    @com.google.inject.Inject(optional = true)
    public void setIdleConnectionTestPeriod(@Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod,
            @Named("bonecp.timeUnit") TimeUnit timeUnit) {
        this.dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod, timeUnit);
    }

    @com.google.inject.Inject(optional = true)
    public void setIdleConnectionTestPeriodInMinutes(@Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod) {
        this.dataSource.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriod);
    }

    @com.google.inject.Inject(optional = true)
    public void setIdleConnectionTestPeriodInSeconds(@Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod) {
        this.dataSource.setIdleConnectionTestPeriodInSeconds(idleConnectionTestPeriod);
    }

    @com.google.inject.Inject(optional = true)
    public void setIdleMaxAge(@Named("bonecp.idleMaxAge") long idleMaxAge, @Named("bonecp.timeUnit") TimeUnit timeUnit) {
        this.dataSource.setIdleMaxAge(idleMaxAge, timeUnit);
    }

    @com.google.inject.Inject(optional = true)
    public void setInitSQL(@Named("bonecp.initSQL") String initSQL) {
        this.dataSource.setInitSQL(initSQL);
    }

    @Inject
    public void setJdbcUrl(@Named("JDBC.url") String jdbcUrl) {
        this.dataSource.setJdbcUrl(jdbcUrl);
    }

    @com.google.inject.Inject(optional = true)
    public void setLazyInit(@Named("bonecp.lazyInit") boolean lazyInit) {
        this.dataSource.setLazyInit(lazyInit);
    }

    @com.google.inject.Inject(optional = true)
    public void setLogStatementsEnabled(@Named("bonecp.logStatementsEnabled") boolean logStatementsEnabled) {
        this.dataSource.setLogStatementsEnabled(logStatementsEnabled);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxConnectionAge(@Named("bonecp.maxConnectionAge") long maxConnectionAge,
            @Named("bonecp.timeUnit") TimeUnit timeUnit) {
        this.dataSource.setMaxConnectionAge(maxConnectionAge, timeUnit);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxConnectionAgeInSeconds(@Named("bonecp.maxConnectionAgeInSeconds") long maxConnectionAgeInSeconds) {
        this.dataSource.setMaxConnectionAgeInSeconds(maxConnectionAgeInSeconds);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxConnectionsPerPartition(@Named("bonecp.maxConnectionsPerPartition") int maxConnectionsPerPartition) {
        this.dataSource.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
    }

    @com.google.inject.Inject(optional = true)
    public void setMinConnectionsPerPartition(@Named("bonecp.minConnectionsPerPartition") int minConnectionsPerPartition) {
        this.dataSource.setMinConnectionsPerPartition(minConnectionsPerPartition);
    }

    @com.google.inject.Inject(optional = true)
    public void setPartitionCount(@Named("bonecp.partitionCount") int partitionCount) {
        this.dataSource.setPartitionCount(partitionCount);
    }

    @Inject
    public void setPassword(@Named("JDBC.password") String password) {
        this.dataSource.setPassword(password);
    }

    @com.google.inject.Inject(optional = true)
    public void setPoolAvailabilityThreshold(@Named("bonecp.poolAvailabilityThreshold") int poolAvailabilityThreshold) {
        this.dataSource.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
    }

    @com.google.inject.Inject(optional = true)
    public void setPoolName(@Named("bonecp.poolName") String poolName) {
        this.dataSource.setPoolName(poolName);
    }

    @com.google.inject.Inject(optional = true)
    public void setQueryExecuteTimeLimit(@Named("bonecp.queryExecuteTimeLimit") int queryExecuteTimeLimit,
            @Named("bonecp.timeUnit") TimeUnit timeUnit) {
        this.dataSource.setQueryExecuteTimeLimit(queryExecuteTimeLimit, timeUnit);
    }

    @com.google.inject.Inject(optional = true)
    public void setReleaseHelperThreads(@Named("bonecp.releaseHelperThreads") int releaseHelperThreads) {
        this.dataSource.setReleaseHelperThreads(releaseHelperThreads);
    }

    @com.google.inject.Inject(optional = true)
    public void setServiceOrder(@Named("bonecp.serviceOrder") String serviceOrder) {
        this.dataSource.setServiceOrder(serviceOrder);
    }

    @com.google.inject.Inject(optional = true)
    public void setStatementReleaseHelperThreads(@Named("bonecp.statementReleaseHelperThreads") int statementReleaseHelperThreads) {
        this.dataSource.setStatementReleaseHelperThreads(statementReleaseHelperThreads);
    }

    @com.google.inject.Inject(optional = true)
    public void setStatementsCacheSize(@Named("bonecp.statementsCacheSize") int statementsCacheSize) {
        this.dataSource.setStatementsCacheSize(statementsCacheSize);
    }

    @com.google.inject.Inject(optional = true)
    public void setTransactionRecoveryEnabled(@Named("bonecp.transactionRecoveryEnabled") boolean transactionRecoveryEnabled) {
        this.dataSource.setTransactionRecoveryEnabled(transactionRecoveryEnabled);
    }

    @Inject
    public void setUsername(@Named("JDBC.username") String username) {
        this.dataSource.setUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    public DataSource get() {
        return this.dataSource;
    }

}
