package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.example.domains.Member;
import com.github.braisdom.objsql.example.domains.Order;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class QueryExample extends MSSQLExample {

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
                    .setMemberId((long)i)
                    .setAmount(RandomUtils.nextFloat(10.0f, 30.0f))
                    .setQuantity(RandomUtils.nextFloat(100.0f, 300.0f))
                    .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00")));
        }

        int[] createdMembersCount = Member.create(members.toArray(new Member[]{}),
                true, true);
        int[] createdOrderCount = Order.create(orders.toArray(new Order[]{}),
                true, true);

        Assert.assertEquals(createdMembersCount.length, 100);
        Assert.assertEquals(createdOrderCount.length, 100);
    }

    @Test
    public void countMember() throws SQLException {
        prepareQueryData();

        long allMemberCount = Member.countAll();
        long memberCount = Member.count("id > ?", 10);

        Assert.assertTrue(allMemberCount == 100);
        Assert.assertTrue(memberCount == 90);
    }

    @Test
    public void queryByName() throws SQLException {
        prepareQueryData();
        Member member = Member.queryByName("Ralph");

        Assert.assertNotNull(member);
        Assert.assertEquals(member.getName(), "Ralph");
    }

    @Test
    public void queryAll() throws SQLException {
        prepareQueryData();

        List<Member> members = Member.queryAll();

        Assert.assertEquals(members.size(), 100);
    }

    @Test
    public void rawQuery() throws SQLException {
        prepareQueryData();

        List<Member> members = Member.queryBySql("SELECT id, name FROM members WHERE id > ?", 10);
        List<Member> members2 = Member.queryBySql("SELECT * FROM members WHERE name = ?", "Jonathan");
        List<Member> members3 = Member.queryBySql("SELECT name AS _name FROM members WHERE name = ?", "Jonathan");

        Assert.assertTrue(members.size() > 0);
        Assert.assertTrue(members2.size() > 0);
        Assert.assertTrue(members3.size() > 0);
        Assert.assertEquals(members3.get(0).getRawAttribute("_name"), "Jonathan");
    }

    @Test
    public void queryFirst() throws SQLException {
        prepareQueryData();

        Member member = Member.queryFirst("name = ?", "Jonathan");
        Assert.assertNotNull(member);
        Assert.assertEquals(member.getName(), "Jonathan");
    }

    @Test
    public void queryByPredicate() throws SQLException {
        prepareQueryData();

        List<Member> members = Member.query("id > ?", 8);

        Assert.assertNotNull(members);
        Assert.assertTrue(members.size() == 92);
    }
}
