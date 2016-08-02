package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;

public class AutoMappingBehaviorConfigurationSetting implements ConfigurationSetting {

	private final AutoMappingBehavior autoMappingBehavior;
	public AutoMappingBehaviorConfigurationSetting(final AutoMappingBehavior autoMappingBehavior) {
		this.autoMappingBehavior = autoMappingBehavior;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setAutoMappingBehavior(autoMappingBehavior);
	}
}
