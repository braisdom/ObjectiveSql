ObjectiveSQL is an ORM framework in Java base on ActiveRecord pattern, which encourages rapid development and clean, codes with the least, and convention over configuration.


### Features

- Dynamic code generation with [JSR 269](https://jcp.org/en/jsr/detail?id=269) for Java API of database access
- Full Java API of database access without coding
- Dynamically SQL programming with Java syntax,  and very close to SQL syntax

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

> 1) Operator overload makes Java syntax very close to SQL syntax
>
> 2) Funcational SQL programming is reused and clear

- [Count order by distinct member, and summary amount and quantity of order](https://github.com/braisdom/ObjectiveSql/blob/master/examples/springboot-sample/src/main/java/com/github/braisdom/objsql/sample/model/Member.java#L41)
- [Calculate LPLY(Same Period Last Year) and LP(Last Period) of products sales for a while](https://github.com/braisdom/ObjectiveSql/blob/master/examples/springboot-sample/src/main/java/com/github/braisdom/objsql/sample/model/Product.java#L45)

