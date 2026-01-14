plugins {
    id("java")
}

group = "net.ledestudios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Create libs folder in project root and add HytaleServer.jar to libs folder.
    compileOnly(fileTree("${rootDir}/libs") { include("*.jar") })

    implementation("org.spongepowered:configurate-yaml:4.2.0")
}
