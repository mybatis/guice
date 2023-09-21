/*
 *    Copyright 2009-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
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

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

public final class TypeHandlerConfigurationSettingProvider implements Provider<ConfigurationSetting> {
  @Inject
  private Injector injector;

  private final Key<? extends TypeHandler<?>> key;

  public TypeHandlerConfigurationSettingProvider(final Key<? extends TypeHandler<?>> key) {
    this.key = key;
  }

  @Override
  public ConfigurationSetting get() {
    final TypeHandler<?> handlerInstance = injector.getInstance(key);
    return new ConfigurationSetting() {
      @Override
      public void applyConfigurationSetting(Configuration configuration) {
        configuration.getTypeHandlerRegistry().register(handlerInstance);
      }
    };
  }

}
