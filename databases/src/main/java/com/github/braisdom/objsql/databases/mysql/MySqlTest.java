package com.github.braisdom.objsql.databases.mysql;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.databases.DataMock;
import com.github.braisdom.objsql.databases.TableCreator;
import com.github.braisdom.objsql.databases.postgresql.PostgreSqlConnectionFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlTest {
    @Before
    public void initDatasource() {
        String url = "jdbc:mysql://119.45.52.117:3306/objective_sql";
        String user = "yan";
        String password = "Ly19960613.";
        Databases.installConnectionFactory(new PostgreSqlConnectionFactory(url, user, password));
    }

    @Test
    public void createTables() throws IOException, SQLException {
        TableCreator tableCreator = new TableCreator(TableCreator.DATABASE_TYPE_MYSQL);
        Connection connection = Databases.getConnectionFactory().getConnection(ConnectionFactory.DEFAULT_DATA_SOURCE_NAME);
        tableCreator.create(connection);
        connection.close();
    }

    @Test
    public void mockDataTest() throws SQLException {
        DataMock dataMock = new DataMock();
        dataMock.generateData();
    }

}
