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
package org.mybatis.guice;

public class CustomType {

  private Long value;

  public CustomType() {
  }

  public CustomType(long currentTimeMillis) {
    setValue(currentTimeMillis);
  }

  /**
   * @return the value
   */
  public Long getValue() {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(Long value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return value != null ? value.toString() : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CustomType other = (CustomType) obj;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (Math.abs(value - other.value) > 5)
      return false;
    return true;
  }

}
