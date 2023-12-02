/*
 *    Copyright 2009-2023 the original author or authors.
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

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;
import static com.google.inject.matcher.Matchers.not;
import static com.google.inject.name.Names.named;
import static com.google.inject.util.Providers.guicify;
import static org.mybatis.guice.Preconditions.checkArgument;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.matcher.AbstractMatcher;

import java.lang.reflect.Method;
import java.util.Set;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionManager;
import org.mybatis.guice.mappers.MapperProvider;
import org.mybatis.guice.session.SqlSessionManagerProvider;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;

abstract class AbstractMyBatisModule extends AbstractModule {

  protected static final AbstractMatcher<Method> DECLARED_BY_OBJECT = new AbstractMatcher<Method>() {
    @Override
    public boolean matches(Method method) {
      return method.getDeclaringClass() == Object.class;
    }
  };

  protected static final AbstractMatcher<Method> SYNTHETIC = new AbstractMatcher<Method>() {
    @Override
    public boolean matches(Method method) {
      return method.isSynthetic();
    }
  };

  private ClassLoader resourcesClassLoader = getDefaultClassLoader();

  private ClassLoader driverClassLoader = getDefaultClassLoader();

  /**
   * Return a set of all classes contained in the given package.
   *
   * @param packageName
   *          the package has to be analyzed.
   *
   * @return a set of all classes contained in the given package.
   */
  protected static Set<Class<?>> getClasses(String packageName) {
    return AbstractMyBatisModule.getClasses(new ResolverUtil.IsA(Object.class), packageName);
  }

  /**
   * Return a set of all classes contained in the given package that match with the given test requirement.
   *
   * @param test
   *          the class filter on the given package.
   * @param packageName
   *          the package has to be analyzed.
   *
   * @return a set of all classes contained in the given package.
   */
  protected static Set<Class<?>> getClasses(ResolverUtil.Test test, String packageName) {
    checkArgument(test != null, "Parameter 'test' must not be null");
    checkArgument(packageName != null, "Parameter 'packageName' must not be null");
    return new ResolverUtil<Object>().find(test, packageName).getClasses();
  }

  @Override
  protected final void configure() {
    try {
      // sql session manager
      bind(SqlSessionManager.class).toProvider(SqlSessionManagerProvider.class).in(Scopes.SINGLETON);
      bind(SqlSession.class).to(SqlSessionManager.class).in(Scopes.SINGLETON);

      internalConfigure();

      bindTransactionInterceptors();

      bind(ClassLoader.class).annotatedWith(named("JDBC.driverClassLoader")).toInstance(driverClassLoader);
    } finally {
      resourcesClassLoader = getDefaultClassLoader();
      driverClassLoader = getDefaultClassLoader();
    }
  }

  /**
   * bind transactional interceptors.
   */
  protected void bindTransactionInterceptors() {
    // transactional interceptor
    TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
    requestInjection(interceptor);
    bindInterceptor(any(), not(SYNTHETIC).and(not(DECLARED_BY_OBJECT)).and(annotatedWith(Transactional.class)),
        interceptor);
    // Intercept classes annotated with Transactional, but avoid "double"
    // interception when a mathod is also annotated inside an annotated
    // class.
    bindInterceptor(annotatedWith(Transactional.class),
        not(SYNTHETIC).and(not(DECLARED_BY_OBJECT)).and(not(annotatedWith(Transactional.class))), interceptor);
  }

  /**
   * Bind mapper.
   *
   * @param <T>
   *          the generic type
   * @param mapperType
   *          the mapper type
   */
  final <T> void bindMapper(Class<T> mapperType) {
    bind(mapperType).toProvider(guicify(new MapperProvider<T>(mapperType))).in(Scopes.SINGLETON);
  }

  /**
   * Use resource class loader.
   *
   * @param resourceClassLoader
   *          the resource class loader
   *
   * @since 3.3
   */
  public void useResourceClassLoader(ClassLoader resourceClassLoader) {
    this.resourcesClassLoader = resourceClassLoader;
  }

  /**
   * Gets the resource class loader.
   *
   * @return the resource class loader
   *
   * @since 3.3
   */
  protected final ClassLoader getResourceClassLoader() {
    return resourcesClassLoader;
  }

  /**
   * Use jdbc driver class loader.
   *
   * @param driverClassLoader
   *          the driver class loader
   *
   * @since 3.3
   */
  public void useJdbcDriverClassLoader(ClassLoader driverClassLoader) {
    this.driverClassLoader = driverClassLoader;
  }

  /**
   * Gets the default class loader.
   *
   * @return the default class loader
   *
   * @since 3.3
   */
  private ClassLoader getDefaultClassLoader() {
    return getClass().getClassLoader();
  }

  /**
   * Configures a {@link Binder} via the exposed methods.
   */
  abstract void internalConfigure();

  /**
   * Initialize.
   */
  protected abstract void initialize();

}
