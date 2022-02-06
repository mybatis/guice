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
package org.mybatis.guice.configuration.settings;

import static org.mockito.Mockito.verify;

import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CallSettersOnNullsConfigurationSettingTest {
  @Mock
  private Configuration configuration;

  @Test
  void applyConfigurationSetting_True() {
    CallSettersOnNullsConfigurationSetting setting = new CallSettersOnNullsConfigurationSetting(true);
    setting.applyConfigurationSetting(configuration);
    verify(configuration).setCallSettersOnNulls(true);
  }

  @Test
  void applyConfigurationSetting_False() {
    CallSettersOnNullsConfigurationSetting setting = new CallSettersOnNullsConfigurationSetting(false);
    setting.applyConfigurationSetting(configuration);
    verify(configuration).setCallSettersOnNulls(false);
  }
}
