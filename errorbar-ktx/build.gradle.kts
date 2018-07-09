import org.gradle.kotlin.dsl.kotlin
import org.gradle.language.base.plugins.LifecycleBasePlugin.*
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    android("library")
    kotlin("android")
    dokka
    `git-publish`
    `bintray-release`
}

android {
    compileSdkVersion(SDK_TARGET)
    buildToolsVersion(BUILD_TOOLS)
    defaultConfig {
        minSdkVersion(SDK_MIN)
        targetSdkVersion(SDK_TARGET)
        versionName = VERSION_ANDROIDX
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            res.srcDir("res")
            resources.srcDir("src")
        }
    }
    libraryVariants.all {
        generateBuildConfig?.enabled = false
    }
}

val ktlint by configurations.creating

dependencies {
    api(kotlin("stdlib", VERSION_KOTLIN))
    api(project(":$RELEASE_ARTIFACT"))
    implementation(material())

    ktlint(ktlint())
}

tasks {
    "ktlint"(JavaExec::class) {
        get("check").dependsOn(this)
        group = VERIFICATION_GROUP
        inputs.dir("src")
        outputs.dir("src")
        description = "Check Kotlin code style."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args("--android", "src/**/*.kt")
    }
    "ktlintFormat"(JavaExec::class) {
        group = "formatting"
        inputs.dir("src")
        outputs.dir("src")
        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args("--android", "-F", "src/**/*.kt")
    }

    withType<Javadoc> {
        isEnabled = false
    }
    val dokka by tasks.getting(DokkaTask::class) {
        outputDirectory = "$buildDir/docs"
        doFirst { file(outputDirectory).deleteRecursively() }
    }
    gitPublish {
        repoUri = RELEASE_WEBSITE
        branch = "gh-pages"
        contents.from(dokka.outputDirectory)
    }
    get("gitPublishCopy").dependsOn(dokka)
}

publish {
    bintrayUser = bintrayUserEnv
    bintrayKey = bintrayKeyEnv
    dryRun = false
    repoName = RELEASE_REPO

    userOrg = RELEASE_USER
    groupId = RELEASE_GROUP
    artifactId = "$RELEASE_ARTIFACT-ktx"
    publishVersion = VERSION_ANDROIDX
    desc = RELEASE_DESC
    website = RELEASE_WEBSITE
}
