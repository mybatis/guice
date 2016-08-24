package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class AggressiveLazyLoadingConfigurationSetting implements ConfigurationSetting {

	private final boolean aggressiveLazyLoading;
	
	public AggressiveLazyLoadingConfigurationSetting(final boolean aggressiveLazyLoading) {
		this.aggressiveLazyLoading = aggressiveLazyLoading;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setAggressiveLazyLoading(aggressiveLazyLoading);
	}
}
