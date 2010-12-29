/*
 *    Copyright 2010 The myBatis Team
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

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * Provides the Apache commons-dbcp {@code SharedPoolDataSource}.
 *
 * @version $Id$
 */
public final class SharedPoolDataSourceProvider implements Provider<DataSource> {

    private final SharedPoolDataSource dataSource = new SharedPoolDataSource();

    @Inject
    public SharedPoolDataSourceProvider(ConnectionPoolDataSource cpds) {
        this.dataSource.setConnectionPoolDataSource(cpds);
    }

    @Inject(optional = true)
    public void setDataSourceName(@Named("DBCP.name") String name) {
        this.dataSource.setDataSourceName(name);
    }

    @Inject(optional = true)
    public void setDefaultAutoCommit(@Named("JDBC.autoCommit") boolean autoCommit) {
        this.dataSource.setDefaultAutoCommit(autoCommit);
    }

    @Inject(optional = true)
    public void setDefaultReadOnly(@Named("DBCP.defaultReadOnly") boolean defaultReadOnly) {
        this.dataSource.setDefaultReadOnly(defaultReadOnly);
    }

    @Inject(optional = true)
    public void setDefaultTransactionIsolation(@Named("DBCP.defaultTransactionIsolation") int defaultTransactionIsolation) {
        this.dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
    }

    @Inject(optional = true)
    public void setDescription(@Named("DBCP.description") String description) {
        this.dataSource.setDescription(description);
    }

    @Inject(optional = true)
    public void setJndiEnvironment(@Named("DBCP.jndi.key") String key, @Named("DBCP.jndi.value") String value) {
        this.dataSource.setJndiEnvironment(key, value);
    }

    @Inject(optional = true)
    public void setLoginTimeout(@Named("JDBC.loginTimeout") int loginTimeout) {
        this.dataSource.setLoginTimeout(loginTimeout);
    }

    @Inject(optional = true)
    public void setMinEvictableIdleTimeMillis(@Named("DBCP.minEvictableIdleTimeMillis") int minEvictableIdleTimeMillis) {
        this.dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    @Inject(optional = true)
    public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") int numTestsPerEvictionRun) {
        this.dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    }

    @Inject(optional = true)
    public void setRollbackAfterValidation(@Named("DBCP.rollbackAfterValidation") boolean rollbackAfterValidation) {
        this.dataSource.setRollbackAfterValidation(rollbackAfterValidation);
    }

    @Inject(optional = true)
    public void setTestOnBorrow(@Named("DBCP.testOnBorrow") boolean testOnBorrow) {
        this.dataSource.setTestOnBorrow(testOnBorrow);
    }

    @Inject(optional = true)
    public void setTestOnReturn(@Named("DBCP.testOnReturn") boolean testOnReturn) {
        this.dataSource.setTestOnReturn(testOnReturn);
    }

    @Inject(optional = true)
    public void setTestWhileIdle(@Named("DBCP.testWhileIdle") boolean testWhileIdle) {
        this.dataSource.setTestWhileIdle(testWhileIdle);
    }

    @Inject(optional = true)
    public void setTimeBetweenEvictionRunsMillis(@Named("DBCP.timeBetweenEvictionRunsMillis") int timeBetweenEvictionRunsMillis) {
        this.dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    }

    @Inject(optional = true)
    public void setValidationQuery(@Named("DBCP.validationQuery") String validationQuery) {
        this.dataSource.setValidationQuery(validationQuery);
    }

    /**
     *
     *
     * @param maxActive
     */
    @Inject(optional = true)
    public void setMaxActive(@Named("DBCP.maxActive") final int maxActive) {
        this.dataSource.setMaxActive(maxActive);
    }

    /**
     *
     *
     * @param maxIdle
     */
    @Inject(optional = true)
    public void setMaxIdle(@Named("DBCP.maxIdle") final int maxIdle) {
        this.dataSource.setMaxIdle(maxIdle);
    }

    /**
     *
     *
     * @param maxWait
     */
    @Inject(optional = true)
    public void setMaxWait(@Named("DBCP.maxWait") final int maxWait) {
        this.dataSource.setMaxWait(maxWait);
    }

    /**
     * {@inheritDoc}
     */
    public DataSource get() {
        return this.dataSource;
    }

}
