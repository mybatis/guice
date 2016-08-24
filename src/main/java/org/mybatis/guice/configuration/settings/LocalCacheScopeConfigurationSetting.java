package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.LocalCacheScope;

public class LocalCacheScopeConfigurationSetting implements ConfigurationSetting {

	private final LocalCacheScope localCacheScope;
	
	public LocalCacheScopeConfigurationSetting(final LocalCacheScope localCacheScope){
		this.localCacheScope = localCacheScope;
	}
	
	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setLocalCacheScope(localCacheScope);
	}

}
