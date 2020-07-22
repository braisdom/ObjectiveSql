package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.Query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.funcsql.example.User.RHM_USER_PROFILE;

public class QueryExample {

    private static void createTables(Connection connection) throws SQLException {
        try {
            connection.createStatement().execute("drop table users;");
            connection.createStatement().execute("drop table user_profiles");
        } catch (SQLException ex) {
        }

        connection.createStatement().execute("create table users (id integer, name string, role_id integer, domain_id integer)");
        connection.createStatement().execute("create table user_profiles (id integer, name string, user_id integer)");
        connection.createStatement().execute("insert into users(id, name) values (1, 'hello'), (2, 'world')");
        connection.createStatement().execute("insert into user_profiles(id, name, user_id) values (5, 'profile_1', 1), " +
                "(6, 'profile_2', 2)");
    }

    public static void main(String[] args) throws SQLException {
        Database.installConnectionFactory(new SqliteConnectionFactory());
        createTables(Database.getConnectionFactory().getConnection());

        Query<User> userQuery = User.createQuery();
        List<UserProfile> users = userQuery.limit(2).execute(RHM_USER_PROFILE);
        System.out.println();
    }
}
