plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.0.1"
}

group="eu.fogas"
version="0.0.1"

publishing {
    publications {
        create<MavenPublication>("reflectionUtil") {
            from(components["java"])
        }
    }

/*    repositories {
        maven {
            name = "myRepo"
            url = uri("file://${buildDir}/repo")
        }
    }
 */
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    val slf4jVersion = "2.0.12"
    val log4j2Version = "2.23.1"
    val junitVersion = "5.10.2"

    // logging
    api("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4j2Version")
    api("org.apache.logging.log4j:log4j-api:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-core:$log4j2Version")

    // testing
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
    }
}

tasks.compileJava {
    options.isIncremental = true
    options.isFork = true
    options.isFailOnError = false
    options.compilerArgs.add("-Xlint:unchecked")
    options.isDeprecation = true

    options.release.set(17)
}