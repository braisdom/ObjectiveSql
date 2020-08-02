package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.PersistenceException;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PersistenceExample {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void createMember() throws SQLException, PersistenceException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football"});
        extendedAttributes.put("age", 28);

        Domains.Member newMember = new Domains.Member()
                .setId(12)
                .setNo("200000")
                .setName("Smith")
                .setGender(1)
                .setExtendedAttributes(extendedAttributes)
                .setMobile("15011112222");

        Domains.Member.create(newMember);
        print("Create member 'Smith' successfully.");
    }

    private static void createMemberArray() throws SQLException, PersistenceException {
        Domains.Member newMember1 = new Domains.Member()
                .setNo("200001")
                .setName("Alice")
                .setGender(0)
                .setMobile("15011112222");

        Domains.Member newMember2 = new Domains.Member()
                .setNo("200003")
                .setName("Mary")
                .setGender(0)
                .setMobile("15011112222");

        Domains.Member.create(new Domains.Member[]{newMember1, newMember2});
        print("Create member['Alice', 'Mary'] successfully.");
    }

    private static void updateSmithMember() throws SQLException, PersistenceException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football", "Cooking"});
        extendedAttributes.put("age", 28);

        Domains.Member newMember = new Domains.Member()
                .setName("Smith => Jackson")
                .setExtendedAttributes(extendedAttributes);

        Domains.Member.update(12, newMember);
        print("Update member from 'Smith' to 'Smith => Jackson' successfully.");
    }

    private static void updateJacksonMember() throws SQLException, PersistenceException {
        Domains.Member.update("name = 'Smith => Jackson => Davies'", "name = 'Smith => Jackson'");
        print("Update member from 'Smith => Jackson' to 'Smith => Jackson => Davies' successfully.");
    }

    private static void print(String message, Object... params) {
        Date date = new Date(System.currentTimeMillis());
        System.out.println(String.format("[%s] %s", dateFormat.format(date), String.format(message, params)));
    }

    public static void main(String args[]) throws SQLException, PersistenceException {
        File file = new File("persistence_example.db");

        if (file.exists())
            file.delete();

        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Domains.createTables(Database.getConnectionFactory().getConnection());

        createMember();
        createMemberArray();
        updateSmithMember();
        updateJacksonMember();
    }
}
