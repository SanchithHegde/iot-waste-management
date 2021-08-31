enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
  repositories {
    gradlePluginPortal()
  }

  plugins {
    id("de.fayard.refreshVersions").version("0.20.0")
  }
}

plugins {
  id("de.fayard.refreshVersions")
}

refreshVersions {
  extraArtifactVersionKeyRules(file("refreshVersions-extra-rules.txt"))
}

rootProject.name = "WasteCollection"
include(":app")
