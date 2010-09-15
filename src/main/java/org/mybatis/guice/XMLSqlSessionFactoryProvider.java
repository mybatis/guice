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

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 *
 * @version $Id$
 */
@Singleton
final class XMLSqlSessionFactoryProvider implements Provider<SqlSessionFactory> {

    /**
     * The SqlSessionFactory reference.
     */
    private final SqlSessionFactory factory;

    /**
     * Creates a new SqlSessionFactory reading the XML classpath resource
     * configuration.
     *
     * @param resource the XML classpath resource configuration.
     */
    @Inject
    public XMLSqlSessionFactoryProvider(@Named("mybatis.classpathResource") final String resource) {
        try {
            Reader reader = Resources.getResourceAsReader(this.getClass().getClassLoader(), resource);
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            this.factory = builder.build(reader);
        } catch (IOException e) {
            throw new RuntimeException("Impossible to read classpath resource '"
                    + resource
                    + "', see nested exceptions", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public SqlSessionFactory get() {
        return this.factory;
    }

}
