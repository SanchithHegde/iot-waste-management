enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
  repositories {
    gradlePluginPortal()
  }

  plugins {
    id("de.fayard.refreshVersions").version("0.40.0")
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
