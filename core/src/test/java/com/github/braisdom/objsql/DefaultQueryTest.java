package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.DomainModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.objsql.DatabaseType.*;

public class DefaultQueryTest {

    @Test
    public void testSimpleSQL() {
        Query query = new DefaultQuery(Domain.class);

        Assertions.assertEquals(query.getQuerySQL(MySQL.getDatabaseProductName()), "SELECT * FROM `domains`");
        Assertions.assertEquals(query.getQuerySQL(PostgreSQL.getDatabaseProductName()), "SELECT * FROM \"domains\"");
        Assertions.assertEquals(query.getQuerySQL(Oracle.getDatabaseProductName()), "SELECT * FROM \"DOMAINS\"");
    }

    @DomainModel
    public static class Domain {
        private String name;
    }
}
