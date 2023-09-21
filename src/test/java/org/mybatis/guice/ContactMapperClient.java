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
package org.mybatis.guice;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

import org.mybatis.guice.transactional.Isolation;
import org.mybatis.guice.transactional.Transactional;

@Singleton
public class ContactMapperClient {

  @Inject
  private ContactMapper contactMapper;

  public void setContactMapper(ContactMapper contactMapper) {
    this.contactMapper = contactMapper;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = CustomException.class, exceptionMessage = "Impossible to insert %s contact")
  public void brokenInsert(final Contact contact) throws CustomException {
    this.contactMapper.brokenAdd(contact);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = CustomException.class, exceptionMessage = "Impossible to insert %s contact")
  public void insert(final Contact contact) throws CustomException {
    this.contactMapper.add(contact);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = CustomException.class, exceptionMessage = "Impossible to delete contact with ID %s")
  public void delete(final Integer id) throws CustomException {
    this.contactMapper.delete(id);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = CustomException.class, exceptionMessage = "An error occurred when selecting contact with ID %s")
  public Contact selectById(final Integer id) throws CustomException {
    return this.contactMapper.getById(id);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = CustomException.class, exceptionMessage = "An error occurred when selecting contact with ID %s")
  public Contact selectByIdWithTypeHandler(final Integer id) throws CustomException {
    return this.contactMapper.getByIdWithTypeHandler(id);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = CustomException.class, exceptionMessage = "An error occurred when selecting all stored contacts")
  public List<Contact> getAll() throws CustomException {
    return this.contactMapper.selectAll();
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = CustomException.class, exceptionMessage = "An error occurred when selecting all stored contacts using a database id")
  public List<Contact> getAllWithDatabaseId() throws CustomException {
    return this.contactMapper.selectAllWithDatabaseId();
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, rethrowExceptionsAs = CustomException.class, exceptionMessage = "An error occurred when updating contact %s")
  public void update(final Contact contact) throws CustomException {
    this.contactMapper.update(contact);
  }

}
