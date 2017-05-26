/**
 *    Copyright 2009-2017 the original author or authors.
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
package org.mybatis.guice.datasource.helper;

import com.google.inject.Injector;
import com.google.inject.Key;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.inject.name.Names.named;

final class KeyResolver implements Provider<String> {

  private final Key<String> key;

  private final String defaultValue;

  private final String toString;

  @Inject
  private Injector injector;

  public KeyResolver(final String key, final String defaultValue) {
    this.key = Key.get(String.class, named(key));
    this.defaultValue = defaultValue;
    toString = "${" + key + "}";
  }

  public void setInjector(Injector injector) {
    this.injector = injector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String get() {
    try {
      return injector.getInstance(key);
    } catch (Throwable e) {
      if (defaultValue != null) {
        return defaultValue;
      }
      return toString;
    }
  }

  @Override
  public String toString() {
    return toString;
  }

}
