package com.github.braisdom.example.objsql;

import com.github.braisdom.objsql.Logger;
import com.github.braisdom.objsql.LoggerFactory;
import com.github.braisdom.objsql.util.StringUtil;

import java.util.Arrays;

public class ObjLoggerFactoryImpl implements LoggerFactory {

    private class ObjLoggerImpl implements Logger {

        private final org.slf4j.Logger logger;

        public ObjLoggerImpl(org.slf4j.Logger logger) {
            this.logger = logger;
        }

        @Override
        public void debug(long elapsedTime, String sql, Object[] params) {
            logger.debug(createLogContent(elapsedTime, sql, params));
        }

        @Override
        public void info(long elapsedTime, String sql, Object[] params) {
            logger.info(createLogContent(elapsedTime, sql, params));
        }

        @Override
        public void error(String message, Throwable throwable) {
            logger.error(message, throwable);
        }

        private String createLogContent(long elapsedTime, String sql, Object[] params) {
            String[] paramStrings = Arrays.stream(params).map(param -> String.valueOf(param)).toArray(String[]::new);
            String paramString = String.join(",", paramStrings);
            return String.format("[%dms] %s, with: [%s]", elapsedTime, sql, String.join(",",
                    paramString.length() > 100 ? StringUtil.truncate(paramString, 99) : paramString));
        }
    }

    @Override
    public Logger create(Class<?> clazz) {
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(clazz);
        return new ObjLoggerImpl(logger);
    }
}
