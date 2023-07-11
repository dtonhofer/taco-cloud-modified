package scopes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

// ---
// See https://www.baeldung.com/spring-bean-scopes
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/support/ClassPathXmlApplicationContext.html
// Note that we don't even have anything to create the "Person" bean, this is left to Spring.
// ---

class TestSpringScopeWithXmlConfig {

    // AbstractApplicationContext is a superclass of ClassPathXmlApplicationContext
    // but still supports close()

    private AbstractApplicationContext appContext;

    @BeforeEach
    void createSpringAppContext() {
        appContext = new ClassPathXmlApplicationContext("scopes.xml");
    }

    @Test
    void givenSingletonScope_whenSetName_thenEqualNames() {
        final String NAME = "John Smith";
        // the bean is named in scopes.xml or according to the method that creates it
        Person personA = (Person) appContext.getBean("personSingleton");
        Person personB = (Person) appContext.getBean("personSingleton");
        // object identity?
        assertSame(personA, personB);
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
        assertNotSame(personA, personB);
        // set the person's name and check it changed in both instances
        personA.setName(NAME);
        personB.setName(NAME_OTHER);
        assertEquals(NAME, personA.getName());
        assertEquals(NAME_OTHER, personB.getName());
    }

    @AfterEach
    void closeSpringApplicationContext() {
        if (appContext != null) {
            appContext.close();
        }
    }
}
