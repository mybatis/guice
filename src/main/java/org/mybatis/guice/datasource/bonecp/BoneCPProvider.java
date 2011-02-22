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

import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

import com.google.inject.Inject;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import com.jolbox.bonecp.hooks.ConnectionHook;

/**
 * Provides the BoneCP DataSource.
 *
 * @version $Id$
 */
public final class BoneCPProvider implements Provider<DataSource> {

    private final BoneCPConfig config = new BoneCPConfig();

    @Inject(optional = true)
    public void setAcquireIncrement(@Named("bonecp.acquireIncrement") int acquireIncrement) {
        this.config.setAcquireIncrement(acquireIncrement);
    }

    @Inject(optional = true)
    public void setAcquireRetryAttempts(@Named("bonecp.acquireRetryAttempts") int acquireRetryAttempts) {
        this.config.setAcquireRetryAttempts(acquireRetryAttempts);
    }

    @Inject(optional = true)
    public void setClassLoader(@Named("bonecp.classLoader") ClassLoader classLoader) {
        this.config.setClassLoader(classLoader);
    }

    @Inject(optional = true)
    public void setCloseConnectionWatch(@Named("bonecp.closeConnectionWatch") boolean closeConnectionWatch) {
        this.config.setCloseConnectionWatch(closeConnectionWatch);
    }

    @Inject(optional = true)
    public void setConnectionHook(@Named("bonecp.connectionHook") ConnectionHook connectionHook) {
        this.config.setConnectionHook(connectionHook);
    }

    @Inject(optional = true)
    public void setConnectionHookClassName(@Named("bonecp.connectionHookClassName") String connectionHookClassName) {
        this.config.setConnectionHookClassName(connectionHookClassName);
    }

    @Inject(optional = true)
    public void setConnectionTestStatement(@Named("bonecp.connectionTestStatement") String connectionTestStatement) {
        this.config.setConnectionTestStatement(connectionTestStatement);
    }

    @Inject(optional = true)
    public void setDisableConnectionTracking(@Named("bonecp.disableConnectionTracking") boolean disableConnectionTracking) {
        this.config.setDisableConnectionTracking(disableConnectionTracking);
    }

    @Inject(optional = true)
    public void setDisableJMX(@Named("bonecp.disableJMX") boolean disableJMX) {
        this.config.setDisableJMX(disableJMX);
    }

    @Inject(optional = true)
    public void setDriverProperties(@Named("bonecp.driverProperties") Properties driverProperties) {
        this.config.setDriverProperties(driverProperties);
    }

    @Inject(optional = true)
    public void setIdleConnectionTestPeriod(@Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod,
            @Named("bonecp.timeUnit") TimeUnit timeUnit) {
        this.config.setIdleConnectionTestPeriod(idleConnectionTestPeriod, timeUnit);
    }

    @Inject(optional = true)
    public void setIdleConnectionTestPeriodInMinutes(@Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod) {
        this.config.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriod);
    }

    @Inject(optional = true)
    public void setIdleConnectionTestPeriodInSeconds(@Named("bonecp.idleConnectionTestPeriod") long idleConnectionTestPeriod) {
        this.config.setIdleConnectionTestPeriodInSeconds(idleConnectionTestPeriod);
    }

    @Inject(optional = true)
    public void setIdleMaxAge(@Named("bonecp.idleMaxAge") long idleMaxAge, @Named("bonecp.timeUnit") TimeUnit timeUnit) {
        this.config.setIdleMaxAge(idleMaxAge, timeUnit);
    }

    @Inject(optional = true)
    public void setInitSQL(@Named("bonecp.initSQL") String initSQL) {
        this.config.setInitSQL(initSQL);
    }

    @Inject
    public void setJdbcUrl(@Named("JDBC.url") String jdbcUrl) {
        this.config.setJdbcUrl(jdbcUrl);
    }

    @Inject(optional = true)
    public void setLazyInit(@Named("bonecp.lazyInit") boolean lazyInit) {
        this.config.setLazyInit(lazyInit);
    }

    @Inject(optional = true)
    public void setLogStatementsEnabled(@Named("bonecp.logStatementsEnabled") boolean logStatementsEnabled) {
        this.config.setLogStatementsEnabled(logStatementsEnabled);
    }

    @Inject(optional = true)
    public void setMaxConnectionAge(@Named("bonecp.maxConnectionAge") long maxConnectionAge,
            @Named("bonecp.timeUnit") TimeUnit timeUnit) {
        this.config.setMaxConnectionAge(maxConnectionAge, timeUnit);
    }

    @Inject(optional = true)
    public void setMaxConnectionAgeInSeconds(@Named("bonecp.maxConnectionAgeInSeconds") long maxConnectionAgeInSeconds) {
        this.config.setMaxConnectionAgeInSeconds(maxConnectionAgeInSeconds);
    }

    @Inject(optional = true)
    public void setMaxConnectionsPerPartition(@Named("bonecp.maxConnectionsPerPartition") int maxConnectionsPerPartition) {
        this.config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
    }

    @Inject(optional = true)
    public void setMinConnectionsPerPartition(@Named("bonecp.minConnectionsPerPartition") int minConnectionsPerPartition) {
        this.config.setMinConnectionsPerPartition(minConnectionsPerPartition);
    }

    @Inject(optional = true)
    public void setPartitionCount(@Named("bonecp.partitionCount") int partitionCount) {
        this.config.setPartitionCount(partitionCount);
    }

    @Inject
    public void setPassword(@Named("JDBC.password") String password) {
        this.config.setPassword(password);
    }

    @Inject(optional = true)
    public void setPoolAvailabilityThreshold(@Named("bonecp.poolAvailabilityThreshold") int poolAvailabilityThreshold) {
        this.config.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
    }

    @Inject(optional = true)
    public void setPoolName(@Named("bonecp.poolName") String poolName) {
        this.config.setPoolName(poolName);
    }

    @Inject(optional = true)
    public void setQueryExecuteTimeLimit(@Named("bonecp.queryExecuteTimeLimit") int queryExecuteTimeLimit,
            @Named("bonecp.timeUnit") TimeUnit timeUnit) {
        this.config.setQueryExecuteTimeLimit(queryExecuteTimeLimit, timeUnit);
    }

    @Inject(optional = true)
    public void setReleaseHelperThreads(@Named("bonecp.releaseHelperThreads") int releaseHelperThreads) {
        this.config.setReleaseHelperThreads(releaseHelperThreads);
    }

    @Inject(optional = true)
    public void setServiceOrder(@Named("bonecp.serviceOrder") String serviceOrder) {
        this.config.setServiceOrder(serviceOrder);
    }

    @Inject(optional = true)
    public void setStatementReleaseHelperThreads(@Named("bonecp.statementReleaseHelperThreads") int statementReleaseHelperThreads) {
        this.config.setStatementReleaseHelperThreads(statementReleaseHelperThreads);
    }

    @Inject(optional = true)
    public void setStatementsCacheSize(@Named("bonecp.statementsCacheSize") int statementsCacheSize) {
        this.config.setStatementsCacheSize(statementsCacheSize);
    }

    @Inject(optional = true)
    public void setTransactionRecoveryEnabled(@Named("bonecp.transactionRecoveryEnabled") boolean transactionRecoveryEnabled) {
        this.config.setTransactionRecoveryEnabled(transactionRecoveryEnabled);
    }

    @Inject
    public void setUsername(@Named("JDBC.username") String username) {
        this.config.setUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    public DataSource get() {
        return new BoneCPDataSource(this.config);
    }

}
