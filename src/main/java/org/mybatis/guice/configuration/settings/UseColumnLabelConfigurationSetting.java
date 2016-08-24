package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class UseColumnLabelConfigurationSetting implements ConfigurationSetting {

	private final boolean useColumnLabel;
	
	public UseColumnLabelConfigurationSetting(final boolean useColumnLabel) {
		this.useColumnLabel = useColumnLabel;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setUseColumnLabel(useColumnLabel);
	}

}
