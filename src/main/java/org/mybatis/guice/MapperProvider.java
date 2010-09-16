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

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

/**
 * 
 *
 * @version $Id$
 */
@Singleton
final class MapperProvider<T> implements Provider<T> {

    public static void bind(Binder binder, Set<Class<?>> mapperTypes) {
        if (mapperTypes.isEmpty()) {
            return;
        }

        for (Class<?> mapperType : mapperTypes) {
            bind(binder, mapperType);
        }
    }

    private static <T> void bind(Binder binder, Class<T> mapperType) {
        binder.bind(mapperType).toProvider(new MapperProvider<T>(mapperType)).in(Scopes.SINGLETON);
    }

    private final Class<T> mapperType;

    @Inject
    private SqlSessionManager sqlSessionManager;

    private MapperProvider(Class<T> mapperType) {
        this.mapperType = mapperType;
    }

    public void setSqlSessionManager(SqlSessionManager sqlSessionManager) {
        this.sqlSessionManager = sqlSessionManager;
    }

    public T get() {
        return this.sqlSessionManager.getMapper(this.mapperType);
    }

}
