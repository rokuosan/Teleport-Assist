import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm") version "1.6.10"

    // Apply the application plugin to add support for building a CLI application in Java.
    application

    // Shadow Jar
    id ("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.deviseworks"
version = "1.1.2"
java.sourceCompatibility = JavaVersion.VERSION_17

val minecraftVersion = "1.18.1"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()

    maven {
        url = URI("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    compileOnly("org.spigotmc:spigot-api:${minecraftVersion}-R0.1-SNAPSHOT")
}

application {
    // Define the main class for the application.
    mainClass.set("com.deviseworks.tpa.AppKt")
}

tasks.withType<KotlinCompile>{
    kotlinOptions{
        jvmTarget = "17"
    }
}

tasks.withType<ProcessResources>{
    val props = mapOf("version" to version)

    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml"){
        expand(props)
    }
}