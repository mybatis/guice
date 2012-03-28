/*
 *    Copyright 2010-2012 The MyBatis Team
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
import static java.lang.Thread.currentThread;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionManager;

/**
 * Method interceptor for {@link Transactional} annotation.
 *
 * @version $Id$
 */
public final class TransactionalMethodInterceptor implements MethodInterceptor {

    private static final Class<?>[] CAUSE_TYPES = new Class[]{ Throwable.class };

    private static final Class<?>[] MESSAGE_CAUSE_TYPES = new Class[]{ String.class, Throwable.class };

    /**
     * This class logger.
     */
    private final Log log = LogFactory.getLog(getClass());

    /**
     * The {@code SqlSessionManager} reference.
     */
    @Inject
    private SqlSessionManager sqlSessionManager;

    /**
     * Sets the SqlSessionManager instance.
     *
     * @param sqlSessionManager the SqlSessionManager instance.
     */
    public void setSqlSessionManager(SqlSessionManager sqlSessionManager) {
        this.sqlSessionManager = sqlSessionManager;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method interceptedMethod = invocation.getMethod();
        Transactional transactional = interceptedMethod.getAnnotation(Transactional.class);

        String debugPrefix = null;
        if (this.log.isDebugEnabled()) {
            debugPrefix = format("[Intercepted method: %s]", interceptedMethod.toGenericString());
        }

        boolean isSessionInherited = this.sqlSessionManager.isManagedSessionStarted();

        if (isSessionInherited) {
            if (log.isDebugEnabled()) {
                log.debug(format("%s - SqlSession already set for thread: %s",
                        debugPrefix,
                        currentThread().getId()));
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug(format("%s - SqlSession not set for thread: %s, creating a new one",
                        debugPrefix,
                        currentThread().getId()));
            }

            sqlSessionManager.startManagedSession(transactional.executorType(), transactional.isolation().getTransactionIsolationLevel());
        }

        Object object = null;
        try {
            object = invocation.proceed();

            if (!isSessionInherited && !transactional.rollbackOnly()) {
                sqlSessionManager.commit(transactional.force());
            }
        } catch (Throwable t) {
            // rollback the transaction
            sqlSessionManager.rollback(transactional.force());

            // check the caught exception is declared in the invoked method
            for (Class<?> exceptionClass : interceptedMethod.getExceptionTypes()) {
                if (exceptionClass.isAssignableFrom(t.getClass())) {
                    throw t;
                }
            }

            // check the caught exception is of same rethrow type
            if (transactional.rethrowExceptionsAs().isAssignableFrom(t.getClass())) {
                throw t;
            }

            // rethrow the exception as new exception
            String errorMessage;
            Object[] initargs;
            Class<?>[] initargsType;

            if (transactional.exceptionMessage().length() != 0) {
                errorMessage = format(transactional.exceptionMessage(), invocation.getArguments());
                initargs = new Object[]{ errorMessage, t };
                initargsType = MESSAGE_CAUSE_TYPES;
            } else {
                initargs = new Object[]{ t };
                initargsType = CAUSE_TYPES;
            }

            Constructor<? extends Throwable> exceptionConstructor = getMatchingConstructor(transactional.rethrowExceptionsAs(), initargsType);
            Throwable rethrowEx = null;
            if (exceptionConstructor != null) {
                try {
                    rethrowEx = exceptionConstructor.newInstance(initargs);
                } catch (Exception e) {
                    errorMessage = format("Impossible to re-throw '%s', it needs the constructor with %s argument(s).",
                            transactional.rethrowExceptionsAs().getName(),
                            Arrays.toString(initargsType));
                    log.error(errorMessage, e);
                    rethrowEx = new RuntimeException(errorMessage, e);
                }
            } else {
                errorMessage = format("Impossible to re-throw '%s', it needs the constructor with %s or %s argument(s).",
                        transactional.rethrowExceptionsAs().getName(),
                        Arrays.toString(CAUSE_TYPES),
                        Arrays.toString(MESSAGE_CAUSE_TYPES));
                log.error(errorMessage);
                rethrowEx = new RuntimeException(errorMessage);
            }

            throw rethrowEx;
        } finally {
            // skip close when the session is inherited from another Transactional method
            if (!isSessionInherited) {
                if (transactional.rollbackOnly()) {
                    if (log.isDebugEnabled()) {
                        log.debug(debugPrefix
                                + " - SqlSession of thread: "
                                + currentThread().getId()
                                + " was in rollbackOnly mode, rolling it back");
                    }

                    sqlSessionManager.rollback(true);
                }

                if (log.isDebugEnabled()) {
                    log.debug(format("%s - SqlSession of thread: %s terminated its life-cycle, closing it",
                            debugPrefix,
                            currentThread().getId()));
                }

                sqlSessionManager.close();
            } else if (log.isDebugEnabled()) {
                log.debug(format("%s - SqlSession of thread: %s is inherited, skipped close operation",
                        debugPrefix,
                        currentThread().getId()));
            }
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> Constructor<E> getMatchingConstructor(Class<E> type,
            Class<?>[] argumentsType) {
        Class<? super E> currentType = type;
        while (Object.class != currentType) {
            for (Constructor<?> constructor : currentType.getConstructors()) {
                if (Arrays.equals(argumentsType, constructor.getParameterTypes())) {
                    return (Constructor<E>) constructor;
                }
            }
            currentType = currentType.getSuperclass();
        }
        return null;
    }

}
