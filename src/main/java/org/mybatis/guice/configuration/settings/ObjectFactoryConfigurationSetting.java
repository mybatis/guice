package org.mybatis.guice.configuration.settings;


import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class ObjectFactoryConfigurationSetting implements Provider<ConfigurationSetting> {

	@Inject
	private Injector injector;
	
	private final Class<? extends ObjectFactory> objectFactoryType;
	
	public ObjectFactoryConfigurationSetting(Class<? extends ObjectFactory> objectFactoryType) {
		this.objectFactoryType = objectFactoryType;
	}

	public void setInjector(final Injector injector){
		this.injector = injector;
	}

	@Override
	public ConfigurationSetting get() {
		final ObjectFactory objectFactory = injector.getInstance(objectFactoryType);
		return new ConfigurationSetting(){
			@Override
			public void applyConfigurationSetting(Configuration configuration) {
				configuration.setObjectFactory(objectFactory);
			}
		};
	}

}
