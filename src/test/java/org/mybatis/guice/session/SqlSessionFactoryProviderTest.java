/*
 *    Copyright 2009-2026 the original author or authors.
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
package org.mybatis.guice.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SqlSessionFactoryProviderTest {
  private SqlSessionFactoryProvider sqlSessionFactoryProvider;
  @Mock
  private Configuration configuration;

  @BeforeEach
  void beforeTest() {
    sqlSessionFactoryProvider = new SqlSessionFactoryProvider();
    sqlSessionFactoryProvider.createNewSqlSessionFactory(configuration);
  }

  @Test
  void get() {
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryProvider.get();

    assertNotNull(sqlSessionFactory);
    assertEquals(configuration, sqlSessionFactory.getConfiguration());
  }
}
