# To take a new SQL experience in Java
Although Java is a static language , but it has provided the ability to extend code dynamically since version 1.6([JSR269](https://www.jcp.org/en/jsr/detail?id=269)), makes the same as Ruby, Python, Javascript and other dynamic languages, except that it provides extension point at compiling.

SQL programming in Java has always been a difficult problem. Sometimes it is mixed in Java codess as a string, sometimes it exists in the template language, and when it is encapsulated by Fluent, it cannot be associated with Domain Model also.

Commonly, there are two ways of SQL programming in Java, CRUD behavior of Domain Model, and the complex querying for analysing.

The ObjectiveSql is a beta version now, but more features will be comed, and it will get better and better

## Beginning

```java
// Installing the connection factory for ObjectiveSql
Databases.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));

```



## Definition of DomainModel

```java
@DomainModel
public class Member {
  @Size(min = 5, max = 20)
  private String no;
  
  @Queryable
  private String name;
  
  @Queryable
  private Integer gender;
  private String mobile;
  
  @Relation(relationType = RelationType.HAS_MANY)
  private List<Order> orders;

  @Column(transition = JsonColumnTransitional.class)
  private Map extendedAttributes;

  @Transient
  private String otherInfo;
  
  // public static void doTheDomainLogicMethod()....
}
```

## You can...

```java
// Querying
Member member = Member.queryByPrimaryKey(10);
java.util.List<Member> members = Member.queryAll();
int count = Member.count("id > ?", 10);
java.util.List<Member> members = Member.queryByName("Jone");
java.util.List<Member> members = Member.queryByGender(1);

// Querying with relation
List<Member> members = Member.query("id > (?)",
                new Relationship[]{Member.HAS_MANY_ORDERS}, 1);

// Creating
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

## You can also...

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

More see: [example](https://github.com/braisdom/ObjectiveSql/tree/master/example/src/main/java/com/github/braisdom/objsql/example)

