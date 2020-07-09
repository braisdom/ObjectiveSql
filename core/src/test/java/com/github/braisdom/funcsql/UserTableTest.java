package com.github.braisdom.funcsql;

import com.github.braisdom.xsql.impl.UserTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserTableTest {

    private void createTables(Connection connection) throws SQLException {
        try {
            connection.createStatement().execute("drop table users;");
            connection.createStatement().execute("drop table user_profiles");
        }catch (SQLException ex) {}

        connection.createStatement().execute("create table users (id integer, name string, role_id integer, domain_id integer)");
        connection.createStatement().execute("create table user_profiles (id integer, name string, user_id integer)");
        connection.createStatement().execute("insert into users(id, name) values (1, 'hello'), (2, 'world')");
        connection.createStatement().execute("insert into user_profiles(id, name, user_id) values (1, 'profile_1', 1), " +
                "(2, 'profile_2', 2)");
    }

    @Test
    public void testQuerySimplyHasMany() throws SQLException {
        ConnectionFactory connectionFactory = new ConnectionFactoryTest();
        Connection connection = connectionFactory.getConnection();

        Database.setConnectionFactory(connectionFactory);

        createTables(connection);

        SimpleQuery simpleQuery = UserTable.createSimpleQuery();
        List<User> users = simpleQuery.hasMany(UserProfile.class)
                .limit(2).orderBy("id ASC").executeSimply(User.class);

        Assertions.assertEquals(users.size(), 2);
        Assertions.assertEquals(users.get(0).getId(), 1);
        Assertions.assertNotNull(users.get(0).getUserProfiles());
        Assertions.assertEquals(users.get(0).getUserProfiles().size(), 2);

//            SimpleQuery simpleQuery2 = UserTable.createSimpleQuery();
//            List<User> users2 = simpleQuery2.filter("name = '%s'", "hello").limit(1).executeSimply(User.class);
//
//            Assertions.assertEquals(users2.size(), 1);
//            Assertions.assertEquals(users2.get(0).getId(), 1);
//
//            SimpleQuery simpleQuery3 = UserTable.createSimpleQuery();
//            List<Row> users3 = simpleQuery3.select(new String[]{"name", "count(1) as name_count"})
//                    .groupBy("name").executeSimply(User.class);
//
//            Assertions.assertEquals(users3.size(), 2);
//            Assertions.assertEquals(users3.get(0).getInteger("name_count"), 1);
    }

    @Test
    public void testQuerySimplyBelongsTo() throws SQLException {
        ConnectionFactory connectionFactory = new ConnectionFactoryTest();
        Connection connection = connectionFactory.getConnection();

        Database.setConnectionFactory(connectionFactory);

        createTables(connection);

        SimpleQuery simpleQuery = UserTable.createSimpleQuery();
    }
}
