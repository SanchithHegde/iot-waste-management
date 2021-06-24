buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.2.1")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.36")
    classpath("gradle.plugin.com.onesignal:onesignal-gradle-plugin:0.12.10")
  }
}

plugins {
  id("com.diffplug.spotless") version "5.12.5"
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  kotlin {
    encoding("UTF-8")
    target("**/*.kt")
    targetExclude("**/build/")
    ktlint("0.38.1").userData(mapOf("indent_size" to "2", "continuation_indent_size" to "2"))
    ktfmt("0.25").googleStyle()

    endWithNewline()
    trimTrailingWhitespace()
  }

  format("xml") {
    encoding("UTF-8")
    target("**/*.xml")
    targetExclude("**/build/*", ".idea/")

    endWithNewline()
    indentWithSpaces(2)
    trimTrailingWhitespace()
  }
}
