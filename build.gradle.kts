plugins {
    kotlin("jvm") version "1.3.72"
}

group = "hu.aradipatrik"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.ocraft:ocraft-s2client-bot:0.3.17")
}

kotlin {
    java.targetCompatibility = JavaVersion.VERSION_1_9
    java.sourceCompatibility = JavaVersion.VERSION_1_9
}
