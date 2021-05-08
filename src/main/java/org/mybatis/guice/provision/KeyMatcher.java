/*
 *    Copyright 2009-2021 the original author or authors.
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
package org.mybatis.guice.provision;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.matcher.AbstractMatcher;

public final class KeyMatcher<T> extends AbstractMatcher<Binding<?>> {
  private final Key<T> key;

  KeyMatcher(Key<T> key) {
    this.key = key;
  }

  @Override
  public boolean matches(Binding<?> t) {
    return key.getTypeLiteral().getRawType().isAssignableFrom(t.getKey().getTypeLiteral().getRawType());
  }

  public static <T> KeyMatcher<T> create(Key<T> key) {
    return new KeyMatcher<T>(key);
  }
}
