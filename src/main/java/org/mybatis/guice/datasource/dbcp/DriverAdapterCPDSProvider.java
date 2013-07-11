/*
 *    Copyright 2010-2012 The MyBatis Team
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

import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.ConnectionPoolDataSource;

/**
 * Provides the Apache commons-dbcp {@code DriverAdapterCPDS}.
 *
 * @version $Id$
 */
public final class DriverAdapterCPDSProvider implements Provider<ConnectionPoolDataSource> {

    private final DriverAdapterCPDS adapter = new DriverAdapterCPDS();

    @Inject
    public DriverAdapterCPDSProvider(@Named("JDBC.driver") final String driver,
            @Named("JDBC.url") final String url) {
        try {
            adapter.setDriver(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver '"
                    + driver
                    + "' not found in the classpath", e);
        }
        adapter.setUrl(url);
    }

    /**
     *
     * @param username
     * @since 3.3
     */
    @com.google.inject.Inject(optional = true)
    public void setUser(@Named("JDBC.username") final String username) {
        adapter.setUser(username);
    }

    /**
     *
     * @param password
     * @since 3.3
     */
    @com.google.inject.Inject(optional = true)
    public void setPassword(@Named("JDBC.password") final String password) {
        adapter.setPassword(password);
    }

    @com.google.inject.Inject(optional = true)
    public void setDescription(@Named("DBCP.description") String description) {
        adapter.setDescription(description);
    }

    @com.google.inject.Inject(optional = true)
    public void setLoginTimeout(@Named("JDBC.loginTimeout") int seconds) {
        adapter.setLoginTimeout(seconds);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxActive(@Named("DBCP.maxActive") int maxActive) {
        adapter.setMaxActive(maxActive);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxIdle(@Named("DBCP.maxIdle") int maxIdle) {
        adapter.setMaxIdle(maxIdle);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxPreparedStatements(@Named("DBCP.maxOpenPreparedStatements") int maxPreparedStatements) {
        adapter.setMaxPreparedStatements(maxPreparedStatements);
    }

    @com.google.inject.Inject(optional = true)
    public void setMinEvictableIdleTimeMillis(@Named("DBCP.minEvictableIdleTimeMillis") int minEvictableIdleTimeMillis) {
        adapter.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    @com.google.inject.Inject(optional = true)
    public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") int numTestsPerEvictionRun) {
        adapter.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    }

    @com.google.inject.Inject(optional = true)
    public void setPoolPreparedStatements(@Named("DBCP.poolPreparedStatements") boolean poolPreparedStatements) {
        adapter.setPoolPreparedStatements(poolPreparedStatements);
    }

    @com.google.inject.Inject(optional = true)
    public void setTimeBetweenEvictionRunsMillis(@Named("DBCP.timeBetweenEvictionRunsMillis") int timeBetweenEvictionRunsMillis) {
        adapter.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    }

    public ConnectionPoolDataSource get() {
        return adapter;
    }

}
