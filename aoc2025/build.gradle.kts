plugins {
    id("java")
    id("io.freefair.aspectj.post-compile-weaving") version "8.4"
}

group = "io.github.brm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.aspectj:aspectjrt:1.9.21")
    aspect("org.aspectj:aspectjweaver:1.9.21")
}

tasks.test {
    useJUnitPlatform()
}