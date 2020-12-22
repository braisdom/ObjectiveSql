package com.github.braisdom.objsql;

public interface TableNameEncoder {

    String getTableName(Class domainModelClass);

}
