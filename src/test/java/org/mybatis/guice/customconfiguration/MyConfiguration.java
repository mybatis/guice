package org.mybatis.guice.customconfiguration;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;

public class MyConfiguration extends Configuration {

    public MyConfiguration(Environment environment) {
        super(environment);
    }
}
