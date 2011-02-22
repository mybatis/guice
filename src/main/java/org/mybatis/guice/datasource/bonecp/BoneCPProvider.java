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

import javax.inject.Provider;
import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.hooks.ConnectionHook;

/**
 * Provides the BoneCP DataSource.
 *
 * @version $Id$
 */
public final class BoneCPProvider implements Provider<DataSource> {

    private final BoneCPConfig config = new BoneCPConfig();

    public void setAcquireIncrement(int acquireIncrement) {
        this.config.setAcquireIncrement(acquireIncrement);
    }

    public void setAcquireRetryAttempts(int acquireRetryAttempts) {
        this.config.setAcquireRetryAttempts(acquireRetryAttempts);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.config.setClassLoader(classLoader);
    }

    public void setCloseConnectionWatch(boolean closeConnectionWatch) {
        this.config.setCloseConnectionWatch(closeConnectionWatch);
    }

    public void setConfigFile(String configFile) {
        this.config.setConfigFile(configFile);
    }

    public void setConnectionHook(ConnectionHook connectionHook) {
        this.config.setConnectionHook(connectionHook);
    }

    public void setConnectionHookClassName(String connectionHookClassName) {
        this.config.setConnectionHookClassName(connectionHookClassName);
    }

    public void setConnectionTestStatement(String connectionTestStatement) {
        this.config.setConnectionTestStatement(connectionTestStatement);
    }

    public void setDisableConnectionTracking(boolean disableConnectionTracking) {
        this.config.setDisableConnectionTracking(disableConnectionTracking);
    }

    public void setDisableJMX(boolean disableJMX) {
        this.config.setDisableJMX(disableJMX);
    }

    public void setDriverProperties(Properties driverProperties) {
        this.config.setDriverProperties(driverProperties);
    }

    public void setIdleConnectionTestPeriod(long idleConnectionTestPeriod, TimeUnit timeUnit) {
        this.config.setIdleConnectionTestPeriod(idleConnectionTestPeriod, timeUnit);
    }

    public void setIdleConnectionTestPeriodInMinutes(long idleConnectionTestPeriod) {
        this.config.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriod);
    }

    public void setIdleConnectionTestPeriodInSeconds(long idleConnectionTestPeriod) {
        this.config.setIdleConnectionTestPeriodInSeconds(idleConnectionTestPeriod);
    }

    public void setIdleMaxAge(long idleMaxAge, TimeUnit timeUnit) {
        this.config.setIdleMaxAge(idleMaxAge, timeUnit);
    }

    public void setInitSQL(String initSQL) {
        this.config.setInitSQL(initSQL);
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.config.setJdbcUrl(jdbcUrl);
    }

    public void setLazyInit(boolean lazyInit) {
        this.config.setLazyInit(lazyInit);
    }

    public void setLogStatementsEnabled(boolean logStatementsEnabled) {
        this.config.setLogStatementsEnabled(logStatementsEnabled);
    }

    public void setMaxConnectionAge(long maxConnectionAge, TimeUnit timeUnit) {
        this.config.setMaxConnectionAge(maxConnectionAge, timeUnit);
    }

    public void setMaxConnectionAgeInSeconds(long maxConnectionAgeInSeconds) {
        this.config.setMaxConnectionAgeInSeconds(maxConnectionAgeInSeconds);
    }

    public void setMaxConnectionsPerPartition(int maxConnectionsPerPartition) {
        this.config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
    }

    public void setMinConnectionsPerPartition(int minConnectionsPerPartition) {
        this.config.setMinConnectionsPerPartition(minConnectionsPerPartition);
    }

    public void setPartitionCount(int partitionCount) {
        this.config.setPartitionCount(partitionCount);
    }

    public void setPassword(String password) {
        this.config.setPassword(password);
    }

    public void setPoolAvailabilityThreshold(int poolAvailabilityThreshold) {
        this.config.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
    }

    public void setPoolName(String poolName) {
        this.config.setPoolName(poolName);
    }

    public void setQueryExecuteTimeLimit(int queryExecuteTimeLimit, TimeUnit timeUnit) {
        this.config.setQueryExecuteTimeLimit(queryExecuteTimeLimit, timeUnit);
    }

    public void setReleaseHelperThreads(int releaseHelperThreads) {
        this.config.setReleaseHelperThreads(releaseHelperThreads);
    }

    public void setServiceOrder(String serviceOrder) {
        this.config.setServiceOrder(serviceOrder);
    }

    public void setStatementReleaseHelperThreads(int statementReleaseHelperThreads) {
        this.config.setStatementReleaseHelperThreads(statementReleaseHelperThreads);
    }

    public void setStatementsCacheSize(int statementsCacheSize) {
        this.config.setStatementsCacheSize(statementsCacheSize);
    }

    public void setTransactionRecoveryEnabled(boolean transactionRecoveryEnabled) {
        this.config.setTransactionRecoveryEnabled(transactionRecoveryEnabled);
    }

    public void setUsername(String username) {
        this.config.setUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    public DataSource get() {
        // TODO Auto-generated method stub
        return null;
    }

}
