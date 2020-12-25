package com.github.braisdom.objsql;

/**
 * By default, rule of table name is the name of Java class underlinized.
 * If you have different rule, you can implement it and inject it into {@code Tables}
 *
 * @see Tables#installTableNameEncoder(TableNameEncoder)
 * @author braisdom
 */
public interface TableNameEncoder {

    /**
     * Returns a table name by given class of domain model.
     * @param domainModelClass
     * @return
     */
    String getTableName(Class domainModelClass);

}
