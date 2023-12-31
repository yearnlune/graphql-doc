buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.20")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:12.0.3")
    }
}

plugins {
    kotlin("jvm") version "1.7.0"
}

val calculatedVersion = getVersionFromGit()

allprojects {
    group = "io.github.yearnlune.graphql.doc"
    version = calculatedVersion

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    val projectDescription: String by project

    if (!project.name.contains("plugin")) {
        apply(plugin = "signing")
        apply(plugin = "maven-publish")

        val dokkaJavadoc by tasks.getting

        tasks.register<Zip>("dokkaZip") {
            from("$buildDir/dokka/html")
            dependsOn(dokkaJavadoc)
        }

        val dokkaJavadocJar by tasks.registering(Jar::class) {
            archiveClassifier.set("javadoc")
            from("$buildDir/dokka/html")
            dependsOn(dokkaJavadoc)
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenKtx") {
                    from(components["java"])
                    artifact(dokkaJavadocJar)

                    pom {
                        name.set(project.name)
                        description.set(projectDescription)
                        url.set("https://github.com/yearnlune/graphql-doc")
                        licenses {
                            license {
                                name.set("MIT License")
                                url.set("https://opensource.org/licenses/MIT")
                            }
                        }
                        developers {
                            developer {
                                id.set("yearnlune")
                                name.set("DONGHWAN KIM")
                                email.set("kdhpopyoa@gmail.com")
                            }
                        }
                        scm {
                            connection.set("scm:git:https://github.com/yearnlune/graphql-doc.git")
                            developerConnection.set("scm:git:ssh://git@github.com:yearnlune/graphql-doc.git")
                            url.set("https://github.com/yearnlune/graphql-doc")
                        }
                    }
                }
            }
            repositories {
                maven {
                    val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                    configure<SigningExtension> {
                        val signingKey: String? by project
                        val signingPassword: String? by project
                        useInMemoryPgpKeys(signingKey, signingPassword)
                        sign(publications["mavenKtx"])
                    }
                    credentials {
                        username = System.getenv("SONATYPE_USERNAME")
                        password = System.getenv("SONATYPE_PASSWORD")
                    }
                }
            }
        }
    }
}

fun getVersionFromGit(): String {
    return runCatching {
        val version = (
                System.getenv("CI_COMMIT_TAG")
                    ?.takeIf { it.isNotEmpty() }
                    ?: ProcessBuilder(listOf("git", "describe", "--tags")).start().inputStream.bufferedReader().readText()
                        .split("\n")[0]
                )
            .trim()
        if (version.startsWith("v")) {
            version.substring(1)
        } else version
    }.getOrElse {
        runCatching {
            return ProcessBuilder(listOf("git", "rev-parse", "HEAD")).start().inputStream.bufferedReader().readText()
                .trim()
                .split("\n")[0].trim() + "-SNAPSHOT"
        }.getOrElse {
            return "unknown"
        }
    }
}
