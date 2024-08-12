pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}


//had to add due to gradle sync issues with user agreements
plugins {
    id("com.gradle.develocity") version("3.17.6")
}

//had to add due to gradle sync issues with user agreements
develocity {
    buildScan {
        publishing.onlyIf { true }
    }
}

//had to add due to gradle sync issues with user agreements
develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}

rootProject.name = "AlexanderMaynard_COMP304_Hands-On_Test2_S24"
include(":app")
 