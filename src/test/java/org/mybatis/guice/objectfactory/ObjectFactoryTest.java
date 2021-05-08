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
package org.mybatis.guice.objectfactory;

import static com.google.inject.name.Names.bindProperties;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Properties;

import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

public class ObjectFactoryTest {
  @Test
  public void objectFactoryInjection() {
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
        bindObjectFactoryType(CustomObjectFactory.class);
      }
    });
    SqlSessionFactory factory = injector.getInstance(SqlSessionFactory.class);
    ObjectFactory objectFactory = factory.getConfiguration().getObjectFactory();
    assertEquals(CustomObjectFactory.class, objectFactory.getClass());
    CustomObjectFactory customObjectFactory = (CustomObjectFactory) objectFactory;
    assertEquals(CustomObject.class, customObjectFactory.getCustomObject().getClass());
  }

  @Test
  public void objectWrapperFactoryInjection() {
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
        bindObjectWrapperFactoryType(CustomObjectWrapperFactory.class);
      }
    });
    SqlSessionFactory factory = injector.getInstance(SqlSessionFactory.class);
    ObjectWrapperFactory objectWrapperFactory = factory.getConfiguration().getObjectWrapperFactory();
    assertEquals(CustomObjectWrapperFactory.class, objectWrapperFactory.getClass());
    CustomObjectWrapperFactory customObjectWrapperFactory = (CustomObjectWrapperFactory) objectWrapperFactory;
    assertEquals(CustomObject.class, customObjectWrapperFactory.getCustomObject().getClass());
  }
}
