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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.ognl.DefaultMemberAccess;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.google.inject.Binder;
import com.google.inject.spi.Message;

/**
 *
 * @version $Id$
 */
public final class XMLMyBatisModule extends AbstractMyBatisModule {

    private static final String KNOWN_MAPPERS = "mapperRegistry.knownMappers";

    private static final String JDBC_TYPE_HANDLERS = "typeHandlerRegistry.JDBC_TYPE_HANDLER_MAP.values()";

    private static final String TYPE_HANDLERS = "typeHandlerRegistry.TYPE_HANDLER_MAP.values()";

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
        try {
            reader = Resources.getResourceAsReader(this.getClass().getClassLoader(), this.classPathResource);
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader,
                    this.environmentId,
                    this.properties);
            this.bind(SqlSessionFactory.class).toInstance(sessionFactory);

            Configuration configuration = sessionFactory.getConfiguration();

            OgnlContext context = new OgnlContext();
            context.setMemberAccess(new DefaultMemberAccess(true, true, true));
            context.setRoot(configuration);

            @SuppressWarnings("unchecked")
            Set<Class<?>> mapperClasses = (Set<Class<?>>) Ognl.getValue(KNOWN_MAPPERS, context, configuration);
            MapperProvider.bind(this.binder(), mapperClasses);

            requestInjection(this.binder(), (Collection<?>) Ognl.getValue(JDBC_TYPE_HANDLERS, context, configuration));
            @SuppressWarnings("unchecked")
            Collection<Map<JdbcType, TypeHandler>> mappedTypeHandlers = (Collection<Map<JdbcType, TypeHandler>>) Ognl.getValue(TYPE_HANDLERS, context, configuration);
            Collection<TypeHandler> typeHandlers = new LinkedList<TypeHandler>();
            for (Map<JdbcType, TypeHandler> mappedTypeHandler: mappedTypeHandlers) {
                for (TypeHandler typeHandler: mappedTypeHandler.values()) {
                    typeHandlers.add(typeHandler);
                }
            }
            requestInjection(this.binder(), typeHandlers);
        } catch (Exception e) {
            this.addError(new Message(new ArrayList<Object>(),"Impossible to read classpath resource '"
                    + this.classPathResource
                    + "', see nested exceptions", e));
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // close quietly
                }
            }
        }
    }

    private static void requestInjection(Binder binder, Collection<?> injectees) {
        for (Object injectee : injectees) {
            binder.requestInjection(injectee);
        }
    }

}
