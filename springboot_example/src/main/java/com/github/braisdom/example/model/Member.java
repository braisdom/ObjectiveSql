package com.github.braisdom.example.model;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Select;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.sql.function.AnsiFunctions.*;

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
        Member.Table memberTable = Member.asTable();
        Order.Table orderTable = Order.asTable();

        Select select = new Select();

        select.from(orderTable, memberTable);
        select.where(orderTable.memberId.eq(memberTable.id));

        select.project(memberTable.no, memberTable.name, memberTable.mobile);

        select.project(countDistinct(orderTable.no).as("order_count"))
                .project(sum(orderTable.quantity).as("total_quantity"))
                .project(sum(orderTable.amount).as("total_amount"))
                .project(min(orderTable.salesAt).as("first_shopping"))
                .project(max(orderTable.salesAt).as("last_shopping"));

        select.groupBy(memberTable.no, memberTable.name, memberTable.mobile);

        return select.execute(DatabaseType.MySQL, Member.class);
    }
}
