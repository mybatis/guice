/**
 *    Copyright 2009-2016 the original author or authors.
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
package org.mybatis.guice.session;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Builds the SqlSessionFactory ant let google-guice injects his components.
 */
@Singleton
public final class SqlSessionFactoryProvider implements Provider<SqlSessionFactory> {

    /**
     * The SqlSessionFactory reference.
     */
    private SqlSessionFactory sqlSessionFactory;

    /**
     * @since 1.0.1
     */
    public SqlSessionFactoryProvider() {
        // do nothing
    }

    /**
     * Creates a new SqlSessionFactory from the specified configuration.
     *
     * @param configuration the specified configration.
     */
    @Deprecated
    public SqlSessionFactoryProvider(final Configuration configuration) {
        // do nothing
    }

    /**
     * Creates a new SqlSessionFactory from the specified configuration.
     *
     * @param configuration the specified configuration.
     * @since 1.0.1
     */
    @Inject
    public void createNewSqlSessionFactory(final Configuration configuration) {
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SqlSessionFactory get() {
        return sqlSessionFactory;
    }

}
