package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;

public class DefaultExecutorTypeConfigurationSetting implements ConfigurationSetting  {

	private final ExecutorType executorType;
	
	public DefaultExecutorTypeConfigurationSetting(final ExecutorType executorType) {
		this.executorType = executorType;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setDefaultExecutorType(executorType);
	}
}
