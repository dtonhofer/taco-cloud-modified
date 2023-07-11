package scopes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

// ---
// See https://www.baeldung.com/spring-bean-scopes
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/AnnotationConfigApplicationContext.html
// ---

// To create the "Person" bean, the name of the generating method is used to name the bean.
// One could also give an argument to the @Bean annotation.
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Bean.html

class TestSpringScopeWithAnnotations {

    // AbstractApplicationContext is a superclass of AnnotationConfigApplicationContext
    // but still supports close()

    private AbstractApplicationContext appContext;

    @Bean
    @Scope("singleton")
    Person personSingleton() {
        return new Person();
    }

    @Bean
    @Scope("prototype")
    Person personPrototype() {
        return new Person();
    }

    @BeforeEach
    void createSpringAppContext() {
        // this does a scan & refresh
        appContext = new AnnotationConfigApplicationContext(TestSpringScopeWithAnnotations.class);
    }

    @Test
    void givenSingletonScope_whenSetName_thenEqualNames() {
        final String NAME = "John Smith";
        Person personA = (Person) appContext.getBean("personSingleton");
        Person personB = (Person) appContext.getBean("personSingleton");
        // object identity?
        assertSame(personA,personB);
        // set the person's name and check it changed in both instances
        personA.setName(NAME);
        assertEquals(NAME, personB.getName());
    }

    @Test
    void givenPrototypeScope_whenSetName_thenEqualNames() {
        final String NAME = "John Smith";
        final String NAME_OTHER = "Anna Jones";
        // the bean is named in scopes.xml or according to the method that creates it
        Person personA = (Person) appContext.getBean("personPrototype");
        Person personB = (Person) appContext.getBean("personPrototype");
        // object identity?
        assertNotSame(personA,personB);
        // set the person's name and check it changed in both instances
        personA.setName(NAME);
        personB.setName(NAME_OTHER);
        assertEquals(NAME, personA.getName());
        assertEquals(NAME_OTHER, personB.getName());
    }

    @AfterEach
    void closeSpringAppContext() {
        if (appContext != null) {
            appContext.close();
        }
    }
}
