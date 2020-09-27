package com.github.braisdom.objsql.databases.postgresql;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.databases.DataMock;
import com.github.braisdom.objsql.databases.TableCreator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgreSQLTest {

    @Before
    public void initDatasource() {
        String url = "jdbc:postgresql://47.100.45.227:5432/postgres?currentSchema=objective_sql";
        String user = "postgres";
        String password = "123456";
        Databases.installConnectionFactory(new PostgreSqlConnectionFactory(url, user, password));
    }

    @Test
    public void createTables() throws IOException, SQLException {
        TableCreator tableCreator = new TableCreator(TableCreator.DATABASE_TYPE_POSTGRESQL);
        Connection connection = Databases.getConnectionFactory().getConnection(ConnectionFactory.DEFAULT_DATA_SOURCE_NAME);
        tableCreator.create(connection);
    }

    @Test
    public void mockDataTest() throws SQLException {
        DataMock dataMock = new DataMock();
        dataMock.generateData();
    }

    @Test void complexQueryTest() {

    }

}
