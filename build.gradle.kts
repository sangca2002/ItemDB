plugins {
    id("java")
}

group = "me.sangca"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://junhyung.nexus/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.1.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}