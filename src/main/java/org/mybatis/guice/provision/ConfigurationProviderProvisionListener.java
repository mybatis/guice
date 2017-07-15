package org.mybatis.guice.provision;

import javax.inject.Provider;

import org.mybatis.guice.configuration.ConfigurationProvider;
import org.mybatis.guice.configuration.settings.ConfigurationSetting;

import com.google.inject.spi.ProvisionListener;
import com.google.inject.util.Providers;

public final class ConfigurationProviderProvisionListener implements ProvisionListener {

  private final Provider<? extends ConfigurationSetting> configurationSettingProvider;
  ConfigurationProviderProvisionListener(final Provider<? extends ConfigurationSetting> configurationSettingProvider){
    this.configurationSettingProvider = configurationSettingProvider;
  }
  
  @Override
  public <T> void onProvision(ProvisionInvocation<T> provision) {
    ConfigurationProvider configurationProvider = (ConfigurationProvider)provision.provision();
    configurationProvider.getInjector().injectMembers(configurationSettingProvider);
    configurationProvider.addConfigurationSetting(configurationSettingProvider.get());
  }

  public static ConfigurationProviderProvisionListener create(final Provider<? extends ConfigurationSetting> configurationSettingProvider) {
    return new ConfigurationProviderProvisionListener(configurationSettingProvider);
  }
  
  public static ConfigurationProviderProvisionListener create(final ConfigurationSetting configurationSetting) {
    return new ConfigurationProviderProvisionListener(Providers.of(configurationSetting));
  }
}
