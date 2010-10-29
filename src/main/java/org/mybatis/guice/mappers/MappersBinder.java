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

import java.util.Set;

import com.google.inject.Binder;
import com.google.inject.Scopes;

/**
 * Utility class to facilitate the MyBatis mappers binding to relative
 * {@link MapperProvider} into Google Guice.
 *
 * @version $Id$
 */
public final class MappersBinder {

    /**
     * Binds the input set of MyBatis mapper types in the relative
     * {@link MapperProvider} provider.
     *
     * @param binder the binder instance where configure the binding.
     * @param mapperTypes the MyBatis mappers types have to be bind.
     */
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

    /**
     * This class can't be instantiated
     */
    private MappersBinder() {
        // do nothing
    }

}
