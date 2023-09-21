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
package org.mybatis.guice.nestedtx;

import jakarta.inject.Inject;

import java.util.List;

import org.mybatis.guice.transactional.Transactional;

public class NestedTxService {

  @Inject
  private NestedTxMapper mapper;

  @Transactional
  public int insertTable1(TableRow row) {
    return mapper.insertTable1(row);
  }

  @Transactional
  public List<TableRow> selectAllTable1() {
    return mapper.selectAllTable1();
  }

  @Transactional
  public int insertTable2(TableRow row) {
    return mapper.insertTable2(row);
  }

  @Transactional
  public List<TableRow> selectAllTable2() {
    return mapper.selectAllTable2();
  }

  @Transactional
  public void goodInserts() {
    TableRow tr = new TableRow();
    tr.setId(1);
    tr.setName("Fred");
    insertTable1(tr);

    tr = new TableRow();
    tr.setId(1);
    tr.setName("Barney");
    insertTable2(tr);

    tr = new TableRow();
    tr.setId(2);
    tr.setName("Betty");
    insertTable2(tr);
  }

  @Transactional
  public void badInsertRollbackAllRows() {
    TableRow tr = new TableRow();
    tr.setId(1);
    tr.setName("Fred");
    insertTable1(tr);

    tr = new TableRow();
    tr.setId(1);
    tr.setName("Barney");
    insertTable2(tr);

    tr = new TableRow();
    tr.setId(1);
    tr.setName("Betty");
    insertTable2(tr);
  }

  @Transactional
  public void ignoreBadInsert() {
    TableRow tr = new TableRow();
    tr.setId(1);
    tr.setName("Fred");
    insertTable1(tr);

    tr = new TableRow();
    tr.setId(1);
    tr.setName("Barney");
    insertTable2(tr);

    tr = new TableRow();
    tr.setId(1);
    tr.setName("Betty");
    try {
      insertTable2(tr);
    } catch (Exception e) {
      // ignore - the other two inserts should be OK
    }
  }

  @Transactional
  public void correctBadInsert() {
    TableRow tr = new TableRow();
    tr.setId(1);
    tr.setName("Fred");
    insertTable1(tr);

    tr = new TableRow();
    tr.setId(1);
    tr.setName("Barney");
    insertTable2(tr);

    tr = new TableRow();
    tr.setId(1);
    tr.setName("Betty");
    try {
      insertTable2(tr);
    } catch (Exception e) {
      // correct and try again
      tr.setId(2);
      insertTable2(tr);
    }
  }
}
