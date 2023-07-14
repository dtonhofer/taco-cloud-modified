# Validation

## JetBrains Intellij IDEA annotations

The IntelliJ IDEA has its own annotations in the package `org.jetbrains.annotations`,
e.g. `org.jetbrains.annotations.NotNull` 

See these pages of IntelliJ IDEA documentation_

- [Annotations](https://www.jetbrains.com/help/idea/annotating-source-code.html) 
- [Nullable/NotNull configuration dialog](https://www.jetbrains.com/help/idea/nullable-notnull-configuration.html)

These annotations are open source and are hosted at GitHub:

- [java-annotations](https://github.com/JetBrains/java-annotations)

Note that the [annotation retention policy](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/annotation/RetentionPolicy.html) 
is `class`, not `runtime`, so the annotation may not be present at runtime:  

- [org.jetbrains.annotations.NotNull](https://github.com/JetBrains/java-annotations/blob/master/common/src/main/java/org/jetbrains/annotations/NotNull.java)

According to [Configure Nullability Annotations](https://www.jetbrains.com/help/idea/annotating-source-code.html#configure-nullability-annotations)
IntelliJ IDEA injects code during build to check constraints from annotations at 
runtime, but I have been unable to elicit such behaviour - just getting warnings at 
compile time. We read:

> When you compile your project, the IDE adds assertions to all 
> code elements annotated with @NotNull. These assertions will throw
> an error if the elements happen to be null at runtime. This
> fail-fast behavior may help you to diagnose the problems at an early stage.
> If this is not the desired effect for you, you can disable these assertions.

For this to actually work you need to configure a proper annotation processor.

This is part of a more general ensemble of compile-time check libraries, which 
are called _Annotation Frameworks_:

- https://errorprone.info/
- https://checkerframework.org/
   - https://checkerframework.org/manual/


## Jakarta Bean Validation

"Jakarta Bean Validation" is the set of annotations and support classes the one encounters in the Spring framework.

The current specification is [Jakarta Bean Validation 3.0, October 2020](https://beanvalidation.org/3.0/)
 
Bean validation used to be located in package `javax.validation`, but Oracle has moved the Java EE elements to the 
Jakarta project so in 3.0, the package has changed. One should now migrate.

A note by the jOOQ maintainer:

[jOOQ 3.16 and Java EE vs Jakarta EE](https://blog.jooq.org/jooq-3-16-and-java-ee-vs-jakarta-ee/)

> So, we bit the bullet and moved on. Starting from jOOQ 3.16, Java EE has gone
> and Jakarta EE is our dependency, where needed.

More stuff under the [_validation_ tag at the jOOQ blog](https://blog.jooq.org/tag/validation/).

### About

The annotations are properly called _Jakarta Bean Validation constraints_:
[Annotation Type Constraint](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/validation/constraint)
from the Jakarta EE 9 specification.

Package `jakarta.validation`  is not an implementation rather than the API declaration & 
specification. The implementation is in fact _Hibernate Validator_.

- [Bean Validation Homepage](https://beanvalidation.org/)
- [Bean Validation 3.0 API docs](https://jakarta.ee/specifications/bean-validation/3.0/apidocs/)
   - [List of constraints in `jakarta.validation.constraints`](https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary.html)
   - [Package summary, Bean Validation 3.0](https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary.html)
   - [Package summary, Jakarta EE 9 - same](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/validation/constraints/package-summary.html)
- [Bean Validation 3.0 specification](https://jakarta.ee/specifications/bean-validation/3.0/jakarta-bean-validation-spec-3.0.html) 
- [Additional resources](https://beanvalidation.org/resources/)
- Specifications
   - Current: [Jakarta Bean Validation 3.0, October 2020](https://beanvalidation.org/3.0/)  
   - Previous: [JSR-380: Bean Validation 2.0, August 2017](https://beanvalidation.org/2.0-jsr380/)
   - Obsolete: [JSR-303: Bean Validation 1,0, October 2009](https://beanvalidation.org/1.0/spec/)

#### More

A short intro at Baeldung: [Java Bean Validation Basics](https://www.baeldung.com/javax-validation)

### Implementation

The reference implementation is _JBoss Hibernate Validator_ (currently _8.0.1.Final_), available [here](https://hibernate.org/validator/).

This is all in the package `org.hibernate.validator` but direct reference to
package is generally not needed. 

- [Manual for Hibernate Validator](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/) - This manual is a "must read"
   - [Example validation](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#_validating_constraints)
   - [List of implemented constraints](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-builtin-constraints)
- [API docs](https://docs.jboss.org/hibernate/stable/validator/api/)
   - [List of constraints in `jakarta.validation.constraints` (Jakarta EE)](https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary.html) 
   - [Additional Hibernate Validator specific constraints in `org.hibernate.validator.constraints`](https://docs.jboss.org/hibernate/stable/validator/api/org/hibernate/validator/constraints/package-summary.html)
   - [Additional Hibernate Validator specific constraints in `org.hibernate.validator.constraints.time`](https://docs.jboss.org/hibernate/stable/validator/api/org/hibernate/validator/constraints/time/package-summary.html)
- [Getting started](https://hibernate.org/validator/documentation/getting-started/)
    
We read:

> Hibernate Validator is the reference implementation of Jakarta Bean Validation.
> The implementation itself as well as the Jakarta Bean Validation API and TCK
> (test conformance kit) are all provided and distributed under the Apache Software
> License 2.0.
> 
> Hibernate Validator 8 and Jakarta Bean Validation 3.0 require Java 11 or later.

### Adding it to the project

Inside an EE environment, the application server will provide the `javax.validation` package
and the implementation for it.

In Java SE environment, you would add a dependency on `hibernate-validator`. This should
pull in _Jakarta Bean Validation API_ automatically.

The _Hibernate Validator_ documentation says 
[here](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#_validating_constraints):

> Note that only classes from the package `jakarta.validation` are used. These are provided
> from the _Bean Validation API_. No classes from _Hibernate Validator_ are directly referenced, 
> resulting in portable code.

Also [here](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-gettingstarted-uel):

> _Hibernate Validator_ requires an implementation of _Jakarta Expression Language_ for evaluating
> dynamic expressions in constraint violation messages 
> (see Section 4.1, [“Default message interpolation”](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-message-interpolation)).

This is for the occasion where you want to construct messages at runtime explaining a violation
and need to access members of an object.

#### Using it in "Spring Boot" 

For _Spring Boot_, the dependency is _Spring Boot Starter Validation_, written `spring-boot-starter-validation`.

- [Maven repository](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation)
- [A howto at 'reflectoring.io'](https://reflectoring.io/bean-validation-with-spring-boot/)
- [The chapter on validation in the Spring Boot manual](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#io.validation)

### Custom constraints/validators

Writing custom constrains/validators is quite useful as writing a specific constraint and using it wherever needed
seems much better than using a generic constraint with the same arguments that appear
in several places (as in `Range(min=100, max=200)` showing up everywhere). 

On the other hand you can write things like this too:

~~~
    final int alpha = 10;
    final int beta = 20;
    public void foo(@Range(min=alpha,max=beta) int x) {
        
    }
~~~

#### Example

I tried this, it seems to work:

[Example code to check a credit card's expiry date](https://github.com/dtonhofer/taco-cloud-modified/tree/master/src/main/java/tacos/validation)

#### Pointers for building custom validators

   - Hibernate: [Creating custom constraints](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-customconstraints)
   - Baeldung: [Spring MVC Custom Validation](https://www.baeldung.com/spring-mvc-custom-validator)
   - Payara blog: [Getting Started with Jakarta EE 9: Jakarta Validation](https://blog.payara.fish/getting-started-with-jakarta-ee-9-jakarta-validation)
   - Stack Overflow: [Building Dynamic ConstraintViolation Error Messages](https://stackoverflow.com/questions/23702975/building-dynamic-constraintviolation-error-messages)
