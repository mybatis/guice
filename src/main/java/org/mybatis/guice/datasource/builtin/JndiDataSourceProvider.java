/*
 *    Copyright 2009-2018 the original author or authors.
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

import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.naming.Context;
import javax.sql.DataSource;

import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;

/**
 * Provides the myBatis built-in JndiDataSourceFactory.
 */
public final class JndiDataSourceProvider implements Provider<DataSource> {

  /** The properties. */
  private final Properties properties = new Properties();

  /**
   * Creates a new JndiDataSourceProvider with the specified JNDI data source.
   *
   * @param dataSource
   *          the JNDI datasource name (fully qualified)
   */
  @Inject
  public JndiDataSourceProvider(@Named("jndi.dataSource") final String dataSource) {
    properties.setProperty(JndiDataSourceFactory.DATA_SOURCE, dataSource);
  }

  /**
   * Sets the initial context.
   *
   * @param initialContext
   *          the new initial context
   */
  @com.google.inject.Inject(optional = true)
  public void setInitialContext(@Named("jndi.initialContext") final String initialContext) {
    properties.setProperty(JndiDataSourceFactory.INITIAL_CONTEXT, initialContext);
  }

  /**
   * Sets the env initial context factory.
   *
   * @param initialContextFactory
   *          the new env initial context factory
   */
  @com.google.inject.Inject(optional = true)
  public void setEnvInitialContextFactory(@Named(Context.INITIAL_CONTEXT_FACTORY) final String initialContextFactory) {
    properties.setProperty(JndiDataSourceFactory.ENV_PREFIX + Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
  }

  /**
   * Sets the env provider URL.
   *
   * @param providerUrl
   *          the new env provider URL
   */
  @com.google.inject.Inject(optional = true)
  public void setEnvProviderURL(@Named(Context.PROVIDER_URL) final String providerUrl) {
    properties.setProperty(JndiDataSourceFactory.ENV_PREFIX + Context.PROVIDER_URL, providerUrl);
  }

  @Override
  public DataSource get() {
    JndiDataSourceFactory factory = new JndiDataSourceFactory();
    factory.setProperties(properties);
    return factory.getDataSource();
  }
}
