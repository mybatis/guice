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
package org.mybatis.guice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class AbstractMyBatisModuleTestCase {

  @Inject
  private Contact contact;

  @Inject
  private ContactMapperClient contactMapperClient;

  @Inject
  private Counter counter;

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public void setContactMapperClient(ContactMapperClient contactMapperClient) {
    this.contactMapperClient = contactMapperClient;
  }

  @Test
  void verifyNotNullMapper() {
    assertNotNull(this.contactMapperClient);
  }

  @Test
  void insertContact() throws Exception {
    this.contactMapperClient.insert(this.contact);
  }

  @Test
  void selectAllContacts() throws Exception {
    List<Contact> contacts = this.contactMapperClient.getAll();
    assert contacts.size() > 0 : "Expected not empty contact table";
  }

  @Test
  void reSelectAllContacts() throws Exception {
    List<Contact> contacts = this.contactMapperClient.getAll();
    assert contacts.size() > 0 : "Expected not empty contact table";
  }

  @Test
  void selectContact() throws Exception {
    Contact contact = this.contactMapperClient.selectById(this.contact.getId());
    assert contact != null : "impossible to retrieve Contact with id '" + this.contact.getId() + "'";
    assert this.contact.equals(contact) : "Expected " + this.contact + " but found " + contact;
  }

  @Test
  void selectContactWithTypeHandler() throws Exception {
    Contact contact = this.contactMapperClient.selectByIdWithTypeHandler(this.contact.getId());
    assert contact != null : "impossible to retrieve Contact with id '" + this.contact.getId() + "'";
    assert this.contact.equals(contact) : "Expected " + this.contact + " but found " + contact;
  }

  @Test
  void catchSQLException() throws Exception {
    Assertions.assertThrows(CustomException.class, () -> {
      this.contactMapperClient.brokenInsert(this.contact);
    });
  }

  @Test
  void testCountInterceptor() throws Exception {
    counter.reset();
    assert 0 == counter.getCount() : "Expected 0 update in counter, but was " + counter.getCount();
    this.contactMapperClient.update(contact);
    assert 1 == counter.getCount() : "Expected 1 update in Executor, but was " + counter.getCount();
  }

}
