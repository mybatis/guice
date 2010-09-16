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

import org.apache.ibatis.session.SqlSessionManager;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;

import com.google.inject.AbstractModule;
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
        this.bind(SqlSessionManager.class).toProvider(SqlSessionManagerProvider.class);

        // transactional interceptor
        TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
        this.binder().requestInjection(interceptor);
        this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), interceptor);
    }

}
