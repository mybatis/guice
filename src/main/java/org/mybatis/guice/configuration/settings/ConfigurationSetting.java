package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public interface ConfigurationSetting {
	public void applyConfigurationSetting(Configuration configuration);
}
