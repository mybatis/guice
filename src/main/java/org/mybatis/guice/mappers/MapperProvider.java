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
package org.mybatis.guice.mappers;

import org.apache.ibatis.session.SqlSessionManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * A generic MyBatis mapper provider.
 *
 * @version $Id$
 */
final class MapperProvider<T> implements Provider<T> {

    private final Class<T> mapperType;

    @Inject
    private SqlSessionManager sqlSessionManager;

    public MapperProvider(Class<T> mapperType) {
        this.mapperType = mapperType;
    }

    public void setSqlSessionManager(SqlSessionManager sqlSessionManager) {
        this.sqlSessionManager = sqlSessionManager;
    }

    public T get() {
        return this.sqlSessionManager.getMapper(this.mapperType);
    }

}
