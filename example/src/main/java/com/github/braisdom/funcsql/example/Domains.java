package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.Persistence;
import com.github.braisdom.funcsql.PersistenceFactory;
import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.annotations.Volatile;
import com.github.braisdom.funcsql.relation.RelationType;
import lombok.Data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class Domains {

    private Domains() {
    }

    @DomainModel
    public static class Member {
        private String no;
        private String name;
        private Integer gender;
        private String mobile;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<Order> orders;

        @Column(transition = JsonColumnTransitional.class)
        private Map extendedAttributes;

        @Volatile
        private String otherInfo;
    }

    @DomainModel
    public static class Order {
        private String no;
        private Long memberId;
        private Float amount;
        private Float quantity;
        private Date salesAt;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Member member;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<OrderLine> orderLines;
    }

    @DomainModel
    public static class OrderLine {
        private String orderNo;
        private Float amount;
        private Float quantity;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Order order;
    }

    public static void createTables(Connection connection) throws SQLException {
        connection.createStatement().execute("drop table if exists members;");
        connection.createStatement().execute("drop table if exists orders");
        connection.createStatement().execute("drop table if exists order_lines");

        connection.createStatement().execute("create table members (id INTEGER PRIMARY KEY AUTOINCREMENT, no TEXT, " +
                "name TEXT, gender INTEGER, mobile TEXT, extended_attributes TEXT)");
        connection.createStatement().execute("create table orders (id INTEGER PRIMARY KEY AUTOINCREMENT, no TEXT, member_id INTEGER, " +
                "amount REAL, quantity REAL, sales_at TEXT)");
        connection.createStatement().execute("create table order_lines (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_no TEXT, amount REAL, quantity REAL)");
    }
}
