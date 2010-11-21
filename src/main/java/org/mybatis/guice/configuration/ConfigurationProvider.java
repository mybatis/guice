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
package org.mybatis.guice.configuration;

import static org.mybatis.guice.iterables.Iterables.foreach;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.guice.iterables.Each;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Provides the myBatis Configuration.
 *
 * @version $Id$
 */
@Singleton
public final class ConfigurationProvider implements Provider<Configuration> {

    /**
     * The myBatis Configuration reference.
     */
    @Inject
    private Environment environment;

    @Inject(optional = true)
    @Named("mybatis.configuration.lazyLoadingEnabled")
    private boolean lazyLoadingEnabled = false;

    @Inject(optional = true)
    @Named("mybatis.configuration.aggressiveLazyLoading")
    private boolean aggressiveLazyLoading = true;

    @Inject(optional = true)
    @Named("mybatis.configuration.multipleResultSetsEnabled")
    private boolean multipleResultSetsEnabled = true;

    @Inject(optional = true)
    @Named("mybatis.configuration.useGeneratedKeys")
    private boolean useGeneratedKeys = false;

    @Inject(optional = true)
    @Named("mybatis.configuration.useColumnLabel")
    private boolean useColumnLabel = true;

    @Inject(optional = true)
    @Named("mybatis.configuration.cacheEnabled")
    private boolean cacheEnabled = true;

    @Inject(optional = true)
    @Named("mybatis.configuration.defaultExecutorType")
    private ExecutorType defaultExecutorType = ExecutorType.SIMPLE;

    @Inject(optional = true)
    @Named("mybatis.configuration.autoMappingBehavior")
    private AutoMappingBehavior autoMappingBehavior = AutoMappingBehavior.PARTIAL;

    @Inject
    private ObjectFactory objectFactory;

    @Inject(optional = true)
    @TypeAliases
    private Map<String, Class<?>> typeAliases;

    @Inject(optional = true)
    private Map<Class<?>, TypeHandler> typeHandlers = Collections.emptyMap();

    @Inject(optional = true)
    @Mappers
    private Set<Class<?>> mapperClasses = Collections.emptySet();

    @Inject(optional = true)
    private Set<Interceptor> plugins = Collections.emptySet();

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
        this.lazyLoadingEnabled = lazyLoadingEnabled;
    }

    /**
     * Adds the user defined type aliases to the myBatis Configuration.
     *
     * @param typeAliases the user defined type aliases.
     */
    public void setTypeAliases(Map<String, Class<?>> typeAliases) {
        this.typeAliases = typeAliases;
    }

    /**
     * Adds the user defined type handlers to the myBatis Configuration.
     *
     * @param typeHandlers the user defined type handlers.
     */
    @Inject(optional = true)
    public void registerTypeHandlers(final Map<Class<?>, TypeHandler> typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    /**
     * Adds the user defined Mapper classes to the myBatis Configuration.
     *
     * @param mapperClasses the user defined Mapper classes.
     */
    public void setMapperClasses(Set<Class<?>> mapperClasses) {
        this.mapperClasses = mapperClasses;
    }

    /**
     * Adds the user defined ObjectFactory to the myBatis Configuration.
     *
     * @param objectFactory
     */
    public void setObjectFactory(final ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    /**
     * Registers the user defined plugins interceptors to the
     * myBatis Configuration.
     *
     * @param plugins the user defined plugins interceptors.
     */
    public void setPlugins(Set<Interceptor> plugins) {
        this.plugins = plugins;
    }

    /**
     * {@inheritDoc}
     */
    public Configuration get() {
        final Configuration configuration = new Configuration(this.environment);
        configuration.setLazyLoadingEnabled(this.lazyLoadingEnabled);
        configuration.setAggressiveLazyLoading(this.aggressiveLazyLoading);
        configuration.setMultipleResultSetsEnabled(this.multipleResultSetsEnabled);
        configuration.setUseGeneratedKeys(this.useGeneratedKeys);
        configuration.setUseColumnLabel(this.useColumnLabel);
        configuration.setCacheEnabled(this.cacheEnabled);
        configuration.setDefaultExecutorType(this.defaultExecutorType);
        configuration.setAutoMappingBehavior(this.autoMappingBehavior);
        configuration.setObjectFactory(this.objectFactory);

        try {
            foreach(this.typeAliases).handle(new Each<Map.Entry<String,Class<?>>>() {
                public void doHandle(Entry<String, Class<?>> alias) {
                    configuration.getTypeAliasRegistry().registerAlias(alias.getKey(), alias.getValue());
                }
            });
            foreach(this.typeHandlers).handle(new Each<Map.Entry<Class<?>,TypeHandler>>() {
                public void doHandle(Entry<Class<?>, TypeHandler> typeHandler) {
                    configuration.getTypeHandlerRegistry().register(typeHandler.getKey(), typeHandler.getValue());
                }
            });
            foreach(this.mapperClasses).handle(new Each<Class<?>>() {
                public void doHandle(Class<?> mapperClass) {
                    if (!configuration.hasMapper(mapperClass)) {
                        configuration.addMapper(mapperClass);
                    }
                }
            });
            foreach(this.plugins).handle(new Each<Interceptor>() {
                public void doHandle(Interceptor interceptor) {
                    configuration.addInterceptor(interceptor);
                }
            });
        } catch (Throwable cause) {
            throw new ProvisionException("An error occurred while building the org.apache.ibatis.session.Configuration", cause);
        } finally {
            ErrorContext.instance().reset();
        }

        return configuration;
    }

}
