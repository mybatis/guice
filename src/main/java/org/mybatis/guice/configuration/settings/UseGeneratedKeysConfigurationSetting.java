package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class UseGeneratedKeysConfigurationSetting implements ConfigurationSetting {

	private final boolean useGeneratedKeys;
	
	public UseGeneratedKeysConfigurationSetting(final boolean useGeneratedKeys) {
		this.useGeneratedKeys = useGeneratedKeys;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setUseGeneratedKeys(useGeneratedKeys);
	}

}
