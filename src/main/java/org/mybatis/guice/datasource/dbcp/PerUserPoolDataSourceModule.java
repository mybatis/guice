/**
 *    Copyright 2009-2019 the original author or authors.
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
package org.mybatis.guice.datasource.dbcp;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.inject.Provider;

public final class PerUserPoolDataSourceModule extends AbstractModule {

  private static final TypeLiteral<Map<String, Boolean>> STRING_BOOLEAN_MAP = new TypeLiteral<Map<String, Boolean>>() {
  };

  private static final TypeLiteral<Map<String, Integer>> STRING_INTEGER_MAP = new TypeLiteral<Map<String, Integer>>() {
  };

  private static final TypeLiteral<Map<String, Long>> STRING_LONG_MAP = new TypeLiteral<Map<String, Long>>() {
  };

  private final Class<? extends Provider<Map<String, Boolean>>> perUserDefaultAutoCommitProviderClass;

  private final Class<? extends Provider<Map<String, Boolean>>> perUserDefaultReadOnlyProviderClass;

  private final Class<? extends Provider<Map<String, Integer>>> perUserDefaultTransactionIsolationProviderClass;

  private final Class<? extends Provider<Map<String, Integer>>> perUserMaxActiveProviderClass;

  private final Class<? extends Provider<Map<String, Integer>>> perUserMaxIdleProviderClass;

  private final Class<? extends Provider<Map<String, Long>>> perUserMaxWaitMillisProviderClass;

  private PerUserPoolDataSourceModule(
      Class<? extends Provider<Map<String, Boolean>>> perUserDefaultAutoCommitProviderClass,
      Class<? extends Provider<Map<String, Boolean>>> perUserDefaultReadOnlyProviderClass,
      Class<? extends Provider<Map<String, Integer>>> perUserDefaultTransactionIsolationProviderClass,
      Class<? extends Provider<Map<String, Integer>>> perUserMaxActiveProviderClass,
      Class<? extends Provider<Map<String, Integer>>> perUserMaxIdleProviderClass,
      Class<? extends Provider<Map<String, Long>>> perUserMaxWaitMillisProviderClass) {
    this.perUserDefaultAutoCommitProviderClass = perUserDefaultAutoCommitProviderClass;
    this.perUserDefaultReadOnlyProviderClass = perUserDefaultReadOnlyProviderClass;
    this.perUserDefaultTransactionIsolationProviderClass = perUserDefaultTransactionIsolationProviderClass;
    this.perUserMaxActiveProviderClass = perUserMaxActiveProviderClass;
    this.perUserMaxIdleProviderClass = perUserMaxIdleProviderClass;
    this.perUserMaxWaitMillisProviderClass = perUserMaxWaitMillisProviderClass;
  }

  @Override
  protected void configure() {
    doBind(perUserDefaultAutoCommitProviderClass, STRING_BOOLEAN_MAP, PerUserDefaultAutoCommit.class);
    doBind(perUserDefaultReadOnlyProviderClass, STRING_BOOLEAN_MAP, PerUserDefaultReadOnly.class);
    doBind(perUserDefaultTransactionIsolationProviderClass, STRING_INTEGER_MAP,
        PerUserDefaultTransactionIsolation.class);
    doBind(perUserMaxActiveProviderClass, STRING_INTEGER_MAP, PerUserMaxTotal.class);
    doBind(perUserMaxIdleProviderClass, STRING_INTEGER_MAP, PerUserMaxIdle.class);
    doBind(perUserMaxWaitMillisProviderClass, STRING_LONG_MAP, PerUserMaxWaitMillis.class);
  }

  private <T> void doBind(Class<? extends Provider<T>> providerClass, TypeLiteral<T> typeLiteral,
      Class<? extends Annotation> annotation) {
    if (providerClass != null) {
      bind(typeLiteral).annotatedWith(annotation).toProvider(providerClass).in(Scopes.SINGLETON);
    }
  }

  public static final class Builder {

    private Class<? extends Provider<Map<String, Boolean>>> perUserDefaultAutoCommitProviderClass;

    private Class<? extends Provider<Map<String, Boolean>>> perUserDefaultReadOnlyProviderClass;

    private Class<? extends Provider<Map<String, Integer>>> perUserDefaultTransactionIsolationProviderClass;

    private Class<? extends Provider<Map<String, Integer>>> perUserMaxActiveProviderClass;

    private Class<? extends Provider<Map<String, Integer>>> perUserMaxIdleProviderClass;

    private Class<? extends Provider<Map<String, Long>>> perUserMaxWaitProviderClass;

    /**
     * Sets the per user default auto commit provider class.
     *
     * @param perUserDefaultAutoCommitProviderClass
     *          the perUserDefaultAutoCommitProviderClass to set
     * @return the builder
     */
    public Builder setPerUserDefaultAutoCommitProviderClass(
        Class<? extends Provider<Map<String, Boolean>>> perUserDefaultAutoCommitProviderClass) {
      this.perUserDefaultAutoCommitProviderClass = perUserDefaultAutoCommitProviderClass;
      return this;
    }

    /**
     * Sets the per user default read only provider class.
     *
     * @param perUserDefaultReadOnlyProviderClass
     *          the perUserDefaultReadOnlyProviderClass to set
     * @return the builder
     */
    public Builder setPerUserDefaultReadOnlyProviderClass(
        Class<? extends Provider<Map<String, Boolean>>> perUserDefaultReadOnlyProviderClass) {
      this.perUserDefaultReadOnlyProviderClass = perUserDefaultReadOnlyProviderClass;
      return this;
    }

    /**
     * Sets the per user default transaction isolation provider class.
     *
     * @param perUserDefaultTransactionIsolationProviderClass
     *          the perUserDefaultTransactionIsolationProviderClass to set
     * @return the builder
     */
    public Builder setPerUserDefaultTransactionIsolationProviderClass(
        Class<? extends Provider<Map<String, Integer>>> perUserDefaultTransactionIsolationProviderClass) {
      this.perUserDefaultTransactionIsolationProviderClass = perUserDefaultTransactionIsolationProviderClass;
      return this;
    }

    /**
     * Sets the per user max active provider class.
     *
     * @param perUserMaxActiveProviderClass
     *          the perUserMaxActiveProviderClass to set
     * @return the builder
     */
    public Builder setPerUserMaxTotalProviderClass(
        Class<? extends Provider<Map<String, Integer>>> perUserMaxActiveProviderClass) {
      this.perUserMaxActiveProviderClass = perUserMaxActiveProviderClass;
      return this;
    }

    /**
     * Sets the per user max idle provider class.
     *
     * @param perUserMaxIdleProviderClass
     *          the perUserMaxIdleProviderClass to set
     * @return the builder
     */
    public Builder setPerUserMaxIdleProviderClass(
        Class<? extends Provider<Map<String, Integer>>> perUserMaxIdleProviderClass) {
      this.perUserMaxIdleProviderClass = perUserMaxIdleProviderClass;
      return this;
    }

    /**
     * Sets the per user max wait provider class.
     *
     * @param perUserMaxWaitProviderClass
     *          the perUserMaxWaitProviderClass to set
     * @return the builder
     */
    public Builder setPerUserMaxWaitMillisProviderClass(
        Class<? extends Provider<Map<String, Long>>> perUserMaxWaitProviderClass) {
      this.perUserMaxWaitProviderClass = perUserMaxWaitProviderClass;
      return this;
    }

    /**
     * Creates the pool.
     *
     * @return the per user pool data source module
     */
    public PerUserPoolDataSourceModule create() {
      return new PerUserPoolDataSourceModule(perUserDefaultAutoCommitProviderClass, perUserDefaultReadOnlyProviderClass,
          perUserDefaultTransactionIsolationProviderClass, perUserMaxActiveProviderClass, perUserMaxIdleProviderClass,
          perUserMaxWaitProviderClass);
    }

  }

}
