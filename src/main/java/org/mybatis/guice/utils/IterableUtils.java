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
package org.mybatis.guice.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 *
 * @version $Id$
 */
public final class IterableUtils {

    /**
     * This class cannot be instantiated
     */
    private IterableUtils() {
        // do nothing
    }

    public static <K, V> void iterate(Map<K, V> map, Each<Entry<K, V>> each) {
        if (map != null && !map.isEmpty()) {
            iterate(map.entrySet(), each);
        }
    }

    public static <T> void iterate(Iterable<T> iterable, Each<T> each) {
        if (iterable != null) {
            Iterator<T> iterator = iterable.iterator();
            boolean hasNext = iterator.hasNext();

            if (hasNext) {
                each.ifOverNotEmptyIterator();

                while (hasNext) {
                    each.doHandle(iterator.next());
                    hasNext = iterator.hasNext();
                }
            }
        }
    }

}
