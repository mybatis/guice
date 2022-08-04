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
package org.mybatis.guice.jta;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;

import javax.sql.DataSource;

import org.apache.aries.transaction.AriesTransactionManager;
import org.apache.aries.transaction.internal.AriesTransactionManagerImpl;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mybatis.guice.MyBatisJtaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JtaCustomXaResourceTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(JtaCustomXaResourceTest.class);

  static AriesTransactionManager manager;
  static DataSource dataSource2;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    LogFactory.useSlf4jLogging();

    manager = new AriesTransactionManagerImpl();

    dataSource2 = BaseDB.createLocalDataSource(BaseDB.NAME_DB2, BaseDB.URL_DB2, manager);
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
    BaseDB.dropTable(BaseDB.URL_DB2);
  }

  private Injector injector;

  JtaProcess process;

  @BeforeEach
  void setup(TestInfo testInfo) throws Exception {
    LOGGER.info("********************************************************************************");
    LOGGER.info("Testing: " + testInfo.getTestMethod() + "(" + getClass().getName() + ")");
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

  @AfterEach
  void tearDown(TestInfo testInfo) throws Exception {
    BaseDB.clearTable(BaseDB.URL_DB2);

    LOGGER.info("********************************************************************************");
    LOGGER.info("Testing done: " + testInfo.getTestMethod() + "(" + getClass().getName() + ")");
    LOGGER.info("********************************************************************************");
  }

  /**
   * begin REQUIRED insert(id=1) commit REQUIRED
   * <p>
   * have 1 rows
   */
  @Test
  void testRequired() throws Exception {
    try {
      process.required(1);
    } catch (Throwable t) {
    }
    assertTrue(CustomXaResourceProvider.isProviderCalled());
  }
}
