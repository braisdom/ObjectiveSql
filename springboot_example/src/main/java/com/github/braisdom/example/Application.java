package com.github.braisdom.example;

import com.github.braisdom.example.objsql.CacheableSQLExecutor;
import com.github.braisdom.example.objsql.ObjLoggerFactoryImpl;
import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.LoggerFactory;
import com.github.braisdom.objsql.SQLExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    private class ApplicationConnectionFactory implements ConnectionFactory {

        private final ApplicationContext applicationContext;

        public ApplicationConnectionFactory(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            DataSource dataSource = (DataSource) applicationContext.getBean(dataSourceName);
            return dataSource.getConnection();
        }
    }

/*
    @Bean(ConnectionFactory.DEFAULT_DATA_SOURCE_NAME)
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:4406/objective_sql");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }
*/

    @Bean(name = ConnectionFactory.DEFAULT_DATA_SOURCE_NAME)
    @ConfigurationProperties("spring.objsql-datasource.objsql-default-datasource")
    public DataSource getDataSource(){
        return DataSourceBuilder.create().build();
    }


    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConnectionFactory connectionFactory = new ApplicationConnectionFactory(event.getApplicationContext());
        SQLExecutor sqlExecutor = new CacheableSQLExecutor();
        LoggerFactory loggerFactory = new ObjLoggerFactoryImpl();

        Databases.installConnectionFactory(connectionFactory);
        Databases.installSqlExecutor(sqlExecutor);
        Databases.installLoggerFactory(loggerFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

