plugins {
    `java-library`
    `maven-publish`
}

group = "eu.fogas"
version = "0.1.1"

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
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.ADOPTIUM
    }

    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    val lombokVersion: String by project
    val junitVersion: String by project

    // testing
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testAnnotationProcessor("org.projectlombok:lombok")

    // lombok
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
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
