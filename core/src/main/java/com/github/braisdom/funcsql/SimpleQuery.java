package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public interface SimpleQuery<T extends Class> extends Query<T> {

    List<T> execute(Relation... relations) throws SQLException;
}
