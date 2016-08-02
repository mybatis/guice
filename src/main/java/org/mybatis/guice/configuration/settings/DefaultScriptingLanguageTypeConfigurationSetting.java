package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

public class DefaultScriptingLanguageTypeConfigurationSetting implements ConfigurationSetting  {

	private final Class<? extends LanguageDriver> defaultScriptingLanguageType;
	
	public DefaultScriptingLanguageTypeConfigurationSetting(
			Class<? extends LanguageDriver> defaultScriptingLanguageType) {
		this.defaultScriptingLanguageType = defaultScriptingLanguageType;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setDefaultScriptingLanguage(defaultScriptingLanguageType);
	}

}
