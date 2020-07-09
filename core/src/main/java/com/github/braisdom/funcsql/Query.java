package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public interface Query<T extends Class> extends Relation {

    List<T> execute(T rowClass, String sql) throws SQLException;

    List<Row> execute(String sql) throws SQLException;
}
