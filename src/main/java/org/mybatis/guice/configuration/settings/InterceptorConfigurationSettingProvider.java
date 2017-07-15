package org.mybatis.guice.configuration.settings;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.inject.Provider;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;

public final class InterceptorConfigurationSettingProvider implements Provider<ConfigurationSetting> {

  @Inject
  private Injector injector;

  private final Class<? extends Interceptor> interceptorClass;
  public InterceptorConfigurationSettingProvider(final Class<? extends Interceptor> interceptorClass) {
    this.interceptorClass = interceptorClass;
  }

  @Override
  public ConfigurationSetting get() {
    final Interceptor interceptor = injector.getInstance(interceptorClass);
    return new ConfigurationSetting() {
      @Override
      public void applyConfigurationSetting(Configuration configuration) {
        configuration.addInterceptor(interceptor);
      }
    };
  }

}
