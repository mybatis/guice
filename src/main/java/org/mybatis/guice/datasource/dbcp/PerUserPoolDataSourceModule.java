/*
 *    Copyright 2010-2011 The MyBatis Team
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

import java.lang.annotation.Annotation;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

/**
 * @version $Id$
 */
public final class PerUserPoolDataSourceModule extends AbstractModule {

    private static final TypeLiteral<Map<String, Boolean>> STRING_BOOLEAN_MAP = new TypeLiteral<Map<String, Boolean>>(){};

    private static final TypeLiteral<Map<String, Integer>> STRING_INTEGER_MAP = new TypeLiteral<Map<String, Integer>>(){};

    private final Class<Provider<Map<String, Boolean>>> perUserDefaultAutoCommitProviderClass;

    private final Class<Provider<Map<String, Boolean>>> perUserDefaultReadOnlyProviderClass;

    private final Class<Provider<Map<String, Integer>>> perUserDefaultTransactionIsolationProviderClass;

    private final Class<Provider<Map<String, Integer>>> perUserMaxActiveProviderClass;

    private final Class<Provider<Map<String, Integer>>> perUserMaxIdleProviderClass;

    private final Class<Provider<Map<String, Integer>>> perUserMaxWaitProviderClass;

    private PerUserPoolDataSourceModule(
            Class<Provider<Map<String, Boolean>>> perUserDefaultAutoCommitProviderClass,
            Class<Provider<Map<String, Boolean>>> perUserDefaultReadOnlyProviderClass,
            Class<Provider<Map<String, Integer>>> perUserDefaultTransactionIsolationProviderClass,
            Class<Provider<Map<String, Integer>>> perUserMaxActiveProviderClass,
            Class<Provider<Map<String, Integer>>> perUserMaxIdleProviderClass,
            Class<Provider<Map<String, Integer>>> perUserMaxWaitProviderClass) {
        this.perUserDefaultAutoCommitProviderClass = perUserDefaultAutoCommitProviderClass;
        this.perUserDefaultReadOnlyProviderClass = perUserDefaultReadOnlyProviderClass;
        this.perUserDefaultTransactionIsolationProviderClass = perUserDefaultTransactionIsolationProviderClass;
        this.perUserMaxActiveProviderClass = perUserMaxActiveProviderClass;
        this.perUserMaxIdleProviderClass = perUserMaxIdleProviderClass;
        this.perUserMaxWaitProviderClass = perUserMaxWaitProviderClass;
    }

    @Override
    protected void configure() {
        doBind(this.binder(), this.perUserDefaultAutoCommitProviderClass, STRING_BOOLEAN_MAP, PerUserDefaultAutoCommit.class);
        doBind(this.binder(), this.perUserDefaultReadOnlyProviderClass, STRING_BOOLEAN_MAP, PerUserDefaultReadOnly.class);
        doBind(this.binder(), this.perUserDefaultTransactionIsolationProviderClass, STRING_INTEGER_MAP, PerUserDefaultTransactionIsolation.class);
        doBind(this.binder(), this.perUserMaxActiveProviderClass, STRING_INTEGER_MAP, PerUserMaxActive.class);
        doBind(this.binder(), this.perUserMaxIdleProviderClass, STRING_INTEGER_MAP, PerUserMaxIdle.class);
        doBind(this.binder(), this.perUserMaxWaitProviderClass, STRING_INTEGER_MAP, PerUserMaxWait.class);
    }

    private static <T> void doBind(Binder binder, Class<Provider<T>> providerClass, TypeLiteral<T> typeLiteral, Class<? extends Annotation> annotation) {
        if (providerClass != null) {
            binder.bind(typeLiteral)
                  .annotatedWith(annotation)
                  .toProvider(providerClass)
                  .in(Scopes.SINGLETON);
        }
    }

    public static final class Builder {

        private Class<Provider<Map<String, Boolean>>> perUserDefaultAutoCommitProviderClass;

        private Class<Provider<Map<String, Boolean>>> perUserDefaultReadOnlyProviderClass;

        private Class<Provider<Map<String, Integer>>> perUserDefaultTransactionIsolationProviderClass;

        private Class<Provider<Map<String, Integer>>> perUserMaxActiveProviderClass;

        private Class<Provider<Map<String, Integer>>> perUserMaxIdleProviderClass;

        private Class<Provider<Map<String, Integer>>> perUserMaxWaitProviderClass;

        /**
         * @param perUserDefaultAutoCommitProviderClass the perUserDefaultAutoCommitProviderClass to set
         */
        public Builder setPerUserDefaultAutoCommitProviderClass(
                Class<Provider<Map<String, Boolean>>> perUserDefaultAutoCommitProviderClass) {
            this.perUserDefaultAutoCommitProviderClass = perUserDefaultAutoCommitProviderClass;
            return this;
        }

        /**
         * @param perUserDefaultReadOnlyProviderClass the perUserDefaultReadOnlyProviderClass to set
         */
        public Builder setPerUserDefaultReadOnlyProviderClass(
                Class<Provider<Map<String, Boolean>>> perUserDefaultReadOnlyProviderClass) {
            this.perUserDefaultReadOnlyProviderClass = perUserDefaultReadOnlyProviderClass;
            return this;
        }

        /**
         * @param perUserDefaultTransactionIsolationProviderClass the perUserDefaultTransactionIsolationProviderClass to set
         */
        public Builder setPerUserDefaultTransactionIsolationProviderClass(
                Class<Provider<Map<String, Integer>>> perUserDefaultTransactionIsolationProviderClass) {
            this.perUserDefaultTransactionIsolationProviderClass = perUserDefaultTransactionIsolationProviderClass;
            return this;
        }

        /**
         * @param perUserMaxActiveProviderClass the perUserMaxActiveProviderClass to set
         */
        public Builder setPerUserMaxActiveProviderClass(
                Class<Provider<Map<String, Integer>>> perUserMaxActiveProviderClass) {
            this.perUserMaxActiveProviderClass = perUserMaxActiveProviderClass;
            return this;
        }

        /**
         * @param perUserMaxIdleProviderClass the perUserMaxIdleProviderClass to set
         */
        public Builder setPerUserMaxIdleProviderClass(
                Class<Provider<Map<String, Integer>>> perUserMaxIdleProviderClass) {
            this.perUserMaxIdleProviderClass = perUserMaxIdleProviderClass;
            return this;
        }

        /**
         * @param perUserMaxWaitProviderClass the perUserMaxWaitProviderClass to set
         */
        public Builder setPerUserMaxWaitProviderClass(
                Class<Provider<Map<String, Integer>>> perUserMaxWaitProviderClass) {
            this.perUserMaxWaitProviderClass = perUserMaxWaitProviderClass;
            return this;
        }

        public PerUserPoolDataSourceModule create() {
            return new PerUserPoolDataSourceModule(this.perUserDefaultAutoCommitProviderClass,
                    this.perUserDefaultReadOnlyProviderClass,
                    this.perUserDefaultTransactionIsolationProviderClass,
                    this.perUserMaxActiveProviderClass,
                    this.perUserMaxIdleProviderClass,
                    this.perUserMaxWaitProviderClass);
        }

    }

}
