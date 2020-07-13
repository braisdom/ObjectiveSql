package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public interface SimpleQuery<T> extends Query<T> {

    List<T> execute(Relation... relations) throws SQLException;

    List<Row> execute() throws SQLException;
}
