/**
 *    Copyright 2009-2020 the original author or authors.
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

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

import java.lang.reflect.Constructor;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

/**
 * A generic MyBatis type provider.
 */
public final class TypeHandlerProvider<TH extends TypeHandler<? extends T>, T> implements Provider<TH> {
  private final TypeLiteral<TH> typeHandlerTypeLiteral;
  private final Class<T> handledType;
  @Inject
  private Injector injector;

  public TypeHandlerProvider(Class<TH> typeHandlerType, Class<T> handledType) {
    this.typeHandlerTypeLiteral = TypeLiteral.get(typeHandlerType);
    this.handledType = handledType;
  }

  public TypeHandlerProvider(TypeLiteral<TH> typeHandlerType, Class<T> handledType) {
    this.typeHandlerTypeLiteral = typeHandlerType;
    this.handledType = handledType;
  }

  TypeHandlerProvider(Injector injector, Class<TH> typeHandlerType, Class<T> handledType) {
    this(typeHandlerType, handledType);
    this.injector = injector;
  }

  TypeHandlerProvider(Injector injector, TypeLiteral<TH> typeHandlerType, Class<T> handledType) {
    this(typeHandlerType, handledType);
    this.injector = injector;
  }

  @Override
  @SuppressWarnings("unchecked")
  public TH get() {
    TH instance = null;
    if (handledType != null) {
      try {
        Constructor<?> c = typeHandlerTypeLiteral.getRawType().getConstructor(Class.class);
        instance = (TH) c.newInstance(handledType);
        injector.injectMembers(instance);
      } catch (NoSuchMethodException ignored) {
        // ignored
      } catch (Exception e) {
        throw new TypeException("Failed invoking constructor for handler " + typeHandlerTypeLiteral.getType(), e);
      }
    }
    if (instance == null) {
      try {
        instance = (TH) typeHandlerTypeLiteral.getRawType().newInstance();
        injector.injectMembers(instance);
      } catch (Exception e) {
        throw new TypeException("Failed invoking constructor for handler " + typeHandlerTypeLiteral.getType(), e);
      }
    }
    return instance;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.typeHandlerTypeLiteral, this.handledType);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    TypeHandlerProvider other = (TypeHandlerProvider) obj;
    return Objects.equals(this.typeHandlerTypeLiteral, other.typeHandlerTypeLiteral)
        && Objects.equals(this.handledType, other.handledType);
  }
}
