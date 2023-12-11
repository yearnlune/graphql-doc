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
    website.set("https://github.com/yearnlune/graphql-doc")
    vcsUrl.set("https://github.com/yearnlune/graphql-doc")

    plugins {
        create("GraphqlDocPlugin") {
            id = "io.github.yearnlune.graphql.doc.directive.plugin"
            implementationClass = "io.github.yearnlune.graphql.doc.directive.plugin.GraphqlDocDirectivePlugin"
            displayName = "Graphql document directive plugin"
            description = "Gradle plugin to graphql doc"
            tags.set(listOf("graphql", "document", "directive"))
        }
    }
}
