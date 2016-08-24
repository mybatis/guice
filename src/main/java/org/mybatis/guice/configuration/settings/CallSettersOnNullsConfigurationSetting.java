package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class CallSettersOnNullsConfigurationSetting implements ConfigurationSetting  {

	private final boolean callSettersOnNulls;
	
	public CallSettersOnNullsConfigurationSetting(final boolean callSettersOnNulls){
		this.callSettersOnNulls = callSettersOnNulls;
	}
	
	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setCallSettersOnNulls(callSettersOnNulls);
	}
}
