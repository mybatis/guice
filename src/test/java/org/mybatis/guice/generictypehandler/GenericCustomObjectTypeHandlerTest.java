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
package org.mybatis.guice.generictypehandler;

import static com.google.inject.name.Names.bindProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.junit.jupiter.api.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

class GenericCustomObjectTypeHandlerTest {
  @Test
  @SuppressWarnings({ "rawtypes", "unchecked" })
  void genericTypeHandler() {
    Injector injector = Guice.createInjector(JdbcHelper.HSQLDB_IN_MEMORY_NAMED, new MyBatisModule() {
      @Override
      protected void initialize() {
        final Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "test");
        myBatisProperties.setProperty("JDBC.username", "sa");
        myBatisProperties.setProperty("JDBC.password", "");
        myBatisProperties.setProperty("JDBC.autoCommit", "false");
        bindProperties(binder(), myBatisProperties);
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        Class handlerClass = (Class) GenericCustomObjectTypeHandler.class;
        handleType(CustomObject.class).withProvidedTypeHandler(handlerClass);
      }
    });
    SqlSessionFactory factory = injector.getInstance(SqlSessionFactory.class);
    TypeHandler<?> typeHandler = factory.getConfiguration().getTypeHandlerRegistry().getTypeHandler(CustomObject.class);
    assertEquals(GenericCustomObjectTypeHandler.class, typeHandler.getClass());
    GenericCustomObjectTypeHandler genericEnumTypeHandler = (GenericCustomObjectTypeHandler) typeHandler;
    assertEquals(CustomObject.class, genericEnumTypeHandler.getType());
    assertNotNull(genericEnumTypeHandler.getInjectedObject());
  }
}
