package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.example.domains.Member;
import com.github.braisdom.objsql.example.domains.Order;
import com.github.braisdom.objsql.pagination.Page;
import com.github.braisdom.objsql.pagination.PagedList;
import com.github.braisdom.objsql.pagination.Paginator;
import com.github.braisdom.objsql.sql.LogicalExpression;
import com.github.braisdom.objsql.sql.Select;
import com.github.braisdom.objsql.sql.expression.EternalExpression;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.github.braisdom.objsql.sql.function.Ansi.count;
import static com.github.braisdom.objsql.sql.function.Ansi.sum;

public class ComplexSQLExample extends SQLiteExample {

    private static final String[] MEMBER_NAMES = {"Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry", "Gerald", "Keith", "Samuel",
            "Willie", "Ralph", "Lawrence", "Nicholas", "Roy", "Benjamin", "Bruce", "Brandon", "Adam", "Harry", "Fred", "Wayne", "Billy", "Steve",
            "Louis", "Jeremy", "Aaron", "Randy", "Howard", "Eugene", "Carlos", "Russell", "Bobby", "Victor", "Martin", "Ernest", "Phillip", "Todd",
            "Jesse", "Craig", "Alan", "Shawn", "Clarence", "Sean", "Philip", "Chris", "Johnny", "Earl", "Jimmy", "Antonio", "James", "John", "Robert",
            "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George",
            "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
            "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold",
            "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"};

    @Test
    public void prepareQueryData() throws SQLException {
        List<Member> members = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            members.add(new Member()
                    .setNo("Q200000" + i)
                    .setName(MEMBER_NAMES[i])
                    .setGender(0)
                    .setMobile("150000000" + i));
        }

        for (int i = 0; i < 100; i++) {
            orders.add(new Order()
                    .setNo("20200000" + i)
                    .setMemberId(i)
                    .setAmount(RandomUtils.nextDouble(10.0f, 30.0f))
                    .setQuantity(RandomUtils.nextDouble(100.0f, 300.0f))
                    .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00")));
        }

        int[] createdMembersCount = Member.create(members.toArray(new Member[]{}),
                true);
        int[] createdOrderCount = Order.create(orders.toArray(new Order[]{}),
                true);

        Assert.assertEquals(createdMembersCount.length, 100);
        Assert.assertEquals(createdOrderCount.length, 100);
    }

    @Test
    public void simpleQuery() throws SQLException {
        prepareQueryData();

        Member.Table member = Member.asTable();
        Select select = new Select();

        select.project(member.gender, count().as("member_count"))
                .from(member)
                .groupBy(member.gender);

        List<Member> members = select.execute(Member.class);

        Assert.assertTrue(members.size() > 0);
        Assert.assertTrue(((Integer)members.get(0).getRawAttribute("member_count")) == 100);
    }

    @Test
    public void joinQuery() throws SQLException {
        prepareQueryData();

        Member.Table member = Member.asTable();
        Order.Table order = Order.asTable();

        Select select = new Select();

        select.project(member.no, member.name, count().as("order_count"))
                .from(member, order)
                .where(member.id.eq(order.memberId))
                .groupBy(member.no, member.name);

        List<Member> members = select.execute(Member.class);
        Assert.assertTrue(members.size() > 0);
    }

    @Test
    public void join2Query() throws SQLException {
        prepareQueryData();

        Member.Table member = Member.asTable();
        Order.Table order = Order.asTable();

        Select select = new Select();

        select.project(member.no, member.name, count().as("order_count"))
                .from(member)
                .leftOuterJoin(order, order.memberId.eq(member.id))
                .groupBy(member.no, member.name);

        List<Member> members = select.execute(Member.class);
        Assert.assertTrue(members.size() > 0);
    }

    @Test
    public void pagedQuery() throws SQLException {
        prepareQueryData();

        Member.Table member = Member.asTable();
        Order.Table order = Order.asTable();

        Paginator<Member> paginator = Databases.getPaginator();
        Page page = Page.create(0, 20);

        Select select = new Select();

        select.project(member.no, member.name, count().as("order_count"))
                .from(member, order)
                .where(member.id.eq(order.memberId))
                .groupBy(member.no, member.name);

        PagedList<Member> members = paginator.paginate(page, select, Member.class);
        Assert.assertTrue(members.size() > 0);
    }

    @Test
    public void complexExpressionQuery() throws SQLException {
        prepareQueryData();

        Order.Table orderTable = Order.asTable();
        Select select = new Select();

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        long begin = DateTime.parse("2020-05-01 00:00:00", dateTimeFormatter).getMillis();
        long end = DateTime.parse("2020-05-02 23:59:59", dateTimeFormatter).getMillis();

        select.project((sum(orderTable.amount) / sum(orderTable.quantity) * 100).as("unit_amount"))
                .from(orderTable)
                .where(orderTable.quantity > 30 &&
                        orderTable.salesAt.between(begin, end))
                .groupBy(orderTable.memberId);

        List<Order> orders = select.execute(Order.class);
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void dynamicExpressionQuery() throws SQLException {
        prepareQueryData();

        String[] filteredNo = {"202000001", "202000002", "202000003"};
        int filteredQuantity = 0;

        Order.Table orderTable = Order.asTable();
        Select select = new Select();
        LogicalExpression eternalExpression = new EternalExpression();

        if(filteredNo.length > 0) {
            eternalExpression = eternalExpression.and(orderTable.no.in(filteredNo));
        }

        if(filteredQuantity != 0) {
            eternalExpression = eternalExpression.and(orderTable.quantity > filteredQuantity);
        }

        select.project((sum(orderTable.amount) / sum(orderTable.quantity) * 100).as("unit_amount"))
                .from(orderTable)
                .where(eternalExpression)
                .groupBy(orderTable.memberId);

        List<Order> orders = select.execute(Order.class);
        Assert.assertTrue(orders.size() > 0);
    }
}
