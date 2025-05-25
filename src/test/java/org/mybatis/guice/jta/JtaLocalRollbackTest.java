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

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.transaction.TransactionManager;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mybatis.guice.transactional.TransactionAttribute;
import org.mybatis.guice.transactional.TransactionToken;

/**
 * Create Requerd transaction. Create internal RequiresNew transaction. Rollback first transaction. Warning: transaction
 * will roll back. XA error code: 100
 */
public class JtaLocalRollbackTest {

  private static DataSource dataSource;
  private static TransactionManager manager;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver").getDeclaredConstructor().newInstance();

    manager = com.arjuna.ats.jta.TransactionManager.transactionManager();

    String className = "org.apache.derby.jdbc.EmbeddedDriver";
    Class.forName(className).getDeclaredConstructor().newInstance();

    dataSource = BaseDB.createLocalDataSource(BaseDB.URL_DB1, manager);
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
    BaseDB.dropTable(BaseDB.URL_DB1);
  }

  @AfterEach
  void tearDown() throws Exception {
    BaseDB.clearTable(BaseDB.URL_DB1);
  }

  @Test
  void testFirstRollback() throws Exception {
    TransactionAttribute firstAttribute = TransactionAttribute.REQUIRED;
    TransactionAttribute secondAttribute = TransactionAttribute.REQUIRESNEW;

    // REQUIRED transaction
    TransactionToken firstToken = firstAttribute.begin(manager);
    try {
      Connection firstCon = dataSource.getConnection();
      BaseDB.insertRow(firstCon, 1, "name 1");
      firstCon.close();

      // REQUIRESNEW transaction
      TransactionToken secondToken = secondAttribute.begin(manager);
      try {
        Connection secondCon = dataSource.getConnection();
        BaseDB.insertRow(secondCon, 2, "name 2");
        secondCon.close();
      } finally {
        secondAttribute.finish(manager, secondToken);
      }

      // roll back REQUIRED after commit REQUIRESNEW
      throw new Exception("rollback");

    } catch (Exception e) {
      manager.setRollbackOnly();
    } finally {
      firstAttribute.finish(manager, firstToken);
    }

    List<Integer> rows = BaseDB.readRows(BaseDB.URL_DB1, BaseDB.NAME_DB1);
    assertEquals(1, rows.size());
    assertEquals(2, rows.get(0).intValue());
  }

  @Test
  void testSecondRollback() throws Exception {
    TransactionAttribute firstAttribute = TransactionAttribute.REQUIRED;
    TransactionAttribute secondAttribute = TransactionAttribute.REQUIRESNEW;

    // REQUIRED transaction
    TransactionToken firstToken = firstAttribute.begin(manager);
    try {
      Connection firstCon = dataSource.getConnection();
      BaseDB.insertRow(firstCon, 1, "name 1");
      firstCon.close();

      // REQUIRESNEW transaction
      TransactionToken secondToken = secondAttribute.begin(manager);
      try {
        Connection secondCon = dataSource.getConnection();
        BaseDB.insertRow(secondCon, 2, "name 2");
        secondCon.close();

        // roll back REQUIRESNEW and commit REQUIRED
        throw new Exception("rollback");
      } catch (Exception e) {
        // not throws exception to REQUITED
        manager.setRollbackOnly();
      } finally {
        secondAttribute.finish(manager, secondToken);
      }

    } catch (Exception e) {
      manager.setRollbackOnly();
    } finally {
      firstAttribute.finish(manager, firstToken);
    }

    List<Integer> rows = BaseDB.readRows(BaseDB.URL_DB1, BaseDB.NAME_DB1);
    assertEquals(1, rows.size());
    assertEquals(1, rows.get(0).intValue());
  }
}
