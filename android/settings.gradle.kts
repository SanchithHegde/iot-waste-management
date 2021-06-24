pluginManagement {
  repositories {
    gradlePluginPortal()
  }

  plugins {
    id("de.fayard.refreshVersions") version "0.10.1"
  }
}

plugins {
  id("de.fayard.refreshVersions")
}

rootProject.name = "Waste Collection"
include(":app")
