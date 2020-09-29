ObjectiveSql’s mission is to provide  the most obvious approach to access various databases, without configuration,  without coding about JDBC&ORM. ObjectiveSql-based programming is the best practice for ActiveRecord, which incorporates both behavior and data and avoids unmeaningful declaration of Java. Thus, ObjectiveSql-based programming will change the way to build an application base on relation database.

### Features

- Defining a domain model with code generating automatically, which carries the query and persistence behavior by itself, no configuration, no empty Interface
- Validating the Java Bean with Jakarta Bean Validation integrated to ObjectiveSql
- Database transaction into an Annotation tagged on a method only
- The relations tagged with Annotation, who will be applied in query as a static field generated automatically

### Defining domain models

```java
@DomainModel
public class Member {
    private String no;
    
    @Queryable
    private String name;
    private Integer gender;
    private String mobile;
    private String otherInfo;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<Order> orders;
}

@DomainModel
public class Order {
    private String no;
    private Integer memberId;
    private Double amount;
    private Double quantity;

    @Relation(relationType = RelationType.BELONGS_TO)
    private Member member;
}
```

### The query methods

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

```java
...
```

### The validation methods

```java
try {
    Map<String, String> requestObject = ...; // From the POST request of SpringBoot
    Member rawMember = Member.newInstanceFrom(requestObject, false);
    newMember.validate(); //Skip the validation
} catch(ValidationException ex) {
    ...
}
```

### The persistence methods

```java
Member newMember = new Member();
// To set the field value for "newMember"
newMember.save(false); //Skip the validation
// newMember.save(true); // Validating the field value before save
```

```java
Map<String, String> requestObject = ...; // From the POST request of SpringBoot
Member rawMember = Member.newInstanceFrom(requestObject, false);
newMember.save(false); //Skip the validation
// newMember.save(true); // Validating the field value before save
```

```java
Member.update(1, changedMemberInstance, false);
```

```java
Member.destory(1);
```

```java
Member.destory("id = ?", 1);
```

```java
...
```

### The relation query

```java
Member member = Member.queryPrimary(1, Member.HAS_MANY_ORDERS);
List<Order> orders = member.getOrders();
```

```java
Member member = Member.queryPrimary(1, Member.HAS_MANY_ORDERS);
List<Order> orders = member.getOrders();
```

```java
Member member = Member.queryByName("demo", Member.HAS_MANY_ORDERS);
List<Order> orders = member.getOrders();
```

### Transactional

```java
@DomainModel
public class Order {
    private String no;
    private Integer memberId;
    private Double amount;
    private Double quantity;

    @Relation(relationType = RelationType.BELONGS_TO)
    private Member member;

    @Transactional
    public static void makeOrder(Order order, OrderLine... orderLines) throws SQLException {
        Order.create(order, false);
        OrderLine.create(orderLines, false);
    }
}
```

