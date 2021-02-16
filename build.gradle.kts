val compatibility: String by project

plugins {
    // Apply the java-library plugin for API and implementation separation.
    id("java-library")
    id("maven-publish")
    id("jacoco")
    id("org.owasp.dependencycheck") version "6.1.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

jacoco {
    toolVersion = "0.8.6"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.iktech"
            artifactId = "artifactz-client"
            version = "1.0"

            from(components["java"])
        }
    }
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
    mavenLocal()
}

dependencies {
    api("org.apache.httpcomponents:httpclient:4.5.10")
    api("commons-lang:commons-lang:2.6")
    api("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
    api("com.fasterxml.jackson.core:jackson-databind:2.12.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("com.jayway.jsonpath:json-path:2.4.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    // Use junit platform for unit tests.
    useJUnitPlatform()
}
