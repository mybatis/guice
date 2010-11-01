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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.guice.configuration.ConfigurationProvider;
import org.mybatis.guice.configuration.ConfigurationProviderTypeListener;
import org.mybatis.guice.configuration.Mappers;
import org.mybatis.guice.configuration.TypeAliases;
import org.mybatis.guice.environment.EnvironmentProvider;
import org.mybatis.guice.mappers.MappersBinder;

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
public final class MyBatisModule extends AbstractMyBatisModule {

    /**
     * The DataSource Provider class reference.
     */
    private final Class<? extends Provider<DataSource>> dataSourceProviderClass;

    /**
     * The TransactionFactory Provider class reference.
     */
    private final Class<? extends TransactionFactory> transactionFactoryType;

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
     * The ObjectFactory Provider class reference.
     */
    private Class<? extends Provider<ObjectFactory>> objectFactoryProviderClass;

    /**
     * The user defined mapper classes.
     */
    private final Set<Class<?>> mapperClasses = new LinkedHashSet<Class<?>>();

    /**
     * Creates a new module that binds all the needed modules to create the
     * SqlSessionFactory, injecting all the required components, using the
     * JdbcTransactionFactory as default transaction factory
     * provider.
     *
     * @param dataSourceProviderClass the DataSource Provider class reference.
     */
    public MyBatisModule(final Class<? extends Provider<DataSource>> dataSourceProviderClass) {
        this(dataSourceProviderClass, JdbcTransactionFactory.class);
    }

    /**
     * Creates a new module that binds all the needed modules to create the
     * SqlSessionFactory, injecting all the required components.
     *
     * @param dataSourceProviderType the DataSource Provider class reference.
     * @param transactionFactoryProviderClass the TransactionFactory Provider
     *        class reference.
     */
    public MyBatisModule(final Class<? extends Provider<DataSource>> dataSourceProviderType,
            final Class<? extends TransactionFactory> transactionFactoryType) {
        if (dataSourceProviderType == null) {
            throw new IllegalArgumentException("Data Source provider class mustn't be null");
        }
        this.dataSourceProviderClass = dataSourceProviderType;

        if (transactionFactoryType == null) {
            throw new IllegalArgumentException("Transaction Factory provider class mustn't be null");
        }
        this.transactionFactoryType = transactionFactoryType;
    }

    /**
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes
     *  {@code Foo}.
     *
     * @param types the specified types have to be bind.
     * @return this {@code SqlSessionFactoryModule} instance.
     */
    public MyBatisModule addSimpleAliases(final Class<?>...types) {
        if (types != null) {
            return this.addSimpleAliases(Arrays.asList(types));
        }
        return this;
    }

    /**
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes
     *  {@code Foo}.
     *
     * @param types the specified types have to be bind.
     * @return this {@code SqlSessionFactoryModule} instance.
     */
    public MyBatisModule addSimpleAliases(final Collection<Class<?>> types) {
        if (types != null) {
            for (Class<?> clazz : types) {
                this.addAlias(clazz.getSimpleName(), clazz);
            }
        }
        return this;
    }

    /**
     * Adds all Classes in the given package as a simple alias.
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes
     *  {@code Foo}.
     *
     * @param packageName the specified package to search for classes to alias.
     * @return this {@code SqlSessionFactoryModule} instance.
     */
    public MyBatisModule addSimpleAliases(final String packageName) {
        return this.addSimpleAliases(getClasses(packageName));
    }

    /**
     * Adds all Classes in the given package as a simple alias.
     * Adding simple aliases means that every specified class will be bound
     * using the simple class name, i.e.  {@code com.acme.Foo} becomes
     *  {@code Foo}.
     *
     * @param packageName the specified package to search for classes to alias.
     * @param test a test to run against the objects found in the specified package.
     * @return this {@code SqlSessionFactoryModule} instance.
     */
    public MyBatisModule addSimpleAliases(final String packageName, final ResolverUtil.Test test) {
        return this.addSimpleAliases(getClasses(test, packageName));
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
     * Adds the user defined myBatis interceptors plugins types, letting
     * google-guice creating them.
     *
     * @param interceptorsClasses the user defined MyBatis interceptors plugins
     *        types.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public MyBatisModule addInterceptorsClasses(Class<? extends Interceptor>...interceptorsClasses) {
        if (interceptorsClasses != null) {
            return this.addInterceptorsClasses(Arrays.asList(interceptorsClasses));
        }
        return this;
    }

    /**
     * Adds the user defined MyBatis interceptors plugins types, letting
     * google-guice creating them.
     *
     * @param interceptorsClasses the user defined MyBatis interceptors plugins
     *        types.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public MyBatisModule addInterceptorsClasses(Collection<Class<? extends Interceptor>> interceptorsClasses) {
        if (interceptorsClasses != null) {
            this.interceptorsClasses.addAll(interceptorsClasses);
        }
        return this;
    }

    /**
     * Adds the user defined MyBatis interceptors plugins types in the given package,
     * letting google-guice creating them.
     *
     * @param packageName.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public MyBatisModule addInterceptorsClasses(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("Parameter 'packageName' must be not null");
        }

        return this.addInterceptorsClasses(new ResolverUtil<Interceptor>()
                .find(new ResolverUtil.IsA(Interceptor.class), packageName)
                .getClasses());
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
     * Adds the user defined mapper classes.
     *
     * @param mapperClasses the user defined mapper classes.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public MyBatisModule addMapperClasses(Class<?>...mapperClasses) {
        if (mapperClasses != null) {
            return this.addMapperClasses(Arrays.asList(mapperClasses));
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
    public MyBatisModule addMapperClasses(Collection<Class<?>> mapperClasses) {
        if (mapperClasses != null) {
            this.mapperClasses.addAll(mapperClasses);
        }
        return this;
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param packageName the specified package to search for mappers to add.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public MyBatisModule addMapperClasses(final String packageName) {
        return this.addMapperClasses(getClasses(packageName));
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param packageName the specified package to search for mappers to add.
     * @param test a test to run against the objects found in the specified package.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public MyBatisModule addMapperClasses(final String packageName, final ResolverUtil.Test test) {
        return this.addMapperClasses(getClasses(test, packageName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        // needed binding
        this.bind(DataSource.class).toProvider(this.dataSourceProviderClass);
        this.bind(TransactionFactory.class).to(this.transactionFactoryType).in(Scopes.SINGLETON);
        this.bind(Environment.class).toProvider(EnvironmentProvider.class);
        this.bind(Configuration.class).toProvider(ConfigurationProvider.class);
        this.bindListener(Matchers.only(new TypeLiteral<ConfigurationProvider>(){}), new ConfigurationProviderTypeListener());
        this.bind(SqlSessionFactory.class).toProvider(SqlSessionFactoryProvider.class);

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
            MappersBinder.bind(this.binder(), this.mapperClasses);
        }

        // the object factory
        if (this.objectFactoryProviderClass != null) {
            this.bind(ObjectFactory.class).toProvider(this.objectFactoryProviderClass).in(Scopes.SINGLETON);
        }
    }

    private static Set<Class<?>> getClasses(ResolverUtil.Test test, String packageName) {
        if (test == null) {
            throw new IllegalArgumentException("Parameter 'test' must be not null");
        }
        if (packageName == null) {
            throw new IllegalArgumentException("Parameter 'packageName' must be not null");
        }
        return new ResolverUtil<Object>().find(test, packageName).getClasses();
    }

    private static Set<Class<?>> getClasses(String packageName) {
        return getClasses(new ResolverUtil.IsA(Object.class), packageName);
    }

}
