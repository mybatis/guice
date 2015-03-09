package org.mybatis.guice;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;
import static com.google.inject.matcher.Matchers.not;

import javax.inject.Provider;
import javax.transaction.TransactionManager;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.guice.transactional.Transactional;
import org.mybatis.guice.transactional.TransactionalMethodInterceptor;
import org.mybatis.guice.transactional.TxTransactionalMethodInterceptor;

public abstract class MyBatisJtaModule extends MyBatisModule {
	private final Log log = LogFactory.getLog(getClass());

	private TransactionManager transactionManager;

	public MyBatisJtaModule() {
	}

	public MyBatisJtaModule(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	protected void bindTransactionInterceptors() {
		TransactionManager manager = getTransactionManager();

		if (manager == null) {
			log.debug("bind default transaction interceptors");
			super.bindTransactionInterceptors();
		} else {
			log.debug("bind XA transaction interceptors");

			// mybatis transactional interceptor
			TransactionalMethodInterceptor interceptor = new TransactionalMethodInterceptor();
			requestInjection(interceptor);

			// jta transactional interceptor
			TxTransactionalMethodInterceptor interceptorTx = new TxTransactionalMethodInterceptor();
			requestInjection(interceptorTx);

			bind(TransactionManager.class).toInstance(manager);
			bindInterceptor(any(), annotatedWith(Transactional.class),
					interceptorTx, interceptor);
			// Intercept classes annotated with Transactional, but avoid "double"
			// interception when a mathod is also annotated inside an annotated class.
			bindInterceptor(annotatedWith(Transactional.class), not(annotatedWith(Transactional.class)), 
					interceptorTx, interceptor);
		}
	}

	protected TransactionManager getTransactionManager() {
		return transactionManager;
	}

	protected void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	protected void bindDefaultTransactionProvider() {
		Class<? extends TransactionFactory> factoryType = getTransactionManager() == null ? 
				JdbcTransactionFactory.class : ManagedTransactionFactory.class;

		bindTransactionFactoryType(factoryType);
	}

	protected static class ProviderImpl<T> implements Provider<T> {
		private T wrapper;

		public ProviderImpl(T wrapper) {
			this.wrapper = wrapper;
		}

		public T get() {
			return wrapper;
		}

	}
}
