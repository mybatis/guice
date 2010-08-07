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

import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.io.StringReader;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 *
 *
 * @version $Id$
 */
public final class MyBatisModuleTestCase {

    private final Contact contact = new Contact();

    @Inject
    private ContactMapperClient contactMapperClient;

    public void setContactMapperClient(ContactMapperClient contactMapperClient) {
        this.contactMapperClient = contactMapperClient;
    }

    @Before
    public void setUp() throws Exception {
        this.contact.setFirstName("John");
        this.contact.setLastName("Doe");

        // db URL setup
        File tmp = File.createTempFile("mybatis-guice_TEST", ".dat");
        tmp.delete();
        final String connectionURL = "jdbc:derby:"
            + tmp.getAbsolutePath()
            + ";create=true";

        final Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "test");
        myBatisProperties.setProperty("JDBC.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        myBatisProperties.setProperty("JDBC.url", connectionURL);
        myBatisProperties.setProperty("JDBC.username", "");
        myBatisProperties.setProperty("JDBC.password", "");
        myBatisProperties.setProperty("JDBC.autoCommit", "true");

        // bindings
        Injector injector = Guice.createInjector(new MyBatisModule(PooledDataSourceProvider.class)
                    .addSimpleAliases(Contact.class)
                    .addMapperClasses(ContactMapper.class),
                new Module() {
                    public void configure(Binder binder) {
                        Names.bindProperties(binder, myBatisProperties);
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

        injector.injectMembers(this);
    }

    @Test
    public void verifyNotNullMapper() {
        assertNotNull(this.contactMapperClient);
    }

}
