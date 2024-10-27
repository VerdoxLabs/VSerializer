plugins {
    id("java")
    id("maven-publish")
}

group = "de.verdox"
version = "1.0"
description = "VSerializer"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.google.code.gson:gson:2.11.0")
    compileOnly("org.jetbrains:annotations:26.0.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

publishing {
    publications.create<MavenPublication>("maven").from(components["java"]);
    publications {
        create<MavenPublication>("lib") {
            artifact(tasks.jar)
        }
    }
    repositories.maven(repositories.mavenLocal())
}

tasks.test {
    useJUnitPlatform()
}