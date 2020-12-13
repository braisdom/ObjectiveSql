package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.DatabaseType;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultExpressionContextTest {

    @Test
    public void testGetAlias() {
        DefaultExpressionContext context = new DefaultExpressionContext(DatabaseType.MySQL);
        Dataset dataset = mock(Dataset.class);
        when(dataset.getOriginalName()).thenReturn("test");

        Dataset dataset1 = mock(Dataset.class);
        when(dataset1.getOriginalName()).thenReturn("test");

        Assert.assertEquals("test_t0", context.getAlias(dataset, true));
        Assert.assertEquals("test_t1", context.getAlias(dataset1, true));
    }

    @Test
    public void testQuote() {
        DefaultExpressionContext context = new DefaultExpressionContext(DatabaseType.MySQL);
        DefaultExpressionContext context1 = new DefaultExpressionContext(DatabaseType.PostgreSQL);
        DefaultExpressionContext context2 = new DefaultExpressionContext(DatabaseType.HSQLDB);

        Assert.assertEquals("`test`", context.quoteTable("test"));
        Assert.assertEquals("`test`.`test`", context.quoteTable("test.test"));

        Assert.assertEquals("\"test\"", context1.quoteTable("test"));
        Assert.assertEquals("\"test\".\"test\"", context1.quoteTable("test.test"));

        Assert.assertEquals("test", context2.quoteTable("test"));
        Assert.assertEquals("test.test", context2.quoteTable("test.test"));

        Assert.assertEquals("`test`", context.quoteColumn("test"));
        Assert.assertEquals("\"test\"", context1.quoteColumn("test"));
        Assert.assertEquals("test", context2.quoteColumn("test"));

        Assert.assertEquals("'test'", context.quoteString("test"));
        Assert.assertEquals("\"test\"", context1.quoteColumn("test"));
        Assert.assertEquals("test", context2.quoteColumn("test"));
    }
}
