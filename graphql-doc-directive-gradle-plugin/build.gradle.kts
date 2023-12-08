plugins {
    `java-gradle-plugin`

    id("com.gradle.plugin-publish") version "1.2.1"
}

tasks {
    java {
        withSourcesJar()
    }
}

gradlePlugin {
    website.set("https://github.com/yearnlune/graphql-doc-directive")
    vcsUrl.set("https://github.com/yearnlune/graphql-doc-directive")

    plugins {
        create("GraphqlDocPlugin") {
            id = "io.github.yearnlune.graphql.doc.plugin"
            implementationClass = "io.github.yearnlune.graphql.doc.plugin.GraphqlDocPlugin"
            displayName = "Graphql document directive plugin"
            description = "Gradle plugin to graphql doc"
            tags.set(listOf("graphql", "document", "directive"))
        }
    }
}