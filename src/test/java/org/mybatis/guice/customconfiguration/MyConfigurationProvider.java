package org.mybatis.guice.customconfiguration;

import com.google.inject.Inject;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.mybatis.guice.configuration.ConfigurationProvider;

public class MyConfigurationProvider extends ConfigurationProvider {

    /**
     * @param environment
     * @since 1.0.1
     */
    @Inject
    public MyConfigurationProvider(Environment environment) {
        super(environment);
    }

    @Override
    protected Configuration newConfiguration(Environment environment) {
        return new MyConfiguration(environment);
    }
}
