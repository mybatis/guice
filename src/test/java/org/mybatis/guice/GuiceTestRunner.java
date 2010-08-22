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

import java.io.File;
import java.io.StringReader;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 * 
 *
 * @version $Id$
 */
public final class GuiceTestRunner extends BlockJUnit4ClassRunner {

    private final Injector injector;

    public GuiceTestRunner(Class<?> klass) throws InitializationError {
        super(klass);

        try {
            // db URL setup
            File tmp = File.createTempFile("mybatis-guice_TEST", ".dat");
            tmp.delete();

            final Properties myBatisProperties = new Properties();
            myBatisProperties.setProperty("mybatis.environment.id", "test");
            myBatisProperties.setProperty("JDBC.schema", tmp.getAbsolutePath());
            myBatisProperties.setProperty("derby.create", "true");
            myBatisProperties.setProperty("JDBC.username", "");
            myBatisProperties.setProperty("JDBC.password", "");
            myBatisProperties.setProperty("JDBC.autoCommit", "false");

            final Contact contact = new Contact();
            contact.setFirstName("John");
            contact.setLastName("Doe");

            // bindings
            this.injector = Guice.createInjector(JdbcHelper.Derby_Embedded,
                    new MyBatisModule(PooledDataSourceProvider.class)
                        .addSimpleAliases(Contact.class)
                        .addMapperClasses(ContactMapper.class),
                    new Module() {
                        public void configure(Binder binder) {
                            Names.bindProperties(binder, myBatisProperties);
                            binder.bind(Contact.class).toInstance(contact);
                        }
                    });

            // prepare the test db
            DataSource dataSource = injector.getInstance(DataSource.class);
            ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
            runner.setAutoCommit(true);
            runner.setStopOnError(true);
            runner.runScript(new StringReader(
                    "CREATE TABLE contact (id INT NOT NULL PRIMARY KEY GENERATED "
                    + "ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), first_name "
                    + "VARCHAR(20) NOT NULL, last_name VARCHAR(20) NOT NULL);"));
            runner.closeConnection();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred when initializing", e);
        }
    }

    @Override
    protected Object createTest() throws Exception {
        return this.injector.getInstance(this.getTestClass().getJavaClass());
    }

}
