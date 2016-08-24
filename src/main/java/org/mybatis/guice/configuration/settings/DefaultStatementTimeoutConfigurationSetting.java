package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class DefaultStatementTimeoutConfigurationSetting implements ConfigurationSetting  {
	
	private final Integer defaultStatementTimeout;
	
	public DefaultStatementTimeoutConfigurationSetting(Integer defaultStatementTimeout) {
		this.defaultStatementTimeout = defaultStatementTimeout;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setDefaultStatementTimeout(defaultStatementTimeout);
	}

}
