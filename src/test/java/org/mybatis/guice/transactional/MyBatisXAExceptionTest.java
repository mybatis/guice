/*
 *    Copyright 2009-2026 the original author or authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import javax.transaction.xa.XAException;

import org.junit.jupiter.api.Test;

class MyBatisXAExceptionTest {

  @Test
  void constructor_WithMessageAndErrorCode() {
    MyBatisXAException exception = new MyBatisXAException("failure", XAException.XAER_RMERR);
    assertEquals("failure", exception.getMessage());
    assertEquals(XAException.XAER_RMERR, exception.errorCode);
  }

  @Test
  void constructor_WithCause() {
    IllegalStateException cause = new IllegalStateException("cause");
    MyBatisXAException exception = new MyBatisXAException("failure", XAException.XAER_PROTO, cause);
    assertEquals("failure", exception.getMessage());
    assertEquals(XAException.XAER_PROTO, exception.errorCode);
    assertSame(cause, exception.getCause());
  }
}
