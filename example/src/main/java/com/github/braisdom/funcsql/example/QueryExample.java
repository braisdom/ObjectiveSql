package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.PersistenceException;
import com.github.braisdom.funcsql.Table;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryExample {

    private static final String[] MEMBER_NAMES = {"Joe","Juan","Jack","Albert","Jonathan","Justin","Terry","Gerald","Keith","Samuel",
            "Willie","Ralph","Lawrence","Nicholas","Roy","Benjamin","Bruce","Brandon","Adam","Harry","Fred","Wayne","Billy","Steve",
            "Louis","Jeremy","Aaron","Randy","Howard","Eugene","Carlos","Russell","Bobby","Victor","Martin","Ernest","Phillip","Todd",
            "Jesse","Craig","Alan","Shawn","Clarence","Sean","Philip","Chris","Johnny","Earl","Jimmy","Antonio","James","John","Robert",
            "Michael","William","David","Richard","Charles","Joseph","Thomas","Christopher","Daniel","Paul","Mark","Donald","George",
            "Kenneth","Steven","Edward","Brian","Ronald","Anthony","Kevin","Jason","Matthew","Gary","Timothy","Jose","Larry","Jeffrey",
            "Frank","Scott","Eric","Stephen","Andrew","Raymond","Gregory","Joshua","Jerry","Dennis","Walter","Patrick","Peter","Harold",
            "Douglas","Henry","Carl","Arthur","Ryan","Roger"};

    private static void createMembers() throws SQLException, PersistenceException {
        List<Domains.Member> members = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            members.add(new Domains.Member()
                    .setNo("Q200000" + i)
                    .setName(MEMBER_NAMES[i])
                    .setGender(0)
                    .setMobile("150000000" + i));
        }

        Domains.Member.create(members.toArray(new Domains.Member[]{}));
    }

    private static void countMember() throws SQLException {
        Domains.Member.count();
        Domains.Member.count("id > 10");
    }

    private static void rawQuery() throws SQLException {
        Domains.Member.query("SELECT id, name FROM members WHERE id < 10");
    }

    public static void main(String[] args) throws SQLException, PersistenceException {
        File file = new File("query_example.db");

        if (file.exists())
            file.delete();

        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Connection connection = Database.getConnectionFactory().getConnection();
        Domains.createTables(connection);

        createMembers();
        countMember();
        rawQuery();
    }
}
