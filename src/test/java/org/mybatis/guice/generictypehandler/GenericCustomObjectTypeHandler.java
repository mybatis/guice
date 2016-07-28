/**
 *    Copyright 2009-2015 the original author or authors.
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
package org.mybatis.guice.generictypehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import javax.inject.Inject;

@MappedTypes(CustomObject.class)
public class GenericCustomObjectTypeHandler<E extends CustomObject> extends BaseTypeHandler<E> {
    @Inject
    private InjectedObject injectedObject;
    private Class<E> type;
    
    public GenericCustomObjectTypeHandler(Class<E> type) {
        this.type = type;
    }
    
    public GenericCustomObjectTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getName());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return createResult(name);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String name = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return createResult(name);
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String name = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return createResult(name);
        }
    }
    
    private E createResult(String name) {
        try {
            E customObject = type.newInstance();
            customObject.setName(name);
            return customObject;
        } catch (InstantiationException e) {
            throw new PersistenceException(e);
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e);
        }
    }

    public InjectedObject getInjectedObject() {
        return injectedObject;
    }

    public Class<E> getType() {
        return type;
    }
}
