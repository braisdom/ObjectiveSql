ObjectiveSql 用极简的方式解决了Java 与不同类型数据库之间的交互，封装了常用的数据库访问能力，程序员几乎零编码，就可以满足应用系统对数据库访问的需求，主要特点如下：

- 在编译期，自动生成数据库访问的相关Java代码，程序员只需关注业务特性的开发
- 结合IntelliJ 插件提供Code Completion 特性，为开发提供帮助
- 封装了不同类型数据库（例如：Oracle, MySQL，MSSql等）函数，使SQL 能够好友的和Java 结合，同时也为不同数据库的函数使用方法提供参考
- 将SQL 中出现的各类表达式封装为Java 函数，不再拼接字符串，能够有效的避免SQL 的语法错误

#### 首先定义一个实体模型

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

#### 你可以这样使用…

```java
Member member1 = Member.queryByPrimaryKey(11);
Member member2 = Member.queryFirst("id = ?", 11);
List<Member> members1 = Member.query("id > ?", 8);
List<Member> members2 = Member.queryAll();
List<Member> members3 = Member.queryBySql("SELECT id, name FROM members WHERE id < ?", 10);
int count = Member.count("id > ?", 10);
```

#### 你也可以这样使用…

```java
Member newMember = new Member()
                .setId(100)
                .setNo("100000")
                .setName("Pamela")
                .setGender(1)
                .setMobile("15011112222");

 Member member = Member.create(newMember, true);
```

#### 更高级地使用…

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

上述代码中出现的方法和实例变量均是编译期动态生成([JSR269](https://www.jcp.org/en/jsr/detail?id=269)规范)，主要包括：

- DomainMode 的实例变量的Setter和Getter 方法
- Query 和Persistence 相关方法
- SQL 语句结构和表达式封装的相关类，例如：XXModel.Table，Column 等