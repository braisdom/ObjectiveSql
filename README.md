The ObjectiveSql makes it easy to CRUD operations on databases(needs to define an  annotation only) . It is a best ActiveRecord pratice in Java, make the programmer will discard the configuration(XML, JSOM, etc) and ORM. The SQL resused is possible in ObjectiveSql and the SQL abstracted in Java is more clear, more programable. 

### Features

- Generating the code about CRUD operations on databases at compiling
- Supporting the code completion with IntelliJ IDEA
- The functions encapsulated for various database, make it easy to program between java and database
- Making the expressions in SQL become Java expressions, easier to program and reuse

### Define a DomainModel

```java
@DomainModel
public class Member {
    private String no;
    private String name;
    private Integer gender;
    private String mobile;
    private String otherInfo;
}
```

### The query methods below

```java
Member member1 = Member.queryByPrimaryKey(11);
Member member2 = Member.queryFirst("id = ?", 11);
List<Member> members1 = Member.query("id > ?", 8);
List<Member> members2 = Member.queryAll();
List<Member> members3 = Member.queryBySql("SELECT id, name FROM members WHERE id < ?", 10);
int count = Member.count("id > ?", 10);
```

### The persistence methods below

```java
Member newMember = new Member()
                .setId(100)
                .setNo("100000")
                .setName("Pamela")
                .setGender(1)
                .setMobile("15011112222");

 Member member = Member.create(newMember, true);
```

### The usage for abstracted SQL expression

```java
import static com.github.braisdom.objsql.sql.expression.Expressions.$;
import static com.github.braisdom.objsql.sql.expression.Expressions.and;

@DomainModel
public class Member {
     private String no;
     private String name;
     private Integer gender;
     private String mobile;
     @Relation(relationType = RelationType.HAS_MANY)
     private List<Order> orders;
}

@DomainModel
public class Order {
    private String no;
    private Integer memberId;
    private Float amount;
    private Float quantity;
}

Member.Table member = Member.asTable();
Order.Table order = Order.asTable();

Select select = new Select(member);

Expression memberNameFilter = member.name.eq($("Jack"));
Expression memberGenderFilter = member.gender.eq($(0));

select.project(member.id, member.name)
        .leftOuterJoin(order, order.memberId.eq(member.id))
        .where(and(memberNameFilter, memberGenderFilter));

List<Member> members = select.execute();
```
