package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TablesTest {

    @Test
    public void testGetTableName() {
        Assertions.assertEquals(Tables.getTableName(DemoTable.class), "demo_tables");
        Assertions.assertEquals(Tables.getTableName(DemoTable2.class), "demo_table2");
    }

    @Test
    public void testGetColumnName() {
        Assertions.assertEquals(Tables.getColumnName(DemoTable.class, "testField"), "test_field");
        Assertions.assertEquals(Tables.getColumnName(DemoTable2.class, "testField"), "cus_test_field");
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
}
