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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.guice.configuration.ConfigurationProvider;
import org.mybatis.guice.configuration.ConfigurationProviderTypeListener;
import org.mybatis.guice.configuration.Mappers;
import org.mybatis.guice.configuration.TypeAliases;
import org.mybatis.guice.environment.EnvironmentProvider;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;
import org.mybatis.guice.transactionfactory.JdbcTransactionFactoryProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

/**
 * Easy to use helper Module that alleviates users to write the boilerplate
 * google-guice bindings to create the SqlSessionFactory.
 *
 * @version $Id$
 */
public final class MyBatisModule extends AbstractModule {

    /**
     * The DataSource Provider class reference.
     */
    private final Class<? extends Provider<DataSource>> dataSourceProviderClass;

    /**
     * The TransactionFactory Provider class reference.
     */
    private final Class<? extends Provider<TransactionFactory>> transactionFactoryProviderClass;

    /**
     * The user defined aliases.
     */
    private final Map<String, Class<?>> aliases = new HashMap<String, Class<?>>();

    /**
     * The user defined type handlers.
     */
    private final Map<Class<?>, Class<? extends TypeHandler>> handlers = new HashMap<Class<?>, Class<? extends TypeHandler>>();

    /**
     * The user defined Interceptor classes.
     */
    private final Set<Class<? extends Interceptor>> interceptorsClasses = new HashSet<Class<? extends Interceptor>>();

    /**
     * The user defined mapper classes.
     */
    private final Set<Class<?>> mapperClasses = new HashSet<Class<?>>();

    /**
     * The ObjectFactory Provider class reference.
     */
    private Class<? extends Provider<ObjectFactory>> objectFactoryProviderClass;

    /**
     * Creates a new module that binds all the needed modules to create the
     * SqlSessionFactory, injecting all the required components, using the
     * {@link JdbcTransactionFactoryProvider} as default transaction factory
     * provider.
     *
     * @param dataSourceProviderClass the DataSource Provider class reference.
     */
    public MyBatisModule(final Class<? extends Provider<DataSource>> dataSourceProviderClass) {
        this(dataSourceProviderClass, JdbcTransactionFactoryProvider.class);
    }

    /**
     * Creates a new module that binds all the needed modules to create the
     * SqlSessionFactory, injecting all the required components.
     *
     * @param dataSourceProviderClass the DataSource Provider class reference.
     * @param transactionFactoryProviderClass the TransactionFactory Provider
     *        class reference.
     */
    public MyBatisModule(final Class<? extends Provider<DataSource>> dataSourceProviderClass,
            final Class<? extends Provider<TransactionFactory>> transactionFactoryProviderClass) {
        if (dataSourceProviderClass == null) {
            throw new IllegalArgumentException("Data Source provider class mustn't be null");
        }
        this.dataSourceProviderClass = dataSourceProviderClass;

        if (transactionFactoryProviderClass == null) {
            throw new IllegalArgumentException("Transaction Factory provider class mustn't be null");
        }
        this.transactionFactoryProviderClass = transactionFactoryProviderClass;
    }

    /**
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes
     *  {@code Foo}.
     *
     * @param types he specified types have to be bind.
     * @return this {@code SqlSessionFactoryModule} instance.
     */
    public MyBatisModule addSimpleAliases(final Class<?>...types) {
        for (Class<?> clazz : types) {
            this.addAlias(clazz.getSimpleName(), clazz);
        }
        return this;
    }

    /**
     * Add a user defined binding.
     *
     * @param alias the string type alias
     * @param clazz the type has to be bound.
     */
    public MyBatisModule addAlias(final String alias, final Class<?> clazz) {
        this.aliases.put(alias, clazz);
        return this;
    }

    /**
     * Add a user defined Type Handler letting google-guice creating it.
     *
     * @param type the specified type has to be handled.
     * @param handler the handler type.
     * @return this {@code SqlSessionFactoryModule} instance.
     */
    public MyBatisModule addTypeHandler(final Class<?> type, final Class<? extends TypeHandler> handler) {
        this.handlers.put(type, handler);
        return this;
    }

    /**
     * Adds the user defined iBatis interceptors plugins types, letting
     * google-guice creating them.
     *
     * @param interceptorsClasses the user defined iBatis interceptors plugins
     *        types.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public MyBatisModule addInterceptorsClasses(Class<? extends Interceptor>...interceptorsClasses) {
        if (interceptorsClasses == null || interceptorsClasses.length == 0) {
            return this;
        }

        for (Class<? extends Interceptor> interceptorsClass : interceptorsClasses) {
            this.interceptorsClasses.add(interceptorsClass);
        }
        return this;
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param mapperClasses the user defined mapper classes.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public MyBatisModule addMapperClasses(Class<?>...mapperClasses) {
        if (mapperClasses == null || mapperClasses.length == 0) {
            return this;
        }

        for (Class<?> mapperClass : mapperClasses) {
            this.mapperClasses.add(mapperClass);
        }
        return this;
    }

    /**
     * Sets the ObjectFactory provider class.
     *
     * @param objectFactoryProviderClass the ObjectFactory provider class.
     * @return this {@code SqlSessionFactoryModule} instance.
     */
    public MyBatisModule setObjectFactoryProviderClass(Class<? extends Provider<ObjectFactory>> objectFactoryProviderClass) {
        this.objectFactoryProviderClass = objectFactoryProviderClass;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        this.bindListener(Matchers.only(new TypeLiteral<ConfigurationProvider>(){}), new ConfigurationProviderTypeListener());

        this.bind(DataSource.class).toProvider(this.dataSourceProviderClass);
        this.bind(TransactionFactory.class).toProvider(this.transactionFactoryProviderClass);
        this.bind(Environment.class).toProvider(EnvironmentProvider.class);
        this.bind(Configuration.class).toProvider(ConfigurationProvider.class);
        this.bind(SqlSessionFactory.class).toProvider(SqlSessionFactoryProvider.class);
        this.bind(SqlSessionManager.class).toProvider(SqlSessionManagerProvider.class);

        TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
        this.binder().requestInjection(interceptor);
        this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), interceptor);

        // optional bindings

        // aliases
        if (!this.aliases.isEmpty()) {
            this.bind(new TypeLiteral<Map<String, Class<?>>>(){}).annotatedWith(TypeAliases.class).toInstance(this.aliases);
        }

        // type handlers
        if (!this.handlers.isEmpty()) {
            MapBinder<Class<?>, TypeHandler> handlerBinder =
                MapBinder.newMapBinder(this.binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<TypeHandler>(){});
            for (Entry<Class<?>, Class<? extends TypeHandler>> entry : this.handlers.entrySet()) {
                handlerBinder.addBinding(entry.getKey()).to(entry.getValue()).in(Scopes.SINGLETON);
            }
        }

        // interceptors plugin
        if (!this.interceptorsClasses.isEmpty()) {
            Multibinder<Interceptor> cacheMultibinder = Multibinder.newSetBinder(this.binder(), Interceptor.class);
            for (Class<? extends Interceptor> interceptorClass : this.interceptorsClasses) {
                cacheMultibinder.addBinding().to(interceptorClass).in(Scopes.SINGLETON);
            }
        }

        // mappers
        if (!this.mapperClasses.isEmpty()) {
            this.bind(new TypeLiteral<Set<Class<?>>>() {}).annotatedWith(Mappers.class).toInstance(this.mapperClasses);

            for (Class<?> mapperType : this.mapperClasses) {
                bindMapperProvider(this.binder(), mapperType);
            }
        }

        // the object factory
        if (this.objectFactoryProviderClass != null) {
            this.bind(ObjectFactory.class).toProvider(this.objectFactoryProviderClass).in(Scopes.SINGLETON);
        }
    }

    private static <T> void bindMapperProvider(Binder binder, Class<T> mapperType) {
        binder.bind(mapperType).toProvider(new MapperProvider<T>(mapperType)).in(Scopes.SINGLETON);
    }

}
