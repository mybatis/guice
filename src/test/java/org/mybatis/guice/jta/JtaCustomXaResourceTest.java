/**
 *    Copyright 2009-2018 the original author or authors.
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
package org.mybatis.guice.jta;

import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;

import org.apache.aries.transaction.AriesTransactionManager;
import org.apache.aries.transaction.internal.AriesTransactionManagerImpl;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mybatis.guice.MyBatisJtaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;

public class JtaCustomXaResourceTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(JtaCustomXaResourceTest.class);

  static AriesTransactionManager manager;
  static DataSource dataSource2;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    LogFactory.useSlf4jLogging();

    manager = new AriesTransactionManagerImpl();

    dataSource2 = BaseDB.createLocalDataSource(BaseDB.NAME_DB2, BaseDB.URL_DB2, manager);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    BaseDB.dropTable(BaseDB.URL_DB2);
  }

  @Rule
  public TestName testName = new TestName();
  private Injector injector;

  JtaProcess process;

  @Before
  public void setup() throws Exception {
    LOGGER.info("********************************************************************************");
    LOGGER.info("Testing: " + testName.getMethodName() + "(" + getClass().getName() + ")");
    LOGGER.info("********************************************************************************");
    LogFactory.useSlf4jLogging();

    LOGGER.info("create injector");
    injector = Guice.createInjector(new PrivateModule() {

      @Override
      protected void configure() {
        install(new MyBatisJtaModule(manager) {

          @Override
          protected void initialize() {
            environmentId("db2");
            bindDataSourceProvider(new ProviderImpl<DataSource>(dataSource2));
            bindDefaultTransactionProvider();
            bindDatabaseIdProvider(new VendorDatabaseIdProvider());
            bindXAResourceProvider(CustomXaResourceProvider.class);

            addMapperClass(JtaMapper.class);

            bind(JtaService2Impl.class);
            bind(JtaProcess.class);
          }
        });

        expose(JtaService2Impl.class);
        expose(JtaProcess.class);
      };
    });

    injector.injectMembers(this);
    process = injector.getInstance(JtaProcess.class);
  }

  @After
  public void tearDown() throws Exception {
    BaseDB.clearTable(BaseDB.URL_DB2);

    LOGGER.info("********************************************************************************");
    LOGGER.info("Testing done: " + testName.getMethodName() + "(" + getClass().getName() + ")");
    LOGGER.info("********************************************************************************");
  }

  /**
   * begin REQUIRED insert(id=1) commit REQUIRED
   * 
   * have 1 rows
   */
  @Test
  public void testRequired() throws Exception {
    try {
      process.required(1);
    } catch (Throwable t) {
    }
    assertTrue(CustomXaResourceProvider.isProviderCalled());
  }
}
