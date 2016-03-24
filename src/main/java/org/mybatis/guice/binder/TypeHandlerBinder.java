/**
 *    Copyright 2009-2015 the original author or authors.
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
package org.mybatis.guice.binder;

import org.apache.ibatis.type.TypeHandler;

import com.google.inject.TypeLiteral;

/**
 * Bind the given {@code TypeHandler} to an already defined type.
 */
public interface TypeHandlerBinder<T> {

    /**
     * Bind the given {@code TypeHandler} to an already defined type.
     *
     * @param handler The {@code TypeHandler} has to be bound
     */
    void with(Class<? extends TypeHandler<? extends T>> handler);
    
    /**
     * Bind the given {@code TypeHandler} to an already defined type.
     *
     * @param handler The {@code TypeHandler} has to be bound
     */
    void with(TypeLiteral<? extends TypeHandler<? extends T>> handler);

}
