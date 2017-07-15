package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;

public class AliasConfigurationSetting implements ConfigurationSetting {

  private final String alias;
  private final Class<?> clazz;
  public AliasConfigurationSetting(final String alias, final Class<?> clazz){
    this.alias = alias;
    this.clazz = clazz;
  }
  
  @Override
  public void applyConfigurationSetting(Configuration configuration) {
    configuration.getTypeAliasRegistry().registerAlias(alias, clazz);
  }

}
