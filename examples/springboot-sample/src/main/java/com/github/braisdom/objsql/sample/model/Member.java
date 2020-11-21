package com.github.braisdom.objsql.sample.model;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Select;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.sql.function.Ansi.*;

@DomainModel
public class Member implements Serializable {

    @Queryable
    @Column(updatable = false)
    private String no;

    @Queryable
    private String name;
    private Integer gender;
    private String mobile;
    private String otherInfo;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<Order> orders;

    /**
     * Count order by distinct member, and summary amount and quantity of order.
     *
     * @return
     * @throws SQLSyntaxException
     * @throws SQLException
     */
    public static List<Member> countOrders() throws SQLSyntaxException, SQLException {
        Member.Table member = Member.asTable();
        Order.Table order = Order.asTable();

        Select select = new Select();

        select.from(order, member)
                .where(order.memberId.eq(member.id));
        select.project(member.no,
                member.name,
                member.mobile,
                countDistinct(order.no).as("order_count"),
                sum(order.quantity).as("total_quantity"),
                sum(order.amount).as("total_amount"),
                min(order.salesAt).as("first_shopping"),
                max(order.salesAt).as("last_shopping"));
        select.groupBy(member.no, member.name, member.mobile);

        return select.execute(DatabaseType.MySQL, Member.class);
    }
}
