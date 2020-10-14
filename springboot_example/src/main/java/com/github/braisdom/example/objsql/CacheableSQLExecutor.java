package com.github.braisdom.example.objsql;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.objsql.DefaultSQLExecutor;
import com.github.braisdom.objsql.TableRowAdapter;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CacheableSQLExecutor<T> extends DefaultSQLExecutor<T> {

    private static final List<Class<? extends Serializable>> CACHEABLE_CLASSES =
            Arrays.asList(new Class[]{Member.class});
    private static final Integer CACHED_OBJECT_EXPIRED = 60;
    private static final String KEY_SHA = "SHA";

    private Jedis jedis = new Jedis("127.0.0.1", 6379);
    private MessageDigest messageDigest;

    public CacheableSQLExecutor() {
        try {
            messageDigest = MessageDigest.getInstance(KEY_SHA);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public List<T> query(Connection connection, String sql,
                         TableRowAdapter tableRowAdapter, Object... params) throws SQLException {
        Class<?> domainClass = tableRowAdapter.getDomainModelClass();

        if (CACHEABLE_CLASSES.contains(domainClass)) {
            if(!Serializable.class.isAssignableFrom(domainClass))
                throw new IllegalArgumentException(String.format("The %s cannot be serialized"));

            messageDigest.update(sql.getBytes());

            String hashedSqlId = new BigInteger(messageDigest.digest()).toString(64);
            byte[] rawObjects = jedis.get(hashedSqlId.getBytes());

            if (rawObjects != null) {
                return (List<T>) SerializationUtils.deserialize(rawObjects);
            } else {
                List<T> objects = super.query(connection, sql, tableRowAdapter, params);
                byte[] encodedObjects = SerializationUtils.serialize(objects);
                SetParams expiredParams = SetParams.setParams().ex(CACHED_OBJECT_EXPIRED);

                jedis.set(hashedSqlId.getBytes(), encodedObjects, expiredParams);

                return objects;
            }
        }
        return super.query(connection, sql, tableRowAdapter, params);
    }
}
