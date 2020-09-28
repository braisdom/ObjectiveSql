The ObjectiveSql makes it easy to CRUD operations on databases(just define an Annotation). It is a best ActiveRecord pratice in Java, it includes everything about you need to build most SQL for business developing. At the same time, the SQL resused is possible in ObjectiveSql and the SQL abstracted in Java is more clear, more programable. 

### Features

- No coding to define a "DomainModel", which carries the query and persistence behavior by itself
- Supporting the code completion with IntelliJ IDEA
- The functions encapsulated for various database, who makes it easy to program between java and database
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
Member member = Member.queryByPrimaryKey(11);
```

```java
Member member = Member.queryFirst("id = ?", 11);
```

```java
List<Member> members = Member.query("id > ?", 8);
```

```java
List<Member> members = Member.queryAll();
```

```java
int count = Member.count("id > ?", 10);
```

...

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
