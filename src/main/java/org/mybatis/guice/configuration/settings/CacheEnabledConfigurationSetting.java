package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class CacheEnabledConfigurationSetting implements ConfigurationSetting  {

	private final boolean useCacheEnabled;
	
	public CacheEnabledConfigurationSetting(final boolean useCacheEnabled) {
		this.useCacheEnabled = useCacheEnabled;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setCacheEnabled(useCacheEnabled);
	}
}
