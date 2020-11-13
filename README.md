ObjectiveSQL is an ORM framework in Java base on ActiveRecord pattern, which encourages rapid development and clean, codes with the least, and convention over configuration.


### 1 Features

- Dynamic code generation with JSR 269 for Java API of database access
- Full Java API of database access without coding
- Object-oriented SQL programming for complex SQL in Java

### 2 Solutions for simple SQL programming
#### 2.1 Defining domain models only

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
```

#### 2.2 Query&Update methods 

```java
Member.countAll();
Member.count("id > ?", 10);
Member.queryByPrimaryKey(11);
Member.queryFirst("id = ?", 11);
Member.query("id > ?", 8);
Member.queryAll();
...
```

#### 2.3 The relation query

```java
Member.queryAll(Member.HAS_MANY_ORDERS);
Member.queryPrimary(1, Member.HAS_MANY_ORDERS);
Member.queryByName("demo", Member.HAS_MANY_ORDERS);
...
```

### 3 Solutions for complex SQL programming

```java
Order.Table orderTable = Order.asTable();
Select select = new Select();

select.project(sum(orderTable.amount) / sum(orderTable.quantity) * $(100) )
    .from(orderTable)
    .groupBy(orderTable.productId);
```
Generated SQL below：
```sql
SELECT SUM(order.amount) / SUM(order.quantity)  * 100
      FROM orders AS order GROUP BY order.produc_id
```

### 4 Guides/[中文](http://www.objsql.com/)

If you are using Maven just add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>com.github.braisdom</groupId>
    <artifactId>objective-sql</artifactId>
    <version>1.3.7</version>
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



