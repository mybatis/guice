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
package org.mybatis.guice.provision;

import com.google.inject.spi.ProvisionListener;
import com.google.inject.util.Providers;

import javax.inject.Provider;

import org.mybatis.guice.configuration.ConfigurationProvider;
import org.mybatis.guice.configuration.settings.ConfigurationSetting;

public final class ConfigurationProviderProvisionListener implements ProvisionListener {

  private final Provider<? extends ConfigurationSetting> configurationSettingProvider;

  ConfigurationProviderProvisionListener(final Provider<? extends ConfigurationSetting> configurationSettingProvider) {
    this.configurationSettingProvider = configurationSettingProvider;
  }

  @Override
  public <T> void onProvision(ProvisionInvocation<T> provision) {
    ConfigurationProvider configurationProvider = (ConfigurationProvider) provision.provision();
    configurationProvider.addConfigurationSettingProvider(configurationSettingProvider);
  }

  public static ConfigurationProviderProvisionListener create(
      final Provider<? extends ConfigurationSetting> configurationSettingProvider) {
    return new ConfigurationProviderProvisionListener(configurationSettingProvider);
  }

  public static ConfigurationProviderProvisionListener create(final ConfigurationSetting configurationSetting) {
    return new ConfigurationProviderProvisionListener(Providers.of(configurationSetting));
  }
}
