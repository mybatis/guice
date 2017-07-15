package org.mybatis.guice.configuration.settings;

import javax.inject.Provider;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

public class TypeHandlerConfigurationSettingProvider implements Provider<ConfigurationSetting> {
  @Inject private Injector injector;

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
