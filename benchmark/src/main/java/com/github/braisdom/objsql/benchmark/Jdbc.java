package com.github.braisdom.objsql.benchmark;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Jdbc implements ORMFramework {

    public static final String FRAMEWORK_NAME = "jdbc";

    private final HikariDataSource dataSource;

    public Jdbc(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void initialize() throws Exception {
        Connection conn = null;
        PreparedStatement pstat = null;
        try {
            conn = dataSource.getConnection();
            pstat = conn.prepareStatement("insert into user(id, name, age) values(?, ?, ?)");
            pstat.setInt(1, 1);
            pstat.setString(2, "ash");
            pstat.setInt(3, 25);
            pstat.executeUpdate();
            return;
        } finally {
            if (pstat != null) {
                pstat.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public User query() throws Exception {
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pstat = conn.prepareStatement("select id, name, age from user where id = ?");
            pstat.setInt(1, 1);
            rs = pstat.executeQuery();
            User u = null;
            if (rs.next()) {
                u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setAge(rs.getInt("age"));
            }
            return u;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstat != null) {
                pstat.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void teardown() {
        dataSource.close();
    }
}
