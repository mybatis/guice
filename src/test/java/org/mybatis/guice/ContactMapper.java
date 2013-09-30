/*
 *    Copyright 2010-2012 The MyBatis Team
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
package org.mybatis.guice;

import java.util.List;

/**
 * 
 *
 * @version $Id$
 */
public interface ContactMapper {

    void brokenAdd(Contact contact);

    void add(Contact contact);

    void update(Contact contact);

    void delete(Integer id);

    Contact getById(Integer id);

    Contact getByIdWithTypeHandler(Integer id);

    List<Contact> selectAll();

    List<Contact> selectAllWithDatabaseId();
}
