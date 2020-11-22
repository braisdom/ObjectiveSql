ObjectiveSQL is an ORM framework in Java base on ActiveRecord pattern, which encourages rapid development and clean, codes with the least, and convention over configuration.


### Features

- Dynamic code generation with [JSR 269](https://jcp.org/en/jsr/detail?id=269) for Java API of database access
- Full Java API of database access without coding
- Dynamically SQL programming with Java syntax,  and very close to SQL syntax

### Installation

#### IntelliJ IDEA plugin installation

Installation step: Preferences/Settings* -> *Plugins* -> *Search with "ObjectiveSql" in market* -> *Install*

#### Maven dependencies installation

```xml
<!-- In standalone -->
<dependency>
    <groupId>com.github.braisdom</groupId>
    <artifactId>objective-sql</artifactId>
    <version>1.3.8</version>
</dependency>
```

```xml
<!-- In Spring Boot -->
<dependency>
  <groupId>com.github.braisdom</groupId>
  <artifactId>springboot</artifactId>
  <version>1.0.0</version>
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

Member newMember = new Member()
        .setNo("100000")
        .setName("Pamela")
        .setGender(1)
        .setMobile("15011112222");
Member.create(newMember);

Member[] members = new Member[]{newMember1, newMember2, newMember3};
        Member.create(members);

Member.update(1L, newMember);

Member.destroy(1L);

Member.execute(String.format("DELETE FROM %s WHERE name = 'Mary'", Member.TABLE_NAME));

...
```

```java
Member.queryAll(Member.HAS_MANY_ORDERS);
Member.queryPrimary(1, Member.HAS_MANY_ORDERS);
Member.queryByName("demo", Member.HAS_MANY_ORDERS);
...
```

### Complex SQL programming

```java
Order.Table orderTable = Order.asTable();
Select select = new Select();

select.project(sum(orderTable.amount) / sum(orderTable.quantity) * 100)
    .from(orderTable)
    .groupBy(orderTable.productId);
```

```sql
SELECT SUM(order.amount) / SUM(order.quantity)  * 100
      FROM orders AS order GROUP BY order.product_id
```

```java
Member.Table member = Member.asTable();
Order.Table order = Order.asTable();

Select select = new Select();

select.from(order, member)
        .where(order.memberId.eq(member.id));
select.project(member.no,
        member.name,
        member.mobile,
        countDistinct(order.no).as("order_count"),
        sum(order.quantity).as("total_quantity"),
        sum(order.amount).as("total_amount"),
        min(order.salesAt).as("first_shopping"),
        max(order.salesAt).as("last_shopping"));
select.groupBy(member.no, member.name, member.mobile);
```

```sql
SELECT
	`T0`.`no` ,
	`T0`.`name` ,
	`T0`.`mobile` ,
	COUNT(DISTINCT `T1`.`no` ) AS `order_count`,
	SUM(`T1`.`quantity` ) AS `total_quantity`,
	SUM(`T1`.`amount` ) AS `total_amount`,
	MIN(`T1`.`sales_at` ) AS `first_shopping`,
	MAX(`T1`.`sales_at` ) AS `last_shopping`
FROM `orders` AS `T1`, `members` AS `T0`
WHERE (`T1`.`member_id` = `T0`.`id` )
GROUP BY `T0`.`no` , `T0`.`name` , `T0`.`mobile`
```

> 1) Java syntax very close to SQL syntax
>
> 2) SQL program will be changed to logical program, resuable and procedural

- [Count order by distinct member, and summary amount and quantity of order](https://github.com/braisdom/ObjectiveSql/blob/master/examples/springboot-sample/src/main/java/com/github/braisdom/objsql/sample/model/Member.java#L41)
- [Calculate LPLY(Same Period Last Year) and LP(Last Period) of products sales for a while](https://github.com/braisdom/ObjectiveSql/blob/master/examples/springboot-sample/src/main/java/com/github/braisdom/objsql/sample/model/Product.java#L45)

