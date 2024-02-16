plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.modsen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
    testImplementation("org.testng:testng:7.1.0")
    testImplementation("org.testng:testng:7.1.0")
    testImplementation("org.testng:testng:7.1.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}