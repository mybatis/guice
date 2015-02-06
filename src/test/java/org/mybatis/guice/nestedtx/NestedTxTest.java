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
package org.mybatis.guice.nestedtx;

import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

public class NestedTxTest {

    private Injector injector;
    private NestedTxService service;

    @Before
    public void setup() throws Exception {
        injector = Guice.createInjector(new MyBatisModule() {
            @Override
            protected void initialize() {
                bindDataSourceProviderType(PooledDataSourceProvider.class);
                bindTransactionFactoryType(JdbcTransactionFactory.class);

                install(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);

                Properties connectionProps = new Properties();
                connectionProps.setProperty("mybatis.environment.id", "jdbc");
                connectionProps.setProperty("JDBC.username", "sa");
                connectionProps.setProperty("JDBC.password", "");
                connectionProps.setProperty("JDBC.autoCommit", "false");

                Names.bindProperties(binder(), connectionProps);

                addMapperClass(NestedTxMapper.class);
                bind(NestedTxService.class);
            }
        });

        // prepare the test db
        Environment environment = this.injector
                .getInstance(SqlSessionFactory.class).getConfiguration()
                .getEnvironment();
        DataSource dataSource = environment.getDataSource();
        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
        runner.setAutoCommit(true);
        runner.setStopOnError(true);
        runner.runScript(getResourceAsReader("org/mybatis/guice/nestedtx/setupdb.sql"));
        runner.closeConnection();

        service = injector.getInstance(NestedTxService.class);
    }

    @Test
    public void testGoodInserts() {
        service.goodInserts();

        List<TableRow> tableRows = service.selectAllTable1();
        assertEquals(1, tableRows.size());

        tableRows = service.selectAllTable2();
        assertEquals(2, tableRows.size());
    }

    @Test
    public void testBadInsertRollbackAllRows() {
        try {
            service.badInsertRollbackAllRows();
        } catch (Exception e) {
            // ignore - should rollback all
        }

        List<TableRow> tableRows = service.selectAllTable1();
        assertEquals(0, tableRows.size());

        tableRows = service.selectAllTable2();
        assertEquals(0, tableRows.size());
    }

    @Test
    public void testIgnoreBadInsert() {
        service.ignoreBadInsert();

        List<TableRow> tableRows = service.selectAllTable1();
        assertEquals(1, tableRows.size());

        tableRows = service.selectAllTable2();
        assertEquals(1, tableRows.size());
    }

    @Test
    public void testCorrectBadInsert() {
        service.correctBadInsert();

        List<TableRow> tableRows = service.selectAllTable1();
        assertEquals(1, tableRows.size());

        tableRows = service.selectAllTable2();
        assertEquals(2, tableRows.size());
    }
}
