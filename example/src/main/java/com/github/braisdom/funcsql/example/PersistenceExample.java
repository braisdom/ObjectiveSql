package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.PersistenceException;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PersistenceExample {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void createSimpleMember() throws SQLException, PersistenceException {
        Domains.Member newMember = new Domains.Member()
                .setId(11)
                .setNo("200000")
                .setName("Pamela")
                .setGender(1)
                .setMobile("15011112222");

        Domains.Member.create(newMember);
    }

    private static void createSimpleCopyFromMember() throws SQLException, PersistenceException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football"});
        extendedAttributes.put("age", 28);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", 10);
        attributes.put("no", "200000");
        attributes.put("name", "Carter");
        attributes.put("gender", 1);
        attributes.put("mobile", "15011112222");
        attributes.put("extendedAttributes", extendedAttributes);

        Domains.Member.create(Domains.Member.copyFrom(attributes));
    }

    private static void createSimpleCopyFromUnderlineMember() throws SQLException, PersistenceException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football"});
        extendedAttributes.put("age", 28);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", 9);
        attributes.put("no", "200000");
        attributes.put("name", "Barbara\t");
        attributes.put("gender", 1);
        attributes.put("mobile", "15011112222");
        attributes.put("extended_attributes", extendedAttributes);

        Domains.Member.create(Domains.Member.copyFrom(attributes));
    }

    private static void createFromJsonMember() throws SQLException, PersistenceException {
        String json = "{\"id\":7,\"no\":\"200000\",\"name\":\"Smith\",\"gender\":1,\"mobile\":\"15011112222\"," +
                "\"extendedAttributes\":{\"hobbies\":[\"Play football\"],\"age\":28}}";
        Domains.Member newMember = new GsonBuilder().create().fromJson(json, Domains.Member.class);

        Domains.Member.create(newMember);
    }

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

        System.out.println(new GsonBuilder().create().toJson(newMember));

        Domains.Member.create(newMember);
    }

    private static void createMemberArray() throws SQLException, PersistenceException {
        Domains.Member newMember1 = new Domains.Member()
                .setId(13)
                .setNo("200001")
                .setName("Alice")
                .setGender(0)
                .setMobile("15011112222");

        Domains.Member newMember2 = new Domains.Member()
                .setNo("200003")
                .setName("Mary")
                .setGender(0)
                .setMobile("15011112222");

        Domains.Member newMember3 = new Domains.Member()
                .setNo("200004")
                .setName("Denise")
                .setGender(0)
                .setMobile("15011112222");

        Domains.Member.create(new Domains.Member[]{newMember1, newMember2, newMember3});
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

    private static void deleteAliceMember() throws SQLException, PersistenceException {
        Domains.Member.destroy(13);
        print("Delete Alice successfully");
    }

    private static void deleteMaryMember() throws SQLException, PersistenceException {
        Domains.Member.destroy("name = 'Mary'");
        print("Delete Mary successfully");
    }

    private static void executeDeleteDenise() throws SQLException, PersistenceException {
        Domains.Member.execute(String.format("DELETE FROM %s WHERE name = 'Denise'", Domains.Member.TABLE_NAME));
        print("Delete Mary successfully");
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

        createSimpleMember();
        createSimpleCopyFromMember();
        createSimpleCopyFromUnderlineMember();
        createFromJsonMember();
        createMember();
        createMemberArray();
        updateSmithMember();
        updateJacksonMember();
        deleteAliceMember();
        deleteMaryMember();
        executeDeleteDenise();
    }
}
