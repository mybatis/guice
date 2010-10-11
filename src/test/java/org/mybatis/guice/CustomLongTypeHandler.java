/*
 *    Copyright 2010 The mybatis-guice Team
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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * @version $Id$
 */
public class CustomLongTypeHandler extends BaseTypeHandler {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
            Object parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == JdbcType.TIMESTAMP) {
            if (parameter instanceof CustomType) {
                ps.setTimestamp(i,
                        new Timestamp(((CustomType) parameter).getValue()));
            }
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        
        Object value = rs.getObject(columnName);
        if (value instanceof Timestamp) {
            CustomType t = new CustomType();
            t.setValue(((Timestamp) value).getTime());
            return t;
        }
        return null;
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Object value = cs.getObject(columnIndex);
        if (value instanceof Timestamp) {
            CustomType t = new CustomType();
            t.setValue(((Timestamp) value).getTime());
            return t;
        }
        return null;
    }

}
