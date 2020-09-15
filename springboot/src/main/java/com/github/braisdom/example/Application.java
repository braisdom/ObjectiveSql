package com.github.braisdom.example;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    private class MysqlConnectionFactory implements ConnectionFactory {

        private final ApplicationContext applicationContext;

        public MysqlConnectionFactory(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            DataSource dataSource = (DataSource) applicationContext.getBean(dataSourceName);
            return dataSource.getConnection();
        }
    }

    @Component
    private class ObjectiveSqlInitializer  {

        @EventListener
        public void onApplicationEvent(ApplicationStartedEvent event) {
            ConnectionFactory connectionFactory = new MysqlConnectionFactory(event.getApplicationContext());
            Databases.installConnectionFactory(connectionFactory);
        }
    }

    @Bean(ConnectionFactory.DEFAULT_DATA_SOURCE_NAME)
    public DataSource initializeDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://192.168.10.50:3306/");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("123456");
        return dataSourceBuilder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

