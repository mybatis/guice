package org.mybatis.guice.jta;

import java.util.List;

public interface JtaService {
    int insertTable(TableRow row);
    
    List<TableRow> selectAllTable();
	
	String getName();
}
