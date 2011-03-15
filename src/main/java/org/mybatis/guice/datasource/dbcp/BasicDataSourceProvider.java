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

import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.inject.Provider;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Provides the Apache commons-dbcp {@code BasicDataSource}.
 *
 * @version $Id$
 */
public final class BasicDataSourceProvider implements Provider<DataSource> {

    /**
     * The BasicDataSource reference.
     */
    private final BasicDataSource dataSource = new BasicDataSource();

    /**
     * Creates a new BasicDataSource using the needed parameter.
     *
     * @param driver The JDBC driver class.
     * @param url the database URL of the form <code>jdbc:subprotocol:subname</code>.
     * @param username the database user.
     * @param password the user's password.
     */
    @Inject
    public BasicDataSourceProvider(@Named("JDBC.driver") final String driver,
            @Named("JDBC.url") final String url,
            @Named("JDBC.username") final String username,
            @Named("JDBC.password") final String password) {
        this.dataSource.setDriverClassName(driver);
        this.dataSource.setUrl(url);
        this.dataSource.setUsername(username);
        this.dataSource.setPassword(password);
    }

    /**
     *
     *
     * @param autoCommit
     */
    @Inject(optional = true)
    public void setAutoCommit(@Named("JDBC.autoCommit") final boolean autoCommit) {
        this.dataSource.setDefaultAutoCommit(autoCommit);
    }

    /**
     *
     *
     * @param loginTimeout
     */
    @Inject(optional = true)
    public void setLoginTimeout(@Named("JDBC.loginTimeout") final int loginTimeout) {
        try {
            this.dataSource.setLoginTimeout(loginTimeout);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible to set DBCP login timeout '"
                    + loginTimeout
                    + "', see nested exceptions", e);
        }
    }

    /**
     *
     * @param driverProperties
     */
    @Inject(optional = true)
    public void setDriverProperties(@Named("JDBC.driverProperties") final Properties driverProperties) {
        for (Entry<Object, Object> property : driverProperties.entrySet()) {
            String name = property.getKey().toString();
            String value = property.getValue().toString();
            this.dataSource.addConnectionProperty(name, value);
        }
    }

    /**
     *
     *
     * @param allow
     */
    @Inject(optional = true)
    public void setAccessToUnderlyingConnectionAllowed(@Named("DBCP.accessToUnderlyingConnectionAllowed") final boolean allow) {
        this.dataSource.setAccessToUnderlyingConnectionAllowed(allow);
    }

    /**
     *
     *
     * @param defaultCatalog
     */
    @Inject(optional = true)
    public void setDefaultCatalog(@Named("DBCP.defaultCatalog") final String defaultCatalog) {
        this.dataSource.setDefaultCatalog(defaultCatalog);
    }

    /**
     *
     *
     * @param defaultReadOnly
     */
    @Inject(optional = true)
    public void setDefaultReadOnly(@Named("DBCP.defaultReadOnly") final boolean defaultReadOnly) {
        this.dataSource.setDefaultReadOnly(defaultReadOnly);
    }

    /**
     *
     *
     * @param defaultTransactionIsolation
     */
    @Inject(optional = true)
    public void setDefaultTransactionIsolation(@Named("DBCP.defaultTransactionIsolation") final int defaultTransactionIsolation) {
        this.dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
    }

    /**
     *
     *
     * @param initialSize
     */
    @Inject(optional = true)
    public void setInitialSize(@Named("DBCP.initialSize") final int initialSize) {
        this.dataSource.setInitialSize(initialSize);
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
     * @param maxOpenPreparedStatements
     */
    @Inject(optional = true)
    public void setMaxOpenPreparedStatements(@Named("DBCP.maxOpenPreparedStatements") final int maxOpenPreparedStatements) {
        this.dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
    }

    /**
     *
     *
     * @param maxWait
     */
    @Inject(optional = true)
    public void setMaxWait(@Named("DBCP.maxWait") final long maxWait) {
        this.dataSource.setMaxWait(maxWait);
    }

    /**
     *
     *
     * @param minEvictableIdleTimeMillis
     */
    @Inject(optional = true)
    public void setMinEvictableIdleTimeMillis(@Named("DBCP.minEvictableIdleTimeMillis") final long minEvictableIdleTimeMillis) {
        this.dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    /**
     *
     *
     * @param minIdle
     */
    @Inject(optional = true)
    public void setMinIdle(@Named("DBCP.minIdle") final int minIdle) {
        this.dataSource.setMinIdle(minIdle);
    }

    /**
     *
     *
     * @param numTestsPerEvictionRun
     */
    @Inject(optional = true)
    public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") final int numTestsPerEvictionRun) {
        this.dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    }

    /**
     *
     *
     * @param poolPreparedStatements
     */
    @Inject(optional = true)
    public void setPoolPreparedStatements(@Named("DBCP.poolPreparedStatements") final boolean poolPreparedStatements) {
        this.dataSource.setPoolPreparedStatements(poolPreparedStatements);
    }

    /**
     *
     *
     * @param testOnBorrow
     */
    @Inject(optional = true)
    public void setTestOnBorrow(@Named("DBCP.testOnBorrow") final boolean testOnBorrow) {
        this.dataSource.setTestOnBorrow(testOnBorrow);
    }

    /**
     *
     *
     * @param testOnReturn
     */
    @Inject(optional = true)
    public void setTestOnReturn(@Named("DBCP.testOnReturn") final boolean testOnReturn) {
        this.dataSource.setTestOnReturn(testOnReturn);
    }

    /**
     *
     *
     * @param testWhileIdle
     */
    @Inject(optional = true)
    public void setTestWhileIdle(@Named("DBCP.testWhileIdle") final boolean testWhileIdle) {
        this.dataSource.setTestWhileIdle(testWhileIdle);
    }

    /**
     *
     *
     * @param validationQuery
     */
    @Inject(optional = true)
    public void setValidationQuery(@Named("DBCP.validationQuery") final String validationQuery) {
        this.dataSource.setValidationQuery(validationQuery);
    }

    /**
     * {@inheritDoc}
     */
    public DataSource get() {
        return this.dataSource;
    }

}
