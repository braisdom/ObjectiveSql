package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.DefaultPersistence;
import com.github.braisdom.funcsql.Persistence;
import com.github.braisdom.funcsql.PersistenceException;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.annotations.Volatile;
import com.github.braisdom.funcsql.relation.RelationType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

public class PersistenceExample {

    @DomainModel
    public static class Member {
        private long id;
        private String no;
        private String name;
        private int gender;

        @Volatile
        private String mobile;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<Order> orders;

        @Override
        public String toString() {
            return "Member{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @DomainModel
    public static class Order {
        private long id;
        private String no;
        private long memberId;
        private float amount;
        private float quantity;
        private Time salesAt;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Member member;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<OrderLine> orderLines;

        @Override
        public String toString() {
            return "Order{" +
                    "no='" + no + '\'' +
                    '}';
        }
    }

    @DomainModel
    public static class OrderLine {
        private long id;
        private String orderNo;
        private float amount;
        private float quantity;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Order order;
    }

    private static void createTables(Connection connection) throws SQLException {
        try {
            connection.createStatement().execute("drop table members;");
            connection.createStatement().execute("drop table orders");
            connection.createStatement().execute("drop table order_lines");
        } catch (SQLException ex) {
        }

        connection.createStatement().execute("create table members (id INTEGER, no TEXT, name TEXT, gender INTEGER, mobile TEXT)");
        connection.createStatement().execute("create table orders (id INTEGER, no TEXT, member_id INTEGER, " +
                "amount REAL, quantity REAL, sales_at TEXT)");
        connection.createStatement().execute("create table order_lines (id integer, order_no TEXT, amount REAL, quantity REAL)");

//        connection.createStatement().execute("insert into members(id, no, name, gender, mobile) " +
//                "values (1, '000001', 'Smith', 1, '15000000001'), (2, '000002', 'Lewis', 2, '15000000002')");
//        connection.createStatement().execute("insert into orders(id, no, member_id, amount, quantity, sales_at) " +
//                "values (1, '1000000', 1, 100.50, 1.0, '1970-01-01 08:00:02')," +
//                "       (2, '1000002', 1, 200.50, 3.0, '1970-01-01 10:00:02')");
    }

    public static void main(String args[]) throws SQLException, PersistenceException {
        Database.installConnectionFactory(new SqliteConnectionFactory("persistence.db"));
        createTables(Database.getConnectionFactory().getConnection());

        Member newMember = new Member()
        .setId(1)
        .setNo("100000")
        .setName("Lewis")
        .setGender(1)
        .setMobile("15011112222");

        newMember.save();
    }
}
