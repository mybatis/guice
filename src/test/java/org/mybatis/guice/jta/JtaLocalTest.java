/*
 *    Copyright 2009-2023 the original author or authors.
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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import jakarta.transaction.TransactionManager;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.junit.jupiter.api.*;
import org.mybatis.guice.MyBatisJtaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JtaLocalTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(JtaLocalTest.class);

  static TransactionManager manager;
  static DataSource dataSource1;
  static DataSource dataSource2;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    LogFactory.useSlf4jLogging();

    manager = com.arjuna.ats.jta.TransactionManager.transactionManager();

    dataSource1 = BaseDB.createLocalDataSource( BaseDB.URL_DB1, manager
    );
    dataSource2 = BaseDB.createLocalDataSource( BaseDB.URL_DB2, manager
    );
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
    BaseDB.dropTable(BaseDB.URL_DB1);
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
            environmentId("db1");
            bindDataSourceProvider(new ProviderImpl<DataSource>(dataSource1));
            bindDefaultTransactionProvider();
            bindDatabaseIdProvider(new VendorDatabaseIdProvider());

            addMapperClass(JtaMapper.class);

            bind(JtaService1Impl.class);
          }
        });

        expose(JtaService1Impl.class);
      };
    }, new PrivateModule() {

      @Override
      protected void configure() {
        install(new MyBatisJtaModule(manager) {

          @Override
          protected void initialize() {
            environmentId("db2");
            bindDataSourceProvider(new ProviderImpl<DataSource>(dataSource2));
            bindDefaultTransactionProvider();
            bindDatabaseIdProvider(new VendorDatabaseIdProvider());

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
    BaseDB.clearTable(BaseDB.URL_DB1);
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
  void testRequired(TestInfo testInfo) throws Exception {
    process.required(1);
    checkCountRows(testInfo, 1);
  }

  /**
   * begin REQUIRES_NEW insert(id=1) commit REQUIRES_NEW
   * <p>
   * have 1 rows
   */
  @Test
  void testRequiresNew(TestInfo testInfo) throws Exception {
    process.requiresNew(1);
    checkCountRows(testInfo, 1);
  }

  /**
   * begin REQUIRED insert(id=1) roll back REQUIRED
   * <p>
   * have 0 rows
   */
  @Test
  void testRequiredAndRollback(TestInfo testInfo) throws Exception {
    try {
      process.requiredAndRollback(1);
    } catch (JtaRollbackException e) {
    }
    checkCountRows(testInfo, 0);
  }

  /**
   * begin REQUIRES_NEW insert(id=1) roll back REQUIRES_NEW
   * <p>
   * have 0 rows
   */
  @Test
  void testRequiresNewAndRollback(TestInfo testInfo) throws Exception {
    try {
      process.requiresNewAndRollback(1);
    } catch (JtaRollbackException e) {
    }
    checkCountRows(testInfo, 0);
  }

  /**
   * begin REQUIRED insert(id=1) begin REQUIRES_NEW insert(id=2) commit REQUIRES_NEW commit REQUIRED
   * <p>
   * have 2 rows
   */
  @Test
  void testRequiredAndRequiresNew(TestInfo testInfo) throws Exception {
    process.requiredAndRequiresNew();
    checkCountRows(testInfo, 2);
  }

  /**
   * begin REQUIRED begin REQUIRES_NEW insert(id=2) commit REQUIRES_NEW insert(id=1) commit REQUIRED
   * <p>
   * have 2 rows
   */
  @Test
  void testRequiresNewAndRequired(TestInfo testInfo) throws Exception {
    process.requiresNewAndRequired();
    checkCountRows(testInfo, 2);
  }

  /**
   * begin REQUIRED insert(id=1) begin REQUIRES_NEW insert(id=2) roll back REQUIRES_NEW commit REQUIRED
   * <p>
   * have 1 rows and id=1 (from commited REQUIRED)
   */
  @Test
  void testRollbackInternalRequiresNew(TestInfo testInfo) throws Exception {
    try {
      process.rollbackInternalRequiresNew();
    } catch (JtaRollbackException e) {
    }
    checkCountRowsAndIndex(testInfo, 1, 1);
  }

  /**
   * begin REQUIRED begin REQUIRES_NEW insert(id=1) roll back REQUIRES_NEW insert(id=2) commit REQUIRED
   * <p>
   * have 1 rows and id=2 (from commited REQUIRED)
   */
  @Test
  void testRollbackInternalRequiresNew2(TestInfo testInfo) throws Exception {
    try {
      process.rollbackInternalRequiresNew2();
    } catch (JtaRollbackException e) {
    }
    checkCountRowsAndIndex(testInfo, 1, 2);
  }

  /**
   * begin REQUIRED begin REQUIRES_NEW insert(id=1) commit REQUIRES_NEW insert(id=2) roll back REQUIRED
   * <p>
   * have 1 rows and id=1 (from commited REQUIRES_NEW)
   */
  @Test
  void testRollbackExternalRequired(TestInfo testInfo) throws Exception {
    try {
      process.rollbackExternalRequired();
    } catch (JtaRollbackException e) {
    }
    checkCountRowsAndIndex(testInfo, 1, 1);
  }

  /**
   * begin REQUIRED insert(id=1) begin REQUIRES_NEW insert(id=2) commit REQUIRES_NEW roll back REQUIRED
   * <p>
   * have 1 rows and id=2 (from commited REQUIRES_NEW)
   */
  @Test
  void testRollbackExternalRequired2(TestInfo testInfo) throws Exception {
    try {
      process.rollbackExternalRequired2();
    } catch (JtaRollbackException e) {
    }
    checkCountRowsAndIndex(testInfo, 1, 2);
  }

  private void checkCountRows(TestInfo testInfo, int count) throws Exception {
    String name = testInfo.getDisplayName();
    List<Integer> readRows;
    readRows = BaseDB.readRows(BaseDB.URL_DB1, BaseDB.NAME_DB1);
    LOGGER.info("db1 check count rows {}:{}", count, readRows.size());

    assertEquals(count, readRows.size(), name + " db1 count rows");

    readRows = BaseDB.readRows(BaseDB.URL_DB2, BaseDB.NAME_DB2);
    LOGGER.info("db2 check count rows {}:{}", count, readRows.size());

    assertEquals(count, readRows.size(), name + " db2 count rows");
  }

  private void checkCountRowsAndIndex(TestInfo testInfo, int count, int index) throws Exception {
    String name = testInfo.getDisplayName();
    List<Integer> readRows;
    readRows = BaseDB.readRows(BaseDB.URL_DB1, BaseDB.NAME_DB1);

    LOGGER.info("{} db1 check count rows {}:{}", new Object[] { name, count, readRows.size() });
    LOGGER.info("{} db1 check row id {}:{}", new Object[] { name, index, readRows.get(0).intValue() });

    assertEquals(count, readRows.size(), name + " db1 count rows");
    assertEquals(index, readRows.get(0).intValue(), name + " db1 row id");

    readRows = BaseDB.readRows(BaseDB.URL_DB2, BaseDB.NAME_DB2);
    LOGGER.info("{} db2 check count rows {}:{}", new Object[] { name, count, readRows.size() });
    LOGGER.info("{} db2 check row id {}:{}", new Object[] { name, index, readRows.get(0).intValue() });

    assertEquals(count, readRows.size(), name + " db2 count rows");
    assertEquals(index, readRows.get(0).intValue(), name + " db2 row id");
  }
}
