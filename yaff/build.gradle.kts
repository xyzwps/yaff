plugins {
    java
}

repositories {
    mavenCentral()
    mavenLocal()
}

val lombokVersion: String by project
val graalvmVersion = "24.2.1"
val junitVersion = "5.11.0"

dependencies {
    implementation("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    implementation("org.graalvm.polyglot:polyglot:${graalvmVersion}")
    implementation("org.graalvm.polyglot:js:${graalvmVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}