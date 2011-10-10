/*
 *    Copyright 2010 The myBatis Team
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
import java.text.ParseException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import javax.inject.Inject;

/**
 * @version $Id$
 */
public class AddressTypeHandler implements TypeHandler<Address> {

    private AddressConverter addressConverter;

    public void setParameter(PreparedStatement ps, int i, Address parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, jdbcType.TYPE_CODE);
        } else {
            ps.setString(i, addressConverter.convert(parameter));
        }
    }

    public Address getResult(ResultSet rs, String columnName) throws SQLException {
        String input = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            try {
                return addressConverter.convert(input);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Address getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String input = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            try {
                return addressConverter.convert(input);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Inject
    public void setAddressConverter(AddressConverter addressConverter) {
        this.addressConverter = addressConverter;
    }
}
