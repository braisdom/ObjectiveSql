package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.annotations.*;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.transition.SqlDateTimeTransitional;

import javax.validation.constraints.Size;
import java.sql.*;
import java.util.List;
import java.util.Map;

public final class Domains {

    private Domains() {
    }

    @DomainModel
    public static class Member {
        // The validation constraints, it will be applied for validate method.
        // for example: Validator.Violation[] violations = member.validate();
        @Size(min = 5, max = 20)
        private String no;

        // The queryByName method will be generated, usage example:
        // List<Member> members = Member.queryByName("Benjamin");
        @Queryable
        private String name;

        @Queryable
        private Integer gender;
        private String mobile;

        // The HAS_MANY_ORDERS field will be generated,
        // public static final Relationship HAS_MANY_ORDERS = ...
        @Relation(relationType = RelationType.HAS_MANY)
        private List<Order> orders;

        @Column(transition = JsonColumnTransitional.class)
        private Map extendedAttributes;

        // The field will not be save into database;
        @Transient
        private String otherInfo;
    }

    @DomainModel
    public static class Order {
        private String no;
        private Integer memberId;
        private Float amount;
        private Float quantity;

        @Column(transition = SqlDateTimeTransitional.class)
        private Timestamp salesAt;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Member member;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<OrderLine> orderLines;

        @Transactional
        public static void makeOrder(Order order, OrderLine... orderLines) throws SQLException {
            Order.create(order, true);
            OrderLine.create(orderLines, true);
        }
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
