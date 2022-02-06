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
package org.mybatis.guice.jta;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.guice.transactional.Transactional;

public class JtaService1Impl {
  private final Log log = LogFactory.getLog(getClass());

  @Inject
  JtaMapper mapper;
  @Inject
  SqlSessionFactory factory;

  public JtaService1Impl() {
  }

  @Transactional
  public int insertTable(TableRow row) {
    log.debug(getName() + " insertTable");
    return mapper.insertTable(row);
  }

  @Transactional
  public List<TableRow> selectAllTable() {
    log.debug(getName() + " selectAllTable");
    return mapper.selectAllTable();
  }

  public String getName() {
    return factory.getConfiguration().getEnvironment().getId();
  }
}
