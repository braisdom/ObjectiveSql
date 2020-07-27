package com.github.braisdom.funcsql;

import java.lang.reflect.Field;

public interface ColumnValueIntervenor {

    Object sleeping(Field field, Object value);

    Object waking(Field field, Object value);
}
