package com.github.braisdom.objsql.spring;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ExtensionsDataSourceProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class ObjSqlAutoConfiguration {

    private class SpringBootConnectionFactory implements ConnectionFactory {

        private Map<String, DataSource> dataSourceMap = new HashMap<>();

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            DataSource dataSource = dataSourceMap.get(dataSourceName);
            if(dataSource == null)
                throw new NullPointerException(String.format("Cannot find data source for '%s'", dataSourceName));
            return dataSource.getConnection();
        }
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public ConnectionFactory prepareConnectionFactory(DataSource dataSource,
                                                      ExtensionsDataSourceProperties dataSourceProperties) {
        SpringBootConnectionFactory connectionFactory = new SpringBootConnectionFactory();
        Map<String, DataSourceProperties> extensions = dataSourceProperties.getExtensions();

        connectionFactory.dataSourceMap.put(ConnectionFactory.DEFAULT_DATA_SOURCE_NAME, dataSource);

        for(Map.Entry<String, DataSourceProperties> entry : extensions.entrySet()) {
            DataSourceProperties properties = entry.getValue();
            DataSource extensionDataSource = properties.initializeDataSourceBuilder()
                    .type(properties.getType()).build();
            connectionFactory.dataSourceMap.put(entry.getKey(), extensionDataSource);
        }
        Databases.installConnectionFactory(connectionFactory);
        return connectionFactory;
    }

}
