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

import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;

/**
 *
 * @version $Id$
 */
abstract class AbstractMyBatisModule extends AbstractModule {

    /**
     * The user defined mapper classes.
     */
    private final Set<Class<?>> mapperClasses = new HashSet<Class<?>>();

    private final Class<? extends Provider<SqlSessionFactory>> sqlSessionFactoryProviderType;

    public AbstractMyBatisModule(Class<? extends Provider<SqlSessionFactory>> sqlSessionFactoryProviderType) {
        this.sqlSessionFactoryProviderType = sqlSessionFactoryProviderType;
    }

    /**
     * Adds the user defined mapper classes.
     *
     * @param mapperClasses the user defined mapper classes.
     * @return this {@code SqlSessionFactoryModule} instance.
     * 
     */
    public AbstractMyBatisModule addMapperClasses(Class<?>...mapperClasses) {
        if (mapperClasses == null || mapperClasses.length == 0) {
            return this;
        }

        for (Class<?> mapperClass : mapperClasses) {
            this.mapperClasses.add(mapperClass);
        }
        return this;
    }

    /**
     * Returns the set mapper classes.
     *
     * @return the set mapper classes.
     */
    protected Set<Class<?>> getMapperClasses() {
        return this.mapperClasses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        // sql session factory provider
        this.bind(SqlSessionFactory.class).toProvider(this.sqlSessionFactoryProviderType);
        // sql session manager
        this.bind(SqlSessionManager.class).toProvider(SqlSessionManagerProvider.class);

        // mappers
        if (!this.mapperClasses.isEmpty()) {
            for (Class<?> mapperType : this.mapperClasses) {
                bindMapperProvider(this.binder(), mapperType);
            }
        }

        // transactional interceptor
        TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
        this.binder().requestInjection(interceptor);
        this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), interceptor);
    }

    private static <T> void bindMapperProvider(Binder binder, Class<T> mapperType) {
        binder.bind(mapperType).toProvider(new MapperProvider<T>(mapperType)).in(Scopes.SINGLETON);
    }

}
