### ActiveRecord in Java



#### Dynamics in Java

Java has long been known as a static language,  as you probably know, a static language means that the structure also cannot be changed in runtime except data type definition at compiling, such as, you cannot add a method or field after compiling generally, although you can modify the bytecode with ASM,  but it is helpless when programmer coding, because IDE only suports code completion base on Java code existed， and bytecode modification cannot respond effectively to change from different application. Therefore bytecode modification only can be used in dynamic proxy or to modify original features.

In java, generics only applies to instance of Class, static method is the basic behavior of domain object, it is a representation of the real domain and without intermediate or persistent state,  just a apply of object-oriented design. Thus, the sub class cannot reuse features of parent class, leads to code repeatly in anywhere.

#### About ActiveRecord

ActiveRecord is a domain logic pattern defined by Martin Fowler, it describes a practical application in actual domain, only makes the Java class more object-oriented, not only includes status of domain but also involves logics, makes codes can describe accurately complex bussiness logic and rule.

Due to the static nature of the Java language, ActiveRecord cannot be applied widely in Java, it is only widely used in Rails, Python, and so on. Dynamic proxy is used in Java generally which can describe bussiness login by application,  but also lead to no-implementational Interface and unnecessary abstraction.

#### Solution

ObjectiveSql base on JSR269 which is the best practice of ActiveRecord in Java, it provides ability to code completion with plugin of IntelliJ IDEA, and abstracts behaviours of database access, for example:

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

Only  a annotation definition, you obtain ability to access database with SQL, you can...

```java
Member member = Member.queryByPrimaryKey(11);
```

```java
List<Member> members = Member.queryAll();
```

For more, visit: https://github.com/braisdom/ObjectiveSql