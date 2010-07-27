/*
 *    Copyright 2010 The iBaGuice Team
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

/**
 * Easy to use helper Module that alleviates users to write the boilerplate google-guice
 * bindings to create the SqlSessionFactory from XML configuration file.
 *
 * @version $Id$
 */
public final class XMLSqlSessionFactoryModule extends AbstractSqlSessionFactoryModule {

    /**
     * Creates a new module that binds all the needed modules to create the
     * SqlSessionFactory, injecting all the required components.
     */
    public XMLSqlSessionFactoryModule() {
        super(XMLSqlSessionFactoryProvider.class);
    }

}
