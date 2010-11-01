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

import java.util.Set;

import org.apache.ibatis.session.SqlSessionManager;
import org.mybatis.guice.mappers.MapperProvider;
import org.mybatis.guice.session.SqlSessionManagerProvider;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;

/**
 *
 * @version $Id$
 */
abstract class AbstractMyBatisModule extends AbstractModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        // sql session manager
        this.bind(SqlSessionManager.class).toProvider(SqlSessionManagerProvider.class).asEagerSingleton();

        // transactional interceptor
        TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
        this.requestInjection(interceptor);
        this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), interceptor);
    }

    /**
     * Binds the input set of MyBatis mapper types in the relative
     * {@link MapperProvider} provider.
     *
     * @param binder the binder instance where configure the binding.
     * @param mapperTypes the MyBatis mappers types have to be bind.
     */
    protected static void bindMappers(Binder binder, Set<Class<?>> mapperTypes) {
        if (mapperTypes.isEmpty()) {
            return;
        }

        for (Class<?> mapperType : mapperTypes) {
            bindMapper(binder, mapperType);
        }
    }

    private static <T> void bindMapper(Binder binder, Class<T> mapperType) {
        binder.bind(mapperType).toProvider(new MapperProvider<T>(mapperType)).in(Scopes.SINGLETON);
    }

}
