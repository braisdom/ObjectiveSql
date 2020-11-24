ObjectiveSQL is an ORM framework in Java based on ActiveRecord pattern, which encourages rapid development and clean, codes with the least, and convention over configuration.


### Features

- Dynamic code generation with [JSR 269](https://jcp.org/en/jsr/detail?id=269) for Java API of database access
- Full Java API of database access without coding
- Dynamically SQL programming with Java syntax,  and very similar to SQL syntax

### Installation

#### IntelliJ IDEA plugin installation

Installation step: `Preferences/Settings -> Plugins -> Search with "ObjectiveSql" in market -> Install`

#### Maven dependencies installation

```xml
<!-- In standalone -->
<dependency>
    <groupId>com.github.braisdom</groupId>
    <artifactId>objective-sql</artifactId>
    <version>1.3.9</version>
</dependency>
```

```xml
<!-- In Spring Boot -->
<dependency>
  <groupId>com.github.braisdom</groupId>
  <artifactId>objsql-springboot</artifactId>
  <version>1.0.1</version>
</dependency>
```

### Examples

ObjectiveSQL provides full example for various databases below, You can open it directly with IntelliJ IDEA as a standalone project. In fact, they are not just examples, but also unit tests of ObjectiveSQL in various databases.

[MySQL](https://github.com/braisdom/ObjectiveSql/tree/master/examples/mysql),  [Oracle](https://github.com/braisdom/ObjectiveSql/tree/master/examples/oracle),  [MS SQL Server](https://github.com/braisdom/ObjectiveSql/tree/master/examples/sqlserver),  [SQLite](https://github.com/braisdom/ObjectiveSql/tree/master/examples/sqlite),  [PostgreSQL](https://github.com/braisdom/ObjectiveSql/tree/master/examples/postgres),  [Spring Boot](https://github.com/braisdom/ObjectiveSql/tree/master/examples/springboot-sample)

### Simple SQL programming without coding

```java
// You only define a domain model and fill related properties
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
```

```java
// You will get behaviors of query, update and delete without coding.
Member.countAll();
Member.count("id > ?", 1);
Member.queryByPrimaryKey(1);
Member.queryFirst("id = ?", 1);
Member.query("id > ?", 1);
Member.queryAll();

Member[] members = new Member[]{newMember1, newMember2, newMember3};
Member.create(newMember1);
Member.create(members);

Member.update(1L, newMember);
Member.destroy(1L);
Member.execute(String.format("DELETE FROM %s WHERE name = 'Mary'", Member.TABLE_NAME));
...
```

```java
// Querying objects with convenient methods, and it will carry the related objects
Member.queryAll(Member.HAS_MANY_ORDERS);
Member.queryPrimary(1, Member.HAS_MANY_ORDERS);
Member.queryByName("demo", Member.HAS_MANY_ORDERS);
...
```

### Complex SQL programming

```java
// SQL programming with Java syntax without losing the features of SQL syntax
Order.Table orderTable = Order.asTable();
Select select = new Select();

select.project(sum(orderTable.amount) / sum(orderTable.quantity) * 100)
        .from(orderTable)
        .where(orderTable.quantity > 30 &&
            orderTable.salesAt.between($("2020-10-10 00:00:00"), $("2020-10-30 23:59:59")))
        .groupBy(orderTable.productId);
```

```sql
-- SQL syntax is the same as Java syntax
SELECT ((((SUM(`T0`.`amount` ) / SUM(`T0`.`quantity` ) )) * 100))
FROM `orders` AS `T0`
WHERE ((`T0`.`quantity` > 30) AND 
       `T0`.`sales_at` BETWEEN '2020-10-10 00:00:00' AND '2020-10-30 23:59:59')
GROUP BY `T0`.`product_id`
```

See more:

- [Count order by distinct member, and summary amount and quantity of order](https://github.com/braisdom/ObjectiveSql/blob/master/examples/springboot-sample/src/main/java/com/github/braisdom/objsql/sample/model/Member.java#L41)
- [Calculate LPLY(Same Period Last Year) and LP(Last Period) of products sales for a while](https://github.com/braisdom/ObjectiveSql/blob/master/examples/springboot-sample/src/main/java/com/github/braisdom/objsql/sample/model/Product.java#L45)

