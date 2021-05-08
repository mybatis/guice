/*
 *    Copyright 2009-2020 the original author or authors.
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
import static org.mockito.Mockito.when;

import com.google.inject.Injector;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ObjectFactoryConfigurationSettingTest {
  @Mock
  private Configuration configuration;
  @Mock
  private Injector injector;
  @Mock
  private TestObjectFactory objectFactory;

  @Test
  public void applyConfigurationSetting() {
    when(injector.getInstance(TestObjectFactory.class)).thenReturn(objectFactory);
    ObjectFactoryConfigurationSetting setting = new ObjectFactoryConfigurationSetting(TestObjectFactory.class);
    setting.setInjector(injector);
    setting.get().applyConfigurationSetting(configuration);
    verify(injector).getInstance(TestObjectFactory.class);
    verify(configuration).setObjectFactory(objectFactory);
  }

  private static class TestObjectFactory implements ObjectFactory {
    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public <T> T create(Class<T> type) {
      return null;
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
      return null;
    }

    @Override
    public <T> boolean isCollection(Class<T> type) {
      return false;
    }
  }
}
