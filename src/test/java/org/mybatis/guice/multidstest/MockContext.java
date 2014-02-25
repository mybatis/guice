package org.mybatis.guice.multidstest;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MockContext extends InitialContext {

    private static Map<String, Object> bindings = new HashMap<String, Object>();

    public MockContext(boolean lazy) throws NamingException {
        super(lazy);
    }

    public Object lookup(String name) throws NamingException {
        return bindings.get(name);
    }

    public void bind(String name, Object obj) throws NamingException {
        bindings.put(name, obj);
    }
}
