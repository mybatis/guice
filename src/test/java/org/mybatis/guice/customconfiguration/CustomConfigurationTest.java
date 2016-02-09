package org.mybatis.guice.customconfiguration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import java.util.Properties;

import static com.google.inject.name.Names.bindProperties;
import static org.junit.Assert.assertTrue;

public class CustomConfigurationTest {

    protected Properties createTestProperties() {
        final Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "test");
        myBatisProperties.setProperty("JDBC.username", "sa");
        myBatisProperties.setProperty("JDBC.password", "");
        myBatisProperties.setProperty("JDBC.autoCommit", "false");
        return myBatisProperties;
    }

    @Test
    public void customConfigurationProviderWithMyBatisModule() throws Exception {
        Injector injector = Guice.createInjector(new MyBatisModule() {
            @Override
            protected void initialize() {
                install(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);
                bindProperties(binder(), createTestProperties());

                useConfigurationProvider(MyConfigurationProvider.class);
                bindDataSourceProviderType(PooledDataSourceProvider.class);
                bindTransactionFactoryType(JdbcTransactionFactory.class);
            }
        });
        Configuration configuration = injector.getInstance(Configuration.class);
        assertTrue("Configuration not an instanceof MyConfiguration", MyConfiguration.class.isAssignableFrom(configuration.getClass()));
    }
}
