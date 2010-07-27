/*
 *    Copyright 2010 The myBatis Team
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.session.TransactionIsolationLevel;

import com.google.inject.Inject;

/**
 * Method interceptor for {@link Transactional} annotation.
 *
 * @version $Id$
 */
public final class TransactionalMethodInterceptor implements MethodInterceptor {

    /**
     * This class logger.
     */
    private final Log log = LogFactory.getLog(this.getClass());

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
        Transactional transactional = (Transactional) interceptedMethod.getAnnotation(Transactional.class);

        String debugPrefix = null;
        if (this.log.isDebugEnabled()) {
            debugPrefix = "[Intercepted method: "
                + interceptedMethod.toGenericString()
                + "]";
        }

        boolean isSessionInherited = this.sqlSessionManager.isManagedSessionStarted();

        if (isSessionInherited) {
            if (this.log.isDebugEnabled()) {
                this.log.debug(debugPrefix
                        + " - SqlSession already set for thread: "
                        + Thread.currentThread().getId());
            }
        } else {
            if (this.log.isDebugEnabled()) {
                this.log.debug(debugPrefix
                        + " - SqlSession not set for thread: "
                        + Thread.currentThread().getId()
                        + ", creating a new one");
            }

            if (TransactionIsolationLevel.NONE == transactional.isolationLevel()) {
                this.sqlSessionManager.startManagedSession(transactional.executorType(), transactional.autoCommit());
            } else {
                this.sqlSessionManager.startManagedSession(transactional.executorType(), transactional.isolationLevel());
            }
        }

        Object object = null;
        try {
            object = invocation.proceed();
            if (!isSessionInherited && !transactional.autoCommit()) {
                this.sqlSessionManager.commit(transactional.force());
            }
        } catch (Throwable t) {
            // rollback the transaction
            this.sqlSessionManager.rollback(transactional.force());

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
            Constructor<? extends Throwable> constructor =
                transactional.rethrowExceptionsAs().getConstructor(Throwable.class);

            Throwable rethrowEx = null;

            try {
                rethrowEx = constructor.newInstance(t);
            } catch (Exception e) {
                String errorMessage = "Impossible to re-throw '"
                    + transactional.rethrowExceptionsAs().getName()
                    + "', it needs the constructor with <Throwable> argument.";
                this.log.error(errorMessage, e);
                rethrowEx = new RuntimeException(errorMessage, e);
            }

            throw rethrowEx;
        } finally {
            // skip close when the session is inherited from another Transactional method
            if (!isSessionInherited) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(debugPrefix
                            + " - SqlSession of thread: "
                            + Thread.currentThread().getId()
                            + " terminated his lyfe-cycle, closing it");
                }

                this.sqlSessionManager.close();
            } else if (this.log.isDebugEnabled()) {
                this.log.debug(debugPrefix
                        + " - SqlSession of thread: "
                        + Thread.currentThread().getId()
                        + " is inherited, skipped close operation");
            }
        }

        return object;
    }

}
