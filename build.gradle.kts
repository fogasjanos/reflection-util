plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.6"
}

group = "eu.fogas"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("reflectionUtil") {
            artifactId = "reflection-util"
            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
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
    maven(url="https://jitpack.io")
}

dependencies {
    val junitVersion = "5.10.2"

    // testing
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
    }

    named<Jar>("jar") {
        manifest {
            attributes(mapOf("Implementation-Title" to project.name,
                    "Implementation-Version" to project.version,
                    "Package" to "eu.fogas.reflection",
                    "Built-By" to "fogasjanos"))
        }
        shouldRunAfter("build")
    }

    named<JavaCompile>("compileJava") {
        options.isIncremental = true
        options.isFork = true
        options.isFailOnError = false
        options.compilerArgs.add("-Xlint:unchecked")
        options.isDeprecation = true

        options.release.set(17)
    }

    named<Wrapper>("wrapper") {
        version = 8.7
        distributionType = Wrapper.DistributionType.BIN
    }
}
