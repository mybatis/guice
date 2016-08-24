package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class MultipleResultSetsEnabledConfigurationSetting implements ConfigurationSetting {
	
	private final boolean multipleResultSetsEnabled;

	public MultipleResultSetsEnabledConfigurationSetting(final boolean multipleResultSetsEnabled) {
		this.multipleResultSetsEnabled = multipleResultSetsEnabled;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setMultipleResultSetsEnabled(multipleResultSetsEnabled);
	}
}
