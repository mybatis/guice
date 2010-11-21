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
package org.mybatis.guice.iterables;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 *
 * @version $Id$
 */
public final class Iterables {

    /**
     * This class cannot be instantiated
     */
    private Iterables() {
        // do nothing
    }

    public static <K, V> ItemHandler<Entry<K, V>> foreach(Map<K, V> map) {
        if (map == null) {
            return foreachEmpty();
        }
        return foreach(map.entrySet());
    }

    public static <T> ItemHandler<T> foreach(Iterable<T> iterable) {
        if (iterable == null) {
            return foreachEmpty();
        }
        return new ItemHandler<T>(iterable);
    }

    @SuppressWarnings("unchecked")
    private static <T> ItemHandler<T> foreachEmpty() {
        return new ItemHandler<T>(Collections.EMPTY_LIST);
    }

}
