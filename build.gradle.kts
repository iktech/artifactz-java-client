val compatibility: String? by project
val ossUsername: String? by project
val ossPassword: String? by project
val projectGroup: Any by project
val tagName: String? = System.getenv("RELEASE_TAG")
group = projectGroup
version = tagName ?: "1.4-SNAPSHOT"

plugins {
    // Apply the java-library plugin for API and implementation separation.
    id("java-library")
    id("maven-publish")
    id("jacoco")
    id("signing")
}

java {
    withJavadocJar()
    withSourcesJar()
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "artifactz-client"

            from(components["java"])

            pom {
                name.set("artifactz-client")
                description.set("Artifactz.io Java Client Library")
                url.set("https://github.com/iktech/artifactz-java-client")
                    licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://raw.githubusercontent.com/iktech/artifactz-java-client/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("ikolomiyets-iktech")
                        name.set("Igor Kolomiyets")
                        email.set("igor.kolomiyets@iktech.io")
                    }
                }
                scm {
                    connection.set("scm:git:ssh://git@github.com:iktech/artifactz-java-client.git")
                    developerConnection.set("scm:git:ssh://git@github.com:iktech/artifactz-java-client.git")
                    url.set("https://github.com/iktech/artifactz-java-client")
                }
            }
        }
    }

    repositories {
        maven {
            credentials {
                username = System.getenv("MAVEN_USERNAME") ?: ossUsername ?: ""
                password = System.getenv("MAVEN_PASSWORD") ?: ossPassword ?: ""
            }

            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"

            url = uri(if (version.toString().endsWith("-SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_KEY")
    if (signingKey != null) {
        val signingPassword = System.getenv("GPG_PASSPHRASE")
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
    sign(publishing.publications["mavenJava"])
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("org.apache.httpcomponents.client5:httpclient5:5.5")
    api("org.apache.commons:commons-lang3:3.18.0")
    api("com.fasterxml.jackson.core:jackson-annotations:2.19.2")
    api("com.fasterxml.jackson.core:jackson-databind:2.19.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.4")
    testImplementation("com.jayway.jsonpath:json-path:2.9.0")
    testImplementation("org.slf4j:slf4j-api:2.0.17")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.4")
}

tasks.test {
    // Use junit platform for unit tests.
    useJUnitPlatform()
    var prop = project.properties["readWriteToken"]
    if (prop != null) {
        systemProperty("readWriteToken", prop)
    }

    prop = project.properties["readOnlyToken"]
    if (prop != null) {
        systemProperty("readOnlyToken", prop)
    }

    prop = project.properties["writeOnlyToken"]
    if (prop != null) {
        systemProperty("writeOnlyToken", prop)
    }

    val url = project.properties["serviceUrl"]
    if (url != null) {
        systemProperty("serviceUrl", url)
    }
}
