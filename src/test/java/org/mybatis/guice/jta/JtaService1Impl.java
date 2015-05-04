package org.mybatis.guice.jta;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.guice.transactional.Transactional;

public class JtaService1Impl {
    private final Log log = LogFactory.getLog(getClass());
    
	@Inject JtaMapper mapper;
	@Inject SqlSessionFactory factory;

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
