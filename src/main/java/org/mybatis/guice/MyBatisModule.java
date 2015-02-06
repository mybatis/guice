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
package org.mybatis.guice;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

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
import org.mybatis.guice.configuration.Mappers;
import org.mybatis.guice.configuration.MappingTypeHandlers;
import org.mybatis.guice.configuration.TypeAliases;
import org.mybatis.guice.environment.EnvironmentProvider;
import org.mybatis.guice.session.SqlSessionFactoryProvider;
import org.mybatis.guice.type.TypeHandlerProvider;

import javax.inject.Provider;
import javax.sql.DataSource;

import java.util.Collection;
import java.util.Set;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;
import static com.google.inject.util.Providers.guicify;
import static org.mybatis.guice.Preconditions.checkArgument;
import static org.mybatis.guice.Preconditions.checkState;

/**
 * Easy to use helper Module that alleviates users to write the boilerplate
 * google-guice bindings to create the SqlSessionFactory.
 *
 * @version $Id$
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

    private MapBinder<String, Class<?>> aliases;

    private MapBinder<Class<?>, TypeHandler<?>> handlers;

    private Multibinder<TypeHandler<?>> mappingTypeHandlers;

    private Multibinder<Interceptor> interceptors;

    private Multibinder<Class<?>> mappers;

    /**
     * {@inheritDoc}
     */
    @Override
    final void internalConfigure() {
        checkState( aliases == null, "Re-entry is not allowed." );
        checkState( handlers == null, "Re-entry is not allowed." );
        checkState( interceptors == null, "Re-entry is not allowed." );
        checkState( mappers == null, "Re-entry is not allowed." );

        aliases = newMapBinder(binder(), new TypeLiteral<String>(){}, new TypeLiteral<Class<?>>(){}, TypeAliases.class);
        handlers = newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<TypeHandler<?>>(){});
        interceptors = newSetBinder(binder(), Interceptor.class);
        mappingTypeHandlers = newSetBinder(binder(), new TypeLiteral<TypeHandler<?>>(){}, MappingTypeHandlers.class);
        mappers = newSetBinder(binder(), new TypeLiteral<Class<?>>(){}, Mappers.class);

        try {
            initialize();
        } finally {
            aliases = null;
            handlers = null;
            interceptors = null;
            mappers = null;
        }

        // fixed bindings
        bind(Environment.class).toProvider(EnvironmentProvider.class).in(Scopes.SINGLETON);
        bind(Configuration.class).toProvider(ConfigurationProvider.class).in(Scopes.SINGLETON);
        
        // replaceable bindings.
        bind(SqlSessionFactory.class).toProvider(sqlSessionFactoryProviderType);

        // parametric bindings
        bind(ObjectFactory.class).to(objectFactoryType).in(Scopes.SINGLETON);
        bind(ObjectWrapperFactory.class).to(objectWrapperFactoryType).in(Scopes.SINGLETON);
        bind(new TypeLiteral<Class<? extends LanguageDriver>>() {}).toInstance(defaultScriptingLanguageType);
        
    }

    /**
     * Set the MyBatis configuration environment id.
     *
     * @param environmentId the MyBatis configuration environment id
     */
    protected final void environmentId(String environmentId) {
        checkArgument( environmentId != null, "Parameter 'environmentId' must be not null");
        bindConstant().annotatedWith(named("mybatis.environment.id")).to(environmentId);
    }

    /**
     *
     * @param lazyLoadingEnabled
     */
    protected final void lazyLoadingEnabled(boolean lazyLoadingEnabled) {
        bindBoolean("mybatis.configuration.lazyLoadingEnabled", lazyLoadingEnabled);
    }

    /**
     *
     * @param aggressiveLazyLoading
     */
    protected final void aggressiveLazyLoading(boolean aggressiveLazyLoading) {
        bindBoolean("mybatis.configuration.aggressiveLazyLoading", aggressiveLazyLoading);
    }

    /**
     *
     * @param multipleResultSetsEnabled
     */
    protected final void multipleResultSetsEnabled(boolean multipleResultSetsEnabled) {
        bindBoolean("mybatis.configuration.multipleResultSetsEnabled", multipleResultSetsEnabled);
    }

    /**
     *
     * @param useGeneratedKeys
     */
    protected final void useGeneratedKeys(boolean useGeneratedKeys) {
        bindBoolean("mybatis.configuration.useGeneratedKeys", useGeneratedKeys);
    }

    /**
     *
     * @param useColumnLabel
     */
    protected final void useColumnLabel(boolean useColumnLabel) {
        bindBoolean("mybatis.configuration.useColumnLabel", useColumnLabel);
    }

    /**
     *
     * @param useCacheEnabled
     */
    protected final void useCacheEnabled(boolean useCacheEnabled) {
        bindBoolean("mybatis.configuration.cacheEnabled", useCacheEnabled);
    }
    
    /**
     * @param sqlSessionFactoryProvider provider for SqlSessionFactory
     */
    protected final void useSqlSessionFactoryProvider(Class<? extends Provider<? extends SqlSessionFactory>> sqlSessionFactoryProvider) {
        this.sqlSessionFactoryProviderType = sqlSessionFactoryProvider;
    }

    /**
     *
     * @param failFast
     */
    protected final void failFast(boolean failFast) {
        bindBoolean("mybatis.configuration.failFast", failFast);
    }

    /**
     * Maps underscores to camel case.
     *
     * @param mapUnderscoreToCamelCase Toggles this settings value.
     */
    protected final void mapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        bindBoolean("mybatis.configuration.mapUnderscoreToCamelCase", mapUnderscoreToCamelCase);
    }

    /**
     *
     * @param name
     * @param value
     */
    private final void bindBoolean(String name, boolean value) {
        bindConstant().annotatedWith(named(name)).to(value);
    }

    /**
     *
     *
     * @param executorType
     */
    protected final void executorType(ExecutorType executorType) {
        checkArgument(executorType != null, "Parameter 'executorType' must be not null");
        bindConstant().annotatedWith(named("mybatis.configuration.defaultExecutorType")).to(executorType);
    }

    /**
     * Configures the local cache scope setting.
     *
     * @param localeCacheScope The cache scope to use.
     * @since 3.4
     */
    protected final void localCacheScope(LocalCacheScope localeCacheScope) {
        checkArgument(localeCacheScope != null, "Parameter 'localCacheScope' must be not null");
        bindConstant().annotatedWith(named("mybatis.configuration.localCacheScope")).to(localeCacheScope);
    }

    /**
     *
     *
     * @param autoMappingBehavior
     */
    protected final void autoMappingBehavior(AutoMappingBehavior autoMappingBehavior) {
        checkArgument(autoMappingBehavior != null, "Parameter 'autoMappingBehavior' must be not null");
        bindConstant().annotatedWith(named("mybatis.configuration.autoMappingBehavior")).to(autoMappingBehavior);
    }

    /**
     * Set the DataSource Provider type has to be bound.
     *
     * @param dataSourceProviderType the DataSource Provider type
     */
    protected final void bindDataSourceProviderType(Class<? extends Provider<DataSource>> dataSourceProviderType) {
        checkArgument(dataSourceProviderType != null, "Parameter 'dataSourceProviderType' must be not null");
        bind(DataSource.class).toProvider(dataSourceProviderType).in(Scopes.SINGLETON);
    }

    /**
     *
     *
     * @param dataSourceProvider
     */
    protected final void bindDataSourceProvider(Provider<DataSource> dataSourceProvider) {
        checkArgument(dataSourceProvider != null, "Parameter 'dataSourceProvider' must be not null");
        bindDataSourceProvider(guicify(dataSourceProvider));
    }

    /**
     *
     *
     * @param dataSourceProvider
     */
    protected final void bindDataSourceProvider(com.google.inject.Provider<DataSource> dataSourceProvider) {
        checkArgument(dataSourceProvider != null, "Parameter 'dataSourceProvider' must be not null");
        bind(DataSource.class).toProvider(dataSourceProvider).in(Scopes.SINGLETON);
    }

    /**
     *
     *
     * @param databaseIdProvider The DatabaseIdProvider class.
     */
    protected final void bindDatabaseIdProvider(Class<? extends DatabaseIdProvider> databaseIdProvider) {
        checkArgument(databaseIdProvider != null, "Parameter 'dataSourceProvider' must be not null");
        bind(DatabaseIdProvider.class).to(databaseIdProvider).in(Scopes.SINGLETON);
    }

    /**
     *
     *
     * @param databaseIdProvider The DatabaseIdProvider instance.
     */
    protected final void bindDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
        checkArgument(databaseIdProvider != null, "Parameter 'dataSourceProvider' must be not null");
        bind(DatabaseIdProvider.class).toInstance(databaseIdProvider);
    }

    /**
     * Set the TransactionFactory type has to be bound.
     *
     * @param transactionFactoryType the TransactionFactory type
     */
    protected final void bindTransactionFactoryType(Class<? extends TransactionFactory> transactionFactoryType) {
        checkArgument(transactionFactoryType != null, "Parameter 'transactionFactoryType' must be not null");
        bind(TransactionFactory.class).to(transactionFactoryType).in(Scopes.SINGLETON);
    }

    /**
     *
     *
     * @param transactionFactoryProvider
     */
    protected final void bindTransactionFactory(Provider<TransactionFactory> transactionFactoryProvider) {
        checkArgument(transactionFactoryProvider != null, "Parameter 'transactionFactoryProvider' must be not null");
        bindTransactionFactory(guicify(transactionFactoryProvider));
    }

    /**
     *
     *
     * @param transactionFactoryProvider
     */
    protected final void bindTransactionFactory(com.google.inject.Provider<TransactionFactory> transactionFactoryProvider) {
        checkArgument(transactionFactoryProvider != null, "Parameter 'transactionFactoryProvider' must be not null");
        bind(TransactionFactory.class).toProvider(transactionFactoryProvider).in(Scopes.SINGLETON);
    }

    /**
     * Sets the ObjectFactory class.
     *
     * @param objectFactoryType the ObjectFactory type
     */
    protected final void bindObjectFactoryType(Class<? extends ObjectFactory> objectFactoryType) {
        checkArgument(objectFactoryType != null, "Parameter 'objectFactoryType' must be not null");
        this.objectFactoryType = objectFactoryType;
    }
    /**
     * Sets the ObjectWrapperFactory class.
     *
     * @param objectWrapperFactoryType the ObjectFactory type
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
     * @param defaultScriptingLanguageType the default LanguageDriver type
     */
    protected final void bindDefaultScriptingLanguageType(Class<? extends LanguageDriver> defaultScriptingLanguageType) {
        checkArgument(defaultScriptingLanguageType != null, "Parameter 'defaultScriptingLanguageType' must be not null");
        this.defaultScriptingLanguageType = defaultScriptingLanguageType;
    }
    
    /**
     * Add a user defined binding.
     *
     * @param alias the string type alias
     */
    protected final AliasBinder addAlias(final String alias) {
        checkArgument(alias != null && alias.length() > 0, "Empty or null 'alias' is not valid");

        return new AliasBinder() {

            public void to(final Class<?> clazz) {
                checkArgument(clazz != null, "Null type not valid for alias '%s'", alias);
                aliases.addBinding(alias).toInstance(clazz);
            }

        };
    }

    /**
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
     *
     * @param type the specified types have to be bind
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
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
     *
     * @param types the specified types have to be bind
     */
    protected final void addSimpleAliases(final Collection<Class<?>> types) {
        checkArgument(types != null, "Parameter 'types' must be not null");

        for (Class<?> type : types) {
            addSimpleAlias(type);
        }
    }

    /**
     * Adds all Classes in the given package as a simple alias.
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
     *
     * @param packageName the specified package to search for classes to alias.
     * @param test a test to run against the objects found in the specified package
     */
    protected final void addSimpleAliases(final String packageName, final ResolverUtil.Test test) {
        addSimpleAliases(getClasses(test, packageName));
    }

    /**
     * Adds all Classes in the given package as a simple alias.
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
     *
     * @param packageName the specified package to search for classes to alias
     */
    protected final void addSimpleAliases(final String packageName) {
        addSimpleAliases(getClasses(packageName));
    }

    /**
     * Add a user defined Type Handler letting google-guice creating it.
     *
     * @param type the specified type has to be handled.
     */
    protected final <T> TypeHandlerBinder<T> handleType(final Class<T> type) {
        checkArgument(type != null, "Parameter 'type' must be not null");

        return new TypeHandlerBinder<T>() {

            public void with(final Class<? extends TypeHandler<? extends T>> handler) {
                checkArgument(handler != null, "TypeHandler must not be null for '%s'", type.getName());
                handlers.addBinding(type).to(handler);
                bindTypeHandler(handler, type);
            }

        };
    }

    /**
     * Adds the user defined MyBatis type handler, letting
     * google-guice creating it.
     *
     * @param handlerClass the handler type.
     */
    protected final void addTypeHandlerClass(Class<? extends TypeHandler<?>> handlerClass) {
        checkArgument(handlerClass != null, "Parameter 'handlerClass' must not be null");
    	mappingTypeHandlers.addBinding().to(handlerClass);
    	bindTypeHandler(handlerClass, null);
    }

    /**
     * Adds the user defined MyBatis type handlers, letting
     * google-guice creating it.
     *
     * @param handlersClasses the handler type.
     */
    protected final void addTypeHandlersClasses(Collection<Class<? extends TypeHandler<?>>> handlersClasses) {
        checkArgument(handlersClasses != null, "Parameter 'handlersClasses' must not be null");

        for (Class<? extends TypeHandler<?>> handlerClass : handlersClasses) {
            addTypeHandlerClass(handlerClass);
        }
    }

    /**
     * Adds the user defined MyBatis type handlers in the given package, letting
     * google-guice creating it.
     *
     * @param packageName the package where looking for type handlers.
     */
    protected final void addTypeHandlerClasses(String packageName) {
        checkArgument(packageName != null, "Parameter 'packageName' must not be null");
        addTypeHandlersClasses(new ResolverUtil<TypeHandler<?>>()
                .find(new ResolverUtil.IsA(TypeHandler.class), packageName)
                .getClasses());
    }

    /**
     * Adds the user defined myBatis interceptor plugins type, letting
     * google-guice creating it.
     *
     * @param interceptorClass The user defined MyBatis interceptor plugin type
     */
    protected final void addInterceptorClass(Class<? extends Interceptor> interceptorClass) {
        checkArgument(interceptorClass != null, "Parameter 'interceptorClass' must not be null");
        interceptors.addBinding().to(interceptorClass).in(Scopes.SINGLETON);
    }

    /**
     * Adds the user defined MyBatis interceptors plugins types, letting
     * google-guice creating them.
     *
     * @param interceptorsClasses the user defined MyBatis Interceptors plugins types
     */
    protected final void addInterceptorsClasses(Collection<Class<? extends Interceptor>> interceptorsClasses) {
        checkArgument(interceptorsClasses != null, "Parameter 'interceptorsClasses' must not be null");

        for (Class<? extends Interceptor> interceptorClass : interceptorsClasses) {
            addInterceptorClass(interceptorClass);
        }
    }

    /**
     * Adds the user defined MyBatis interceptors plugins types in the given package,
     * letting google-guice creating them.
     *
     * @param packageName the package where looking for Interceptors plugins types.
     */
    protected final void addInterceptorsClasses(String packageName) {
        checkArgument(packageName != null, "Parameter 'packageName' must not be null");
        addInterceptorsClasses(new ResolverUtil<Interceptor>()
                .find(new ResolverUtil.IsA(Interceptor.class), packageName)
                .getClasses());
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param mapperClass the user defined mapper classes.
     */
    protected final void addMapperClass(Class<?> mapperClass) {
        checkArgument(mapperClass != null, "Parameter 'mapperClass' must not be null");

        mappers.addBinding().toInstance(mapperClass);
        bindMapper(mapperClass);
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param mapperClasses the user defined mapper classes
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
     * @param packageName the specified package to search for mappers to add.
     */
    protected final void addMapperClasses(final String packageName) {
        addMapperClasses(getClasses(packageName));
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param packageName the specified package to search for mappers to add.
     * @param test a test to run against the objects found in the specified package.
     */
    protected final void addMapperClasses(final String packageName, final ResolverUtil.Test test) {
        addMapperClasses(getClasses(test, packageName));
    }

    /**
     * Return a set of all classes contained in the given package.
     *
     * @param packageName the package has to be analyzed.
     * @return a set of all classes contained in the given package.
     */
    private static Set<Class<?>> getClasses(String packageName) {
        return getClasses(new ResolverUtil.IsA(Object.class), packageName);
    }

    /**
     * Return a set of all classes contained in the given package that match with
     * the given test requirement.
     *
     * @param test the class filter on the given package.
     * @param packageName the package has to be analyzed.
     * @return a set of all classes contained in the given package.
     */
    private static Set<Class<?>> getClasses(ResolverUtil.Test test, String packageName) {
        checkArgument(test != null, "Parameter 'test' must not be null");
        checkArgument(packageName != null, "Parameter 'packageName' must not be null");
        return new ResolverUtil<Object>().find(test, packageName).getClasses();
    }

    final <TH extends TypeHandler<? extends T>, T> void bindTypeHandler(Class<TH> typeHandlerType, Class<T> type) {
        bind(typeHandlerType).toProvider(guicify(new TypeHandlerProvider<TH, T>(typeHandlerType, type))).in(Scopes.SINGLETON);
    }
}
