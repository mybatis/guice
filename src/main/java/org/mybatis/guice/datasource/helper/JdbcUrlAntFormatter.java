/*
 *    Copyright 2009-2025 the original author or authors.
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

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class JdbcUrlAntFormatter implements Provider<String> {

  private static final String VAR_BEGIN = "$";

  private static final String PIPE_SEPARATOR = "|";

  private final List<Provider<String>> appenders = new ArrayList<>();

  private final List<KeyResolver> resolvers = new ArrayList<>();

  /**
   * Constructs a new JdbcUrlAntFormatter based on the provided pattern.
   *
   * @param pattern
   *          the pattern for URL formatting
   */
  public JdbcUrlAntFormatter(final String pattern) {
    initializeAppender(pattern);
  }

  /**
   * Initializes the appenders based on the given pattern.
   *
   * @param pattern
   *          the pattern for URL formatting
   */
  private void initializeAppender(String pattern) {
    int previousIndex = 0;
    int currentIndex;
    while ((currentIndex = pattern.indexOf(VAR_BEGIN, previousIndex)) >= 0) {
      processPatternSubstring(pattern, previousIndex, currentIndex);
      previousIndex = updatePrevPosition(pattern, currentIndex);
    }
    appendRemainingPatternSubstring(pattern, previousIndex);
  }

  // Processes the substring of the pattern based on the currentIndex and previousIndex
  private void processPatternSubstring(String pattern, int previousIndex, int currentIndex) {
    if (currentIndex > 0) {
      appenders.add(Providers.of(pattern.substring(previousIndex, currentIndex)));
    }

    if (currentIndex == pattern.length() - 1) {
      appenders.add(Providers.of(VAR_BEGIN));
    } else if (pattern.charAt(currentIndex + 1) != '{') {
      handleNonCurlyBracePattern(pattern, currentIndex);
    } else {
      handleCurlyBracePattern(pattern, currentIndex);
    }
  }

  // Handles patterns without curly braces
  private void handleNonCurlyBracePattern(String pattern, int currentIndex) {
    if (pattern.charAt(currentIndex + 1) == '$') {
      appenders.add(Providers.of(VAR_BEGIN));
    } else {
      appenders.add(Providers.of(pattern.substring(currentIndex, currentIndex + 2)));
    }
  }

  // Handles patterns with curly braces
  private void handleCurlyBracePattern(String pattern, int currentIndex) {
    int endName = pattern.indexOf('}', currentIndex);
    if (endName < 0) {
      throw new IllegalArgumentException("Syntax error in property: " + pattern);
    }
    processKeyResolver(pattern, currentIndex, endName);
  }

  // Method to append KeyResolver based on the variable
  private void processKeyResolver(String pattern, int startPos, int endPos) {
    StringTokenizer keyTokenizer = new StringTokenizer(pattern.substring(startPos + 2, endPos), PIPE_SEPARATOR);
    String key = keyTokenizer.nextToken();
    String defaultValue = keyTokenizer.hasMoreTokens() ? keyTokenizer.nextToken() : null;
    KeyResolver resolver = new KeyResolver(key, defaultValue);
    appenders.add(resolver);
    resolvers.add(resolver);
  }

  // Updates the previous position based on the current index and pattern
  private int updatePrevPosition(String pattern, int currentIndex) {
    if (pattern.charAt(currentIndex + 1) == '{') {
      return pattern.indexOf('}', currentIndex) + 1;
    } else {
      return currentIndex + (pattern.charAt(currentIndex + 1) == '$' ? 2 : 1);
    }
  }

  // Appends the remaining substring of the pattern
  private void appendRemainingPatternSubstring(String pattern, int previousIndex) {
    if (previousIndex < pattern.length()) {
      appenders.add(Providers.of(pattern.substring(previousIndex)));
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
