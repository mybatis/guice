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

import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.naming.Context;
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
     * Creates a new JndiDataSourceProvider with the specified
     * JNDI data source.
     *
     * @param dataSource the JNDI datasource name (fully qualified)
     */
    @Inject
    public JndiDataSourceProvider(@Named("jndi.dataSource") final String dataSource) {
        properties.setProperty(JndiDataSourceFactory.DATA_SOURCE, dataSource);
    }

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
    * @param initialContextFactory
    */
    @com.google.inject.Inject(optional = true)
    public void setEnvInitialContextFactory(@Named(Context.INITIAL_CONTEXT_FACTORY) final String initialContextFactory) {
        properties.setProperty(JndiDataSourceFactory.ENV_PREFIX+Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
    }

    /**
    *
    *
    * @param providerUrl
    */
    @com.google.inject.Inject(optional = true)
    public void setEnvProviderURL(@Named(Context.PROVIDER_URL) final String providerUrl) {
        properties.setProperty(JndiDataSourceFactory.ENV_PREFIX+Context.PROVIDER_URL, providerUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSource get() {
        JndiDataSourceFactory factory = new JndiDataSourceFactory();
        factory.setProperties(properties);
        return factory.getDataSource();
    }
}
