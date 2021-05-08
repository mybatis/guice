/*
 *    Copyright 2009-2021 the original author or authors.
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
package org.mybatis.guice.multidstest;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface Schema1Mapper {

  @Update({ "drop schema schema1 if exists cascade;" })
  int createSchema1Step1();

  @Update({ "create schema schema1;" })
  int createSchema1Step2();

  @Update({ "CREATE SEQUENCE schema1.test_sequence AS INTEGER START WITH 100 INCREMENT BY 1;" })
  int createSchema1Step3();

  @Select({ "call next value for schema1.test_sequence" })
  Integer getNextValueFromSchema1();
}
