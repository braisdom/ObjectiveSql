package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.Update;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DevelopTest {
    //@Before
    public void before() throws SQLException {
        Database.installConnectionFactory(new MySQLConnectionFactory());
        Connection connection = Database.getConnectionFactory().getConnection();

        try {
            connection.createStatement().execute("drop table users;");
            connection.createStatement().execute("drop table user_profiles");
        } catch (SQLException ex) {
        }

        connection.createStatement().execute("create table users (id integer, name string, role_id integer, domain_id integer)");
        connection.createStatement().execute("create table user_profiles (id integer, name string, user_id integer)");
        connection.createStatement().execute("insert into users(id, name) values (1, 'hello'), (2, 'world')");
        connection.createStatement().execute("insert into user_profiles(id, name, user_id) values (1, 'profile_1', 1), " +
                "(2, 'profile_2', 2)");
    }

    @Test
    public void testUpdate() throws SQLException {
        Database.installConnectionFactory(new MySQLConnectionFactory());
        Update update = User.createUpdate();
        update.set("role_id = %s", 3).where("name = '%s'", "hello").execute();
    }
}
