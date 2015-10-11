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
package org.mybatis.guice.type;

import java.lang.reflect.Constructor;

import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.inject.Provider;

/**
 * A generic MyBatis type provider.
 *
 * @version $Id$
 */
public final class TypeHandlerProvider<TH extends TypeHandler<? extends T>, T> implements Provider<TH> {
    private final Class<TH> typeHandlerType;
    private final Class<T> handledType;
    @Inject
    private Injector injector;

    public TypeHandlerProvider(Class<TH> typeHandlerType, Class<T> handledType) {
        this.typeHandlerType = typeHandlerType;
        this.handledType = handledType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TH get() {
        TH instance = null;
        if (handledType != null) {
            try {
                Constructor<?> c = typeHandlerType.getConstructor(Class.class);
                instance = (TH) c.newInstance(handledType);
                injector.injectMembers(instance);
            } catch (NoSuchMethodException ignored) {
                // ignored
            } catch (Exception e) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerType, e);
            }
        }
        if (instance == null) {
            try {
                instance = (TH) typeHandlerType.newInstance();
                injector.injectMembers(instance);
            } catch (Exception e) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerType, e);
            }
        }
        return instance;
    }
}
