/*
 *    Copyright 2010-2011 The MyBatis Team
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

import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.datasources.PerUserPoolDataSource;

/**
 * Provides the Apache commons-dbcp {@code PerUserPoolDataSource}.
 *
 * @version $Id$
 */
public final class PerUserPoolDataSourceProvider implements Provider<DataSource> {

    private final PerUserPoolDataSource dataSource = new PerUserPoolDataSource();

    @com.google.inject.Inject(optional = true)
    public void setConnectionPoolDataSource(ConnectionPoolDataSource cpds) {
        this.dataSource.setConnectionPoolDataSource(cpds);
    }

    @com.google.inject.Inject(optional = true)
    public void setDataSourceName(@Named("DBCP.name") String name) {
        this.dataSource.setDataSourceName(name);
    }

    @com.google.inject.Inject(optional = true)
    public void setDefaultAutoCommit(@Named("JDBC.autoCommit") boolean autoCommit) {
        this.dataSource.setDefaultAutoCommit(autoCommit);
    }

    @com.google.inject.Inject(optional = true)
    public void setDefaultReadOnly(@Named("DBCP.defaultReadOnly") boolean defaultReadOnly) {
        this.dataSource.setDefaultReadOnly(defaultReadOnly);
    }

    @com.google.inject.Inject(optional = true)
    public void setDefaultTransactionIsolation(@Named("DBCP.defaultTransactionIsolation") int defaultTransactionIsolation) {
        this.dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
    }

    @com.google.inject.Inject(optional = true)
    public void setDescription(@Named("DBCP.description") String description) {
        this.dataSource.setDescription(description);
    }

    @com.google.inject.Inject(optional = true)
    public void setJndiEnvironment(@Named("DBCP.jndi.key") String key, @Named("DBCP.jndi.value") String value) {
        this.dataSource.setJndiEnvironment(key, value);
    }

    @com.google.inject.Inject(optional = true)
    public void setLoginTimeout(@Named("JDBC.loginTimeout") int loginTimeout) {
        this.dataSource.setLoginTimeout(loginTimeout);
    }

    @com.google.inject.Inject(optional = true)
    public void setMinEvictableIdleTimeMillis(@Named("DBCP.minEvictableIdleTimeMillis") int minEvictableIdleTimeMillis) {
        this.dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    @com.google.inject.Inject(optional = true)
    public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") int numTestsPerEvictionRun) {
        this.dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    }

    @com.google.inject.Inject(optional = true)
    public void setRollbackAfterValidation(@Named("DBCP.rollbackAfterValidation") boolean rollbackAfterValidation) {
        this.dataSource.setRollbackAfterValidation(rollbackAfterValidation);
    }

    @com.google.inject.Inject(optional = true)
    public void setTestOnBorrow(@Named("DBCP.testOnBorrow") boolean testOnBorrow) {
        this.dataSource.setTestOnBorrow(testOnBorrow);
    }

    @com.google.inject.Inject(optional = true)
    public void setTestOnReturn(@Named("DBCP.testOnReturn") boolean testOnReturn) {
        this.dataSource.setTestOnReturn(testOnReturn);
    }

    @com.google.inject.Inject(optional = true)
    public void setTestWhileIdle(@Named("DBCP.testWhileIdle") boolean testWhileIdle) {
        this.dataSource.setTestWhileIdle(testWhileIdle);
    }

    @com.google.inject.Inject(optional = true)
    public void setTimeBetweenEvictionRunsMillis(@Named("DBCP.timeBetweenEvictionRunsMillis") int timeBetweenEvictionRunsMillis) {
        this.dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    }

    @com.google.inject.Inject(optional = true)
    public void setValidationQuery(@Named("DBCP.validationQuery") String validationQuery) {
        this.dataSource.setValidationQuery(validationQuery);
    }

    @com.google.inject.Inject(optional = true)
    public void setDefaultMaxActive(@Named("DBCP.maxActive") int maxActive) {
        this.dataSource.setDefaultMaxActive(maxActive);
    }

    @com.google.inject.Inject(optional = true)
    public void setDefaultMaxIdle(@Named("DBCP.maxIdle") int defaultMaxIdle) {
        this.dataSource.setDefaultMaxIdle(defaultMaxIdle);
    }

    @com.google.inject.Inject(optional = true)
    public void setDefaultMaxWait(@Named("DBCP.maxWait") int defaultMaxWait) {
        this.dataSource.setDefaultMaxWait(defaultMaxWait);
    }

    @com.google.inject.Inject(optional = true)
    public void setPerUserDefaultAutoCommit(@PerUserDefaultAutoCommit Map<String, Boolean> perUserDefaultAutoCommit) {
        for (Entry<String, Boolean> entry : perUserDefaultAutoCommit.entrySet()) {
            this.dataSource.setPerUserDefaultAutoCommit(entry.getKey(), entry.getValue());
        }
    }

    @com.google.inject.Inject(optional = true)
    public void setPerUserDefaultReadOnly(@PerUserDefaultReadOnly Map<String, Boolean> perUserDefaultReadOnly) {
        for (Entry<String, Boolean> entry : perUserDefaultReadOnly.entrySet()) {
            this.dataSource.setPerUserDefaultReadOnly(entry.getKey(), entry.getValue());
        }
    }

    @com.google.inject.Inject(optional = true)
    public void setPerUserDefaultTransactionIsolation(@PerUserDefaultTransactionIsolation Map<String, Integer> perUserDefaultTransactionIsolation) {
        for (Entry<String, Integer> entry : perUserDefaultTransactionIsolation.entrySet()) {
            this.dataSource.setPerUserDefaultTransactionIsolation(entry.getKey(), entry.getValue());
        }
    }

    @com.google.inject.Inject(optional = true)
    public void setPerUserMaxActive(@PerUserMaxActive Map<String, Integer> perUserMaxActive) {
        for (Entry<String, Integer> entry : perUserMaxActive.entrySet()) {
            this.dataSource.setPerUserMaxActive(entry.getKey(), entry.getValue());
        }
    }

    @com.google.inject.Inject(optional = true)
    public void setPerUserMaxIdle(@PerUserMaxIdle Map<String, Integer> perUserMaxIdle) {
        for (Entry<String, Integer> entry : perUserMaxIdle.entrySet()) {
            this.dataSource.setPerUserMaxIdle(entry.getKey(), entry.getValue());
        }
    }

    @com.google.inject.Inject(optional = true)
    public void setPerUserMaxWait(@PerUserMaxWait Map<String, Integer> perUserMaxWait) {
        for (Entry<String, Integer> entry : perUserMaxWait.entrySet()) {
            this.dataSource.setPerUserMaxWait(entry.getKey(), entry.getValue());
        }
    }

    public DataSource get() {
        return this.dataSource;
    }

}
