/**
 *    Copyright 2009-2017 the original author or authors.
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
package org.mybatis.guice;

import com.google.inject.*;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.TypeHandler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mybatis.guice.configuration.ConfigurationProvider;
import org.mybatis.guice.configuration.ErrorMapper;
import org.mybatis.guice.configuration.settings.ConfigurationSetting;
import org.mybatis.guice.generictypehandler.CustomObject;
import org.mybatis.guice.generictypehandler.GenericCustomObjectTypeHandler;
import org.mybatis.guice.resolver.alias.Address;
import org.mybatis.guice.resolver.alias.User;
import org.mybatis.guice.resolver.interceptor.FirstInterceptor;
import org.mybatis.guice.resolver.interceptor.SecondInterceptor;
import org.mybatis.guice.resolver.mapper.FirstMapper;
import org.mybatis.guice.resolver.mapper.SecondMapper;
import org.mybatis.guice.resolver.typehandler.AddressTypeHandler;
import org.mybatis.guice.resolver.typehandler.UserTypeHandler;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MyBatisModuleTest {
  @Mock
  private static Configuration staticConfiguration;
  @Mock
  private static SqlSessionFactory staticSqlSessionFactory;
  @Mock
  private static DataSource staticDataSource;
  @Mock
  private Provider<DataSource> dataSourceProvider;
  @Mock
  private javax.inject.Provider<DataSource> javaDataSourceProvider;
  @Mock
  private DataSource dataSource;
  @Mock
  private Provider<TransactionFactory> transactionFactoryProvider;
  @Mock
  private javax.inject.Provider<TransactionFactory> javaTransactionFactoryProvider;
  @Mock
  private TransactionFactory transactionFactory;
  @Mock
  private Provider<ConfigurationSetting> configurationSettingProvider;
  @Mock
  private ConfigurationSetting configurationSetting;
  @Mock
  private DatabaseIdProvider databaseIdProvider;
  @Mock
  private ResolverUtil.Test resolverUtilTest;
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();
  private String databaseId;

  @Before
  public void beforeTest() {
    databaseId = "test_database";
    when(dataSourceProvider.get()).thenReturn(dataSource);
    when(transactionFactoryProvider.get()).thenReturn(transactionFactory);
  }

  @Test
  public void environmentId() {
    final String environmentId = "test_environment";
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId(environmentId);
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(environmentId, configuration.getEnvironment().getId());
  }

  @Test
  public void lazyLoadingEnabled_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isLazyLoadingEnabled());
  }

  @Test
  public void lazyLoadingEnabled_True() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        lazyLoadingEnabled(true);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isLazyLoadingEnabled());
  }

  @Test
  public void lazyLoadingEnabled_False() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        lazyLoadingEnabled(false);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isLazyLoadingEnabled());
  }

  @Test
  public void aggressiveLazyLoading_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isAggressiveLazyLoading());
  }

  @Test
  public void aggressiveLazyLoading_True() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        aggressiveLazyLoading(true);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isAggressiveLazyLoading());
  }

  @Test
  public void aggressiveLazyLoading_False() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        aggressiveLazyLoading(false);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isAggressiveLazyLoading());
  }

  @Test
  public void multipleResultSetsEnabled_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isMultipleResultSetsEnabled());
  }

  @Test
  public void multipleResultSetsEnabled_True() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        multipleResultSetsEnabled(true);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isMultipleResultSetsEnabled());
  }

  @Test
  public void multipleResultSetsEnabled_False() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        multipleResultSetsEnabled(false);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isMultipleResultSetsEnabled());
  }

  @Test
  public void useGeneratedKeys_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isUseGeneratedKeys());
  }

  @Test
  public void useGeneratedKeys_True() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        useGeneratedKeys(true);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isUseGeneratedKeys());
  }

  @Test
  public void useGeneratedKeys_False() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        useGeneratedKeys(false);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isUseGeneratedKeys());
  }

  @Test
  public void useColumnLabel_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isUseColumnLabel());
  }

  @Test
  public void useColumnLabel_True() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        useColumnLabel(true);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isUseColumnLabel());
  }

  @Test
  public void useColumnLabel_False() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        useColumnLabel(false);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isUseColumnLabel());
  }

  @Test
  public void useCacheEnabled_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isCacheEnabled());
  }

  @Test
  public void useCacheEnabled_True() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        useCacheEnabled(true);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isCacheEnabled());
  }

  @Test
  public void useCacheEnabled_False() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        useCacheEnabled(false);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isCacheEnabled());
  }

  @Test
  public void useConfigurationProvider() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        useConfigurationProvider(TestConfigurationProvider.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(staticConfiguration, configuration);
  }

  @Test
  public void useSqlSessionFactoryProvider() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        useSqlSessionFactoryProvider(TestSqlSessionFactoryProvider.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    SqlSessionFactory sqlSessionFactory = injector.getInstance(SqlSessionFactory.class);

    assertEquals(staticSqlSessionFactory, sqlSessionFactory);
  }

  @Test
  public void failFast_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addMapperClass(ErrorMapper.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    injector.getInstance(Configuration.class);

    // Success.
  }

  @Test(expected = CreationException.class)
  public void failFast_True() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        failFast(true);
        addMapperClass(ErrorMapper.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    injector.getInstance(Configuration.class);
  }

  @Test
  public void failFast_False() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        failFast(false);
        addMapperClass(ErrorMapper.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    injector.getInstance(Configuration.class);

    // Success.
  }

  @Test
  public void mapUnderscoreToCamelCase_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isMapUnderscoreToCamelCase());
  }

  @Test
  public void mapUnderscoreToCamelCase_True() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        mapUnderscoreToCamelCase(true);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(true, configuration.isMapUnderscoreToCamelCase());
  }

  @Test
  public void mapUnderscoreToCamelCase_False() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        mapUnderscoreToCamelCase(false);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(false, configuration.isMapUnderscoreToCamelCase());
  }

  @Test
  public void defaultStatementTimeout_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertNull(configuration.getDefaultStatementTimeout());
  }

  @Test
  public void defaultStatementTimeout() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        defaultStatementTimeout(100);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals((Integer) 100, configuration.getDefaultStatementTimeout());
  }

  @Test
  public void bindConfigurationSetting() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        bindConfigurationSetting(configurationSetting);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(configurationSetting).applyConfigurationSetting(configuration);
  }

  @Test
  public void bindConfigurationSettingProvider() {
    when(configurationSettingProvider.get()).thenReturn(configurationSetting);
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        bindConfigurationSettingProvider(configurationSettingProvider);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(configurationSettingProvider).get();
    verify(configurationSetting).applyConfigurationSetting(configuration);
  }

  @Test
  public void executorType_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(ExecutorType.SIMPLE, configuration.getDefaultExecutorType());
  }

  @Test
  public void executorType() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        executorType(ExecutorType.BATCH);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(ExecutorType.BATCH, configuration.getDefaultExecutorType());
  }

  @Test
  public void localCacheScope_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(LocalCacheScope.SESSION, configuration.getLocalCacheScope());
  }

  @Test
  public void localCacheScope() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        localCacheScope(LocalCacheScope.STATEMENT);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(LocalCacheScope.STATEMENT, configuration.getLocalCacheScope());
  }

  @Test
  public void autoMappingBehavior_Default() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(AutoMappingBehavior.PARTIAL, configuration.getAutoMappingBehavior());
  }

  @Test
  public void autoMappingBehavior() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        autoMappingBehavior(AutoMappingBehavior.FULL);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(AutoMappingBehavior.FULL, configuration.getAutoMappingBehavior());
  }

  @Test
  public void bindDataSourceProviderType() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProviderType(TestDataSourceProvider.class);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(staticDataSource, configuration.getEnvironment().getDataSource());
  }

  @Test
  public void bindDataSourceProvider_JavaInject() {
    when(javaDataSourceProvider.get()).thenReturn(dataSource);
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(javaDataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(javaDataSourceProvider).get();
    assertEquals(dataSource, configuration.getEnvironment().getDataSource());
  }

  @Test
  public void bindDataSourceProvider() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(dataSourceProvider).get();
    assertEquals(dataSource, configuration.getEnvironment().getDataSource());
  }

  @Test
  public void bindDatabaseIdProvider_Class() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        bindDatabaseIdProvider(TestDatabaseIdProvider.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(TestDatabaseIdProvider.DATABASE_ID, configuration.getDatabaseId());
  }

  @Test
  public void bindDatabaseIdProvider() throws Throwable {
    when(databaseIdProvider.getDatabaseId(any(DataSource.class))).thenReturn(databaseId);
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        bindDatabaseIdProvider(databaseIdProvider);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(databaseIdProvider).getDatabaseId(any(DataSource.class));
    assertEquals(databaseId, configuration.getDatabaseId());
  }

  @Test
  public void bindTransactionFactoryType() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactoryType(TestTransactionFactory.class);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getEnvironment().getTransactionFactory() instanceof TestTransactionFactory);
  }

  @Test
  public void bindTransactionFactory_JavaInject() {
    when(javaTransactionFactoryProvider.get()).thenReturn(transactionFactory);
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(javaTransactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(javaTransactionFactoryProvider).get();
    assertEquals(this.transactionFactory, configuration.getEnvironment().getTransactionFactory());
  }

  @Test
  public void bindTransactionFactory() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(transactionFactoryProvider).get();
    assertEquals(this.transactionFactory, configuration.getEnvironment().getTransactionFactory());
  }

  @Test
  public void bindObjectFactoryType() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        bindObjectFactoryType(TestObjectFactory.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getObjectFactory() instanceof TestObjectFactory);
  }

  @Test
  public void bindObjectWrapperFactoryType() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        bindObjectWrapperFactoryType(TestObjectWrapperFactory.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getObjectWrapperFactory() instanceof TestObjectWrapperFactory);
  }

  @Test
  public void bindDefaultScriptingLanguageType() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        bindDefaultScriptingLanguageType(TestLanguageDriver.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getDefaultScriptingLanguageInstance() instanceof TestLanguageDriver);
  }

  @Test
  public void addAlias() {
    final String alias = "test_alias";
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addAlias(alias).to(Contact.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases().containsKey(alias));
    assertEquals(Contact.class, configuration.getTypeAliasRegistry().getTypeAliases().get(alias));
  }

  @Test
  public void addSimpleAlias() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addSimpleAlias(Contact.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases()
        .containsKey(Contact.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    assertEquals(Contact.class, configuration.getTypeAliasRegistry().getTypeAliases()
        .get(Contact.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
  }

  @Test
  public void addSimpleAlias_Annotation() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addSimpleAlias(TestAliasAnnotation.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases().containsKey(TestAliasAnnotation.ALIAS));
    assertEquals(TestAliasAnnotation.class,
        configuration.getTypeAliasRegistry().getTypeAliases().get(TestAliasAnnotation.ALIAS));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void addSimpleAliases() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addSimpleAliases(Arrays.asList(Contact.class, TestAliasAnnotation.class));
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases()
        .containsKey(Contact.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    assertEquals(Contact.class, configuration.getTypeAliasRegistry().getTypeAliases()
        .get(Contact.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases().containsKey(TestAliasAnnotation.ALIAS));
    assertEquals(TestAliasAnnotation.class,
        configuration.getTypeAliasRegistry().getTypeAliases().get(TestAliasAnnotation.ALIAS));
  }

  @Test
  public void addSimpleAliases_Package_ResolverUtilTest_None() {
    when(resolverUtilTest.matches(any(Class.class))).thenReturn(false);
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addSimpleAliases(User.class.getPackage().getName(), resolverUtilTest);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(resolverUtilTest, atLeastOnce()).matches(User.class);
    assertFalse(configuration.getTypeAliasRegistry().getTypeAliases()
        .containsKey(User.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    verify(resolverUtilTest, atLeastOnce()).matches(Address.class);
    assertFalse(configuration.getTypeAliasRegistry().getTypeAliases()
        .containsKey(Address.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
  }

  @Test
  public void addSimpleAliases_Package_ResolverUtilTest_All() {
    when(resolverUtilTest.matches(any(Class.class))).thenReturn(true);
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addSimpleAliases(User.class.getPackage().getName(), resolverUtilTest);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(resolverUtilTest, atLeastOnce()).matches(User.class);
    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases()
        .containsKey(User.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    assertEquals(User.class, configuration.getTypeAliasRegistry().getTypeAliases()
        .get(User.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    verify(resolverUtilTest, atLeastOnce()).matches(Address.class);
    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases()
        .containsKey(Address.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    assertEquals(Address.class, configuration.getTypeAliasRegistry().getTypeAliases()
        .get(Address.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
  }

  @Test
  public void addSimpleAliases_Package() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addSimpleAliases(User.class.getPackage().getName());
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases()
        .containsKey(User.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    assertEquals(User.class, configuration.getTypeAliasRegistry().getTypeAliases()
        .get(User.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    assertTrue(configuration.getTypeAliasRegistry().getTypeAliases()
        .containsKey(Address.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
    assertEquals(Address.class, configuration.getTypeAliasRegistry().getTypeAliases()
        .get(Address.class.getSimpleName().toLowerCase(Locale.ENGLISH)));
  }

  @Test
  public void handleType_Class() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        handleType(User.class).with(UserTypeHandler.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeHandlerRegistry().getTypeHandler(User.class) instanceof UserTypeHandler);
    assertNotNull(((UserTypeHandler) configuration.getTypeHandlerRegistry().getTypeHandler(User.class)).getUser());
  }

  @Test
  public void handleType_TypeLiteral() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        handleType(User.class).with(new TypeLiteral<UserTypeHandler>() {
        });
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeHandlerRegistry().getTypeHandler(User.class) instanceof UserTypeHandler);
    assertNotNull(((UserTypeHandler) configuration.getTypeHandlerRegistry().getTypeHandler(User.class)).getUser());
  }

  @Test
  public void handleType_ProvidedClass() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        handleType(User.class).withProvidedTypeHandler(UserTypeHandler.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeHandlerRegistry().getTypeHandler(User.class) instanceof UserTypeHandler);
    assertEquals(User.class,
        ((UserTypeHandler) configuration.getTypeHandlerRegistry().getTypeHandler(User.class)).getType());
    assertNotNull(((UserTypeHandler) configuration.getTypeHandlerRegistry().getTypeHandler(User.class)).getUser());
  }

  @Test
  public void handleType_ProvidedTypeLiteral() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        handleType(User.class).withProvidedTypeHandler(new TypeLiteral<UserTypeHandler>() {
        });
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeHandlerRegistry().getTypeHandler(User.class) instanceof UserTypeHandler);
    assertEquals(User.class,
        ((UserTypeHandler) configuration.getTypeHandlerRegistry().getTypeHandler(User.class)).getType());
    assertNotNull(((UserTypeHandler) configuration.getTypeHandlerRegistry().getTypeHandler(User.class)).getUser());
  }

  @Test
  public void handleType_ProvidedTypeLiteral_Generic() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        handleType(CustomObject.class)
            .withProvidedTypeHandler(new TypeLiteral<GenericCustomObjectTypeHandler<CustomObject>>() {
            });
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertTrue(configuration.getTypeHandlerRegistry()
        .getTypeHandler(CustomObject.class) instanceof GenericCustomObjectTypeHandler);
    assertEquals(CustomObject.class,
        ((GenericCustomObjectTypeHandler<?>) configuration.getTypeHandlerRegistry().getTypeHandler(CustomObject.class))
            .getType());
    assertNotNull(
        ((GenericCustomObjectTypeHandler<?>) configuration.getTypeHandlerRegistry().getTypeHandler(CustomObject.class))
            .getInjectedObject());
  }

  @Test
  public void addTypeHandlerClass() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addTypeHandlerClass(AddressTypeHandler.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertNotNull(configuration.getTypeHandlerRegistry().getInstance(Address.class, AddressTypeHandler.class));
  }

  @Test
  public void addTypeHandlersClasses() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        Collection<Class<? extends TypeHandler<?>>> handlers = new ArrayList<Class<? extends TypeHandler<?>>>();
        handlers.add(AddressTypeHandler.class);
        handlers.add(UserTypeHandler.class);
        addTypeHandlersClasses(handlers);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertNotNull(configuration.getTypeHandlerRegistry().getInstance(Address.class, AddressTypeHandler.class));
    assertNotNull(configuration.getTypeHandlerRegistry().getInstance(User.class, UserTypeHandler.class));
  }

  @Test
  public void addTypeHandlerClasses() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addTypeHandlerClasses(AddressTypeHandler.class.getPackage().getName());
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertNotNull(configuration.getTypeHandlerRegistry().getInstance(Address.class, AddressTypeHandler.class));
    assertNotNull(configuration.getTypeHandlerRegistry().getInstance(User.class, UserTypeHandler.class));
  }

  @Test
  public void addInterceptorClass() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addInterceptorClass(FirstInterceptor.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(1, configuration.getInterceptors().size());
    assertTrue(configuration.getInterceptors().iterator().next() instanceof FirstInterceptor);
  }

  @Test
  public void addInterceptorsClasses() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        Collection<Class<? extends Interceptor>> interceptors = new ArrayList<Class<? extends Interceptor>>();
        interceptors.add(FirstInterceptor.class);
        interceptors.add(SecondInterceptor.class);
        addInterceptorsClasses(interceptors);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(2, configuration.getInterceptors().size());
    List<Interceptor> interceptors = configuration.getInterceptors();
    assertTrue(interceptors.get(0) instanceof FirstInterceptor || interceptors.get(1) instanceof FirstInterceptor);
    assertFalse(interceptors.get(0) instanceof SecondInterceptor && interceptors.get(1) instanceof SecondInterceptor);
  }

  @Test
  public void addInterceptorsClasses_Package() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addInterceptorsClasses(FirstInterceptor.class.getPackage().getName());
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    assertEquals(2, configuration.getInterceptors().size());
    List<Interceptor> interceptors = configuration.getInterceptors();
    assertTrue(interceptors.get(0) instanceof FirstInterceptor || interceptors.get(1) instanceof FirstInterceptor);
    assertFalse(interceptors.get(0) instanceof SecondInterceptor && interceptors.get(1) instanceof SecondInterceptor);
  }

  @Test
  public void addMapperClass() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addMapperClass(FirstMapper.class);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);
    SqlSession sqlSession = injector.getInstance(SqlSession.class);

    assertTrue(configuration.getMapperRegistry().getMappers().contains(FirstMapper.class));
    assertNotNull(configuration.getMapperRegistry().getMapper(FirstMapper.class, sqlSession));
    assertNotNull(injector.getInstance(FirstMapper.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void addMapperClasses() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addMapperClasses(Arrays.asList(FirstMapper.class, SecondMapper.class));
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);
    SqlSession sqlSession = injector.getInstance(SqlSession.class);

    assertTrue(configuration.getMapperRegistry().getMappers().contains(FirstMapper.class));
    assertNotNull(configuration.getMapperRegistry().getMapper(FirstMapper.class, sqlSession));
    assertNotNull(injector.getInstance(FirstMapper.class));
    assertTrue(configuration.getMapperRegistry().getMappers().contains(SecondMapper.class));
    assertNotNull(configuration.getMapperRegistry().getMapper(SecondMapper.class, sqlSession));
    assertNotNull(injector.getInstance(SecondMapper.class));
  }

  @Test
  public void addMapperClasses_Package() {
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addMapperClasses(FirstMapper.class.getPackage().getName());
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);
    SqlSession sqlSession = injector.getInstance(SqlSession.class);

    assertTrue(configuration.getMapperRegistry().getMappers().contains(FirstMapper.class));
    assertNotNull(configuration.getMapperRegistry().getMapper(FirstMapper.class, sqlSession));
    assertNotNull(injector.getInstance(FirstMapper.class));
    assertTrue(configuration.getMapperRegistry().getMappers().contains(SecondMapper.class));
    assertNotNull(configuration.getMapperRegistry().getMapper(SecondMapper.class, sqlSession));
    assertNotNull(injector.getInstance(SecondMapper.class));
  }

  @Test
  public void addMapperClasses_Package_ResolverUtilTest_None() {
    when(resolverUtilTest.matches(any(Class.class))).thenReturn(false);
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addMapperClasses(FirstMapper.class.getPackage().getName(), resolverUtilTest);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);

    verify(resolverUtilTest, atLeastOnce()).matches(FirstMapper.class);
    assertFalse(configuration.getMapperRegistry().getMappers().contains(FirstMapper.class));
    assertNull(injector.getExistingBinding(Key.get(FirstMapper.class)));
    verify(resolverUtilTest, atLeastOnce()).matches(SecondMapper.class);
    assertFalse(configuration.getMapperRegistry().getMappers().contains(SecondMapper.class));
    assertNull(injector.getExistingBinding(Key.get(SecondMapper.class)));
  }

  @Test
  public void addMapperClasses_Package_ResolverUtilTest_All() {
    when(resolverUtilTest.matches(any(Class.class))).thenReturn(true);
    Injector injector = Guice.createInjector(new MyBatisModule() {
      @Override
      protected void initialize() {
        addMapperClasses(FirstMapper.class.getPackage().getName(), resolverUtilTest);
        environmentId("test_environment");
        bindDataSourceProvider(dataSourceProvider);
        bindTransactionFactory(transactionFactoryProvider);
      }
    });

    Configuration configuration = injector.getInstance(Configuration.class);
    SqlSession sqlSession = injector.getInstance(SqlSession.class);

    verify(resolverUtilTest, atLeastOnce()).matches(FirstMapper.class);
    assertTrue(configuration.getMapperRegistry().getMappers().contains(FirstMapper.class));
    assertNotNull(configuration.getMapperRegistry().getMapper(FirstMapper.class, sqlSession));
    assertNotNull(injector.getInstance(FirstMapper.class));
    verify(resolverUtilTest, atLeastOnce()).matches(SecondMapper.class);
    assertTrue(configuration.getMapperRegistry().getMappers().contains(SecondMapper.class));
    assertNotNull(configuration.getMapperRegistry().getMapper(SecondMapper.class, sqlSession));
    assertNotNull(injector.getInstance(SecondMapper.class));
  }

  public static class TestConfigurationProvider extends ConfigurationProvider {

    @Inject
    public TestConfigurationProvider(Environment environment) {
      super(environment);
    }

    @Override
    public Configuration get() {
      return staticConfiguration;
    }
  }

  public static class TestSqlSessionFactoryProvider implements Provider<SqlSessionFactory> {
    @Override
    public SqlSessionFactory get() {
      return staticSqlSessionFactory;
    }
  }

  public static class TestDataSourceProvider implements Provider<DataSource> {
    @Override
    public DataSource get() {
      return staticDataSource;
    }
  }

  public static class TestDatabaseIdProvider implements DatabaseIdProvider {
    private static final String DATABASE_ID = "test_database_id_provider";

    @Override
    public void setProperties(Properties p) {
    }

    @Override
    public String getDatabaseId(DataSource dataSource) throws SQLException {
      return DATABASE_ID;
    }
  }

  public static class TestTransactionFactory extends JdbcTransactionFactory {
  }

  @SuppressWarnings("serial")
  public static class TestObjectFactory extends DefaultObjectFactory {
  }

  public static class TestObjectWrapperFactory extends DefaultObjectWrapperFactory {
  }

  public static class TestLanguageDriver extends RawLanguageDriver {
  }

  @Alias(TestAliasAnnotation.ALIAS)
  public static class TestAliasAnnotation {
    public static final String ALIAS = "annotation_alias";
  }
}
