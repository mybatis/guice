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
package org.mybatis.guice.datasource.c3p0;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Provides the C3P0 DataSource.
 *
 * @version $Id$
 */
public final class C3p0DataSourceProvider implements Provider<DataSource> {

    /**
     * The ComboPooledDataSource reference.
     */
    private final ComboPooledDataSource dataSource = new ComboPooledDataSource();

    /**
     * Creates a new ComboPooledDataSource using the needed parameter.
     *
     * @param driver The JDBC driver class.
     * @param url the database URL of the form <code>jdbc:subprotocol:subname</code>.
     * @param username the database user.
     * @param password the user's password.
     */
    @Inject
    public C3p0DataSourceProvider(@Named("JDBC.driver") final String driver,
            @Named("JDBC.url") final String url,
            @Named("JDBC.username") final String username,
            @Named("JDBC.password") final String password) {
        try {
            this.dataSource.setDriverClass(driver);
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Impossible to initialize C3P0 Data Source with driver class '"
                    + driver
                    + "', see nested exceptions", e);
        }
        this.dataSource.setJdbcUrl(url);
        this.dataSource.setUser(username);
        this.dataSource.setPassword(password);
    }

    /**
     * 
     *
     * @param acquireIncrement
     */
    @com.google.inject.Inject(optional = true)
    public void setAcquireIncrement(@Named("c3p0.acquireIncrement") final int acquireIncrement) {
        this.dataSource.setAcquireIncrement(acquireIncrement);
    }

    /**
     * 
     *
     * @param acquireRetryAttempts
     */
    @com.google.inject.Inject(optional = true)
    public void setAcquireRetryAttempts(@Named("c3p0.acquireRetryAttempts") final int acquireRetryAttempts) {
        this.dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
    }

    /**
     * 
     *
     * @param acquireRetryDelay
     */
    @com.google.inject.Inject(optional = true)
    public void setAcquireRetryDelay(@Named("c3p0.acquireRetryDelay") final int acquireRetryDelay) {
        this.dataSource.setAcquireRetryDelay(acquireRetryDelay);
    }

    /**
     * 
     *
     * @param autoCommit
     */
    @com.google.inject.Inject(optional = true)
    public void setAutoCommitOnClose(@Named("JDBC.autoCommit") final boolean autoCommit) {
        this.dataSource.setAutoCommitOnClose(autoCommit);
    }

    /**
     * 
     * @param driverProperties
     */
    @com.google.inject.Inject(optional = true)
    public void setDriverProperties(@Named("JDBC.driverProperties") final Properties driverProperties) {
        this.dataSource.setProperties(driverProperties);
    }

    /**
     * 
     *
     * @param automaticTestTable
     */
    @com.google.inject.Inject(optional = true)
    public void setAautomaticTestTable(@Named("c3p0.automaticTestTable") final String automaticTestTable) {
        this.dataSource.setAutomaticTestTable(automaticTestTable);
    }

    /**
     * 
     *
     * @param breakAfterAcquireFailure
     */
    @com.google.inject.Inject(optional = true)
    public void setBreakAfterAcquireFailure(@Named("c3p0.breakAfterAcquireFailure") final boolean breakAfterAcquireFailure) {
        this.dataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
    }

    /**
     * 
     *
     * @param checkoutTimeout
     */
    @com.google.inject.Inject(optional = true)
    public void setCheckoutTimeout(@Named("c3p0.checkoutTimeout") final int checkoutTimeout) {
        this.dataSource.setCheckoutTimeout(checkoutTimeout);
    }

    /**
     * 
     *
     * @param connectionCustomizerClassName
     */
    @com.google.inject.Inject(optional = true)
    public void setConnectionCustomizerClassName(@Named("c3p0.connectionCustomizerClassName") final String connectionCustomizerClassName) {
        this.dataSource.setConnectionCustomizerClassName(connectionCustomizerClassName);
    }

    /**
     * 
     *
     * @param connectionTesterClassName
     */
    @com.google.inject.Inject(optional = true)
    public void setConnectionTesterClassName(@Named("c3p0.connectionTesterClassName") final String connectionTesterClassName) {
        try {
            this.dataSource.setConnectionTesterClassName(connectionTesterClassName);
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Impossible to set C3P0 Data Source connection tester class name '"
                    + connectionTesterClassName
                    + "', see nested exceptions", e);
        }
    }

    /**
     * 
     *
     * @param idleConnectionTestPeriod
     */
    @com.google.inject.Inject(optional = true)
    public void setIdleConnectionTestPeriod(@Named("c3p0.idleConnectionTestPeriod") final int idleConnectionTestPeriod) {
        this.dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
    }

    /**
     * 
     *
     * @param initialPoolSize
     */
    @com.google.inject.Inject(optional = true)
    public void setInitialPoolSize(@Named("c3p0.initialPoolSize") final int initialPoolSize) {
        this.dataSource.setInitialPoolSize(initialPoolSize);
    }

    /**
     * 
     *
     * @param maxAdministrativeTaskTime
     */
    @com.google.inject.Inject(optional = true)
    public void setMaxAdministrativeTaskTime(@Named("c3p0.maxAdministrativeTaskTime") final int maxAdministrativeTaskTime) {
        this.dataSource.setMaxAdministrativeTaskTime(maxAdministrativeTaskTime);
    }

    /**
     * 
     *
     * @param maxConnectionAge
     */
    @com.google.inject.Inject(optional = true)
    public void setMaxConnectionAge(@Named("c3p0.maxConnectionAge") final int maxConnectionAge) {
        this.dataSource.setMaxConnectionAge(maxConnectionAge);
    }

    /**
     * 
     *
     * @param maxIdleTime
     */
    @com.google.inject.Inject(optional = true)
    public void setMaxIdleTime(@Named("c3p0.maxIdleTime") final int maxIdleTime) {
        this.dataSource.setMaxIdleTime(maxIdleTime);
    }

    /**
     * 
     *
     * @param maxIdleTimeExcessConnections
     */
    @com.google.inject.Inject(optional = true)
    public void setMaxIdleTimeExcessConnections(@Named("c3p0.maxIdleTimeExcessConnections") final int maxIdleTimeExcessConnections) {
        this.dataSource.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
    }

    /**
     * 
     *
     * @param maxPoolSize
     */
    @com.google.inject.Inject(optional = true)
    public void setMaxPoolSize(@Named("c3p0.maxPoolSize") final int maxPoolSize) {
        this.dataSource.setMaxPoolSize(maxPoolSize);
    }

    /**
     * 
     *
     * @param maxStatements
     */
    @com.google.inject.Inject(optional = true)
    public void setMaxStatements(@Named("c3p0.maxStatements") final int maxStatements) {
        this.dataSource.setMaxStatements(maxStatements);
    }

    /**
     * 
     *
     * @param maxStatementsPerConnection
     */
    @com.google.inject.Inject(optional = true)
    public void setMaxStatementsPerConnection(@Named("c3p0.maxStatementsPerConnection") final int maxStatementsPerConnection) {
        this.dataSource.setMaxStatementsPerConnection(maxStatementsPerConnection);
    }

    /**
     * 
     *
     * @param minPoolSize
     */
    @com.google.inject.Inject(optional = true)
    public void setMinPoolSize(@Named("c3p0.minPoolSize") final int minPoolSize) {
        this.dataSource.setMinPoolSize(minPoolSize);
    }

    /**
     * 
     *
     * @param preferredTestQuery
     */
    @com.google.inject.Inject(optional = true)
    public void setPreferredTestQuery(@Named("c3p0.preferredTestQuery") final String preferredTestQuery) {
        this.dataSource.setPreferredTestQuery(preferredTestQuery);
    }

    /**
     * 
     *
     * @param propertyCycle
     */
    @com.google.inject.Inject(optional = true)
    public void setPropertyCycle(@Named("c3p0.propertyCycle") final int propertyCycle) {
        this.dataSource.setPropertyCycle(propertyCycle);
    }

    /**
     * 
     *
     * @param testConnectionOnCheckin
     */
    @com.google.inject.Inject(optional = true)
    public void setTestConnectionOnCheckin(@Named("c3p0.testConnectionOnCheckin") final boolean testConnectionOnCheckin) {
        this.dataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
    }

    /**
     * 
     *
     * @param testConnectionOnCheckout
     */
    @com.google.inject.Inject(optional = true)
    public void setTestConnectionOnCheckout(@Named("c3p0.testConnectionOnCheckout") final boolean testConnectionOnCheckout) {
        this.dataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);
    }

    /**
     * 
     *
     * @param unreturnedConnectionTimeout
     */
    @com.google.inject.Inject(optional = true)
    public void setUnreturnedConnectionTimeout(@Named("c3p0.unreturnedConnectionTimeout") final int unreturnedConnectionTimeout) {
        this.dataSource.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
    }

    /**
     * 
     *
     * @param usesTraditionalReflectiveProxies
     */
    @com.google.inject.Inject(optional = true)
    public void setUsesTraditionalReflectiveProxies(@Named("c3p0.usesTraditionalReflectiveProxies") final boolean usesTraditionalReflectiveProxies) {
        this.dataSource.setUsesTraditionalReflectiveProxies(usesTraditionalReflectiveProxies);
    }

    /**
     * {@inheritDoc}
     */
    public DataSource get() {
        return this.dataSource;
    }

}
