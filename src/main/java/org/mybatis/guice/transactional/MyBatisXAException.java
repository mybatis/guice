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
package org.mybatis.guice.transactional;

import javax.transaction.xa.XAException;

public class MyBatisXAException extends XAException {
  private static final long serialVersionUID = -7280133560046132709L;

  /**
   * Instantiates a new my batis XA exception.
   *
   * @param message
   *          the message
   * @param errorCode
   *          the error code
   */
  public MyBatisXAException(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  /**
   * Instantiates a new my batis XA exception.
   *
   * @param message
   *          the message
   * @param errorCode
   *          the error code
   * @param t
   *          the t
   */
  public MyBatisXAException(String message, int errorCode, Throwable t) {
    super(message);
    this.errorCode = errorCode;
    initCause(t);
  }

}
