/*
 *    Copyright 2009-2010 The Rocoto Team
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
package org.mybatis.guice.datasource.helper;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * 
 *
 * @version $Id$
 */
public final class JdbcHelperModule extends AbstractModule {

    private static final Named JDBC_DRIVER = Names.named("JDBC.driver");

    private static final Named JDBC_URL = Names.named("JDBC.url");

    private final JdbcHelper helper;

    public JdbcHelperModule(JdbcHelper helper) {
        if (helper == null) {
            throw new IllegalArgumentException("Parameter 'helper' must not be null");
        }
        this.helper = helper;
    }

    @Override
    protected void configure() {
        this.bindConstant().annotatedWith(JDBC_DRIVER).to(this.helper.getDriverClass());
        this.bind(Key.get(String.class, JDBC_URL)).toProvider(new Formatter(this.helper.getUrlTemplate()));
    }

}
