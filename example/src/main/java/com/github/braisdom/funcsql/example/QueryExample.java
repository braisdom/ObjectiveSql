package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import org.junit.Assert;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class QueryExample {

    private static Logger logger = Logger.getLogger(QueryExample.class.getName());

    private static final String[] MEMBER_NAMES = {"Joe","Juan","Jack","Albert","Jonathan","Justin","Terry","Gerald","Keith","Samuel",
            "Willie","Ralph","Lawrence","Nicholas","Roy","Benjamin","Bruce","Brandon","Adam","Harry","Fred","Wayne","Billy","Steve",
            "Louis","Jeremy","Aaron","Randy","Howard","Eugene","Carlos","Russell","Bobby","Victor","Martin","Ernest","Phillip","Todd",
            "Jesse","Craig","Alan","Shawn","Clarence","Sean","Philip","Chris","Johnny","Earl","Jimmy","Antonio","James","John","Robert",
            "Michael","William","David","Richard","Charles","Joseph","Thomas","Christopher","Daniel","Paul","Mark","Donald","George",
            "Kenneth","Steven","Edward","Brian","Ronald","Anthony","Kevin","Jason","Matthew","Gary","Timothy","Jose","Larry","Jeffrey",
            "Frank","Scott","Eric","Stephen","Andrew","Raymond","Gregory","Joshua","Jerry","Dennis","Walter","Patrick","Peter","Harold",
            "Douglas","Henry","Carl","Arthur","Ryan","Roger"};

    private static void createMembers() throws SQLException {
        List<Domains.Member> members = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            members.add(new Domains.Member()
                    .setNo("Q200000" + i)
                    .setName(MEMBER_NAMES[i])
                    .setGender(0)
                    .setMobile("150000000" + i));
        }

        int[] count = Domains.Member.create(members.toArray(new Domains.Member[]{}));
        Assert.assertEquals(count.length, 100);
    }

    private static void countMember() throws SQLException {
        int countGreater10 = Domains.Member.count("id > ?", new Object[]{10});

        Assert.assertEquals(countGreater10, 90);
    }

    private static void queryByName() throws SQLException {
        List<Domains.Member> member = Domains.Member.queryByName("Ralph");
        Assert.assertEquals(member.get(0).getName(), "Ralph");
        Assert.assertEquals(member.get(0).getId(), Integer.valueOf(12));
    }

    private static void rawQuery() throws SQLException {
        List<Domains.Member> members = Domains.Member.query("SELECT id, name FROM members WHERE id < ?", 10);
        List<Domains.Member> members2 = Domains.Member.query("SELECT * FROM members WHERE name = ?", "Jonathan");
        List<Domains.Member> members3 = Domains.Member.query("SELECT name AS _name FROM members WHERE name = ?", "Jonathan");

        Assert.assertEquals(members.size(), 9);
        Assert.assertEquals(members2.size(), 1);
        Assert.assertEquals(members3.get(0).getRawAttribute("_name"), "Jonathan");
    }

    private static void findFirst() throws SQLException {
        Domains.Member member = Domains.Member.findFirst("id = ?", 11);

        Assert.assertNotNull(member);
        Assert.assertEquals(member.getName(), "Willie");
    }

    public static void main(String[] args) throws SQLException {
        File file = new File("query_example.db");

        if (file.exists())
            file.delete();

        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Connection connection = Database.getConnectionFactory().getConnection();
        Domains.createTables(connection);

        createMembers();
        countMember();
        rawQuery();
        queryByName();
        findFirst();
    }
}
