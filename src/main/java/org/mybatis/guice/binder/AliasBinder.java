/*
 *    Copyright 2009-2021 the original author or authors.
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
package org.mybatis.guice.binder;

/**
 * Allows bind a {@code Class} to an already defined type alias.
 */
public interface AliasBinder {

  /**
   * Binds a {@code Class} to an already defined type alias.
   *
   * @param type
   *          The {@code Class} has to be bound to the alias
   */
  void to(Class<?> type);

}
