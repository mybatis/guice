/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.configuration;

import com.google.inject.ProvisionException;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.mybatis.guice.configuration.settings.ConfigurationSetting;
import org.mybatis.guice.configuration.settings.MapperConfigurationSetting;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Provides the myBatis Configuration.
 */
@Singleton
public class ConfigurationProvider implements Provider<Configuration>, ConfigurationSettingListener {

  /**
   * The myBatis Configuration reference.
   */
  private final Environment environment;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.lazyLoadingEnabled")
  private boolean lazyLoadingEnabled = false;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.aggressiveLazyLoading")
  private boolean aggressiveLazyLoading = true;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.multipleResultSetsEnabled")
  private boolean multipleResultSetsEnabled = true;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.useGeneratedKeys")
  private boolean useGeneratedKeys = false;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.useColumnLabel")
  private boolean useColumnLabel = true;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.cacheEnabled")
  private boolean cacheEnabled = true;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.defaultExecutorType")
  private ExecutorType defaultExecutorType = ExecutorType.SIMPLE;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.autoMappingBehavior")
  private AutoMappingBehavior autoMappingBehavior = AutoMappingBehavior.PARTIAL;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.callSettersOnNulls")
  private boolean callSettersOnNulls = false;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.defaultStatementTimeout")
  @Nullable
  private Integer defaultStatementTimeout;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.mapUnderscoreToCamelCase")
  private boolean mapUnderscoreToCamelCase = false;

  @com.google.inject.Inject(optional = true)
  @Named("mybatis.configuration.failFast")
  private boolean failFast = false;

  @com.google.inject.Inject(optional = true)
  private DatabaseIdProvider databaseIdProvider;

  @com.google.inject.Inject
  private DataSource dataSource;

  private Set<ConfigurationSetting> configurationSettings = new HashSet<>();
  private Set<MapperConfigurationSetting> mapperConfigurationSettings = new HashSet<>();

  /**
   * Instantiates a new configuration provider.
   *
   * @param environment
   *          the environment
   *
   * @since 1.0.1
   */
  @com.google.inject.Inject
  public ConfigurationProvider(final Environment environment) {
    this.environment = environment;
  }

  @Deprecated
  public void setEnvironment(Environment environment) {
    // do nothing
  }

  /**
   * Flag to check all statements are completed.
   *
   * @param failFast
   *          flag to check all statements are completed
   *
   * @since 1.0.1
   */
  public void setFailFast(boolean failFast) {
    this.failFast = failFast;
  }

  @Override
  public void addConfigurationSetting(ConfigurationSetting configurationSetting) {
    this.configurationSettings.add(configurationSetting);
  }

  @Override
  public void addMapperConfigurationSetting(MapperConfigurationSetting mapperConfigurationSetting) {
    this.mapperConfigurationSettings.add(mapperConfigurationSetting);
  }

  /**
   * New configuration.
   *
   * @param environment
   *          the environment
   *
   * @return new configuration
   */
  protected Configuration newConfiguration(Environment environment) {
    return new Configuration(environment);
  }

  @Override
  public Configuration get() {
    final Configuration configuration = newConfiguration(environment);
    configuration.setLazyLoadingEnabled(lazyLoadingEnabled);
    configuration.setAggressiveLazyLoading(aggressiveLazyLoading);
    configuration.setMultipleResultSetsEnabled(multipleResultSetsEnabled);
    configuration.setUseGeneratedKeys(useGeneratedKeys);
    configuration.setUseColumnLabel(useColumnLabel);
    configuration.setCacheEnabled(cacheEnabled);
    configuration.setDefaultExecutorType(defaultExecutorType);
    configuration.setAutoMappingBehavior(autoMappingBehavior);
    configuration.setCallSettersOnNulls(callSettersOnNulls);
    configuration.setDefaultStatementTimeout(defaultStatementTimeout);
    configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);

    for (ConfigurationSetting setting : configurationSettings) {
      setting.applyConfigurationSetting(configuration);
    }

    try {
      if (databaseIdProvider != null) {
        configuration.setDatabaseId(databaseIdProvider.getDatabaseId(dataSource));
      }

      for (MapperConfigurationSetting setting : mapperConfigurationSettings) {
        setting.applyConfigurationSetting(configuration);
      }

      if (failFast) {
        configuration.getMappedStatementNames();
      }
    } catch (Throwable cause) {
      throw new ProvisionException("An error occurred while building the org.apache.ibatis.session.Configuration",
          cause);
    } finally {
      ErrorContext.instance().reset();
    }

    return configuration;
  }
}
