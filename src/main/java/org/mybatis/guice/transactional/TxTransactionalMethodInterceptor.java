/*
 *    Copyright 2009-2020 the original author or authors.
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
package org.mybatis.guice.transactional;

import static java.lang.String.format;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.ejb.ApplicationException;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.mybatis.guice.transactional.Transactional.TxType;

/**
 * Method interceptor for {@link Transactional} annotation.
 *
 */
public class TxTransactionalMethodInterceptor implements MethodInterceptor {
  /**
   * This class logger.
   */
  private final Log log = LogFactory.getLog(getClass());

  @Inject
  private TransactionManager manager;

  @Inject
  private Provider<XAResource> xaResourceProvider;

  public TxTransactionalMethodInterceptor() {
  }

  private boolean isApplicationExceptionAvailable() {
    try {
      Class.forName("javax.ejb.ApplicationException");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  private <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationClass) {
    Class<?> current = clazz;
    A annotation = null;
    while (annotation == null && current != null) {
      annotation = current.getAnnotation(annotationClass);
      current = current.getSuperclass();
    }
    return annotation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method interceptedMethod = invocation.getMethod();
    Transactional transactional = interceptedMethod.getAnnotation(Transactional.class);

    // The annotation may be present at the class level instead
    if (transactional == null) {
      transactional = interceptedMethod.getDeclaringClass().getAnnotation(Transactional.class);
    }

    String debugPrefix = null;
    if (this.log.isDebugEnabled()) {
      debugPrefix = String.format("[Intercepted method: %s]", interceptedMethod.toGenericString());
    }

    boolean needsRollback = transactional.rollbackOnly();
    Object object = null;
    TransactionAttribute attribute = null;

    if (manager != null) {
      TxType txType = transactional.value();
      if (TxType.REQUIRED.equals(txType)) {
        attribute = TransactionAttribute.REQUIRED;
      } else if (TxType.REQUIRES_NEW.equals(txType)) {
        attribute = TransactionAttribute.REQUIRESNEW;
      } else if (TxType.MANDATORY.equals(txType)) {
        attribute = TransactionAttribute.MANDATORY;
      } else if (TxType.SUPPORTS.equals(txType)) {
        attribute = TransactionAttribute.SUPPORTS;
      } else if (TxType.NOT_SUPPORTED.equals(txType)) {
        attribute = null; // FIXME add implementation
      } else if (TxType.NEVER.equals(txType)) {
        attribute = TransactionAttribute.NEVER;
      }
    }

    if (attribute == null) {
      if (log.isDebugEnabled()) {
        log.debug(format("%s - skip Tx Transaction", debugPrefix));
      }

      // without Tx
      try {
        object = invocation.proceed();
      } catch (Throwable t) {
        throw t;
      }
    } else {
      if (log.isDebugEnabled()) {
        log.debug(format("%s - Tx Transaction %s begin", debugPrefix, attribute.name()));
      }

      // with Tx
      TransactionToken tranToken = attribute.begin(manager);

      log.debug("enlistResource XASqlSessionManager");
      XAResource xaRes = xaResourceProvider.get();
      tranToken.getActiveTransaction().enlistResource(xaRes);

      try {
        if (log.isDebugEnabled()) {
          log.debug(format("%s - Tx Transaction %s (CompletionAllowed %s) call method", debugPrefix, attribute.name(),
              tranToken.isCompletionAllowed()));
        }
        object = invocation.proceed();

        if (needsRollback) {
          manager.setRollbackOnly();
        }

      } catch (Throwable t) {
        if (log.isDebugEnabled()) {
          log.debug(format("%s - Tx Transaction %s (CompletionAllowed %s) rolling back", debugPrefix, attribute.name(),
              tranToken.isCompletionAllowed()));
        }
        if (isApplicationExceptionAvailable()) {
          ApplicationException ae = t.getClass().getAnnotation(ApplicationException.class);
          ApplicationException parentAe = findAnnotation(t.getClass().getSuperclass(), ApplicationException.class);
          if ((ae == null && parentAe == null) || (ae != null && ae.rollback())
              || (parentAe != null && (!parentAe.inherited() || parentAe.rollback()))) {
            manager.setRollbackOnly();
          }
        } else {
          manager.setRollbackOnly();
        }
        throw t;
      } finally {
        if (log.isDebugEnabled()) {
          log.debug(format("%s - Tx Transaction %s (CompletionAllowed %s) finish", debugPrefix, attribute.name(),
              tranToken.isCompletionAllowed()));
        }
        attribute.finish(manager, tranToken);
      }
    }
    return object;
  }

}
