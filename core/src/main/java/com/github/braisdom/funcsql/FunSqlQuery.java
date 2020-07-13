package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public interface FunSqlQuery extends Query {

    List<Row> executeRawly(Relation... relations) throws SQLException;
}
