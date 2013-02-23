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
	
	public void setParameter(PreparedStatement ps, int i, Integer parameter,
			JdbcType jdbcType) throws SQLException {
		counter.increment();
		if (parameter == null) parameter = 0; 
		ps.setInt(i, parameter);
	}
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
	public Integer getResult(ResultSet rs, int columnIndex) throws SQLException {
		counter.increment();
		Integer ret = rs.getInt(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			return ret;
		}
	}
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
