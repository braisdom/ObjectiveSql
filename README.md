# To take a new SQL experience in Java
How is the ObjectiveSql, how does it stand out from the many ORM frameworks?  Although Java is a static language , it has provided the ability to extend code dynamically since version 1.6([JSR269](https://www.jcp.org/en/jsr/detail?id=269)), makes the same as Ruby, Python, Javascript and other dynamic languages, except that it provides extension point at compiling.

SQL programming in Java has always been a difficult problem. Sometimes it is mixed in Java codess as a string, sometimes it exists in the template language, and when it is encapsulated by Fluent, it cannot be associated with Domain Model also. 

Commonly, there are two ways of SQL programming in Java, CRUD of Domain Model, and the complex querying of analysis, the ObjectiveSql offers both of these ways  with zero code almost.

## Definition of DomainModel

```java
@DomainModel
public static class Member {
	// The validation constraints, it will be applied for validate method.
  // for example: Validator.Violation[] violations = member.validate();
  @Size(min = 5, max = 20)
  private String no;

  // The queryByName method will be generated, usage example:
  // List<Member> members = Member.queryByName("Benjamin");
  @Queryable
  private String name;

  @Queryable
  private Integer gender;
  private String mobile;

  // The HAS_MANY_ORDERS field will be generated,
  // public static final Relationship HAS_MANY_ORDERS = ...
  @Relation(relationType = RelationType.HAS_MANY)
  private List<Order> orders;

  @Column(transition = JsonColumnTransitional.class)
  private Map extendedAttributes;

  // The field will not be save into database;
  @Transient
  private String otherInfo;
}
```

