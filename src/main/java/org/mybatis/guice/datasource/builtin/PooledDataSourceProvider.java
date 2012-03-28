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
package org.mybatis.guice.datasource.builtin;

import com.google.inject.Singleton;
import org.apache.ibatis.datasource.pooled.PooledDataSource;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides the myBatis built-in PooledDataSource.
 *
 * @version $Id$
 */
public final class PooledDataSourceProvider implements Provider<DataSource> {

    private String driver, url, username, password, pingQuery;
    private boolean autoCommit, pingEnabled;
    private int loginTimeout, maximumActiveConnections, maximumCheckoutTime, maximumIdleConnections, pingConnectionsNotUsedFor, timeToWait;
    private Properties driverProperties;

    /**
     * Creates a new PooledDataSource using the needed parameter.
     *
     * @param driver The JDBC driver class.
     * @param url    the database URL of the form <code>jdbc:subprotocol:subname</code>.
     */
    @Inject
    public PooledDataSourceProvider(@Named("JDBC.driver") final String driver,
                                    @Named("JDBC.url") final String url) {
        this.driver = driver;
        this.url = url;
    }

    /**
     * @param username the database user.
     */
    @com.google.inject.Inject(optional = true)
    public void setUsername(@Named("JDBC.username") final String username) {
        this.username = username;
    }

    /**
     * @param password the user's password.
     */
    @com.google.inject.Inject(optional = true)
    public void setPassword(@Named("JDBC.password") final String password) {
        this.password = password;
    }

    /**
     * @param autoCommit
     */
    @com.google.inject.Inject(optional = true)
    public void setAutoCommit(@Named("JDBC.autoCommit") final boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    /**
     * @param loginTimeout
     */
    @com.google.inject.Inject(optional = true)
    public void setLoginTimeout(@Named("JDBC.loginTimeout") final int loginTimeout) {
        this.loginTimeout = loginTimeout;
    }

    @com.google.inject.Inject(optional = true)
    public void setDriverProperties(@Named("JDBC.driverProperties") final Properties driverProperties) {
        this.driverProperties = driverProperties;
    }

    /**
     * @param maximumActiveConnections
     */
    @com.google.inject.Inject(optional = true)
    public void setMaximumActiveConnections(@Named("mybatis.pooled.maximumActiveConnections") final int maximumActiveConnections) {
        this.maximumActiveConnections = maximumActiveConnections;
    }

    /**
     * @param maximumCheckoutTime
     */
    @com.google.inject.Inject(optional = true)
    public void setMaximumCheckoutTime(@Named("mybatis.pooled.maximumCheckoutTime") final int maximumCheckoutTime) {
        this.maximumCheckoutTime = maximumCheckoutTime;
    }

    /**
     * @param maximumIdleConnections
     */
    @com.google.inject.Inject(optional = true)
    public void setMaximumIdleConnections(@Named("mybatis.pooled.maximumIdleConnections") final int maximumIdleConnections) {
        this.maximumIdleConnections = maximumIdleConnections;
    }

    /**
     * @param pingConnectionsNotUsedFor
     */
    @com.google.inject.Inject(optional = true)
    public void setPingConnectionsNotUsedFor(@Named("mybatis.pooled.pingConnectionsNotUsedFor") final int pingConnectionsNotUsedFor) {
        this.pingConnectionsNotUsedFor = pingConnectionsNotUsedFor;
    }

    /**
     * @param pingEnabled
     */
    @com.google.inject.Inject(optional = true)
    public void setPingEnabled(@Named("mybatis.pooled.pingEnabled") final boolean pingEnabled) {
        this.pingEnabled = pingEnabled;
    }

    /**
     * @param pingQuery
     */
    @com.google.inject.Inject(optional = true)
    public void setPingEnabled(@Named("mybatis.pooled.pingQuery") final String pingQuery) {
        this.pingQuery = pingQuery;
    }

    /**
     * @param timeToWait
     */
    @com.google.inject.Inject(optional = true)
    public void setTimeToWait(@Named("mybatis.pooled.timeToWait") final int timeToWait) {
        this.timeToWait = timeToWait;
    }

    /**
     * {@inheritDoc}
     */
    @Singleton
    public DataSource get() {
        PooledDataSource pooledDataSource = new PooledDataSource(this.getClass().getClassLoader(), driver, url, username, password);

        pooledDataSource.setUsername(username);
        pooledDataSource.setPassword(password);
        pooledDataSource.setDefaultAutoCommit(autoCommit);
        pooledDataSource.setDriverProperties(driverProperties);
        pooledDataSource.setPoolMaximumActiveConnections(maximumActiveConnections);
        pooledDataSource.setPoolMaximumCheckoutTime(maximumCheckoutTime);
        pooledDataSource.setPoolMaximumIdleConnections(maximumIdleConnections);
        pooledDataSource.setPoolTimeToWait(timeToWait);

        if (pingEnabled) {
            pooledDataSource.setPoolPingEnabled(pingEnabled);
            pooledDataSource.setPoolPingQuery(pingQuery);
            pooledDataSource.setPoolPingConnectionsNotUsedFor(pingConnectionsNotUsedFor);
        }

        try {
            pooledDataSource.setLoginTimeout(loginTimeout);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible to set login timeout '"
                    + loginTimeout
                    + "' to Pooled Data Source", e);
        }

        return pooledDataSource;
    }

}
