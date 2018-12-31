/**
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import org.apache.naming.java.javaURLContextFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

@ExtendWith(MockitoExtension.class)
public class JndiDataSourceProviderTest {
  private static String dataSourceName;
  private static String contextName;
  private static DataSource globalDataSource;
  private static DataSource contextDataSource;
  private static int factoryLoginTimeout = 1;
  private static int providerLoginTimeout = 2;

  @BeforeAll
  public static void beforeClass() throws Throwable {
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
    System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
    dataSourceName = "dataSource";
    contextName = "jdbc";
    globalDataSource = mock(DataSource.class);
    contextDataSource = mock(DataSource.class);
    InitialContext ic = new InitialContext();
    ic.createSubcontext(contextName);
    ic.bind(dataSourceName, globalDataSource);
    ic.bind(contextName + "/" + dataSourceName, contextDataSource);
  }

  @Test
  public void get_NoContext() {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("jndi.dataSource")).to(dataSourceName);
      }
    });
    JndiDataSourceProvider provider = injector.getInstance(JndiDataSourceProvider.class);

    DataSource dataSource = provider.get();

    assertEquals(globalDataSource, dataSource);
    assertNotEquals(contextDataSource, dataSource);
  }

  @Test
  public void get_Context() {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("jndi.dataSource")).to(dataSourceName);
        bindConstant().annotatedWith(Names.named("jndi.initialContext")).to(contextName);
      }
    });
    JndiDataSourceProvider provider = injector.getInstance(JndiDataSourceProvider.class);

    DataSource dataSource = provider.get();

    assertEquals(contextDataSource, dataSource);
    assertNotEquals(globalDataSource, dataSource);
  }

  @Test
  public void get_Environment() throws Throwable {
    final String initialContextFactory = TestInitialContextFactory.class.getName();
    final String environmentProviderUrl = getClass()
        .getResource("/" + getClass().getName().replace(".", "/") + ".properties").toString();
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bindConstant().annotatedWith(Names.named("jndi.dataSource")).to(dataSourceName);
        bindConstant().annotatedWith(Names.named(Context.INITIAL_CONTEXT_FACTORY)).to(initialContextFactory);
        bindConstant().annotatedWith(Names.named(Context.PROVIDER_URL)).to(environmentProviderUrl);
      }
    });
    JndiDataSourceProvider provider = injector.getInstance(JndiDataSourceProvider.class);

    DataSource dataSource = provider.get();

    assertEquals(globalDataSource, dataSource);
    verify(globalDataSource).setLoginTimeout(factoryLoginTimeout);
    verify(globalDataSource).setLoginTimeout(providerLoginTimeout);
  }

  public static class TestInitialContextFactory implements InitialContextFactory {
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
      Context context = new javaURLContextFactory().getInitialContext(environment);
      DataSource dataSource = (DataSource) context.lookup(dataSourceName);
      try {
        dataSource.setLoginTimeout(factoryLoginTimeout);
      } catch (SQLException e) {
        // Ignore.
      }
      if (environment.get(Context.PROVIDER_URL) != null) {
        try {
          dataSource.setLoginTimeout(providerLoginTimeout);
        } catch (SQLException e) {
          // Ignore.
        }
      }
      return context;
    }
  }
}
