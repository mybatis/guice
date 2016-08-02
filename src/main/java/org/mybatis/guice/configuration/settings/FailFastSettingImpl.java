package org.mybatis.guice.configuration.settings;

import java.lang.annotation.Annotation;

public class FailFastSettingImpl implements FailFastSetting {

	@Override
	public Class<? extends Annotation> annotationType() {
		return FailFastSetting.class;
	}
	
	public static FailFastSetting get(){
		return new FailFastSettingImpl();
	}
}
