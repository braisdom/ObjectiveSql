package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Databases;
import com.github.braisdom.funcsql.Validator;
import com.google.gson.GsonBuilder;
import org.junit.Assert;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class PersistenceExample {

    private static void createSimpleMember() throws SQLException {
        Domains.Member newMember = new Domains.Member()
                .setId(100)
                .setNo("100000")
                .setName("Pamela")
                .setGender(1)
                .setMobile("15011112222");

        Domains.Member member = Domains.Member.create(newMember, true);
        Assert.assertNotNull(member);
    }

    private static void validateMember() {
        Domains.Member newMember = new Domains.Member()
                .setId(2)
                .setNo("100")
                .setName("Pamela")
                .setGender(1)
                .setMobile("15011112222");

        Validator.Violation[] violations = newMember.validate();
        Assert.assertTrue(violations.length > 0);
    }

    private static void createSimpleMemberWithValidation() throws SQLException {
        Domains.Member newMember = new Domains.Member()
                .setId(3)
                .setNo("100000")
                .setName("Pamela")
                .setGender(1)
                .setMobile("15011112222");

        Domains.Member.create(newMember, true);
    }

    private static void createSimpleMemberCopyFromMap() throws SQLException {
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

        Domains.Member.create(Domains.Member.newInstanceFrom(attributes, false), false);
    }

    private static void createSimpleMemberCopyFromUnderlineMap() throws SQLException {
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

        Domains.Member member = Domains.Member.newInstanceFrom(attributes, true);
        Domains.Member.create(member, false);
    }

    private static void createSimpleFromJsonMember() throws SQLException {
        String json = "{\"id\":7,\"no\":\"200000\",\"name\":\"Smith\",\"gender\":1,\"mobile\":\"15011112222\"," +
                "\"extendedAttributes\":{\"hobbies\":[\"Play football\"],\"age\":28}}";
        Domains.Member newMember = new GsonBuilder().create().fromJson(json, Domains.Member.class);

        Domains.Member.create(newMember, false);
    }

    private static void createMember() throws SQLException {
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

        Domains.Member.create(newMember, false);
    }

    private static void createMemberArray() throws SQLException {
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

        Domains.Member newMember3 = new Domains.Member()
                .setNo("200004")
                .setName("Denise")
                .setGender(0)
                .setMobile("15011112222");

        Domains.Member.create(new Domains.Member[]{newMember1, newMember2, newMember3}, false);
    }

    private static void updateSmithMember() throws SQLException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football", "Cooking"});
        extendedAttributes.put("age", 28);

        Domains.Member newMember = new Domains.Member()
                .setName("Smith => Jackson")
                .setExtendedAttributes(extendedAttributes);

        Domains.Member.update(12, newMember, true);
    }

    private static void updateJacksonMember() throws SQLException {
        Domains.Member.update("name = 'Smith => Jackson => Davies'", "name = 'Smith => Jackson'");
    }

    private static void deleteAliceMember() throws SQLException {
        Domains.Member.destroy(13);
    }

    private static void deleteMaryMember() throws SQLException {
        Domains.Member.destroy("name = 'Mary'");
    }

    private static void executeDeleteDenise() throws SQLException {
        Domains.Member.execute(String.format("DELETE FROM %s WHERE name = 'Denise'", Domains.Member.TABLE_NAME));
    }

    private static void createOrder() throws SQLException {
        Domains.Order order = new Domains.Order()
                .setNo("202000001")
                .setMemberId(3)
                .setAmount(3.5f)
                .setQuantity(100.3f)
                .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00"));
        order.save(false);
    }

    public static void main(String args[]) throws SQLException {
        File file = new File("persistence_example.db");

        if (file.exists())
            file.delete();

        Databases.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Domains.createTables(Databases.getConnectionFactory().getConnection());

        createSimpleMember();
        createSimpleMemberCopyFromMap();
        createSimpleMemberCopyFromUnderlineMap();
        createSimpleFromJsonMember();
        createMember();
        createMemberArray();
        updateSmithMember();
        updateJacksonMember();
        deleteAliceMember();
        deleteMaryMember();
        executeDeleteDenise();
        createSimpleMemberWithValidation();
        validateMember();
        createOrder();
    }
}
