package org.mybatis.guice.configuration.settings;


import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class ObjectWrapperFactoryConfigurationSetting implements Provider<ConfigurationSetting> {

	@Inject
	private Injector injector;
	private final Class<? extends ObjectWrapperFactory> objectWrapperFactoryType;
	
	public ObjectWrapperFactoryConfigurationSetting(Class<? extends ObjectWrapperFactory> objectWrapperFactoryType) {
		this.objectWrapperFactoryType = objectWrapperFactoryType;
	}

	public void setInjector(final Injector injector){
		this.injector = injector;
	}

	@Override
	public ConfigurationSetting get() {
		final ObjectWrapperFactory objectWrapperFactory = injector.getInstance(objectWrapperFactoryType);
		return new ConfigurationSetting(){
			@Override
			public void applyConfigurationSetting(Configuration configuration) {
				configuration.setObjectWrapperFactory(objectWrapperFactory);
			}
		};
	}
}
