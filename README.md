

# ObjectiveSql

ORM 是一种将数据库中的关系和各类外部访问方式，以面向对象的方式(OO)的方式进行抽象、封装，为开发者提供统一的编程接口，便于开发者在应用系统中对数据库的访问，包括数据的查询、分析和存储。

ObjectiveSql 并不是一种全新的ORM 框架，它借鉴了很多目前优秀框架的设计经验，同时，也针对目前的ORM框架的不足，提供了较为优雅的方案，解决了实际使用的过程中的很多重复工作，同时以OO 的形式提供SQL 的构造，增加程序的可理解性、可测试性。

Java 是一门静态编程语言，也就意味着，代码编译后，无法对其结构进行变更，即使通过[ASM](https://asm.ow2.io/) 等工具修改字节码，也无法在编码、编译阶段提供有效的支撑。正是因为Java 是静态语言，导致现有的ORM 框架均通过动态代理的方式实现，定义一个Interface，通过Factory 的方式获取接口的动态实现，就可以完成数据库的各类操作，看似是一个优雅的设计，但我们会发现每张表都会出现一个没有任何方法的Interface(因为大都数表没有太多业务性强的数据库访问，即使有也不多)，同是我们又多了很多每表模型定义，有可能还会出现一个Repository，本来很简单的一个特性，被人为的复杂化。业务特性还未出现时，基础的技术框架就已经变得复杂，最终结合复杂的业务特性，整个应用系统就会变得不可理解。

SQL 一直是Java 开发人员比较反感的事物，因为它和传统的逻辑编程语言差别太大，最关键的是它是以字符串的形式混杂在Java 程序中(存在于配置文件更令人反感)，普通的Java 程序员使用着不同的方式拼接字符串，无法重用，难以单元测试，当SQL 过程复杂时，整个.java 文件一片绿色，稍微的小改动都令人崩溃，不知道会不会出现SQL 语法错误。

目前，有一些ORM 框架提供了以Java 的方式构造SQL，解决了一部分重复构造的工作，但依然无法大量数据信息以字符串形式存在于Java 中。

上述问题是我多年Java 经验的感受，不知道其它Java 同行有没有。下面通过一个简单的示例代码展示ObjectiveSql 的部分特性

## 来一个惊奇的体验

首先，安装ObjectiveSql 插件, 安装方法和插件特性请参考： [ObjectiveSql-IntelliJ-Plugin](https://github.com/braisdom/ObjectiveSql-IntelliJ-Plugin)，不安装插件，程序可以正常编译，但无法体会ObjectiveSql 的魅力。

在maven.xml 中增加

```xml
<!-- Add dependency to application -->
<dependency>
  <groupId>com.github.braisdom</groupId>
  <artifactId>objective-sql</artifactId>
  <version>1.2</version>
</dependency>
```

应用程序启动时为ObjectiveSql 注入 ConnectionFactory


```java
// Installing the connection factory for ObjectiveSql
Databases.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));

```

## 定义较为简单的一个会员模型

```java
@DomainModel
public class Member {
  private String no;
  private String name;
  private Integer gender;
  private String mobile;
  
  // public static void doTheDomainLogicMethod()....
}
```

它不是一个普通的JavaBean，而是一个业务的载体，它将会封装所有与Member 相关的业务逻辑。

## 你可以...

```java
// Querying
Member member = Member.queryByPrimaryKey(10);
List<Member> members = Member.queryAll();
int count = Member.count("id > ?", 10);

// Querying with relation
List<Member> members = Member.query("id > (?)",
                new Relationship[]{Member.HAS_MANY_ORDERS}, 1);

// The attrs is a instance of java.util.Map
Member newMember = Member.newInstanceFrom(attrs);
Member member = Member.create(newMember, true);

Member[] newMembers = new Member[]{newMember1, newMember2};
Member member = Member.create(newMembers, true); 

// Updating
Member.update(12, newMember, true);
Member.update("name = 'Jackson'", "name = 'Smith'");

// Deleting
Member.destroy(13);
Member.destroy("name = 'Mary'");

```

上述示例代码中的各类方法都是由ObjectiveSql 在编译期间动态生成的，例如：queryByPrimaryKey, queryAll, query, count 等。

## 你也可以...

```java
Member member = new Member();

member.setNo("00001")
  	.setName("Jone")
  	.setMobile("18900000000")
  	.save(false);
```

上述代码是通过实例的方法进行数据库操作。

## 你还可以...

```java
import static com.github.braisdom.objsql.sql.expression.Expressions.$;
import static com.github.braisdom.objsql.sql.expression.Expressions.and;
import static com.github.braisdom.objsql.sql.function.IsoFunctions.count;

Member.Table member = Member.asTable();

Select select = new Select(member);
select
  .project(member.id, member.name)
  .where(and(member.name.eq($("Jack")), member.gender.eq($(0))));

List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

```

上述代码中的Member.Table 字类，是由ObjectiveSql 根据Model 在编译期动态生成，访类中的属性是一个Column 的实例，可以参与后续的关系数据逻辑处理。

更多示例请参见: [example](https://github.com/braisdom/ObjectiveSql/tree/master/example/src/main/java/com/github/braisdom/objsql/example)

