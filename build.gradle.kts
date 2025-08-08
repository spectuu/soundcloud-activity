plugins {
    id("java")
}

group = "proyect"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-core:3.0.0-beta3")
}

tasks.test {
    useJUnitPlatform()
}