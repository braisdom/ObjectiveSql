package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.annotations.DomainModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.objsql.sql.Expressions.$;
import static org.mockito.Mockito.mock;

public class DefaultColumnTest {

    @Test
    public void testOrder() throws SQLSyntaxException {
        Dataset dataset = mock(Dataset.class);
        DefaultExpressionContext mysqlContext = new DefaultExpressionContext(DatabaseType.MySQL);
        DefaultExpressionContext otherContext = new DefaultExpressionContext(DatabaseType.PostgreSQL);
        DefaultColumn column = new DefaultColumn(DemoTable.class, dataset, "testField");

        Assertions.assertEquals(column.asc().toSql(mysqlContext).trim(), "`T0`.`test_field` ASC".trim());
        Assertions.assertEquals(column.desc().toSql(mysqlContext).trim(), "`T0`.`test_field` DESC".trim());

        Assertions.assertEquals(column.asc().toSql(otherContext).trim(), "\"T0\".\"test_field\" ASC".trim());
        Assertions.assertEquals(column.desc().toSql(otherContext).trim(), "\"T0\".\"test_field\" DESC".trim());
    }

    @Test
    public void testNull() throws SQLSyntaxException {
        Dataset dataset = mock(Dataset.class);
        DefaultExpressionContext mysqlContext = new DefaultExpressionContext(DatabaseType.MySQL);
        DefaultExpressionContext otherContext = new DefaultExpressionContext(DatabaseType.PostgreSQL);
        DefaultColumn column = new DefaultColumn(DemoTable.class, dataset, "testField");

        Assertions.assertEquals(column.isNull().toSql(mysqlContext).trim(), "`T0`.`test_field` IS NULL".trim());
        Assertions.assertEquals(column.isNotNull().toSql(mysqlContext).trim(), "`T0`.`test_field` IS NOT NULL".trim());

        Assertions.assertEquals(column.isNull().toSql(otherContext).trim(), "\"T0\".\"test_field\" IS NULL".trim());
        Assertions.assertEquals(column.isNotNull().toSql(otherContext).trim(), "\"T0\".\"test_field\" IS NOT NULL".trim());
    }

    @Test
    public void testIn() throws SQLSyntaxException {
        Dataset dataset = mock(Dataset.class);
        DefaultExpressionContext mysqlContext = new DefaultExpressionContext(DatabaseType.MySQL);
        DefaultExpressionContext otherContext = new DefaultExpressionContext(DatabaseType.PostgreSQL);
        DefaultColumn column = new DefaultColumn(DemoTable.class, dataset, "testField");

        Expression[] integersExpr = new Expression[]{$(1), $(2), $(3)};

        Assertions.assertEquals(column.in(integersExpr).toSql(mysqlContext).trim(), "`T0`.`test_field` IN (1, 2, 3)".trim());
        Assertions.assertEquals(column.notIn(integersExpr).toSql(mysqlContext).trim(), "`T0`.`test_field` NOT IN (1, 2, 3)".trim());

        Assertions.assertEquals(column.in(integersExpr).toSql(otherContext).trim(), "\"T0\".\"test_field\" IN (1, 2, 3)".trim());
        Assertions.assertEquals(column.notIn(integersExpr).toSql(otherContext).trim(), "\"T0\".\"test_field\" NOT IN (1, 2, 3)".trim());
    }

    @Test
    public void testBetween() throws SQLSyntaxException {
        Dataset dataset = mock(Dataset.class);
        DefaultExpressionContext mysqlContext = new DefaultExpressionContext(DatabaseType.MySQL);
        DefaultExpressionContext otherContext = new DefaultExpressionContext(DatabaseType.PostgreSQL);
        DefaultColumn column = new DefaultColumn(DemoTable.class, dataset, "testField");

        Assertions.assertEquals(column.between($(1), $(2)).toSql(mysqlContext).trim(), "`T0`.`test_field` BETWEEN 1 AND 2".trim());
        Assertions.assertEquals(column.notBetween($(1), $(2)).toSql(mysqlContext).trim(), "`T0`.`test_field` NOT BETWEEN 1 AND 2".trim());

        Assertions.assertEquals(column.between($(1), $(2)).toSql(otherContext).trim(), "\"T0\".\"test_field\" BETWEEN 1 AND 2".trim());
        Assertions.assertEquals(column.notBetween($(1), $(2)).toSql(otherContext).trim(), "\"T0\".\"test_field\" NOT BETWEEN 1 AND 2".trim());
    }

    @Test
    public void testLike() throws SQLSyntaxException {
        Dataset dataset = mock(Dataset.class);
        DefaultExpressionContext mysqlContext = new DefaultExpressionContext(DatabaseType.MySQL);
        DefaultExpressionContext otherContext = new DefaultExpressionContext(DatabaseType.PostgreSQL);
        DefaultColumn column = new DefaultColumn(DemoTable.class, dataset, "testField");

        Assertions.assertEquals(column.like($("%abc%")).toSql(mysqlContext).trim(), "`T0`.`test_field` LIKE '%abc%'".trim());
        Assertions.assertEquals(column.notLike("%abc%").toSql(mysqlContext).trim(), "`T0`.`test_field` NOT LIKE '%abc%'".trim());

        Assertions.assertEquals(column.like($("%abc%")).toSql(otherContext).trim(), "\"T0\".\"test_field\" LIKE '%abc%'".trim());
        Assertions.assertEquals(column.notLike("%abc%").toSql(otherContext).trim(), "\"T0\".\"test_field\" NOT LIKE '%abc%'".trim());
    }

    @Test
    public void testAs() throws SQLSyntaxException {
        Dataset dataset = mock(Dataset.class);
        DefaultExpressionContext mysqlContext = new DefaultExpressionContext(DatabaseType.MySQL);
        DefaultExpressionContext otherContext = new DefaultExpressionContext(DatabaseType.PostgreSQL);
        DefaultColumn column = new DefaultColumn(DemoTable.class, dataset, "testField");

        Assertions.assertEquals(column.as("ABC").toSql(mysqlContext).trim(), "`T0`.`test_field` AS `ABC`".trim());
        Assertions.assertEquals(column.as("ABC").toSql(otherContext).trim(), "\"T0\".\"test_field\" AS \"ABC\"".trim());
    }

    @DomainModel
    private static class DemoTable {
        private String testField;
    }
}
