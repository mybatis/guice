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

import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;

import javax.inject.Named;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Provides the myBatis built-in JndiDataSourceFactory.
 *
 * @version $Id$
 */
public final class JndiDataSourceProvider implements Provider<DataSource> {

    /**
     *
     */
    private final Properties properties = new Properties();

    /**
     *
     *
     * @param initialContext
     */
    @com.google.inject.Inject(optional = true)
    public void setInitialContext(@Named("jndi.initialContext") final String initialContext) {
        properties.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, initialContext);
    }

    /**
     *
     *
     * @param dataSource
     */
    @com.google.inject.Inject(optional = true)
    public void setDataSource(@Named("jndi.dataSource") final String dataSource) {
        properties.setProperty(JndiDataSourceFactory.DATA_SOURCE, dataSource);
    }

    /**
     * {@inheritDoc}
     */
    public DataSource get() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        factory.setProperties(properties);
        return factory.getDataSource();
    }

}
