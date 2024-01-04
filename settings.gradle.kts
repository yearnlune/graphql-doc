pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        flatDir { dirs("./plugins") }
        gradlePluginPortal()
    }
}
rootProject.name = "graphql-doc"
include("graphql-doc-directive")
include("graphql-doc-directive-gradle-plugin")
