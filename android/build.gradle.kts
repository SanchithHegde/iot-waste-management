buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }

  dependencies {
    val libs = project.extensions.getByType<VersionCatalogsExtension>()
      .named("libs") as org.gradle.accessors.dm.LibrariesForLibs

    classpath(libs.plugin.gradle.android)
    classpath(libs.plugin.gradle.hilt)
    classpath(libs.plugin.gradle.kotlin)
    classpath(libs.plugin.gradle.onesignal)
    classpath(libs.plugin.gradle.secrets)
  }
}

plugins {
  id("com.diffplug.spotless")
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
