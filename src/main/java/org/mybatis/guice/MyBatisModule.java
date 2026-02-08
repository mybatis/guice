/*
 *    Copyright 2009-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice;

import static com.google.inject.name.Names.named;
import static com.google.inject.util.Providers.guicify;
import static org.mybatis.guice.Preconditions.checkArgument;

import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import jakarta.inject.Provider;

import java.util.Collection;

import javax.sql.DataSource;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.guice.binder.AliasBinder;
import org.mybatis.guice.binder.TypeHandlerBinder;
import org.mybatis.guice.configuration.ConfigurationProvider;
import org.mybatis.guice.configuration.ConfigurationSettingListener;
import org.mybatis.guice.configuration.settings.AggressiveLazyLoadingConfigurationSetting;
import org.mybatis.guice.configuration.settings.AliasConfigurationSetting;
import org.mybatis.guice.configuration.settings.AutoMappingBehaviorConfigurationSetting;
import org.mybatis.guice.configuration.settings.CacheEnabledConfigurationSetting;
import org.mybatis.guice.configuration.settings.ConfigurationSetting;
import org.mybatis.guice.configuration.settings.DefaultExecutorTypeConfigurationSetting;
import org.mybatis.guice.configuration.settings.DefaultScriptingLanguageTypeConfigurationSetting;
import org.mybatis.guice.configuration.settings.DefaultStatementTimeoutConfigurationSetting;
import org.mybatis.guice.configuration.settings.InterceptorConfigurationSettingProvider;
import org.mybatis.guice.configuration.settings.JavaTypeAndHandlerConfigurationSettingProvider;
import org.mybatis.guice.configuration.settings.LazyLoadingEnabledConfigurationSetting;
import org.mybatis.guice.configuration.settings.LocalCacheScopeConfigurationSetting;
import org.mybatis.guice.configuration.settings.MapUnderscoreToCamelCaseConfigurationSetting;
import org.mybatis.guice.configuration.settings.MapperConfigurationSetting;
import org.mybatis.guice.configuration.settings.MultipleResultSetsEnabledConfigurationSetting;
import org.mybatis.guice.configuration.settings.ObjectFactoryConfigurationSetting;
import org.mybatis.guice.configuration.settings.ObjectWrapperFactoryConfigurationSetting;
import org.mybatis.guice.configuration.settings.TypeHandlerConfigurationSettingProvider;
import org.mybatis.guice.configuration.settings.UseColumnLabelConfigurationSetting;
import org.mybatis.guice.configuration.settings.UseGeneratedKeysConfigurationSetting;
import org.mybatis.guice.environment.EnvironmentProvider;
import org.mybatis.guice.provision.ConfigurationProviderProvisionListener;
import org.mybatis.guice.provision.KeyMatcher;
import org.mybatis.guice.session.SqlSessionFactoryProvider;
import org.mybatis.guice.type.TypeHandlerProvider;

/**
 * Easy to use helper Module that alleviates users to write the boilerplate google-guice bindings to create the
 * SqlSessionFactory.
 */
public abstract class MyBatisModule extends AbstractMyBatisModule {

  /**
   * The ObjectFactory class reference.
   */
  private Class<? extends ObjectFactory> objectFactoryType = DefaultObjectFactory.class;

  /**
   * The ObjectWrapperFactory class reference.
   */
  private Class<? extends ObjectWrapperFactory> objectWrapperFactoryType = DefaultObjectWrapperFactory.class;
  private Class<? extends LanguageDriver> defaultScriptingLanguageType = XMLLanguageDriver.class;

  /**
   * The SqlSessionFactory Provider class reference.
   */
  private Class<? extends Provider<? extends SqlSessionFactory>> sqlSessionFactoryProviderType = SqlSessionFactoryProvider.class;

  private Class<? extends Provider<? extends Configuration>> configurationProviderType = ConfigurationProvider.class;

  @Override
  final void internalConfigure() {
    try {
      initialize();

    } finally {

    }

    // fixed bindings
    bind(Environment.class).toProvider(EnvironmentProvider.class).in(Scopes.SINGLETON);

    // replaceable bindings.
    bind(Configuration.class).toProvider(configurationProviderType).in(Scopes.SINGLETON);
    bind(SqlSessionFactory.class).toProvider(sqlSessionFactoryProviderType);

    // parametric bindings
    bind(ObjectFactory.class).to(objectFactoryType).in(Scopes.SINGLETON);
    bind(ObjectWrapperFactory.class).to(objectWrapperFactoryType).in(Scopes.SINGLETON);

    bindConfigurationSettingProvider(new ObjectFactoryConfigurationSetting(objectFactoryType));
    bindConfigurationSettingProvider(new ObjectWrapperFactoryConfigurationSetting(objectWrapperFactoryType));
    bindConfigurationSetting(new DefaultScriptingLanguageTypeConfigurationSetting(defaultScriptingLanguageType));
  }

  /**
   * Set the MyBatis configuration environment id.
   *
   * @param environmentId
   *          the MyBatis configuration environment id
   */
  protected final void environmentId(String environmentId) {
    checkArgument(environmentId != null, "Parameter 'environmentId' must be not null");
    bindConstant().annotatedWith(named("mybatis.environment.id")).to(environmentId);
  }

  /**
   * Lazy loading enabled.
   *
   * @param lazyLoadingEnabled
   *          the lazy loading enabled
   */
  protected final void lazyLoadingEnabled(boolean lazyLoadingEnabled) {
    bindConfigurationSetting(new LazyLoadingEnabledConfigurationSetting(lazyLoadingEnabled));
  }

  /**
   * Aggressive lazy loading.
   *
   * @param aggressiveLazyLoading
   *          the aggressive lazy loading
   */
  protected final void aggressiveLazyLoading(boolean aggressiveLazyLoading) {
    bindConfigurationSetting(new AggressiveLazyLoadingConfigurationSetting(aggressiveLazyLoading));
  }

  /**
   * Multiple result sets enabled.
   *
   * @deprecated this option has no effect
   *
   * @param multipleResultSetsEnabled
   *          the multiple result sets enabled
   */
  @Deprecated
  protected final void multipleResultSetsEnabled(boolean multipleResultSetsEnabled) {
    bindConfigurationSetting(new MultipleResultSetsEnabledConfigurationSetting(multipleResultSetsEnabled));
  }

  /**
   * Use generated keys.
   *
   * @param useGeneratedKeys
   *          the use generated keys
   */
  protected final void useGeneratedKeys(boolean useGeneratedKeys) {
    bindConfigurationSetting(new UseGeneratedKeysConfigurationSetting(useGeneratedKeys));
  }

  /**
   * Use column label.
   *
   * @param useColumnLabel
   *          the use column label
   */
  protected final void useColumnLabel(boolean useColumnLabel) {
    bindConfigurationSetting(new UseColumnLabelConfigurationSetting(useColumnLabel));
  }

  /**
   * Use cache enabled.
   *
   * @param useCacheEnabled
   *          the use cache enabled
   */
  protected final void useCacheEnabled(boolean useCacheEnabled) {
    bindConfigurationSetting(new CacheEnabledConfigurationSetting(useCacheEnabled));
  }

  /**
   * Use configuration provider.
   *
   * @param configurationProviderType
   *          provider for Configuration
   */
  protected final void useConfigurationProvider(
      Class<? extends Provider<? extends Configuration>> configurationProviderType) {
    this.configurationProviderType = configurationProviderType;
  }

  /**
   * Use sql session factory provider.
   *
   * @param sqlSessionFactoryProvider
   *          provider for SqlSessionFactory
   */
  protected final void useSqlSessionFactoryProvider(
      Class<? extends Provider<? extends SqlSessionFactory>> sqlSessionFactoryProvider) {
    this.sqlSessionFactoryProviderType = sqlSessionFactoryProvider;
  }

  /**
   * Fail fast.
   *
   * @param failFast
   *          the fail fast
   */
  protected final void failFast(boolean failFast) {
    bindBoolean("mybatis.configuration.failFast", failFast);
  }

  /**
   * Maps underscores to camel case.
   *
   * @param mapUnderscoreToCamelCase
   *          Toggles this settings value.
   */
  protected final void mapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
    bindConfigurationSetting(new MapUnderscoreToCamelCaseConfigurationSetting(mapUnderscoreToCamelCase));
  }

  /**
   * set default statement timeout.
   *
   * @param defaultStatementTimeout
   *          default statement timeout in seconds.
   */
  protected final void defaultStatementTimeout(Integer defaultStatementTimeout) {
    bindConfigurationSetting(new DefaultStatementTimeoutConfigurationSetting(defaultStatementTimeout));
  }

  protected final void bindConfigurationSetting(final ConfigurationSetting configurationSetting) {
    bindListener(KeyMatcher.create(Key.get(ConfigurationSettingListener.class)),
        ConfigurationProviderProvisionListener.create(configurationSetting));
  }

  protected final <P extends Provider<? extends ConfigurationSetting>> void bindConfigurationSettingProvider(
      P configurationSettingProvider) {
    bindListener(KeyMatcher.create(Key.get(ConfigurationSettingListener.class)),
        ConfigurationProviderProvisionListener.create(configurationSettingProvider, binder()));
  }

  private final void bindBoolean(String name, boolean value) {
    bindConstant().annotatedWith(named(name)).to(value);
  }

  /**
   * Executor type.
   *
   * @param executorType
   *          the executor type
   */
  protected final void executorType(ExecutorType executorType) {
    checkArgument(executorType != null, "Parameter 'executorType' must be not null");
    bindConfigurationSetting(new DefaultExecutorTypeConfigurationSetting(executorType));
  }

  /**
   * Configures the local cache scope setting.
   *
   * @param localeCacheScope
   *          The cache scope to use.
   *
   * @since 3.4
   */
  protected final void localCacheScope(LocalCacheScope localeCacheScope) {
    checkArgument(localeCacheScope != null, "Parameter 'localCacheScope' must be not null");
    bindConfigurationSetting(new LocalCacheScopeConfigurationSetting(localeCacheScope));
  }

  /**
   * Auto mapping behavior.
   *
   * @param autoMappingBehavior
   *          the auto mapping behavior
   */
  protected final void autoMappingBehavior(AutoMappingBehavior autoMappingBehavior) {
    checkArgument(autoMappingBehavior != null, "Parameter 'autoMappingBehavior' must be not null");
    bindConfigurationSetting(new AutoMappingBehaviorConfigurationSetting(autoMappingBehavior));
  }

  /**
   * Set the DataSource Provider type has to be bound.
   *
   * @param dataSourceProviderType
   *          the DataSource Provider type
   */
  protected final void bindDataSourceProviderType(Class<? extends Provider<DataSource>> dataSourceProviderType) {
    checkArgument(dataSourceProviderType != null, "Parameter 'dataSourceProviderType' must be not null");
    bind(DataSource.class).toProvider(dataSourceProviderType).in(Scopes.SINGLETON);
  }

  /**
   * Bind data source provider.
   *
   * @param dataSourceProvider
   *          the data source provider
   */
  protected final void bindDataSourceProvider(Provider<DataSource> dataSourceProvider) {
    checkArgument(dataSourceProvider != null, "Parameter 'dataSourceProvider' must be not null");
    bindDataSourceProvider(guicify(dataSourceProvider));
  }

  /**
   * Bind data source provider.
   *
   * @param dataSourceProvider
   *          the data source provider
   */
  protected final void bindDataSourceProvider(com.google.inject.Provider<DataSource> dataSourceProvider) {
    checkArgument(dataSourceProvider != null, "Parameter 'dataSourceProvider' must be not null");
    bind(DataSource.class).toProvider(dataSourceProvider).in(Scopes.SINGLETON);
  }

  /**
   * Bind database id provider.
   *
   * @param databaseIdProvider
   *          The DatabaseIdProvider class.
   */
  protected final void bindDatabaseIdProvider(Class<? extends DatabaseIdProvider> databaseIdProvider) {
    checkArgument(databaseIdProvider != null, "Parameter 'dataSourceProvider' must be not null");
    bind(DatabaseIdProvider.class).to(databaseIdProvider).in(Scopes.SINGLETON);
  }

  /**
   * Bind database id provider.
   *
   * @param databaseIdProvider
   *          The DatabaseIdProvider instance.
   */
  protected final void bindDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
    checkArgument(databaseIdProvider != null, "Parameter 'dataSourceProvider' must be not null");
    bind(DatabaseIdProvider.class).toInstance(databaseIdProvider);
  }

  /**
   * Set the TransactionFactory type has to be bound.
   *
   * @param transactionFactoryType
   *          the TransactionFactory type
   */
  protected final void bindTransactionFactoryType(Class<? extends TransactionFactory> transactionFactoryType) {
    checkArgument(transactionFactoryType != null, "Parameter 'transactionFactoryType' must be not null");
    bind(TransactionFactory.class).to(transactionFactoryType).in(Scopes.SINGLETON);
  }

  /**
   * Bind transaction factory.
   *
   * @param transactionFactoryProvider
   *          the transaction factory provider
   */
  protected final void bindTransactionFactory(Provider<TransactionFactory> transactionFactoryProvider) {
    checkArgument(transactionFactoryProvider != null, "Parameter 'transactionFactoryProvider' must be not null");
    bindTransactionFactory(guicify(transactionFactoryProvider));
  }

  /**
   * Bind transaction factory.
   *
   * @param transactionFactoryProvider
   *          the transaction factory provider
   */
  protected final void bindTransactionFactory(
      com.google.inject.Provider<TransactionFactory> transactionFactoryProvider) {
    checkArgument(transactionFactoryProvider != null, "Parameter 'transactionFactoryProvider' must be not null");
    bind(TransactionFactory.class).toProvider(transactionFactoryProvider).in(Scopes.SINGLETON);
  }

  /**
   * Sets the ObjectFactory class.
   *
   * @param objectFactoryType
   *          the ObjectFactory type
   */
  protected final void bindObjectFactoryType(Class<? extends ObjectFactory> objectFactoryType) {
    checkArgument(objectFactoryType != null, "Parameter 'objectFactoryType' must be not null");
    this.objectFactoryType = objectFactoryType;
  }

  /**
   * Sets the ObjectWrapperFactory class.
   *
   * @param objectWrapperFactoryType
   *          the ObjectFactory type
   */
  protected final void bindObjectWrapperFactoryType(Class<? extends ObjectWrapperFactory> objectWrapperFactoryType) {
    checkArgument(objectFactoryType != null, "Parameter 'objectWrapperFactoryType' must be not null");
    this.objectWrapperFactoryType = objectWrapperFactoryType;
  }

  /**
   * Sets the default LanguageDriver class.
   * <p>
   * Due to current limitations in MyBatis, &#64;Inject cannot be used in LanguageDriver class.
   * </p>
   *
   * @param defaultScriptingLanguageType
   *          the default LanguageDriver type
   */
  protected final void bindDefaultScriptingLanguageType(Class<? extends LanguageDriver> defaultScriptingLanguageType) {
    checkArgument(defaultScriptingLanguageType != null, "Parameter 'defaultScriptingLanguageType' must be not null");
    this.defaultScriptingLanguageType = defaultScriptingLanguageType;
  }

  /**
   * Add a user defined binding.
   *
   * @param alias
   *          the string type alias
   *
   * @return the alias binder
   */
  protected final AliasBinder addAlias(final String alias) {
    checkArgument(alias != null && !alias.isEmpty(), "Empty or null 'alias' is not valid");

    return clazz -> {
      checkArgument(clazz != null, "Null type not valid for alias '%s'", alias);
      bindConfigurationSetting(new AliasConfigurationSetting(alias, clazz));
    };
  }

  /**
   * Adding simple aliases means that every specified class will be bound using the simple class name, i.e.
   * {@code com.acme.Foo} becomes {@code Foo}.
   *
   * @param type
   *          the specified types have to be bind
   */
  protected final void addSimpleAlias(final Class<?> type) {
    checkArgument(type != null, "Parameter 'type' must be not null");

    String alias = type.getSimpleName();
    // check if the class uses the Alias annotation.
    final Alias annotation = type.getAnnotation(Alias.class);
    if (annotation != null) {
      alias = annotation.value();
    }
    addAlias(alias).to(type);
  }

  /**
   * Adding simple aliases means that every specified class will be bound using the simple class name, i.e.
   * {@code com.acme.Foo} becomes {@code Foo}.
   *
   * @param types
   *          the specified types have to be bind
   */
  protected final void addSimpleAliases(final Collection<Class<?>> types) {
    checkArgument(types != null, "Parameter 'types' must be not null");

    for (Class<?> type : types) {
      addSimpleAlias(type);
    }
  }

  /**
   * Adds all Classes in the given package as a simple alias. Adding simple aliases means that every specified class
   * will be bound using the simple class name, i.e. {@code com.acme.Foo} becomes {@code Foo}.
   *
   * @param packageName
   *          the specified package to search for classes to alias.
   * @param test
   *          a test to run against the objects found in the specified package
   */
  protected final void addSimpleAliases(final String packageName, final ResolverUtil.Test test) {
    addSimpleAliases(getClasses(test, packageName));
  }

  /**
   * Adds all Classes in the given package as a simple alias. Adding simple aliases means that every specified class
   * will be bound using the simple class name, i.e. {@code com.acme.Foo} becomes {@code Foo}.
   *
   * @param packageName
   *          the specified package to search for classes to alias
   */
  protected final void addSimpleAliases(final String packageName) {
    addSimpleAliases(getClasses(packageName));
  }

  /**
   * Add a user defined Type Handler letting google-guice creating it.
   *
   * @param <T>
   *          the generic type
   * @param type
   *          the specified type has to be handled.
   *
   * @return the type handler binder
   */
  protected final <T> TypeHandlerBinder<T> handleType(final Class<T> type) {
    checkArgument(type != null, "Parameter 'type' must be not null");

    return new TypeHandlerBinder<T>() {

      @Override
      public void with(final Class<? extends TypeHandler<? extends T>> handler) {
        checkArgument(handler != null, "TypeHandler must not be null for '%s'", type.getName());

        bindTypeHandler(TypeLiteral.get(handler));
        bindConfigurationSettingProvider(JavaTypeAndHandlerConfigurationSettingProvider.create(type, Key.get(handler)));
      }

      @Override
      public void with(final TypeLiteral<? extends TypeHandler<? extends T>> handler) {
        checkArgument(handler != null, "TypeHandler must not be null for '%s'", type.getName());

        bindTypeHandler(handler);
        bindConfigurationSettingProvider(JavaTypeAndHandlerConfigurationSettingProvider.create(type, Key.get(handler)));
      }

      @Override
      public void withProvidedTypeHandler(final Class<? extends TypeHandler<? extends T>> handler) {
        checkArgument(handler != null, "TypeHandler must not be null for '%s'", type.getName());

        bindProvidedTypeHandler(TypeLiteral.get(handler), type);
        bindConfigurationSettingProvider(JavaTypeAndHandlerConfigurationSettingProvider.create(type, Key.get(handler)));
      }

      @Override
      public void withProvidedTypeHandler(final TypeLiteral<? extends TypeHandler<? extends T>> handler) {
        checkArgument(handler != null, "TypeHandler must not be null for '%s'", type.getName());

        bindProvidedTypeHandler(handler, type);
        bindConfigurationSettingProvider(JavaTypeAndHandlerConfigurationSettingProvider.create(type, Key.get(handler)));
      }

      final <TH extends TypeHandler<? extends T>> void bindTypeHandler(TypeLiteral<TH> typeHandlerType) {
        bind(typeHandlerType).in(Scopes.SINGLETON);
      }

      final <TH extends TypeHandler<? extends T>> void bindProvidedTypeHandler(TypeLiteral<TH> typeHandlerType,
          Class<T> type) {
        bind(typeHandlerType).toProvider(guicify(new TypeHandlerProvider<>(typeHandlerType, type)))
            .in(Scopes.SINGLETON);
      }
    };
  }

  /**
   * Adds the user defined MyBatis type handler, letting google-guice creating it.
   *
   * @param handlerClass
   *          the handler type.
   */
  protected final void addTypeHandlerClass(final Class<? extends TypeHandler<?>> handlerClass) {
    checkArgument(handlerClass != null, "Parameter 'handlerClass' must not be null");
    bind(TypeLiteral.get(handlerClass)).in(Scopes.SINGLETON);

    bindConfigurationSettingProvider(new TypeHandlerConfigurationSettingProvider(Key.get(handlerClass)));
  }

  /**
   * Adds the user defined MyBatis type handlers, letting google-guice creating it.
   *
   * @param handlersClasses
   *          the handler type.
   */
  protected final void addTypeHandlersClasses(Collection<Class<? extends TypeHandler<?>>> handlersClasses) {
    checkArgument(handlersClasses != null, "Parameter 'handlersClasses' must not be null");

    for (Class<? extends TypeHandler<?>> handlerClass : handlersClasses) {
      addTypeHandlerClass(handlerClass);
    }
  }

  /**
   * Adds the user defined MyBatis type handlers in the given package, letting google-guice creating it.
   *
   * @param packageName
   *          the package where looking for type handlers.
   */
  protected final void addTypeHandlerClasses(String packageName) {
    checkArgument(packageName != null, "Parameter 'packageName' must not be null");
    addTypeHandlersClasses(
        new ResolverUtil<TypeHandler<?>>().find(new ResolverUtil.IsA(TypeHandler.class), packageName).getClasses());
  }

  /**
   * Adds the user defined myBatis interceptor plugins type, letting google-guice creating it.
   *
   * @param interceptorClass
   *          The user defined MyBatis interceptor plugin type
   */
  protected final void addInterceptorClass(final Class<? extends Interceptor> interceptorClass) {
    checkArgument(interceptorClass != null, "Parameter 'interceptorClass' must not be null");
    bindConfigurationSettingProvider(new InterceptorConfigurationSettingProvider(interceptorClass));
  }

  /**
   * Adds the user defined MyBatis interceptors plugins types, letting google-guice creating them.
   *
   * @param interceptorsClasses
   *          the user defined MyBatis Interceptors plugins types
   */
  protected final void addInterceptorsClasses(Collection<Class<? extends Interceptor>> interceptorsClasses) {
    checkArgument(interceptorsClasses != null, "Parameter 'interceptorsClasses' must not be null");

    for (Class<? extends Interceptor> interceptorClass : interceptorsClasses) {
      addInterceptorClass(interceptorClass);
    }
  }

  /**
   * Adds the user defined MyBatis interceptors plugins types in the given package, letting google-guice creating them.
   *
   * @param packageName
   *          the package where looking for Interceptors plugins types.
   */
  protected final void addInterceptorsClasses(String packageName) {
    checkArgument(packageName != null, "Parameter 'packageName' must not be null");
    addInterceptorsClasses(
        new ResolverUtil<Interceptor>().find(new ResolverUtil.IsA(Interceptor.class), packageName).getClasses());
  }

  /**
   * Adds the user defined mapper classes.
   *
   * @param mapperClass
   *          the user defined mapper classes.
   */
  protected final void addMapperClass(Class<?> mapperClass) {
    checkArgument(mapperClass != null, "Parameter 'mapperClass' must not be null");

    bindListener(KeyMatcher.create(Key.get(ConfigurationSettingListener.class)),
        ConfigurationProviderProvisionListener.create(new MapperConfigurationSetting(mapperClass)));
    bindMapper(mapperClass);
  }

  /**
   * Adds the user defined mapper classes.
   *
   * @param mapperClasses
   *          the user defined mapper classes
   */
  protected final void addMapperClasses(Collection<Class<?>> mapperClasses) {
    checkArgument(mapperClasses != null, "Parameter 'mapperClasses' must not be null");

    for (Class<?> mapperClass : mapperClasses) {
      addMapperClass(mapperClass);
    }
  }

  /**
   * Adds the user defined mapper classes.
   *
   * @param packageName
   *          the specified package to search for mappers to add.
   */
  protected final void addMapperClasses(final String packageName) {
    addMapperClasses(getClasses(packageName));
  }

  /**
   * Adds the user defined mapper classes.
   *
   * @param packageName
   *          the specified package to search for mappers to add.
   * @param test
   *          a test to run against the objects found in the specified package.
   */
  protected final void addMapperClasses(final String packageName, final ResolverUtil.Test test) {
    addMapperClasses(getClasses(test, packageName));
  }
}
