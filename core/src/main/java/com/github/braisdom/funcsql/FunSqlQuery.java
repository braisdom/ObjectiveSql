package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public interface FunSqlQuery<T> extends Query<T> {

    List<T> execute() throws SQLException;

    List<Row> execute(Relation... relations) throws SQLException;
}
