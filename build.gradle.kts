plugins {
    id("java")
    id("maven-publish")
}

group = "de.verdox.vserializer"
version = "1.0.2"
description = "vserializer"

repositories {
    mavenCentral()
}

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    compileOnly("com.google.code.gson:gson:2.11.0")
    compileOnly("org.jetbrains:annotations:26.0.1")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.jetbrains:annotations:26.0.1")
    testImplementation("com.google.code.gson:gson:2.11.0")
    testImplementation("org.ow2.asm:asm-tree:9.7")
}

publishing {
    publications.create<MavenPublication>("maven").from(components["java"]);
    publications {
        create<MavenPublication>("lib") {
            artifact(tasks.jar)
        }
    }
    repositories.maven(repositories.mavenLocal())
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/VerdoxLabs/VSerializer")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }
}

tasks{
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(21)
    }


    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}

tasks.test {
    useJUnitPlatform()
}