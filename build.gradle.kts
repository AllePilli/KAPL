plugins {
    kotlin("jvm") version "1.6.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    @Suppress("GradlePackageUpdate")
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}