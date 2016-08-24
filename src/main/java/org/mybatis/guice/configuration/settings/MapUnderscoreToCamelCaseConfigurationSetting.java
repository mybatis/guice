package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class MapUnderscoreToCamelCaseConfigurationSetting implements ConfigurationSetting {

	private final boolean mapUnderscoreToCamelCase;
	
	public MapUnderscoreToCamelCaseConfigurationSetting(final boolean mapUnderscoreToCamelCase) {
		this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
	}

}
