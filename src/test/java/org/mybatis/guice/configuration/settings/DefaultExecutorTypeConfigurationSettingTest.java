/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.configuration.settings;

import static org.mockito.Mockito.verify;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class DefaultExecutorTypeConfigurationSettingTest {
  @Mock
  private Configuration configuration;
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void applyConfigurationSetting_Batch() {
    DefaultExecutorTypeConfigurationSetting setting = new DefaultExecutorTypeConfigurationSetting(ExecutorType.BATCH);
    setting.applyConfigurationSetting(configuration);
    verify(configuration).setDefaultExecutorType(ExecutorType.BATCH);
  }

  @Test
  public void applyConfigurationSetting_Reuse() {
    DefaultExecutorTypeConfigurationSetting setting = new DefaultExecutorTypeConfigurationSetting(ExecutorType.REUSE);
    setting.applyConfigurationSetting(configuration);
    verify(configuration).setDefaultExecutorType(ExecutorType.REUSE);
  }

  @Test
  public void applyConfigurationSetting_Simple() {
    DefaultExecutorTypeConfigurationSetting setting = new DefaultExecutorTypeConfigurationSetting(ExecutorType.SIMPLE);
    setting.applyConfigurationSetting(configuration);
    verify(configuration).setDefaultExecutorType(ExecutorType.SIMPLE);
  }
}
