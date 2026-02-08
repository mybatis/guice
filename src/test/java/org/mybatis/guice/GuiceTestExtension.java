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
package org.mybatis.guice;

import com.google.inject.Module;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

public final class GuiceTestExtension extends AbstractGuiceTestExtension {

  public GuiceTestExtension() throws SQLException {
    super();
  }

  @Override
  protected List<Module> createMyBatisModule() {
    List<Module> modules = new ArrayList<Module>(3);

    modules.add(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);
    modules.add(new MyBatisModule() {

      @Override
      protected void initialize() {
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClass(ContactMapper.class);
        handleType(CustomType.class).with(CustomLongTypeHandler.class);
        handleType(Address.class).with(AddressTypeHandler.class);
        addInterceptorClass(CountUpdateInterceptor.class);
        addTypeHandlerClass(ContactIdTypeHandler.class);
        addTypeHandlerClass(ContactNameTypeHandler.class);
        bindDatabaseIdProvider(VendorDatabaseIdProvider.class);
        defaultStatementTimeout(null);
      }

    });
    modules.add(new MyBatisModule() {

      @Override
      protected void initialize() {
        addMapperClass(ContactMapper.class);
        handleType(CustomType.class).with(CustomLongTypeHandler.class);
        handleType(Address.class).with(AddressTypeHandler.class);
        addTypeHandlerClass(ContactIdTypeHandler.class);
        addTypeHandlerClass(ContactNameTypeHandler.class);
      }

    });

    return modules;
  }

  @Override
  protected Properties createTestProperties() {
    final Properties myBatisProperties = new Properties();
    myBatisProperties.setProperty("mybatis.environment.id", "test");
    myBatisProperties.setProperty("JDBC.username", "sa");
    myBatisProperties.setProperty("JDBC.password", "");
    myBatisProperties.setProperty("JDBC.autoCommit", "false");
    return myBatisProperties;
  }
}
