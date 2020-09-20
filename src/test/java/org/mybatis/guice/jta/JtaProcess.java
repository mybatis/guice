/**
 *    Copyright 2009-2020 the original author or authors.
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

import javax.inject.Inject;
import javax.inject.Provider;

import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JtaProcess {
  private static final Logger LOGGER = LoggerFactory.getLogger(JtaLocalTest.class);

  @Inject
  Provider<JtaProcess> provider;
  @Inject
  JtaService1Impl db1;
  @Inject
  JtaService2Impl db2;

  public JtaProcess() {
  }

  private JtaProcess getProvider() {
    JtaProcess process = provider.get();
    return process;
  }

  private void inserts(int offset) {
    LOGGER.info("inserts to db1");
    inserts(db1, offset);
    LOGGER.info("inserts to db2");
    inserts(db2, offset);
  }

  /**
   * begin REQUIRED insert(id=1) commit REQUIRED
   *
   * have 1 rows
   */
  @Transactional
  public void required(int offset) {
    LOGGER.info("insert REQUIED transaction");
    inserts(offset);
  }

  /**
   * begin REQUIRES_NEW insert(id=1) commit REQUIRES_NEW
   *
   * have 1 rows
   */
  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void requiresNew(int offset) {
    LOGGER.info("insert REQUIES_NEW transaction");
    inserts(offset);
  }

  /**
   * begin REQUIRED insert(id=1) roll back REQUIRED
   *
   * have 0 rows
   */
  @Transactional
  public void requiredAndRollback(int offset) throws JtaRollbackException {
    LOGGER.info("insert REQUIED transaction");
    inserts(offset);
    LOGGER.info("roll back REQUIED transaction");
    throw new JtaRollbackException();
  }

  /**
   * begin REQUIRES_NEW insert(id=1) roll back REQUIRES_NEW
   *
   * have 0 rows
   */
  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void requiresNewAndRollback(int offset) throws JtaRollbackException {
    LOGGER.info("insert REQUIES_NEW transaction");
    inserts(offset);
    LOGGER.info("roll back REQUIES_NEW transaction");
    throw new JtaRollbackException();
  }

  /**
   * begin REQUIRED insert(id=1) begin REQUIRES_NEW insert(id=2) commit REQUIRES_NEW commit REQUIRED
   *
   * have 2 rows
   */
  @Transactional
  public void requiredAndRequiresNew() {
    JtaProcess process = getProvider();

    LOGGER.info("insert REQUIED transaction");
    process.inserts(1);
    process.requiresNew(2);
  }

  /**
   * begin REQUIRED begin REQUIRES_NEW insert(id=1) commit REQUIRES_NEW insert(id=2) commit REQUIRED
   *
   * have 2 rows
   */
  @Transactional
  public void requiresNewAndRequired() {
    JtaProcess process = getProvider();

    process.requiresNew(1);
    LOGGER.info("insert REQUIED transaction");
    process.inserts(2);
  }

  /**
   * begin REQUIRED insert(id=1) begin REQUIRES_NEW insert(id=2) roll back REQUIRES_NEW commit REQUIRED
   *
   * have 1 rows and id=1 (from commited REQUIRED)
   */
  @Transactional
  public void rollbackInternalRequiresNew() throws JtaRollbackException {
    JtaProcess process = getProvider();

    LOGGER.info("transaction process:" + "\n\t* begin REQUIRED" + "\n\t*   insert(id=1)" + "\n\t*   begin REQUIRES_NEW"
        + "\n\t*      insert(id=2)" + "\n\t*   roll back REQUIRES_NEW" + "\n\t* commit REQUIRED" + "\n\t*"
        + "\n\t* have 1 rows and id=1 (from commited REQUIRED)");

    LOGGER.info("insert REQUIED transaction");
    process.inserts(1);

    try {
      process.requiresNewAndRollback(2);
    } catch (JtaRollbackException e) {
    }
  }

  /**
   * begin REQUIRED begin REQUIRES_NEW insert(id=1) roll back REQUIRES_NEW insert(id=2) commit REQUIRED
   *
   * have 1 rows and id=2 (from commited REQUIRED)
   */
  @Transactional
  public void rollbackInternalRequiresNew2() throws JtaRollbackException {
    JtaProcess process = getProvider();

    LOGGER.info("transaction process:" + "\n\t* begin REQUIRED" + "\n\t*   begin REQUIRES_NEW"
        + "\n\t*      insert(id=1)" + "\n\t*   roll back REQUIRES_NEW" + "\n\t*   insert(id=2)"
        + "\n\t* commit REQUIRED" + "\n\t*" + "\n\t* have 1 rows and id=2 (from commited REQUIRED)");

    try {
      process.requiresNewAndRollback(1);
    } catch (JtaRollbackException e) {
    }

    LOGGER.info("insert REQUIED transaction");
    process.inserts(2);
  }

  /**
   * begin REQUIRED begin REQUIRES_NEW insert(id=1) commit REQUIRES_NEW insert(id=2) roll back REQUIRED
   *
   * have 1 rows and id=1 (from commited REQUIRES_NEW)
   */
  @Transactional
  public void rollbackExternalRequired() throws JtaRollbackException {
    JtaProcess process = getProvider();

    LOGGER.info("transaction process:" + "\n\t* begin REQUIRED" + "\n\t*   begin REQUIRES_NEW"
        + "\n\t*      insert(id=1)" + "\n\t*   commit REQUIRES_NEW" + "\n\t*   insert(id=2)"
        + "\n\t* roll back REQUIRED" + "\n\t* " + "\n\t* have 1 rows and id=1 (from commited REQUIRES_NEW)");

    process.requiresNew(1);
    LOGGER.info("insert REQUIED transaction");
    process.inserts(2);
    LOGGER.info("roll back REQUIED transaction");
    throw new JtaRollbackException();
  }

  /**
   * begin REQUIRED insert(id=1) begin REQUIRES_NEW insert(id=2) commit REQUIRES_NEW roll back REQUIRED
   *
   * have 1 rows and id=2 (from commited REQUIRES_NEW)
   */
  @Transactional
  public void rollbackExternalRequired2() throws JtaRollbackException {
    JtaProcess process = getProvider();

    LOGGER.info("transaction process:" + "\n\t* begin REQUIRED" + "\n\t*   insert(id=1)" + "\n\t*   begin REQUIRES_NEW"
        + "\n\t*      insert(id=2)" + "\n\t*   commit REQUIRES_NEW" + "\n\t* roll back REQUIRED" + "\n\t*"
        + "\n\t* have 1 rows and id=2 (from commited REQUIRES_NEW)");

    LOGGER.info("insert REQUIED transaction");
    process.inserts(1);
    process.requiresNew(2);
    LOGGER.info("roll back REQUIED transaction after commit REQUIRES_NEW transaction");
    throw new JtaRollbackException();
  }

  private void inserts(JtaService1Impl service, int offset) {
    TableRow tr = new TableRow();
    tr.setId(offset);
    tr.setName("name " + offset);
    service.insertTable(tr);
  }

  private void inserts(JtaService2Impl service, int offset) {
    TableRow tr = new TableRow();
    tr.setId(offset);
    tr.setName("name " + offset);
    service.insertTable(tr);
  }
}
