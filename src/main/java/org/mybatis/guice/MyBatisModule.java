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

import static org.mybatis.guice.iterables.Iterables.foreach;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.guice.configuration.ConfigurationProvider;
import org.mybatis.guice.configuration.Mappers;
import org.mybatis.guice.configuration.TypeAliases;
import org.mybatis.guice.datasource.builtin.UnpooledDataSourceProvider;
import org.mybatis.guice.environment.EnvironmentProvider;
import org.mybatis.guice.iterables.Each;
import org.mybatis.guice.session.SqlSessionFactoryProvider;

import com.google.inject.Module;
import com.google.inject.Provider;
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
public final class MyBatisModule extends AbstractMyBatisModule {

    /**
     * The DataSource Provider class reference.
     */
    private final Class<? extends Provider<DataSource>> dataSourceProviderType;

    /**
     * The TransactionFactory class reference.
     */
    private final Class<? extends TransactionFactory> transactionFactoryType;

    /**
     * The user defined aliases.
     */
    private final Map<String, Class<?>> aliases;

    /**
     * The user defined type handlers.
     */
    private final Map<Class<?>, Class<? extends TypeHandler>> handlers;

    /**
     * The user defined Interceptor classes.
     */
    private final Set<Class<? extends Interceptor>> interceptorsClasses;

    /**
     * The ObjectFactory class reference.
     */
    private Class<? extends ObjectFactory> objectFactoryType;

    /**
     * The user defined mapper classes.
     */
    private final Set<Class<?>> mapperClasses;

    /**
     * Creates a new module that binds all the needed modules to create the
     * SqlSessionFactory, injecting all the required components.
     *
     * @param dataSourceProviderType the DataSource Provider class reference.
     * @param transactionFactoryType the TransactionFactory class reference.
     * @param aliases the user defined aliases.
     * @param handlers the user defined type handlers.
     * @param interceptorsClasses the user defined Interceptor classes.
     * @param objectFactoryType the ObjectFactory class reference.
     * @param mapperClasses the user defined mapper classes.
     */
    private MyBatisModule(
            Class<? extends Provider<DataSource>> dataSourceProviderType,
            Class<? extends TransactionFactory> transactionFactoryType,
            Map<String, Class<?>> aliases,
            Map<Class<?>, Class<? extends TypeHandler>> handlers,
            Set<Class<? extends Interceptor>> interceptorsClasses,
            Class<? extends ObjectFactory> objectFactoryType,
            Set<Class<?>> mapperClasses) {
        this.dataSourceProviderType = dataSourceProviderType;
        this.transactionFactoryType = transactionFactoryType;
        this.aliases = aliases;
        this.handlers = handlers;
        this.interceptorsClasses = interceptorsClasses;
        this.objectFactoryType = objectFactoryType;
        this.mapperClasses = mapperClasses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        // needed binding
        this.bind(DataSource.class).toProvider(this.dataSourceProviderType);
        this.bind(TransactionFactory.class).to(this.transactionFactoryType).in(Scopes.SINGLETON);
        this.bind(Environment.class).toProvider(EnvironmentProvider.class).in(Scopes.SINGLETON);
        this.bind(Configuration.class).toProvider(ConfigurationProvider.class).in(Scopes.SINGLETON);
        this.bind(ObjectFactory.class).to(this.objectFactoryType).in(Scopes.SINGLETON);
        this.bind(SqlSessionFactory.class).toProvider(SqlSessionFactoryProvider.class);

        // optional bindings

        // aliases
        if (!this.aliases.isEmpty()) {
            this.bind(new TypeLiteral<Map<String, Class<?>>>(){}).annotatedWith(TypeAliases.class).toInstance(this.aliases);
        }

        // type handlers
        foreach(this.handlers).handle(new Each<Map.Entry<Class<?>,Class<? extends TypeHandler>>>() {

            private MapBinder<Class<?>, TypeHandler> handlerBinder;

            public void doHandle(Entry<Class<?>, Class<? extends TypeHandler>> alias) {
                if (this.handlerBinder == null) {
                    this.handlerBinder =
                        MapBinder.newMapBinder(binder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<TypeHandler>(){});
                }

                this.handlerBinder.addBinding(alias.getKey()).to(alias.getValue()).in(Scopes.SINGLETON);
            }
        });

        // interceptors plugin
        foreach(this.interceptorsClasses).handle(new Each<Class<? extends Interceptor>>() {

            private Multibinder<Interceptor> interceptorsMultibinder;

            public void doHandle(Class<? extends Interceptor> interceptorType) {
                if (this.interceptorsMultibinder == null) {
                    this.interceptorsMultibinder = Multibinder.newSetBinder(binder(), Interceptor.class);
                }
                this.interceptorsMultibinder.addBinding().to(interceptorType).in(Scopes.SINGLETON);
            }
        });

        // mappers
        if (!this.mapperClasses.isEmpty()) {
            this.bind(new TypeLiteral<Set<Class<?>>>() {}).annotatedWith(Mappers.class).toInstance(this.mapperClasses);
            foreach(this.mapperClasses).handle(new EachMapper(this.binder()));
        }
    }

    /**
     * The {@link MyBatisModule} Builder.
     *
     * By default the Builder uses the following settings:
     * <ul>
     * <li>DataSource Provider type: {@link UnpooledDataSourceProvider};</li>
     * <li>TransactionFactory type: org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;</li>
     * <li>ObjectFactory type: org.apache.ibatis.reflection.factory.ObjectFactory.</li>
     * </ul>
     */
    public static final class Builder {

        /**
         * The DataSource Provider class reference.
         */
        private Class<? extends Provider<DataSource>> dataSourceProviderType = UnpooledDataSourceProvider.class;

        /**
         * The TransactionFactory class reference.
         */
        private Class<? extends TransactionFactory> transactionFactoryType = JdbcTransactionFactory.class;

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
        private Class<? extends ObjectFactory> objectFactoryType = DefaultObjectFactory.class;

        /**
         * The user defined mapper classes.
         */
        private final Set<Class<?>> mapperClasses = new LinkedHashSet<Class<?>>();

        /**
         * Set the DataSource Provider type has to be bound.
         *
         * @param dataSourceProviderType the DataSource Provider type.
         * @return this {@code Builder} instance.
         */
        public Builder setDataSourceProviderType(Class<? extends Provider<DataSource>> dataSourceProviderType) {
            if (dataSourceProviderType == null) {
                throw new IllegalArgumentException("Parameter 'dataSourceProviderType' must be not null");
            }
            this.dataSourceProviderType = dataSourceProviderType;
            return this;
        }

        /**
         * Set the TransactionFactory type has to be bound.
         *
         * @param transactionFactoryType the TransactionFactory type.
         * @return this {@code Builder} instance.
         */
        public Builder setTransactionFactoryType(Class<? extends TransactionFactory> transactionFactoryType) {
            if (transactionFactoryType == null) {
                throw new IllegalArgumentException("Parameter 'transactionFactoryType' must be not null");
            }
            this.transactionFactoryType = transactionFactoryType;
            return this;
        }

        /**
         * Adding simple aliases means that every specified class will be bound
         * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
         *
         * @param types the specified types have to be bind.
         * @return this {@code Builder} instance.
         */
        public Builder addSimpleAliases(final Class<?>...types) {
            if (types != null) {
                return this.addSimpleAliases(Arrays.asList(types));
            }
            return this;
        }

        /**
         * Adding simple aliases means that every specified class will be bound
         * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
         *
         * @param types the specified types have to be bind.
         * @return this {@code Builder} instance.
         */
        public Builder addSimpleAliases(final Collection<Class<?>> types) {
            foreach(types).handle(new Each<Class<?>>() {
                public void doHandle(Class<?> clazz) {
                    addAlias(clazz.getSimpleName(), clazz);
                }
            });
            return this;
        }

        /**
         * Adds all Classes in the given package as a simple alias.
         * Adding simple aliases means that every specified class will be bound
         * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
         *
         * @param packageName the specified package to search for classes to alias.
         * @return this {@code Builder} instance.
         */
        public Builder addSimpleAliases(final String packageName) {
            return this.addSimpleAliases(getClasses(packageName));
        }

        /**
         * Adds all Classes in the given package as a simple alias.
         * Adding simple aliases means that every specified class will be bound
         * using the simple class name, i.e.  {@code com.acme.Foo} becomes {@code Foo}.
         *
         * @param packageName the specified package to search for classes to alias.
         * @param test a test to run against the objects found in the specified package.
         * @return this {@code Builder} instance.
         */
        public Builder addSimpleAliases(final String packageName, final ResolverUtil.Test test) {
            return this.addSimpleAliases(getClasses(test, packageName));
        }

        /**
         * Add a user defined binding.
         *
         * @param alias the string type alias
         * @param clazz the type has to be bound.
         */
        public Builder addAlias(final String alias, final Class<?> clazz) {
            this.aliases.put(alias, clazz);
            return this;
        }

        /**
         * Add a user defined Type Handler letting google-guice creating it.
         *
         * @param type the specified type has to be handled.
         * @param handler the handler type.
         * @return this {@code Builder} instance.
         */
        public Builder addTypeHandler(final Class<?> type, final Class<? extends TypeHandler> handler) {
            this.handlers.put(type, handler);
            return this;
        }

        /**
         * Adds the user defined myBatis interceptors plugins types, letting
         * google-guice creating them.
         *
         * @param interceptorsClasses the user defined MyBatis interceptors plugins types.
         * @return this {@code Builder} instance.
         * 
         */
        public Builder addInterceptorsClasses(Class<? extends Interceptor>...interceptorsClasses) {
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
         * @return this {@code Builder} instance.
         * 
         */
        public Builder addInterceptorsClasses(Collection<Class<? extends Interceptor>> interceptorsClasses) {
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
         * @return this {@code Builder} instance.
         * 
         */
        public Builder addInterceptorsClasses(String packageName) {
            if (packageName == null) {
                throw new IllegalArgumentException("Parameter 'packageName' must be not null");
            }
            return this.addInterceptorsClasses(new ResolverUtil<Interceptor>()
                    .find(new ResolverUtil.IsA(Interceptor.class), packageName)
                    .getClasses());
        }

        /**
         * Sets the ObjectFactory class.
         *
         * @param objectFactoryType the ObjectFactory type.
         * @return this {@code Builder} instance.
         */
        public Builder setObjectFactoryType(Class<? extends ObjectFactory> objectFactoryType) {
            if (objectFactoryType == null) {
                throw new IllegalArgumentException("Parameter 'objectFactoryType' must be not null");
            }
            this.objectFactoryType = objectFactoryType;
            return this;
        }

        /**
         * Adds the user defined mapper classes.
         *
         * @param mapperClasses the user defined mapper classes.
         * @return this {@code Builder} instance.
         * 
         */
        public Builder addMapperClasses(Class<?>...mapperClasses) {
            if (mapperClasses != null) {
                return this.addMapperClasses(Arrays.asList(mapperClasses));
            }
            return this;
        }

        /**
         * Adds the user defined mapper classes.
         *
         * @param mapperClasses the user defined mapper classes.
         * @return this {@code Builder} instance.
         * 
         */
        public Builder addMapperClasses(Collection<Class<?>> mapperClasses) {
            if (mapperClasses != null) {
                this.mapperClasses.addAll(mapperClasses);
            }
            return this;
        }

        /**
         * Adds the user defined mapper classes.
         *
         * @param packageName the specified package to search for mappers to add.
         * @return this {@code Builder} instance.
         * 
         */
        public Builder addMapperClasses(final String packageName) {
            return this.addMapperClasses(getClasses(packageName));
        }

        /**
         * Adds the user defined mapper classes.
         *
         * @param packageName the specified package to search for mappers to add.
         * @param test a test to run against the objects found in the specified package.
         * @return this {@code Builder} instance.
         * 
         */
        public Builder addMapperClasses(final String packageName, final ResolverUtil.Test test) {
            return this.addMapperClasses(getClasses(test, packageName));
        }

        /**
         * Create a new {@link MyBatisModule} instance based on this {@link Builder}
         * instance configuration.
         *
         * @return a new {@link MyBatisModule} instance.
         */
        public Module create() {
            return new MyBatisModule(this.dataSourceProviderType,
                    this.transactionFactoryType,
                    this.aliases,
                    this.handlers,
                    this.interceptorsClasses,
                    this.objectFactoryType,
                    this.mapperClasses);
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

}
