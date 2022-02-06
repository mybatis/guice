/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.datasource.helper;

import com.google.inject.Injector;
import com.google.inject.util.Providers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Provider;

public final class JdbcUrlAntFormatter implements Provider<String> {

  private static final String VAR_BEGIN = "$";

  private static final String PIPE_SEPARATOR = "|";

  private final List<Provider<String>> appenders = new ArrayList<Provider<String>>();

  private final List<KeyResolver> resolvers = new ArrayList<KeyResolver>();

  /**
   * Instantiates a new jdbc url ant formatter.
   *
   * @param pattern
   *          the pattern
   */
  public JdbcUrlAntFormatter(final String pattern) {
    int prev = 0;
    int pos;
    while ((pos = pattern.indexOf(VAR_BEGIN, prev)) >= 0) {
      if (pos > 0) {
        appenders.add(Providers.of(pattern.substring(prev, pos)));
      }
      if (pos == pattern.length() - 1) {
        appenders.add(Providers.of(VAR_BEGIN));
        prev = pos + 1;
      } else if (pattern.charAt(pos + 1) != '{') {
        if (pattern.charAt(pos + 1) == '$') {
          appenders.add(Providers.of(VAR_BEGIN));
          prev = pos + 2;
        } else {
          appenders.add(Providers.of(pattern.substring(pos, pos + 2)));
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
        appenders.add(resolver);
        resolvers.add(resolver);
        prev = endName + 1;
      }
    }
    if (prev < pattern.length()) {
      appenders.add(Providers.of(pattern.substring(prev)));
    }
  }

  /**
   * Sets the injector.
   *
   * @param injector
   *          the new injector
   */
  @Inject
  public void setInjector(Injector injector) {
    for (KeyResolver resolver : resolvers) {
      resolver.setInjector(injector);
    }
  }

  @Override
  public String get() {
    StringBuilder buffer = new StringBuilder();
    for (Provider<String> appender : appenders) {
      buffer.append(appender.get());
    }
    return buffer.toString();
  }

  @Override
  public String toString() {
    return appenders.toString();
  }

}
