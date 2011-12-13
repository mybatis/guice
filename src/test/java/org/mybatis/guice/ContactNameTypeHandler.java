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
public class ContactNameTypeHandler implements TypeHandler<String> {
	private Counter counter;
	
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		counter.increment();
		ps.setString(i, parameter);
	}
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		counter.increment();
		String ret = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			return ret;
		}
	}
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		counter.increment();
		String ret = rs.getString(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			return ret;
		}
	}
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		counter.increment();
		String ret = cs.getString(columnIndex);
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
