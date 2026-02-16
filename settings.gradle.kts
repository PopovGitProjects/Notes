//pluginManagement {
//    repositories {
////        google {
////            content {
////                includeGroupByRegex("com\\.android.*")
////                includeGroupByRegex("com\\.google.*")
////                includeGroupByRegex("androidx.*")
////            }
////        }
//        google()
//        mavenCentral()
//        gradlePluginPortal()
//    }
//}
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//    }
//}
//
//rootProject.name = "Notes"
//include(":app")
pluginManagement {
    repositories {
        // Зеркало для плагинов (Aliyun - очень стабильное)
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }

        // Стандартные репозитории как запасные
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Зеркала для библиотек
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }

        // Яндекс (если есть специфические библиотеки, иногда работает быстрее)
        // maven { url = uri("https://common-repo.maven.yandex.net/") }

        google()
        mavenCentral()
    }
}

rootProject.name = "Notes"
include(":app")
 