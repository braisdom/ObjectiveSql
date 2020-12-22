package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.PrimaryKey;
import org.junit.Assert;
import org.junit.Test;

public class TablesTest {

    @Test
    public void testGetTableName() {
        Assert.assertEquals(Tables.getTableName(DemoTable.class), "demo_tables");
        Assert.assertEquals(Tables.getTableName(DemoTable2.class), "demo_table2");
    }

    @Test
    public void testGetColumnName() {
        Assert.assertEquals(Tables.getColumnName(DemoTable.class, "testField"), "test_field");
        Assert.assertEquals(Tables.getColumnName(DemoTable2.class, "testField"), "cus_test_field");
    }

    @Test
    public void testPrimaryKey() {
        Assert.assertEquals(Tables.getPrimaryField(DemoTable.class).getName(), "id");
        Assert.assertEquals(Tables.getPrimaryKeyColumnName(DemoTable.class), "id");
        Assert.assertEquals(Tables.getPrimaryField(DemoTable3.class).getName(), "testId");
        Assert.assertEquals(Tables.getPrimaryKeyColumnName(DemoTable3.class), "test_id");
    }

    @DomainModel
    private static class DemoTable {
        private String testField;
    }

    @DomainModel(tableName = "demo_table2")
    private static class DemoTable2 {
        @Column(name = "cus_test_field")
        private String testField;
    }

    @DomainModel
    private static class DemoTable3 {
        @PrimaryKey
        private Integer testId;
        @Column(name = "cus_test_field")
        private String testField;
    }
}
