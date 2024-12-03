plugins {
    id("java")
    id("maven-publish")
}

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
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

tasks {
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                groupId = "de.verdox"
                artifactId = "vserializer"
                version = "1.0.5"
                from(components["java"])
                url = "https://github.com/VerdoxLabs/VSerializer"
                licenses {
                    license {
                        name = "GNU GENERAL PUBLIC LICENSE Version 3"
                        url = "https://www.gnu.org/licenses/gpl-3.0.en.html"
                    }
                }
                developers {
                    developer {
                        id = "verdox"
                        name = "Lukas Jonsson"
                        email = "mail.ysp@web.de"
                    }
                }
            }
        }
    }
    publications.create<MavenPublication>("maven").from(components["java"]);
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/verdoxlabs/vserializer")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}
