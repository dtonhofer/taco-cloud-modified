plugins {
    java
    application
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "sia"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
val lombok_version = "1.18.26"

repositories {
    mavenCentral()
}

// from https://github.com/spring-projects/spring-boot/issues/16251
/*
val developmentOnly2: Configuration = configurations.create("developmentOnly2")
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly2)
    }
}
*/

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    testImplementation("junit:junit:4.13.1")
    // https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator
    // implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

    compileOnly("org.projectlombok:lombok:" + lombok_version)
    annotationProcessor("org.projectlombok:lombok:"  + lombok_version)

    testCompileOnly("org.projectlombok:lombok:" + lombok_version)
    testAnnotationProcessor("org.projectlombok:lombok:"  + lombok_version)

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<JavaCompile> {
    doFirst {
        println("AnnotationProcessorPath for '$name' is ${options.annotationProcessorPath?.joinToString(prefix = "\n", separator = "\n", transform = { it -> it.toString() })}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes ["Main-Class"] = "tacos.TacoCloudApplication"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}
