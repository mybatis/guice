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
package org.mybatis.guice;

import com.google.inject.Module;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class XMLGuiceTestExtension extends AbstractGuiceTestExtension {

  public XMLGuiceTestExtension() throws SQLException {
    super();
  }

  @Override
  protected List<Module> createMyBatisModule() {
    List<Module> modules = new ArrayList<Module>(2);
    modules.add(new XMLMyBatisModule() {

      @Override
      protected void initialize() {
      }

    });
    return modules;
  }

  @Override
  protected Properties createTestProperties() {
    return new Properties();
  }

}
