package com.github.braisdom.objsql.example.oracle;

import com.github.braisdom.objsql.annotations.*;
import com.github.braisdom.objsql.relation.RelationType;
import org.joda.time.DateTime;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public final class Domains {

    private Domains() {
    }

    @DomainModel(tableName = "SCOTT.MEMBERS",
            primaryClass = java.math.BigDecimal.class,
            primaryKeyDefaultValue = "members_seq.nextval")
    public static class Member {
        @Size(min = 5, max = 20)
        private String no;

        @Queryable
        private String name;

        @Queryable
        @Column(transition = BigDecimalToIntegerTransition.class)
        private Integer gender;

        private String mobile;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<Order> orders;

        @Column(transition = JsonColumnTransition.class)
        private Map extendedAttributes;

        @Column(transition = OracleDateTimeTransition.class, sqlType = JDBCType.TIMESTAMP)
        private Timestamp registeredAt;

        @Column(transition = OracleDateTimeTransition.class, sqlType = JDBCType.TIMESTAMP)
        private Timestamp updatedAt;

        @Transient
        private String otherInfo;

        public Member setRegisteredAtWithJoda(DateTime dateTime) {
            registeredAt = new Timestamp(dateTime.getMillis());
            return this;
        }
    }

    @DomainModel(tableName = "SCOTT.ORDERS",
            primaryClass = java.math.BigDecimal.class,
            primaryKeyDefaultValue = "members_seq.nextval")
    public static class Order {
        private String no;
        private BigDecimal memberId;
        private BigDecimal amount;
        private BigDecimal quantity;

        @Column(transition = OracleDateTimeTransition.class, sqlType = JDBCType.TIMESTAMP)
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

    @DomainModel
    public static class OrderLine {
        private String orderNo;
        private BigDecimal amount;
        private BigDecimal quantity;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Order order;
    }
}