/*
 *    Copyright 2009-2018 the original author or authors.
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
package org.mybatis.guice.configuration.settings;

import com.google.inject.Injector;
import com.google.inject.Key;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

public final class JavaTypeAndHandlerConfigurationSettingProvider implements Provider<ConfigurationSetting> {
  @Inject
  private Injector injector;

  private final Wrapper<?> wrapper;

  private JavaTypeAndHandlerConfigurationSettingProvider(final Wrapper<?> wrapper) {
    this.wrapper = wrapper;
  }

  @Override
  public ConfigurationSetting get() {
    return wrapper.get(injector);
  }

  public static <T> JavaTypeAndHandlerConfigurationSettingProvider create(final Class<T> type,
      final Key<? extends TypeHandler<? extends T>> key) {
    return new JavaTypeAndHandlerConfigurationSettingProvider(new Wrapper<T>(type, key));
  }

  private static class Wrapper<T> {
    private final Class<T> type;
    private final Key<? extends TypeHandler<? extends T>> key;

    private Wrapper(final Class<T> type, final Key<? extends TypeHandler<? extends T>> key) {
      this.type = type;
      this.key = key;
    }

    ConfigurationSetting get(Injector injector) {
      final TypeHandler<? extends T> handlerInstance = injector.getInstance(key);
      return new ConfigurationSetting() {
        @Override
        public void applyConfigurationSetting(Configuration configuration) {
          configuration.getTypeHandlerRegistry().register(type, handlerInstance);
        }
      };
    }
  }
}
