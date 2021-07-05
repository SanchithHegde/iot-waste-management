pluginManagement {
  repositories {
    gradlePluginPortal()
  }

  plugins {
    id("de.fayard.refreshVersions").version("0.10.1")
  }
}

plugins {
  id("de.fayard.refreshVersions")
}

refreshVersions {
  extraArtifactVersionKeyRules(file("refreshVersions-extra-rules.txt"))
}

rootProject.name = "Waste Collection"
include(":app")
