/*
 *    Copyright 2010-2011 The MyBatis Team
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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.runners.model.InitializationError;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import com.google.inject.Module;

/**
 * 
 *
 * @version $Id: GuiceTestRunner.java 3980 2011-10-24 10:54:01Z simone.tripodi $
 */
public final class GuicePackageTestRunner extends AbstractGuiceTestRunner {

    public GuicePackageTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<Module> createMyBatisModule() {
        List<Module> modules = new ArrayList<Module>(3);

        modules.add(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);
        modules.add(new MyBatisModule() {

            @Override
            protected void initialize() {
                bindDataSourceProviderType(PooledDataSourceProvider.class);
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                addMapperClass(ContactMapper.class);
                addInterceptorClass(CountUpdateInterceptor.class);
                addTypeHandlerClasses("org.mybatis.guice");
            }

        });

        return modules;
    }

    @Override
    protected Properties createTestProperties() {
        final Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "test");
        myBatisProperties.setProperty("JDBC.username", "sa");
        myBatisProperties.setProperty("JDBC.password", "");
        myBatisProperties.setProperty("JDBC.autoCommit", "false");
        return myBatisProperties;
    }
}
