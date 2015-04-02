package org.mybatis.guice.jta;

import javax.transaction.xa.XAResource;

import org.mybatis.guice.transactional.XASqlSessionManagerProvider;

public class CustomXaResourceProvider extends XASqlSessionManagerProvider {
    private static boolean providerCalled = false;

    @Override
    public XAResource get() {
        providerCalled = true;
        return super.get();
    }

    public static boolean isProviderCalled() {
        return providerCalled;
    }
}
