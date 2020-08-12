package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import org.junit.Assert;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelationExample {

    private static final String[] MEMBER_NAMES = {"Joe","Juan","Jack","Albert","Jonathan","Justin","Terry"};

    private static void createRelationData() throws SQLException {
        List<Domains.Member> members = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            members.add(new Domains.Member()
                    .setId(i)
                    .setNo("Q200000" + i)
                    .setName(MEMBER_NAMES[i])
                    .setGender(0)
                    .setMobile("150000000" + i));
        }

        int[] count = Domains.Member.create(members.toArray(new Domains.Member[]{}), true);
        Assert.assertEquals(count.length, 6);
    }

    public static void main(String args[]) throws SQLException {
        File file = new File("relation_example.db");

        if (file.exists())
            file.delete();

        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Connection connection = Database.getConnectionFactory().getConnection();
        Domains.createTables(connection);

        createRelationData();
    }
}
