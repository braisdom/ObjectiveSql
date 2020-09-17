package com.github.braisdom.objsql;

public interface ForcedFieldValueConverterFactory {

    ForcedFieldValueConverter createValueConverter(Class clazz);
}
