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
package org.mybatis.guice.jta.simple;

import jakarta.inject.Inject;

import java.util.List;

import org.mybatis.guice.transactional.Transactional;

public class CombinedService {
  @Inject
  private Schema1Service schema1Service;
  @Inject
  private Schema2Service schema2Service;

  @Transactional
  public void insert2RecordsIntoSchema1And1RecordIntoSchema2AndThrow(Exception e) throws Exception {
    insert2RecordsIntoSchema1And1RecordIntoSchema2();
    throw e;
  }

  @Transactional
  public void insert2RecordsIntoSchema1And1RecordIntoSchema2AndRollbackAll() {
    insert2RecordsIntoSchema1And1RecordIntoSchema2();
    throw new RuntimeException("Exception to force rollback - should rollback all inserts");
  }

  @Transactional
  public void insert2RecordsIntoSchema1And1RecordIntoSchema2() {
    Name name = new Name(1, "Fred");
    schema1Service.insertName(name);

    name = new Name(2, "Wilma");
    schema1Service.insertName(name);

    name = new Name(3, "Barney");
    schema2Service.insertName(name);
  }

  @Transactional
  public void insert2RecordsIntoSchema1And1RecordIntoSchema2AndRollbackSchema2() {
    Name name = new Name(1, "Fred");
    schema1Service.insertNameWithNewTransaction(name);

    name = new Name(2, "Wilma");
    schema1Service.insertNameWithNewTransaction(name);

    name = new Name(3, "Barney");
    schema2Service.insertName(name);

    throw new RuntimeException("Exception to force rollback - should rollback all schema 2 inserts");
  }

  @Transactional
  public List<Name> getAllNamesFromSchema1() {
    return schema1Service.getAllNames();
  }

  @Transactional
  public List<Name> getAllNamesFromSchema2() {
    return schema2Service.getAllNames();
  }
}
