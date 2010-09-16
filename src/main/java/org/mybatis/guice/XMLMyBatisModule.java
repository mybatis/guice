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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.inject.spi.Message;

/**
 *
 * @version $Id$
 */
public final class XMLMyBatisModule extends AbstractMyBatisModule {

    private static final String MAPPER_REGISTRY_FIELD = "mapperRegistry";

    private static final String KNOWN_MAPPERS_FIELD = "knownMappers";

    private final String classPathResource;

    private final String environmentId;

    private Properties properties;

    public XMLMyBatisModule(String classPathResource) {
        this(classPathResource, null, null);
    }

    public XMLMyBatisModule(String classPathResource, String environmentId) {
        this(classPathResource, environmentId, null);
    }

    public XMLMyBatisModule(String classPathResource, Properties properties) {
        this(classPathResource, null, properties);
    }

    public XMLMyBatisModule(String classPathResource, String environmentId, Properties properties) {
        if (classPathResource == null) {
            throw new IllegalArgumentException("Parameter 'classPathResource' must be not null");
        }
        this.classPathResource = classPathResource;
        this.environmentId = environmentId;
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        Reader reader = null;
        Field mapperRegistryField = null;
        Field knownMappersField = null;
        try {
            reader = Resources.getResourceAsReader(this.getClass().getClassLoader(), this.classPathResource);
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader,
                    this.environmentId,
                    this.properties);
            this.bind(SqlSessionFactory.class).toInstance(sessionFactory);

            Configuration configuration = sessionFactory.getConfiguration();

            mapperRegistryField = configuration.getClass().getDeclaredField(MAPPER_REGISTRY_FIELD);
            setAccessibility(mapperRegistryField, true);

            MapperRegistry mapperRegistry = (MapperRegistry) mapperRegistryField.get(configuration);

            knownMappersField = mapperRegistry.getClass().getDeclaredField(KNOWN_MAPPERS_FIELD);
            setAccessibility(knownMappersField, true);

            @SuppressWarnings("unchecked")
            Set<Class<?>> mapperClasses = (Set<Class<?>>) knownMappersField.get(mapperRegistry);
            MapperProvider.bind(this.binder(), mapperClasses);
        } catch (Exception e) {
            this.addError(new Message(new ArrayList<Object>(),"Impossible to read classpath resource '"
                    + this.classPathResource
                    + "', see nested exceptions", e));
        } finally {
            setAccessibility(knownMappersField, false);
            setAccessibility(mapperRegistryField, false);
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // close quietly
                }
            }
        }
    }

    private static void setAccessibility(Field field, boolean accessible) {
        if (field != null) {
            field.setAccessible(accessible);
        }
    }

}
