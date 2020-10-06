package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.Validator;
import com.github.braisdom.objsql.example.Domains.Member;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class PersistenceExample {

    private static void createSimpleMember() throws SQLException {
        Member newMember = new Member();
        newMember.setNo("100000")
                .setName("Pamela")
                .setGender(1)
                .setRegisteredAtWithJoda(DateTime.now())
                .setUpdatedAt(Timestamp.valueOf("2020-10-05 00:00:00"))
                .setMobile("15011112222");

        Member member = Member.create(newMember, true, true);
        Assert.assertNotNull(member);
    }

    private static void validateMember() {
        Member newMember = new Member()
                .setNo("100")
                .setName("Pamela")
                .setGender(1)
                .setMobile("15011112222");

        Validator.Violation[] violations = newMember.validate();
        Assert.assertTrue(violations.length > 0);
    }

    private static void createSimpleMemberWithValidation() throws SQLException {
        Member newMember = new Member()
                .setNo("100000")
                .setName("Pamela")
                .setGender(1)
                .setMobile("15011112222");
        Member.create(newMember, true, true);
    }

    private static void createSimpleMemberCopyFromMap() throws SQLException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football"});
        extendedAttributes.put("age", 28);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", 10L);
        attributes.put("no", "200000");
        attributes.put("name", "Carter");
        attributes.put("gender", 1);
        attributes.put("mobile", "15011112222");
        attributes.put("extendedAttributes", extendedAttributes);

        Member.create(Member.newInstanceFrom(attributes, false),
                false, true);
    }

    private static void createSimpleMemberCopyFromUnderlineMap() throws SQLException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football"});
        extendedAttributes.put("age", 28);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", 9L);
        attributes.put("no", "200000");
        attributes.put("name", "Barbara\t");
        attributes.put("gender", 1);
        attributes.put("mobile", "15011112222");
        attributes.put("extended_attributes", extendedAttributes);

        Member member = Member.newInstanceFrom(attributes, true);
        Member.create(member, false, true);
    }

    private static void createSimpleFromJsonMember() throws SQLException {
        String json = "{\"id\":7,\"no\":\"200000\",\"name\":\"Smith\",\"gender\":1,\"mobile\":\"15011112222\"," +
                "\"extendedAttributes\":{\"hobbies\":[\"Play football\"],\"age\":28}}";
        Member newMember = new GsonBuilder().create().fromJson(json, Member.class);

        Member.create(newMember, false, true);
    }

    private static void createMember() throws SQLException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football"});
        extendedAttributes.put("age", 28);

        Member newMember = new Member()
                .setNo("200000")
                .setName("Smith")
                .setGender(1)
                .setExtendedAttributes(extendedAttributes)
                .setMobile("15011112222");

        Member.create(newMember, false, true);
    }

    private static void createMemberArray() throws SQLException {
        Member newMember1 = new Member()
                .setNo("200001")
                .setName("Alice")
                .setGender(0)
                .setMobile("15011112222");

        Member newMember2 = new Member()
                .setNo("200003")
                .setName("Mary")
                .setGender(0)
                .setMobile("15011112222");

        Member newMember3 = new Member()
                .setNo("200004")
                .setName("Denise")
                .setGender(0)
                .setMobile("15011112222");

        Member.create(new Member[]{newMember1, newMember2, newMember3},
                false, true);
    }

    private static void updateSmithMember() throws SQLException {
        Map<String, Object> extendedAttributes = new HashMap<>();
        extendedAttributes.put("hobbies", new String[]{"Play football", "Cooking"});
        extendedAttributes.put("age", 28);

        Member newMember = new Member()
                .setName("Smith => Jackson")
                .setExtendedAttributes(extendedAttributes);

        Member.update(12L, newMember, true);
    }

    private static void updateJacksonMember() throws SQLException {
        Member.update("name = 'Smith => Jackson => Davies'", "name = 'Smith => Jackson'");
    }

    private static void deleteAliceMember() throws SQLException {
        Member.destroy(13L);
    }

    private static void deleteMaryMember() throws SQLException {
        Member.destroy("name = 'Mary'");
    }

    private static void executeDeleteDenise() throws SQLException {
        Member.execute(String.format("DELETE FROM %s WHERE name = 'Denise'", Member.TABLE_NAME));
    }

    private static void createOrder() throws SQLException {
        Domains.Order order = new Domains.Order()
                .setNo("202000001")
                .setMemberId(3L)
                .setAmount(3.5f)
                .setQuantity(100.3f)
                .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00"));
        order.save(false, true);
    }

    public static void run() throws SQLException {
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
