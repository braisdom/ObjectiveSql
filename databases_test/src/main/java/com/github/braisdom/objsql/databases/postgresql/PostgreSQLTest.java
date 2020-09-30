package com.github.braisdom.objsql.databases.postgresql;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.databases.DataMock;
import com.github.braisdom.objsql.databases.TableCreator;
import com.github.braisdom.objsql.databases.model.Order;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
        connection.close();
    }

    @Test
    public void testMockData() throws SQLException {
        DataMock dataMock = new DataMock();
        dataMock.generateMembers();
        dataMock.generateProducts();
        dataMock.generateOrdersAndOrderLines();
    }

    @Test
    public void testDelete() throws SQLException {
        Assert.assertEquals(Order.destroy(1000l), 1);
    }

    @Test
    public void testComplexQuery() throws SQLSyntaxException, SQLException {
        PostgresProductSales productSales = new PostgresProductSales();

        productSales.salesBetween("2020-09-01 00:00:00", "2020-09-10 00:00:00")
                .productIn("P2020000018", "P202000007", "P2020000011");

        List<DynamicModel> execute = productSales.execute(Databases.getDefaultDataSourceName());
        execute.forEach(System.out::println);
    }

}
