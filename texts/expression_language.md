# (Unified) Expression Language

The _Expression Language_ aka. _Jakarta Expression Language_
aka. _Unified Expression Language_ aka. _EL_ 
is used to access bean/object members and call functions from within
scripts and templates. 

That's the _session model object access_ expressions you see in 
Thymeleaf templates, attached to Thymeleaf attributes.

At the [Wikipedia entry for \'Jakarta Expression Language\'](https://en.wikipedia.org/wiki/Jakarta_Expression_Language) we read:

> The Jakarta Expression Language (EL; formerly Expression Language and 
> Unified Expression Language) is a special purpose programming language 
> mostly used in Jakarta EE web applications for embedding and evaluating
> expressions in web pages. The specification writers and expert groups 
> of the Java EE web-tier technologies have worked on a unified expression
> language which was first included in the JSP 2.1 specification (JSR-245),
> and later specified by itself in JSR-341, part of Java EE 7.

Nowadays, it is just called _(Jakarta) Expression Language_ as the _Unified_ refers to
the effort of integrating _JSP EL_ and _JSF EL_ into _JSP 2.1 EL_ (May 2006).

## Package

Classes reside in the package `jakarta.el`.

## Specification

The specifications of several versions can be found [here](https://jakarta.ee/specifications/expression-language/).

In particular, the specification and JavaDoc for:

- [Jakarta Expression Language 5.0](https://jakarta.ee/specifications/expression-language/5.0/jakarta-expression-language-spec-5.0.html)
- [Module jakarta.el JavaDoc](https://jakarta.ee/specifications/expression-language/5.0/apidocs/jakarta.el/module-summary.html)

and

- [Jakarta Expression Language 4.0](https://jakarta.ee/specifications/expression-language/4.0/jakarta-expression-language-spec-4.0.html)
- [Package jakarta.el JavaDoc](https://jakarta.ee/specifications/expression-language/4.0/apidocs/)

The source for the Java API and the documentation (but not the implementation underlying the
API) is at [GitHub](https://github.com/jakartaee/expression-language).

## Releases

- [Jakarta Expression Language™ 5.0.0](https://projects.eclipse.org/projects/ee4j.el/releases/5.0.0)
  of May 2022 is part of
  [Jakarta 10](https://projects.eclipse.org/releases/jakarta-10) (the descendant of Java Enterprise Edition).
- The latest version is
  [Jakarta Expression Language™ 5.0.1](https://projects.eclipse.org/projects/ee4j.el/releases/5.0.1)
  of June 2022.
- The next version will be
  [Jakarta Expression Language™ 6.0.0](https://projects.eclipse.org/projects/ee4j.el/releases/6.0.0)
  foreseen for March 2024.

## Implementations

The reference implementation is [Eclipse expressly](https://github.com/eclipse-ee4j/expressly)

## Connection to Hibernate Validator

Hibernate Validator uses it in the "message text" constructions. 

At [this page](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-gettingstarted-uel)
we read:

> Hibernate Validator requires an implementation of Jakarta Expression Language for 
> evaluating dynamic expressions in constraint violation messages (see
> [Section 4.1](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#chapter-message-interpolation), 
> “Default message interpolation”). When your application runs in a Jakarta EE
> container such as WildFly/JBoss EAP, an EL implementation is already provided by
> the container. In a Java SE environment, however, you have to add an implementation 
> as dependency to your POM file. For instance, you can add the following dependency to
> use the Jakarta EL reference implementation: (expressly is mentioned) ...

