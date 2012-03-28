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
package org.mybatis.guice.datasource.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Provider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.util.Providers;

/**
 *
 *
 * @version $Id$
 */
final class Formatter implements Provider<String> {

    private static final String VAR_BEGIN = "$";

    private static final String PIPE_SEPARATOR = "|";

    private final List<Provider<String>> appenders = new ArrayList<Provider<String>>();

    private final List<KeyResolver> resolvers = new ArrayList<KeyResolver>();

    public Formatter(final String pattern) {
        int prev = 0;
        int pos;
        while ((pos = pattern.indexOf(VAR_BEGIN, prev)) >= 0) {
            if (pos > 0) {
                this.appenders.add(Providers.of(pattern.substring(prev, pos)));
            }
            if (pos == pattern.length() - 1) {
                this.appenders.add(Providers.of(VAR_BEGIN));
                prev = pos + 1;
            } else if (pattern.charAt(pos + 1) != '{') {
                if (pattern.charAt(pos + 1) == '$') {
                    this.appenders.add(Providers.of(VAR_BEGIN));
                    prev = pos + 2;
                } else {
                    this.appenders.add(Providers.of(pattern.substring(pos, pos + 2)));
                    prev = pos + 2;
                }
            } else {
                int endName = pattern.indexOf('}', pos);
                if (endName < 0) {
                    throw new IllegalArgumentException("Syntax error in property: " + pattern);
                }
                StringTokenizer keyTokenizer = new StringTokenizer(pattern.substring(pos + 2, endName), PIPE_SEPARATOR);
                String key = keyTokenizer.nextToken();
                String defaultValue = null;
                if (keyTokenizer.hasMoreTokens()) {
                    defaultValue = keyTokenizer.nextToken();
                }
                KeyResolver resolver = new KeyResolver(key, defaultValue);
                this.appenders.add(resolver);
                this.resolvers.add(resolver);
                prev = endName + 1;
            }
        }
        if (prev < pattern.length()) {
            this.appenders.add(Providers.of(pattern.substring(prev)));
        }
    }

    @Inject
    public void setInjector(Injector injector) {
        for (KeyResolver resolver : this.resolvers) {
            resolver.setInjector(injector);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String get() {
        StringBuilder buffer = new StringBuilder();
        for (Provider<String> appender : this.appenders) {
            buffer.append(appender.get());
        }
        return buffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.appenders.toString();
    }

}
