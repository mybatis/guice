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
package org.mybatis.guice.sample;

import static com.google.inject.name.Names.bindProperties;
import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.guice.CustomException;
import org.mybatis.guice.MyBatisModule;
// import org.mybatis.guice.XMLMyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.mybatis.guice.sample.dao.UserDao;
import org.mybatis.guice.sample.dao.UserDaoImpl;
import org.mybatis.guice.sample.domain.User;
import org.mybatis.guice.sample.mapper.UserMapper;
import org.mybatis.guice.sample.service.FooService;
import org.mybatis.guice.sample.service.FooServiceDaoImpl;

/**
 * Example of MyBatis-Guice basic integration usage.
 *
 * This is the recommended scenario.
 */
public class SampleSqlSessionTest {

  private Injector injector;

  private FooService fooService;

  @BeforeEach
  public void setupMyBatisGuice() throws Exception {

    // bindings
    List<Module> modules = this.createMyBatisModule();
    this.injector = Guice.createInjector(modules);

    // prepare the test db
    Environment environment = this.injector.getInstance(SqlSessionFactory.class).getConfiguration().getEnvironment();
    DataSource dataSource = environment.getDataSource();
    ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
    runner.setAutoCommit(true);
    runner.setStopOnError(true);
    runner.runScript(getResourceAsReader("org/mybatis/guice/sample/db/database-schema.sql"));
    runner.runScript(getResourceAsReader("org/mybatis/guice/sample/db/database-test-data.sql"));
    runner.closeConnection();

    this.fooService = this.injector.getInstance(FooService.class);
  }

  protected List<Module> createMyBatisModule() {
    List<Module> modules = new ArrayList<Module>();
    modules.add(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);
    modules.add(new MyBatisModule() {

      @Override
      protected void initialize() {
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClass(UserMapper.class);
      }

    });
    /*
     * modules.add(new XMLMyBatisModule() {
     * @Override protected void initialize() { setEnvironmentId("test");
     * setClassPathResource("org/mybatis/guice/sample/mybatis-config.xml"); } });
     */
    modules.add(new Module() {
      @Override
      public void configure(Binder binder) {
        bindProperties(binder, createTestProperties());
        binder.bind(FooService.class).to(FooServiceDaoImpl.class);
        binder.bind(UserDao.class).to(UserDaoImpl.class);
      }
    });

    return modules;
  }

  protected static Properties createTestProperties() {
    final Properties myBatisProperties = new Properties();
    myBatisProperties.setProperty("mybatis.environment.id", "test");
    myBatisProperties.setProperty("JDBC.username", "sa");
    myBatisProperties.setProperty("JDBC.password", "");
    myBatisProperties.setProperty("JDBC.autoCommit", "false");
    return myBatisProperties;
  }

  @Test
  public void testFooService() {
    User user = this.fooService.doSomeBusinessStuff("u1");
    assertNotNull(user);
    assertEquals("Pocoyo", user.getName());
  }

  @Test
  public void testTransactionalOnClassAndMethod() {
    User user = new User();
    user.setName("Christian Poitras");
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      this.fooService.brokenInsert(user);
    });
  }

  @Test
  public void testTransactionalOnClass() {
    User user = new User();
    user.setName("Christian Poitras");
    Assertions.assertThrows(CustomException.class, () -> {
      this.fooService.brokenInsert2(user);
    });
  }
}
