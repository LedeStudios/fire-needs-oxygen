plugins {
    id("java")
    id("com.gradleup.shadow") version "9.1.0"
    id("io.freefair.lombok") version "8.14.2"
}

group = "net.ledestudios"
version = "0.1.0"

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    archiveClassifier.set("")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
    mavenCentral()
}

dependencies {
    // Create libs folder in project root and add HytaleServer.jar to libs folder.
    compileOnly(fileTree("${rootDir}/libs") { include("*.jar") })

    implementation("org.spongepowered:configurate-yaml:4.2.0")
}
