package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class LazyLoadingEnabledConfigurationSetting implements ConfigurationSetting  {

	private final boolean lazyLoadingEnabled;
	
	public LazyLoadingEnabledConfigurationSetting(final boolean lazyLoadingEnabled) {
		this.lazyLoadingEnabled = lazyLoadingEnabled;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setLazyLoadingEnabled(lazyLoadingEnabled);
	}

}
