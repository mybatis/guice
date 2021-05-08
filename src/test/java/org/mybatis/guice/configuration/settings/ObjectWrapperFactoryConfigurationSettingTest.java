/*
 *    Copyright 2009-2021 the original author or authors.
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

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ObjectWrapperFactoryConfigurationSettingTest {
  @Mock
  private Configuration configuration;
  @Mock
  private Injector injector;
  @Mock
  private TestObjectWrapperFactory objectWrapperFactory;

  @Test
  public void applyConfigurationSetting() {
    when(injector.getInstance(TestObjectWrapperFactory.class)).thenReturn(objectWrapperFactory);
    ObjectWrapperFactoryConfigurationSetting setting = new ObjectWrapperFactoryConfigurationSetting(
        TestObjectWrapperFactory.class);
    setting.setInjector(injector);
    setting.get().applyConfigurationSetting(configuration);
    verify(injector).getInstance(TestObjectWrapperFactory.class);
    verify(configuration).setObjectWrapperFactory(objectWrapperFactory);
  }

  private static class TestObjectWrapperFactory implements ObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object object) {
      return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
      return null;
    }
  }
}
