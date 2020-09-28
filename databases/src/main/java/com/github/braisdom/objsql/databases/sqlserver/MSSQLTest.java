package com.github.braisdom.objsql.databases.sqlserver;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.databases.DataMock;
import com.github.braisdom.objsql.databases.TableCreator;
import com.github.braisdom.objsql.databases.model.Order;
import com.github.braisdom.objsql.databases.postgresql.PostgreSqlConnectionFactory;
import com.github.braisdom.objsql.databases.postgresql.PostgresProductSales;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MSSQLTest {

    @Before
    public void initDatasource() {
        String url = "jdbc:sqlserver://47.100.45.227:1433;databaseName=objective_sql;currentSchema=dbo";
        String user = "SA";
        String password = "Ly19960613.";
        Databases.installConnectionFactory(new PostgreSqlConnectionFactory(url, user, password));
    }

    @Test
    public void createTables() throws IOException, SQLException {
        TableCreator tableCreator = new TableCreator(TableCreator.DATABASE_TYPE_SQLSERVER);
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
        Assert.assertEquals(Order.destroy(1000), 1);
    }

    @Test
    public void testComplexQuery() throws SQLSyntaxException, SQLException {
        MSSQLProductSales productSales = new MSSQLProductSales();

        productSales.salesBetween("2020-09-01 00:00:00", "2020-09-10 00:00:00")
                .productIn("P2020000018", "P202000007", "P2020000011");

        List<DynamicModel> execute = productSales.execute(Databases.getDefaultDataSourceName());
        execute.forEach(System.out::println);
    }


}
