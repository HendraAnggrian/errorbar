plugins {
    android("library")
    kotlin("android")
    dokka
    bintray
    `bintray-release`
}

android {
    compileSdkVersion(SDK_TARGET)
    buildToolsVersion(BUILD_TOOLS)
    defaultConfig {
        minSdkVersion(SDK_MIN)
        targetSdkVersion(SDK_TARGET)
        versionName = "$VERSION_ANDROIDX-alpha02"
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
        }
    }
    libraryVariants.all {
        generateBuildConfigProvider?.configure {
            enabled = false
        }
    }
}

val ktlint by configurations.creating

dependencies {
    api(kotlin("stdlib", VERSION_KOTLIN))
    api(project(":$RELEASE_ARTIFACT"))
    implementation(material("$VERSION_ANDROIDX-alpha02"))

    ktlint(ktlint())
}

tasks {
    register<JavaExec>("ktlint") {
        group = org.gradle.language.base.plugins.LifecycleBasePlugin.VERIFICATION_GROUP
        inputs.dir("src")
        outputs.dir("src")
        description = "Check Kotlin code style."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args("--android", "src/**/*.kt")
    }
    "check" {
        dependsOn("ktlint")
    }
    register<JavaExec>("ktlintFormat") {
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
    named<org.jetbrains.dokka.gradle.DokkaTask>("dokka") {
        outputDirectory = "$buildDir/docs"
        doFirst { file(outputDirectory).deleteRecursively() }
    }
}

publish {
    bintrayUser = BINTRAY_USER
    bintrayKey = BINTRAY_KEY
    dryRun = false
    repoName = RELEASE_REPO

    userOrg = RELEASE_USER
    groupId = RELEASE_GROUP
    artifactId = "$RELEASE_ARTIFACT-ktx"
    publishVersion = "$VERSION_ANDROIDX-alpha02"
    desc = RELEASE_DESC
    website = RELEASE_WEBSITE
}
