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
package org.mybatis.guice;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.guice.Counter;

/**
 * TypeHandler for user name.
 * 
 * @author poitrac
 */
public class ContactIdTypeHandler implements TypeHandler<Integer> {
	private Counter counter;
	
	@Override
	public void setParameter(PreparedStatement ps, int i, Integer parameter,
			JdbcType jdbcType) throws SQLException {
		counter.increment();
		if (parameter == null) parameter = 0; 
		ps.setInt(i, parameter);
	}

	@Override
	public Integer getResult(ResultSet rs, String columnName)
			throws SQLException {
		counter.increment();
		Integer ret = rs.getInt(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			return ret;
		}
	}

	@Override
	public Integer getResult(ResultSet rs, int columnIndex) throws SQLException {
		counter.increment();
		Integer ret = rs.getInt(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			return ret;
		}
	}

	@Override
	public Integer getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		counter.increment();
		Integer ret = cs.getInt(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			return ret;
		}
	}

	@Inject
	public void setCounter(Counter counter) {
		this.counter = counter;
	}
}
