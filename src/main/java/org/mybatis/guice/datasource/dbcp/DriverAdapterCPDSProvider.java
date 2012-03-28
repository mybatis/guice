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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.ConnectionPoolDataSource;

import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;

/**
 * Provides the Apache commons-dbcp {@code DriverAdapterCPDS}.
 *
 * @version $Id$
 */
public final class DriverAdapterCPDSProvider implements Provider<ConnectionPoolDataSource> {

    private final DriverAdapterCPDS adapter = new DriverAdapterCPDS();

    @Inject
    public DriverAdapterCPDSProvider(@Named("JDBC.driver") final String driver,
            @Named("JDBC.url") final String url,
            @Named("JDBC.username") final String username,
            @Named("JDBC.password") final String password) {
        try {
            this.adapter.setDriver(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver '"
                    + driver
                    + "' not found in the classpath", e);
        }
        this.adapter.setUrl(url);
        this.adapter.setUser(username);
        this.adapter.setPassword(password);
    }

    @com.google.inject.Inject(optional = true)
    public void setDescription(@Named("DBCP.description") String description) {
        this.adapter.setDescription(description);
    }

    @com.google.inject.Inject(optional = true)
    public void setLoginTimeout(@Named("JDBC.loginTimeout") int seconds) {
        this.adapter.setLoginTimeout(seconds);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxActive(@Named("DBCP.maxActive") int maxActive) {
        this.adapter.setMaxActive(maxActive);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxIdle(@Named("DBCP.maxIdle") int maxIdle) {
        this.adapter.setMaxIdle(maxIdle);
    }

    @com.google.inject.Inject(optional = true)
    public void setMaxPreparedStatements(@Named("DBCP.maxOpenPreparedStatements") int maxPreparedStatements) {
        this.adapter.setMaxPreparedStatements(maxPreparedStatements);
    }

    @com.google.inject.Inject(optional = true)
    public void setMinEvictableIdleTimeMillis(@Named("DBCP.minEvictableIdleTimeMillis") int minEvictableIdleTimeMillis) {
        this.adapter.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    @com.google.inject.Inject(optional = true)
    public void setNumTestsPerEvictionRun(@Named("DBCP.numTestsPerEvictionRun") int numTestsPerEvictionRun) {
        this.adapter.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    }

    @com.google.inject.Inject(optional = true)
    public void setPoolPreparedStatements(@Named("DBCP.poolPreparedStatements") boolean poolPreparedStatements) {
        this.adapter.setPoolPreparedStatements(poolPreparedStatements);
    }

    @com.google.inject.Inject(optional = true)
    public void setTimeBetweenEvictionRunsMillis(@Named("DBCP.timeBetweenEvictionRunsMillis") int timeBetweenEvictionRunsMillis) {
        this.adapter.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    }

    public ConnectionPoolDataSource get() {
        return this.adapter;
    }

}
