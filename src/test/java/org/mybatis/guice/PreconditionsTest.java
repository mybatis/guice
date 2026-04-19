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
package org.mybatis.guice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

class PreconditionsTest {

  @Test
  void checkArgument() {
    assertDoesNotThrow(() -> Preconditions.checkArgument(true));
    assertThrows(IllegalArgumentException.class, () -> Preconditions.checkArgument(false));
  }

  @Test
  void checkArgument_WithErrorMessage() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> Preconditions.checkArgument(false, "boom"));
    assertEquals("boom", e.getMessage());
  }

  @Test
  void checkArgument_WithTemplate() {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> Preconditions.checkArgument(false, "value %s", 10));
    assertEquals("value 10", e.getMessage());
  }

  @Test
  void checkState() {
    assertDoesNotThrow(() -> Preconditions.checkState(true));
    assertThrows(IllegalStateException.class, () -> Preconditions.checkState(false));
  }

  @Test
  void checkState_WithErrorMessage() {
    assertDoesNotThrow(() -> Preconditions.checkState(true, "boom"));
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> Preconditions.checkState(false, "boom"));
    assertEquals("boom", e.getMessage());
  }

  @Test
  void checkState_WithTemplate() {
    assertDoesNotThrow(() -> Preconditions.checkState(true, "value %s", 11));
    IllegalStateException e = assertThrows(IllegalStateException.class,
        () -> Preconditions.checkState(false, "value %s", 11));
    assertEquals("value 11", e.getMessage());
  }

  @Test
  void checkNotNull() {
    Object value = new Object();
    assertSame(value, Preconditions.checkNotNull(value));
    assertThrows(NullPointerException.class, () -> Preconditions.checkNotNull(null));
  }

  @Test
  void checkNotNull_WithMessage() {
    Object value = new Object();
    assertSame(value, Preconditions.checkNotNull(value, "missing"));
    NullPointerException e = assertThrows(NullPointerException.class,
        () -> Preconditions.checkNotNull(null, "missing"));
    assertEquals("missing", e.getMessage());
  }

  @Test
  void checkNotNull_WithTemplate() {
    Object value = new Object();
    assertSame(value, Preconditions.checkNotNull(value, "missing %s", "name"));
    NullPointerException e = assertThrows(NullPointerException.class,
        () -> Preconditions.checkNotNull(null, "missing %s", "name"));
    assertEquals("missing name", e.getMessage());
  }

  @Test
  void checkContentsNotNull() {
    List<String> values = Arrays.asList("a", "b");
    assertSame(values, Preconditions.checkContentsNotNull(values));

    assertThrows(NullPointerException.class, () -> Preconditions.checkContentsNotNull((Iterable<?>) null));
    assertThrows(NullPointerException.class, () -> Preconditions.checkContentsNotNull(Arrays.asList("a", null)));
  }

  @Test
  void checkContentsNotNull_WithMessage() {
    List<String> values = Arrays.asList("a", "b");
    assertSame(values, Preconditions.checkContentsNotNull(values, "x"));
    NullPointerException e = assertThrows(NullPointerException.class,
        () -> Preconditions.checkContentsNotNull(Arrays.asList("a", null), "x"));
    assertEquals("x", e.getMessage());
  }

  @Test
  void checkContentsNotNull_WithTemplate() {
    List<String> values = Arrays.asList("a", "b");
    assertSame(values, Preconditions.checkContentsNotNull(values, "bad %s", "value"));
    NullPointerException e = assertThrows(NullPointerException.class,
        () -> Preconditions.checkContentsNotNull(Arrays.asList("a", null), "bad %s", "value"));
    assertEquals("bad value", e.getMessage());
  }

  @Test
  void checkContentsNotNull_CollectionContainsThrowsNullPointerException() {
    AbstractCollection<String> collection = new AbstractCollection<>() {
      @Override
      public Iterator<String> iterator() {
        return Collections.singleton("ok").iterator();
      }

      @Override
      public int size() {
        return 1;
      }

      @Override
      public boolean contains(Object o) {
        throw new NullPointerException("intentional");
      }
    };

    assertSame(collection, Preconditions.checkContentsNotNull(collection));
  }

  @Test
  void checkContentsNotNull_NonCollectionIterable() {
    Iterable<String> iterable = () -> Arrays.asList("a", "b").iterator();
    Iterable<String> iterableWithNull = () -> Arrays.asList("a", null).iterator();

    assertSame(iterable, Preconditions.checkContentsNotNull(iterable));
    assertThrows(NullPointerException.class, () -> Preconditions.checkContentsNotNull(iterableWithNull));
  }

  @Test
  void checkElementIndex() {
    assertDoesNotThrow(() -> Preconditions.checkElementIndex(0, 1));
    assertThrows(IndexOutOfBoundsException.class, () -> Preconditions.checkElementIndex(-1, 1, "idx"));
    assertThrows(IndexOutOfBoundsException.class, () -> Preconditions.checkElementIndex(1, 1, "idx"));
    assertThrows(IllegalArgumentException.class, () -> Preconditions.checkElementIndex(0, -1));
  }

  @Test
  void checkPositionIndex() {
    assertDoesNotThrow(() -> Preconditions.checkPositionIndex(1, 1));
    assertThrows(IndexOutOfBoundsException.class, () -> Preconditions.checkPositionIndex(-1, 1, "p"));
    assertThrows(IndexOutOfBoundsException.class, () -> Preconditions.checkPositionIndex(2, 1, "p"));
    assertThrows(IllegalArgumentException.class, () -> Preconditions.checkPositionIndex(0, -1));
  }

  @Test
  void checkPositionIndexes() {
    assertDoesNotThrow(() -> Preconditions.checkPositionIndexes(0, 1, 1));
    assertThrows(IndexOutOfBoundsException.class, () -> Preconditions.checkPositionIndexes(2, 1, 2));
  }

  @Test
  void format_WithAndWithoutExtraArgs() {
    assertEquals("hello world", Preconditions.format("hello %s", "world"));
    assertEquals("hello %s", Preconditions.format("hello %s"));
    assertEquals("a [b, c]", Preconditions.format("a", "b", "c"));
  }

  @Test
  void privateConstructor() throws Exception {
    Constructor<Preconditions> constructor = Preconditions.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    constructor.newInstance();
  }
}
