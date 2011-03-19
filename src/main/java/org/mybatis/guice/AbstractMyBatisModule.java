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

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;
import static com.google.inject.util.Providers.guicify;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;
import org.mybatis.guice.mappers.MapperProvider;
import org.mybatis.guice.session.SqlSessionManagerProvider;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Scopes;

/**
 *
 * @version $Id$
 */
abstract class AbstractMyBatisModule extends AbstractModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void configure() {
        // sql session manager
        binder().bind(SqlSessionManager.class).toProvider(SqlSessionManagerProvider.class).in(Scopes.SINGLETON);
        binder().bind(SqlSession.class).to(SqlSessionManager.class).in(Scopes.SINGLETON);

        // transactional interceptor
        TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
        binder().requestInjection(interceptor);
        binder().bindInterceptor(any(), annotatedWith(Transactional.class), interceptor);

        internalConfigure();
    }

    /**
     * 
     * @param <T>
     * @param mapperType
     */
    final <T> void bindMapper(Class<T> mapperType) {
        binder().bind(mapperType).toProvider(guicify(new MapperProvider<T>(mapperType))).in(Scopes.SINGLETON);
    }

    /**
     * Configures a {@link Binder} via the exposed methods.
     */
    abstract void internalConfigure();

    /**
     * 
     */
    protected abstract void initialize();

}
