import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec

fun DependencyHandler.android() = "com.android.tools.build:gradle:$androidPluginVersion"
inline val PluginDependenciesSpec.`android-library` get() = id("com.android.library")
inline val PluginDependenciesSpec.`android-application` get() = id("com.android.application")

fun DependencyHandler.support(module: String, version: String, vararg groupSuffixes: String) =
    "${StringBuilder("com.android.support").apply {
        groupSuffixes.forEach { append(".$it") }
    }}:$module:$version"

fun DependencyHandler.anko(module: String) = "org.jetbrains.anko:anko-$module:$ankoVersion"

fun DependencyHandler.junit() = "junit:junit:$junitVersion"

fun DependencyHandler.ktlint() = "com.github.shyiko:ktlint:$ktlintVersion"

fun DependencyHandler.dokka() = "org.jetbrains.dokka:dokka-android-gradle-plugin:$dokkaVersion"
inline val PluginDependenciesSpec.dokka get() = id("org.jetbrains.dokka-android")

fun DependencyHandler.gitPublish() = "org.ajoberstar:gradle-git-publish:$gitPublishVersion"
inline val PluginDependenciesSpec.`git-publish` get() = id("org.ajoberstar.git-publish")

fun DependencyHandler.bintrayRelease() = "com.novoda:bintray-release:$bintrayReleaseVersion"
inline val PluginDependenciesSpec.`bintray-release` get() = id("com.novoda.bintray-release")