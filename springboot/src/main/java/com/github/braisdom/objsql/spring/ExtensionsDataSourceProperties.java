package com.github.braisdom.objsql.spring;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.datasource")
public class ExtensionsDataSourceProperties {
    private Map<String, DataSourceProperties> extensions = new LinkedHashMap();

    public Map<String, DataSourceProperties> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, DataSourceProperties> extensions) {
        this.extensions = extensions;
    }
}
