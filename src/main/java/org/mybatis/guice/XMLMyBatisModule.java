/*
 *    Copyright 2010-2012 The MyBatis Team
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

import static com.google.inject.internal.util.$Preconditions.checkArgument;
import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.apache.ibatis.ognl.Ognl.getValue;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.ognl.DefaultMemberAccess;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * Easy to use helper Module that alleviates users to write the boilerplate
 * google-guice bindings to create the SqlSessionFactory, via XML configuration.
 *
 * @version $Id$
 */
public abstract class XMLMyBatisModule extends AbstractMyBatisModule {

    private static final String DEFAULT_CONFIG_RESOURCE = "mybatis-config.xml";

    private static final String DEFAULT_ENVIRONMENT_ID = "development";

    private static final String KNOWN_MAPPERS = "mapperRegistry.knownMappers";

    private static final String TYPE_HANDLERS = "typeHandlerRegistry.TYPE_HANDLER_MAP.values()";

    private static final String ALL_TYPE_HANDLERS = "typeHandlerRegistry.ALL_TYPE_HANDLERS_MAP.values()";

    private static final String INTERCEPTORS = "interceptorChain.interceptors";

    private String classPathResource = DEFAULT_CONFIG_RESOURCE;

    private String environmentId = DEFAULT_ENVIRONMENT_ID;

    private Properties properties = new Properties();

    /**
     * Set the MyBatis configuration class path resource.
     *
     * @param classPathResource the MyBatis configuration class path resource
     */
    protected final void setClassPathResource(String classPathResource) {
        checkArgument( classPathResource != null, "Parameter 'classPathResource' must be not null");
        this.classPathResource = classPathResource;
    }

    /**
     * Set the MyBatis configuration environment id.
     *
     * @param environmentId the MyBatis configuration environment id
     */
    protected final void setEnvironmentId(String environmentId) {
        checkArgument( environmentId != null, "Parameter 'environmentId' must be not null");
        this.environmentId = environmentId;
    }

    /**
     * Add the variables will be used to replace placeholders in the MyBatis configuration.
     *
     * @param properties the variables will be used to replace placeholders in the MyBatis configuration
     */
    protected final void addProperties(Properties properties) {
        if (properties != null) {
            this.properties.putAll(properties);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    final void internalConfigure() {
        this.initialize();

        Reader reader = null;
        try {
            reader = getResourceAsReader(getResourceClassLoader(), classPathResource);
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader,
                    environmentId,
                    properties);
            bind(SqlSessionFactory.class).toInstance(sessionFactory);

            Configuration configuration = sessionFactory.getConfiguration();

            OgnlContext context = new OgnlContext();
            context.setMemberAccess(new DefaultMemberAccess(true, true, true));
            context.setRoot(configuration);

            // bind mappers
            Object introspectedMapperClasses = getValue(KNOWN_MAPPERS, context, configuration);
            // mybatis 3.2 contains a HashMap, previous versions a HashSet
            Collection<Class<?>> mapperClasses = null;
            if (Map.class.isAssignableFrom(introspectedMapperClasses.getClass())) {
              mapperClasses = ((Map) introspectedMapperClasses).keySet();
            } else {
              mapperClasses = (Collection) introspectedMapperClasses; 
            }
            for (Class<?> mapperType : mapperClasses) {
                bindMapper(mapperType);
            }

            // request injection for type handlers
            @SuppressWarnings("unchecked")
            Collection<Map<JdbcType, TypeHandler<?>>> mappedTypeHandlers = (Collection<Map<JdbcType, TypeHandler<?>>>) getValue(TYPE_HANDLERS, context, configuration);
            for (Map<JdbcType, TypeHandler<?>> mappedTypeHandler: mappedTypeHandlers) {
                for (TypeHandler<?> handler : mappedTypeHandler.values()) {
                    requestInjection(handler);
                }
            }
            @SuppressWarnings("unchecked")
            Collection<TypeHandler<?>> allTypeHandlers = (Collection<TypeHandler<?>>) getValue(ALL_TYPE_HANDLERS, context, configuration);
            for (TypeHandler<?> handler: allTypeHandlers) {
                requestInjection(handler);
            }

            // request injection for interceptors
            @SuppressWarnings("unchecked")
            Collection<Interceptor> interceptors = (Collection<Interceptor>) getValue(INTERCEPTORS, context, configuration);
            for (Interceptor interceptor : interceptors) {
                requestInjection(interceptor);
            }
        } catch (Exception e) {
            addError("Impossible to read classpath resource '%s', see nested exceptions: %s",
                    classPathResource,
                    e.getMessage());
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

}
