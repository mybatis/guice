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

import java.util.Map.Entry;

import org.apache.ibatis.type.TypeHandler;

import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

/**
 * 
 *
 * @version $Id$
 */
final class EachAlias extends AbstractBinderEach<Entry<Class<?>, Class<? extends TypeHandler>>> {

    private MapBinder<Class<?>, TypeHandler> handlerBinder;

    public EachAlias(final Binder binder) {
        super(binder);
    }

    public void ifOverNotEmptyIterator() {
        this.handlerBinder =
            MapBinder.newMapBinder(this.getBinder(), new TypeLiteral<Class<?>>(){}, new TypeLiteral<TypeHandler>(){});
    }

    public void doHandle(Entry<Class<?>, Class<? extends TypeHandler>> entry) {
        this.handlerBinder.addBinding(entry.getKey()).to(entry.getValue()).in(Scopes.SINGLETON);
    }

}
