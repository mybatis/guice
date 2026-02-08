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
package org.mybatis.guice.resolver.typehandler;

import jakarta.inject.Inject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.guice.resolver.alias.User;

public class UserTypeHandler extends BaseTypeHandler<User> {
  @Inject
  private User user;
  private Class<? extends User> type;

  public UserTypeHandler() {
  }

  public UserTypeHandler(Class<? extends User> type) {
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, User parameter, JdbcType jdbcType) throws SQLException {
  }

  @Override
  public User getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return null;
  }

  @Override
  public User getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return null;
  }

  @Override
  public User getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return null;
  }

  public User getUser() {
    return user;
  }

  public Class<? extends User> getType() {
    return type;
  }
}
