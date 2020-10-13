ObjectiveSql’s mission is to provide  the most intuitive and convenient approach to access various databases, without configuration,  without coding relevant to `JDBC`&`ORM`. It is an abstraction and encapsulation of java in relation database domain logic.

ObjectiveSql-based programming is the best practice in `ActiveRecord` pattern in Java, which incorporates both behavior and data and avoids meaningless `Interface` declaration of Java. Thus, ObjectiveSql-based programming will change the way to build an application base on relation database especially in the WEB development.

### Features

- Defining a domain model, which carries the query and persistence capabilities by itself, no configuration, no no-implementational `Interface`
- Validating the Java Bean with Jakarta Bean Validation integrated to `ObjectiveSql`
- Database transactions simply add an `Annotation` to the method
- Add `Queryable` annotation to field, which can be queried by static method `queryByFieldName`

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

### Query methods

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

### Validation methods

```java
try {
    Map<String, String> requestObject = ...; // From the POST request of SpringBoot
    Member rawMember = Member.newInstanceFrom(requestObject, false);
    newMember.validate(); //Skip the validation
} catch(ValidationException ex) {
    ...
}
```

### Persistence methods

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

### Guides/[中文](http://www.objsql.com/)

If you are using Maven just add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>com.github.braisdom</groupId>
    <artifactId>objective-sql</artifactId>
    <version>1.3.4</version>
</dependency>
```

**Installing IntelliJ Plugin**:  *Preferences/Settings* -> *Plugins* -> *Search with "ObjectiveSql" in market* -> *Install*

- [Naming Conventions](https://github.com/braisdom/ObjectiveSql/wiki/Naming-Conventions)
- [Generated Methods](https://github.com/braisdom/ObjectiveSql/wiki/Generated-Methods)
- [DataSource Configuration](https://github.com/braisdom/ObjectiveSql/wiki/DataSource-Configuration)
- [Validations](https://github.com/braisdom/ObjectiveSql/wiki/Validations)
- [Transaction Principle](https://github.com/braisdom/ObjectiveSql/wiki/Transaction-Principle)
- [Data Types between database and Java](https://github.com/braisdom/ObjectiveSql/wiki/Data-Types-between-database-and-Java)
- [Extension Point](https://github.com/braisdom/ObjectiveSql/wiki/Extension-Point)
- Extensions
  - [Caching data into Redis](https://github.com/braisdom/ObjectiveSql/wiki/Caching-data-into-Redis)
  - [How to save a ProtoBuffer message](https://github.com/braisdom/ObjectiveSql/wiki/How-to-save-a-ProtoBuffer-message)
  - [How to integrate application Log framework to ObjectiveSql](https://github.com/braisdom/ObjectiveSql/wiki/Integrate-application-Log-framework-to-ObjectiveSql)
  - [Customizing ColumnTransitional](https://github.com/braisdom/ObjectiveSql/wiki/ColumnTransitional)



