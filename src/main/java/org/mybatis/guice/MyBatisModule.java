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
package org.mybatis.guice;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;
import static com.google.inject.util.Providers.guicify;

import java.util.Collection;
import java.util.Set;

import javax.inject.Provider;
import javax.sql.DataSource;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.guice.binder.AliasBinder;
import org.mybatis.guice.binder.TypeHandlerBinder;
import org.mybatis.guice.configuration.ConfigurationProvider;
import org.mybatis.guice.configuration.Mappers;
import org.mybatis.guice.configuration.TypeAliases;
import org.mybatis.guice.environment.EnvironmentProvider;
import org.mybatis.guice.session.SqlSessionFactoryProvider;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

/**
 * Easy to use helper Module that alleviates users to write the boilerplate
 * google-guice bindings to create the SqlSessionFactory.
 *
 * @version $Id$
 */
public abstract class MyBatisModule extends AbstractMyBatisModule {

    /**
     * The ObjectFactory Provider class reference.
     */
    private Class<? extends ObjectFactory> objectFactoryType = DefaultObjectFactory.class;

    private MapBinder<String, Class<?>> aliases;

    private MapBinder<Class<?>, TypeHandler<?>> handlers;

    private Multibinder<Interceptor> interceptors;

    private Multibinder<Class<?>> mappers;

    /**
     * {@inheritDoc}
     */
    @Override
    final void internalConfigure() {
        if (this.aliases != null) {
            throw new IllegalStateException("Re-entry is not allowed.");
        }
        if (this.handlers != null) {
            throw new IllegalStateException("Re-entry is not allowed.");
        }
        if (this.interceptors != null) {
            throw new IllegalStateException("Re-entry is not allowed.");
        }
        if (this.mappers != null) {
            throw new IllegalStateException("Re-entry is not allowed.");
        }

        this.aliases = newMapBinder(binder(), new TypeLiteral<String>(){}, new TypeLiteral<Class<?>>(){}, TypeAliases.class);
        this.handlers = newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<TypeHandler<?>>(){});
        this.interceptors = newSetBinder(binder(), Interceptor.class);
        this.mappers = newSetBinder(binder(), new TypeLiteral<Class<?>>(){}, Mappers.class);

        try {
            this.initialize();
        } finally {
            this.aliases = null;
            this.handlers = null;
            this.interceptors = null;
            this.mappers = null;
        }

        // fixed bindings
        binder().bind(Environment.class).toProvider(EnvironmentProvider.class).in(Scopes.SINGLETON);
        binder().bind(Configuration.class).toProvider(ConfigurationProvider.class).in(Scopes.SINGLETON);
        binder().bind(SqlSessionFactory.class).toProvider(SqlSessionFactoryProvider.class).in(Scopes.SINGLETON);

        // parametric bindings
        binder().bind(ObjectFactory.class).to(this.objectFactoryType).in(Scopes.SINGLETON);
    }

    /**
     * Set the MyBatis configuration environment id.
     *
     * @param environmentId the MyBatis configuration environment id
     */
    protected final void environmentId(String environmentId) {
        if (environmentId == null) {
            throw new IllegalArgumentException("Parameter 'environmentId' must be not null");
        }
        binder().bindConstant().annotatedWith(named("mybatis.environment.id")).to(environmentId);
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
     * 
     * @param failFast
     */
    protected final void failFast(boolean failFast) {
        bindBoolean("mybatis.configuration.failFast", failFast);
    }

    /**
     * 
     * @param name
     * @param value
     */
    private final void bindBoolean(String name, boolean value) {
        binder().bindConstant().annotatedWith(named(name)).to(value);
    }

    /**
     * 
     *
     * @param executorType
     */
    protected final void executorType(ExecutorType executorType) {
        if (executorType == null) {
            throw new IllegalArgumentException("Parameter 'executorType' must be not null");
        }
        binder().bindConstant().annotatedWith(named("mybatis.configuration.defaultExecutorType")).to(executorType);
    }

    /**
     * 
     *
     * @param autoMappingBehavior
     */
    protected final void autoMappingBehavior(AutoMappingBehavior autoMappingBehavior) {
        if (autoMappingBehavior == null) {
            throw new IllegalArgumentException("Parameter 'autoMappingBehavior' must be not null");
        }
        binder().bindConstant().annotatedWith(named("mybatis.configuration.autoMappingBehavior")).to(autoMappingBehavior);
    }

    /**
     * Set the DataSource Provider type has to be bound.
     *
     * @param dataSourceProviderType the DataSource Provider type
     */
    protected final void bindDataSourceProviderType(Class<? extends Provider<DataSource>> dataSourceProviderType) {
        if (dataSourceProviderType == null) {
            throw new IllegalArgumentException("Parameter 'dataSourceProviderType' must be not null");
        }
        binder().bind(DataSource.class).toProvider(dataSourceProviderType).in(Scopes.SINGLETON);
    }

    /**
     * 
     *
     * @param dataSourceProvider
     */
    protected final void bindDataSourceProvider(Provider<DataSource> dataSourceProvider) {
        if (dataSourceProvider == null) {
            throw new IllegalArgumentException("Parameter 'dataSourceProvider' must be not null");
        }
        bindDataSourceProvider(guicify(dataSourceProvider));
    }

    /**
     * 
     *
     * @param dataSourceProvider
     */
    protected final void bindDataSourceProvider(com.google.inject.Provider<DataSource> dataSourceProvider) {
        if (dataSourceProvider == null) {
            throw new IllegalArgumentException("Parameter 'dataSourceProvider' must be not null");
        }
        binder().bind(DataSource.class).toProvider(dataSourceProvider).in(Scopes.SINGLETON);
    }

    /**
     * Set the TransactionFactory type has to be bound.
     *
     * @param transactionFactoryType the TransactionFactory type
     */
    protected final void bindTransactionFactoryType(Class<? extends TransactionFactory> transactionFactoryType) {
        if (transactionFactoryType == null) {
            throw new IllegalArgumentException("Parameter 'transactionFactoryType' must be not null");
        }
        binder().bind(TransactionFactory.class).to(transactionFactoryType).in(Scopes.SINGLETON);
    }

    /**
     * 
     *
     * @param transactionFactoryProvider
     */
    protected final void bindTransactionFactory(Provider<TransactionFactory> transactionFactoryProvider) {
        if (transactionFactoryProvider == null) {
            throw new IllegalArgumentException("Parameter 'transactionFactoryProvider' must be not null");
        }
        bindTransactionFactory(guicify(transactionFactoryProvider));
    }

    /**
     * 
     *
     * @param transactionFactoryProvider
     */
    protected final void bindTransactionFactory(com.google.inject.Provider<TransactionFactory> transactionFactoryProvider) {
        if (transactionFactoryProvider == null) {
            throw new IllegalArgumentException("Parameter 'transactionFactoryProvider' must be not null");
        }
        binder().bind(TransactionFactory.class).toProvider(transactionFactoryProvider).in(Scopes.SINGLETON);
    }

    /**
     * Sets the ObjectFactory class.
     *
     * @param objectFactoryType the ObjectFactory type
     */
    protected final void bindObjectFactoryType(Class<? extends ObjectFactory> objectFactoryType) {
        if (objectFactoryType == null) {
            throw new IllegalArgumentException("Parameter 'objectFactoryType' must be not null");
        }
        this.objectFactoryType = objectFactoryType;
    }

    /**
     * Add a user defined binding.
     *
     * @param alias the string type alias
     * @param clazz the type has to be bound.
     */
    protected final AliasBinder addAlias(final String alias) {
        if (alias == null || alias.length() == 0) {
            throw new IllegalArgumentException("Empty or null 'alias' is not valid");
        }

        return new AliasBinder() {

            public void to(final Class<?> clazz) {
                if (clazz == null) {
                    throw new IllegalArgumentException(String.format("Null type not valid for alias '%s'", alias));
                }

                aliases.addBinding(alias).toInstance(clazz);
            }

        };
    }

    /**
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
     *
     * @param types the specified types have to be bind
     */
    protected final void addSimpleAlias(final Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Parameter 'type' must be not null");
        }

        addAlias(type.getSimpleName()).to(type);
    }

    /**
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
     *
     * @param types the specified types have to be bind
     */
    protected final void addSimpleAliases(final Collection<Class<?>> types) {
        if (types == null) {
            throw new IllegalArgumentException("Parameter 'types' must be not null");
        }

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
        this.addSimpleAliases(getClasses(test, packageName));
    }

    /**
     * Adds all Classes in the given package as a simple alias.
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
     *
     * @param packageName the specified package to search for classes to alias
     */
    protected final void addSimpleAliases(final String packageName) {
        this.addSimpleAliases(getClasses(packageName));
    }

    /**
     * Add a user defined Type Handler letting google-guice creating it.
     *
     * @param type the specified type has to be handled.
     * @param handler the handler type.
     */
    protected final TypeHandlerBinder handleType(final Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Parameter 'type' must not be null");
        }

        return new TypeHandlerBinder() {

            public void with(final Class<? extends TypeHandler<?>> handler) {
                if (handler == null) {
                    throw new IllegalArgumentException(
                            String.format("TypeHandler must not be null for '%s'", type.getName()));
                }

                handlers.addBinding(type).to(handler).in(Scopes.SINGLETON);
            }

        };
    }

    /**
     * Adds the user defined myBatis interceptor plugins type, letting
     * google-guice creating it.
     *
     * @param interceptorClasse The user defined MyBatis interceptor plugin type
     */
    protected final void addInterceptorClass(Class<? extends Interceptor> interceptorClass) {
        if (interceptorClass == null) {
            throw new IllegalArgumentException("Parameter 'interceptorsClass' must not be null");
        }

        interceptors.addBinding().to(interceptorClass).in(Scopes.SINGLETON);
    }

    /**
     * Adds the user defined MyBatis interceptors plugins types, letting
     * google-guice creating them.
     *
     * @param interceptorsClasses the user defined MyBatis Interceptors plugins types
     */
    protected final void addInterceptorsClasses(Collection<Class<? extends Interceptor>> interceptorsClasses) {
        if (interceptorsClasses == null) {
            throw new IllegalArgumentException("Parameter 'interceptorsClasses' must not be null");
        }

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
        if (packageName == null) {
            throw new IllegalArgumentException("Parameter 'packageName' must be not null");
        }
        this.addInterceptorsClasses(new ResolverUtil<Interceptor>()
                .find(new ResolverUtil.IsA(Interceptor.class), packageName)
                .getClasses());
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param mapperClasses the user defined mapper classes.
     */
    protected final void addMapperClass(Class<?> mapperClass) {
        if (mapperClass == null) {
            throw new IllegalArgumentException("Parameter 'mapperClass' must not be null");
        }

        mappers.addBinding().toInstance(mapperClass);
        bindMapper(mapperClass);
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param mapperClasses the user defined mapper classes
     */
    protected final void addMapperClasses(Collection<Class<?>> mapperClasses) {
        if (mapperClasses == null) {
            throw new IllegalArgumentException("Parameter 'mapperClasses' must not be null");
        }

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
        this.addMapperClasses(getClasses(packageName));
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param packageName the specified package to search for mappers to add.
     * @param test a test to run against the objects found in the specified package.
     */
    protected final void addMapperClasses(final String packageName, final ResolverUtil.Test test) {
        this.addMapperClasses(getClasses(test, packageName));
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
        if (test == null) {
            throw new IllegalArgumentException("Parameter 'test' must be not null");
        }
        if (packageName == null) {
            throw new IllegalArgumentException("Parameter 'packageName' must be not null");
        }
        return new ResolverUtil<Object>().find(test, packageName).getClasses();
    }

}
