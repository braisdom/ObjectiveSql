package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.annotations.*;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.transition.SqlDateTimeTransition;
import org.joda.time.DateTime;

import javax.validation.constraints.Size;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public final class Domains {

    private Domains() {
    }

    // @DomainModel(primaryClass = Integer.class) for SQLite
//    @DomainModel(primaryKeyDefaultValue = "default")
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

        @Column(transition = JsonColumnTransition.class)
        private Map extendedAttributes;

        @Column(transition = SqlDateTimeTransition.class)
        private Timestamp registeredAt;

        @Column(transition = SqlDateTimeTransition.class)
        private Timestamp updatedAt;

        // The field will not be save into database;
        @Transient
        private String otherInfo;

        public Member setRegisteredAtWithJoda(DateTime dateTime) {
            registeredAt = new Timestamp(dateTime.getMillis());
            return this;
        }
    }

    // @DomainModel(primaryClass = Integer.class) for SQLite
    @DomainModel
    public static class Order {
        private String no;
        private Long memberId;
        private Float amount;
        private Float quantity;

        @Column(transition = SqlDateTimeTransition.class)
        private Timestamp salesAt;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Member member;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<OrderLine> orderLines;

        @Transactional
        public static void makeOrder(Order order, OrderLine... orderLines) throws SQLException {
            Order.create(order, false);
            OrderLine.create(orderLines, false);
        }
    }

    // @DomainModel(primaryClass = Integer.class) for SQLite
    @DomainModel
    public static class OrderLine {
        private String orderNo;
        private Float amount;
        private Float quantity;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Order order;
    }
}