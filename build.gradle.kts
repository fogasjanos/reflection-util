plugins {
    `java-library`
    id("io.freefair.lombok") version "3.8.0"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {

    api("org.slf4j:slf4j-api:1.7.26")

    implementation("org.slf4j:slf4j-log4j12:1.7.26")
    implementation("org.apache.logging.log4j:log4j-core:2.12.0")
    implementation("org.projectlombok:lombok:1.18.10")

    testImplementation("junit:junit:4.12")
}
