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
package org.mybatis.guice.configuration;

import java.util.Map.Entry;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

/**
 * 
 *
 * @version $Id$
 */
final class EachTypeHandler extends Each<Entry<Class<?>, TypeHandler>> {

    public EachTypeHandler(final Configuration configuration) {
        super(configuration);
    }

    /**
     * {@inheritDoc}
     */
    public void each(Entry<Class<?>, TypeHandler> typeHandler) {
        this.getConfiguration().getTypeHandlerRegistry().register(typeHandler.getKey(), typeHandler.getValue());
    }

}
