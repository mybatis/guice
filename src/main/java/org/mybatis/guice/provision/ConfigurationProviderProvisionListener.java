/*
 *    Copyright 2009-2025 the original author or authors.
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
package org.mybatis.guice.provision;

import com.google.inject.Binder;
import com.google.inject.MembersInjector;
import com.google.inject.spi.ProvisionListener;

import jakarta.inject.Provider;

import org.mybatis.guice.configuration.ConfigurationSettingListener;
import org.mybatis.guice.configuration.settings.ConfigurationSetting;
import org.mybatis.guice.configuration.settings.MapperConfigurationSetting;

public final class ConfigurationProviderProvisionListener implements ProvisionListener {

  private final ConfigurationProviderProvisionAction action;

  ConfigurationProviderProvisionListener(ConfigurationProviderProvisionAction action) {
    this.action = action;
  }

  @Override
  public <T> void onProvision(ProvisionInvocation<T> provision) {
    ConfigurationSettingListener configurationSettingListener = (ConfigurationSettingListener) provision.provision();
    this.action.perform(configurationSettingListener);
  }

  public static <P extends Provider<? extends ConfigurationSetting>> ConfigurationProviderProvisionListener create(
      final P configurationSettingProvider, final Binder binder) {
    @SuppressWarnings("unchecked")
    final MembersInjector<P> membersInjector = (MembersInjector<P>) binder
        .getMembersInjector(configurationSettingProvider.getClass());
    return new ConfigurationProviderProvisionListener(configurationSettingListener -> {
      membersInjector.injectMembers(configurationSettingProvider);
      configurationSettingListener.addConfigurationSetting(configurationSettingProvider.get());
    });
  }

  public static ConfigurationProviderProvisionListener create(final ConfigurationSetting configurationSetting) {
    return new ConfigurationProviderProvisionListener(
        configurationSettingListener -> configurationSettingListener.addConfigurationSetting(configurationSetting));
  }

  public static ConfigurationProviderProvisionListener create(
      final MapperConfigurationSetting mapperConfigurationSetting) {
    return new ConfigurationProviderProvisionListener(configurationSettingListener -> configurationSettingListener
        .addMapperConfigurationSetting(mapperConfigurationSetting));
  }

  private interface ConfigurationProviderProvisionAction {
    void perform(ConfigurationSettingListener configurationSettingListener);
  }
}
