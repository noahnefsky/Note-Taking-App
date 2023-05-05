import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.ic.newFile

plugins {
    application
    kotlin("jvm") version "1.6.20"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

group = "net.codebot"
version = "1.0.0"

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDirectory.set(compileKotlin.destinationDirectory)

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test"))
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("net.sf.cssbox:pdf2dom:1.8") {
        exclude("xml-apis", "xml-apis")
        exclude("xerces", "xercesImpl")
        exclude("net.sf.cssbox", "cssbox")

    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainModule.set("notes")
    mainClass.set("notes.Main")
}

javafx {
    // version is determined by the plugin above
    version = "18.0.2"
    modules = listOf("javafx.controls", "javafx.graphics", "javafx.web")
}

// https://stackoverflow.com/questions/74453018/jlink-package-kotlin-in-both-merged-module-and-kotlin-stdlib
jlink {
    forceMerge("kotlin")
    launcher {
        name = "notesApp"
    }
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
