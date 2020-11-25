package com.github.braisdom.objsql.pagination;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Databases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class PagedSQLBuilderTest {

    @Test
    public void testAnsiSQLPagination() throws SQLException {
        PagedSQLBuilder sqlBuilder = Databases.getPagedSQLBuilderFactory()
                .createPagedSQLBuilder(DatabaseType.Unknown);
        String querySQL1 = "SELECT * FROM members";
        String querySQL2 = "SELECT name FROM members";

        String countSQL1 = sqlBuilder.buildCountSQL(querySQL1);
        String countSQL2 = sqlBuilder.buildCountSQL(querySQL2);

        Assertions.assertEquals(countSQL1, "SELECT COUNT(*) AS count_ FROM (SELECT * FROM members) AS T");
        Assertions.assertEquals(countSQL2, "SELECT COUNT(*) AS count_ FROM (SELECT name FROM members) AS T");
    }
}
