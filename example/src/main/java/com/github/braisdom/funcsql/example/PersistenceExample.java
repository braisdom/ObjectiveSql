package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.PersistenceException;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PersistenceExample {

    private static void createMember() throws SQLException, PersistenceException {
        Map<String, String> extendedAttributes = new HashMap<>();
        extendedAttributes.put("name", "hello world");

        Domains.Member newMember = new Domains.Member()
                .setNo("200000")
                .setName("Smith")
                .setGender(1)
                .setExtendedAttributes(extendedAttributes)
                .setMobile("15011112222");

        Domains.Member.create(newMember);
    }

    public static void main(String args[]) throws SQLException, PersistenceException {
        File file = new File("persistence_example.db");

        if (file.exists())
            file.delete();

        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Domains.createTables(Database.getConnectionFactory().getConnection());

        createMember();
    }
}
