plugins {
    `java-library`
    id("io.freefair.lombok") version "3.8.0"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    val slf4jVersion = "1.7.26"
    val log4j2Version = "2.12.1"
    val lombokVersion = "1.18.10"
    val junitVersion = "4.12"

    // logging
    api("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4j2Version")
    api("org.apache.logging.log4j:log4j-api:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-core:$log4j2Version")

    // testing
    testImplementation("junit:junit:$junitVersion")

    // Lombok
    testImplementation("org.projectlombok:lombok:$lombokVersion")

}
