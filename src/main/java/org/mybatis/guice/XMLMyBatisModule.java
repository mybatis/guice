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

import static org.mybatis.guice.utils.IterableUtils.iterate;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.ognl.DefaultMemberAccess;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.google.inject.Module;
import com.google.inject.spi.Message;

/**
 *
 * @version $Id$
 */
public final class XMLMyBatisModule extends AbstractMyBatisModule {

    private static final String KNOWN_MAPPERS = "mapperRegistry.knownMappers";

    private static final String TYPE_HANDLERS = "typeHandlerRegistry.TYPE_HANDLER_MAP.values()";

    private static final String INTERCEPTORS = "interceptorChain.interceptors";

    private final String classPathResource;

    private final String environmentId;

    private final Properties properties;

    private XMLMyBatisModule(String classPathResource, String environmentId, Properties properties) {
        this.classPathResource = classPathResource;
        this.environmentId = environmentId;
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
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

            // bind mappers
            Set<Class<?>> mapperClasses = (Set<Class<?>>) Ognl.getValue(KNOWN_MAPPERS, context, configuration);
            iterate(mapperClasses, new EachMapper(this.binder()));

            // request injection for type handlers
            Collection<Map<JdbcType, TypeHandler>> mappedTypeHandlers = (Collection<Map<JdbcType, TypeHandler>>) Ognl.getValue(TYPE_HANDLERS, context, configuration);
            for (Map<JdbcType, TypeHandler> mappedTypeHandler: mappedTypeHandlers) {
                iterate(mappedTypeHandler.values(), new EachRequestInjection<TypeHandler>(this.binder()));
            }

            // request injection for interceptors
            Collection<Interceptor> interceptors = (Collection<Interceptor>) Ognl.getValue(INTERCEPTORS, context, configuration);
            iterate(interceptors, new EachRequestInjection<Interceptor>(this.binder()));
        } catch (Exception e) {
            this.addError(new Message(new ArrayList<Object>(), "Impossible to read classpath resource '"
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

    /**
     * The {@link XMLMyBatisModule} Builder.
     *
     * By default the builder looks for {@code mybatis-config.xml} resource in the root class path,
     * {@code development} as environment id and an empty variables definition.
     */
    public static final class Builder {

        private static final String DEFAULT_CONFIG_RESOURCE = "mybatis-config.xml";

        private static final String DEFAULT_ENVIRONMENT_ID = "development";

        private String classPathResource = DEFAULT_CONFIG_RESOURCE;

        private String environmentId = DEFAULT_ENVIRONMENT_ID;

        private Properties properties = new Properties();

        /**
         * Set the MyBatis configuration class path resource.
         *
         * @param classPathResource the MyBatis configuration class path resource.
         * @return this {@code Builder} instance.
         */
        public Builder setClassPathResource(String classPathResource) {
            if (classPathResource == null) {
                throw new IllegalArgumentException("Parameter 'classPathResource' must be not null");
            }
            this.classPathResource = classPathResource;
            return this;
        }

        /**
         * Set the MyBatis configuration environment id.
         *
         * @param environmentId the MyBatis configuration environment id.
         * @return this {@code Builder} instance.
         */
        public Builder setEnvironmentId(String environmentId) {
            if (environmentId == null) {
                throw new IllegalArgumentException("Parameter 'environmentId' must be not null");
            }
            this.environmentId = environmentId;
            return this;
        }

        /**
         * Add the variables will be used to replace placeholders in the MyBatis configuration.
         *
         * @param properties the variables will be used to replace placeholders in the MyBatis configuration.
         * @return this {@code Builder} instance.
         */
        public Builder addProperties(Properties properties) {
            if (properties != null) {
                this.properties.putAll(properties);
            }
            return this;
        }

        /**
         * Create a new {@link XMLMyBatisModule} instance based on this {@link Builder}
         * instance configuration.
         *
         * @return a new {@link XMLMyBatisModule} instance.
         */
        public Module create() {
            return new XMLMyBatisModule(this.classPathResource, this.environmentId, this.properties);
        }

    }

}
