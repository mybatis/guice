package org.mybatis.guice.transactional;

import javax.transaction.xa.XAException;

public class MyBatisXAException extends XAException {
	private static final long serialVersionUID = -7280133560046132709L;

	public MyBatisXAException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MyBatisXAException(String message, int errorCode, Throwable t) {
        super(message);
        this.errorCode = errorCode;
        initCause(t);
    }

}
