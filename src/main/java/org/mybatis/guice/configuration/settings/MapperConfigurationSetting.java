package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public final class MapperConfigurationSetting implements ConfigurationSetting {

  private final Class<?> mapperClass;
  
  public MapperConfigurationSetting(Class<?> mapperClass) {
    this.mapperClass = mapperClass;
  }

  @Override
  public void applyConfigurationSetting(Configuration configuration) {
    if (!configuration.hasMapper(mapperClass)) {
      configuration.addMapper(mapperClass);
    }
  }

}
