/**
 *    Copyright 2009-2015 the original author or authors.
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

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides the myBatis built-in UnpooledDataSource.
 *
 * @version $Id$
 */
public final class UnpooledDataSourceProvider implements Provider<DataSource> {

    /**
     * The UnpooledDataSource reference.
     */
    private final UnpooledDataSource unpooledDataSource;

    /**
     * Creates a new UnpooledDataSource using the needed parameter.
     *
     * @param driver The JDBC driver class.
     * @param url the database URL of the form <code>jdbc:subprotocol:subname</code>.
     * @param driverClassLoader ClassLoader to use to load JDBC driver class.
     */
    @Inject
    public UnpooledDataSourceProvider(@Named("JDBC.driver") final String driver,
            @Named("JDBC.url") final String url,
            @Named("JDBC.driverClassLoader") final ClassLoader driverClassLoader) {
        unpooledDataSource = new UnpooledDataSource(driverClassLoader, driver, url, null, null);
    }

    /**
     *
     * @param username
     * @since 3.3
     */
    @com.google.inject.Inject(optional = true)
    public void setUser(@Named("JDBC.username") final String username) {
        unpooledDataSource.setUsername(username);
    }

    /**
     *
     * @param password
     * @since 3.3
     */
    @com.google.inject.Inject(optional = true)
    public void setPassword(@Named("JDBC.password") final String password) {
        unpooledDataSource.setPassword(password);
    }

    /**
     *
     *
     * @param autoCommit
     */
    @com.google.inject.Inject(optional = true)
    public void setAutoCommit(@Named("JDBC.autoCommit") final boolean autoCommit) {
        unpooledDataSource.setAutoCommit(autoCommit);
    }

    /**
     *
     *
     * @param loginTimeout
     */
    @com.google.inject.Inject(optional = true)
    public void setLoginTimeout(@Named("JDBC.loginTimeout") final int loginTimeout) {
        try {
            unpooledDataSource.setLoginTimeout(loginTimeout);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible to set login timeout '"
                    + loginTimeout
                    + "' to Unpooled Data Source", e);
        }
    }

    @com.google.inject.Inject(optional = true)
    public void setDriverProperties(@Named("JDBC.driverProperties") final Properties driverProperties) {
        unpooledDataSource.setDriverProperties(driverProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource get() {
        return unpooledDataSource;
    }

}
