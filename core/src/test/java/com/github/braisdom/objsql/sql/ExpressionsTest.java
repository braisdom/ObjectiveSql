package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.DatabaseType;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpressionsTest {

    @Test
    public void testColumn() throws SQLSyntaxException {
        Dataset dataset1 = mock(Dataset.class);
        when(dataset1.getOriginalName()).thenReturn("test");

        Dataset dataset2 = mock(Dataset.class);
        when(dataset2.getOriginalName()).thenReturn("test");
        when(dataset2.getAlias()).thenReturn("alias");

        Column column1 = Expressions.column(dataset1, "name");
        Column column2 = Expressions.column(dataset2, "name");

        Assert.assertEquals("`test_t0`.`name` ASC", column1.asc().toSql(DatabaseType.MySQL).trim());
        Assert.assertEquals("`alias`.`name` ASC", column2.asc().toSql(DatabaseType.MySQL).trim());
        Assert.assertEquals("\"test_t0\".\"name\" ASC", column1.asc().toSql(DatabaseType.PostgreSQL).trim());
        Assert.assertEquals("\"alias\".\"name\" ASC", column2.asc().toSql(DatabaseType.PostgreSQL).trim());
        Assert.assertEquals("test_t0.name ASC", column1.asc().toSql(DatabaseType.HSQLDB).trim());
        Assert.assertEquals("alias.name ASC", column2.asc().toSql(DatabaseType.HSQLDB).trim());
    }

    @Test
    public void testParen() throws SQLSyntaxException {
        Dataset dataset1 = mock(Dataset.class);
        when(dataset1.getOriginalName()).thenReturn("test");

        Dataset dataset2 = mock(Dataset.class);
        when(dataset2.getOriginalName()).thenReturn("test");
        when(dataset2.getAlias()).thenReturn("alias");

        Column column1 = Expressions.column(dataset1, "name");
        Column column2 = Expressions.column(dataset2, "name");

        Assert.assertEquals("(`test_t0`.`name`  + `alias`.`name` )",
                Expressions.paren(column1.plus(column2)).toSql(DatabaseType.MySQL).trim());
    }
}
