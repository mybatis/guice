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
package org.mybatis.guice.mappers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.ibatis.session.SqlSessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MapperProviderTest {
  private MapperProvider<TestMapper> mapperProvider;
  @Mock
  private SqlSessionManager sqlSessionManager;
  private TestMapper mapper;

  @BeforeEach
  public void beforeTest() {
    mapperProvider = new MapperProvider<TestMapper>(TestMapper.class);
    mapperProvider.setSqlSessionManager(sqlSessionManager);
    mapper = new TestMapper();
    when(sqlSessionManager.getMapper(TestMapper.class)).thenReturn(mapper);
  }

  @Test
  void get() {
    TestMapper mapper = mapperProvider.get();
    assertNotNull(mapper);
    verify(sqlSessionManager).getMapper(TestMapper.class);
  }

  private static class TestMapper {
  }
}
