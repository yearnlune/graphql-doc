import java.io.ByteArrayOutputStream
import java.nio.charset.Charset

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

val isWindows = System.getProperty("os.name").toString().lowercase().contains("windows")
val executor = if (isWindows) "cmd" else "/bin/sh"

val getGitTag = { ->
    val stdout = ByteArrayOutputStream()
    val stderr = ByteArrayOutputStream()
    var tag = System.getenv("CI_COMMIT_TAG")
    runCatching {
        if (tag == null) {
            exec {
                commandLine(executor, "git", "describe", "--tags")
                standardOutput = stdout
                errorOutput = stderr
            }
            tag = stdout.toString(Charset.defaultCharset()).split("\n")[0].trim()
        }
        if (tag.startsWith("v")) {
            tag = tag.substring(1)
        }
    }.fold({
        tag
    }, {
        println(it.localizedMessage)
        it.printStackTrace()
        exec {
            commandLine(executor, "git", "describe", "--tags")
            standardOutput = stdout
            errorOutput = stderr
        }
        stdout.toString(Charset.defaultCharset()).split("\n")[0].trim()
    })
}

gradle.extra["getGitTag"] = getGitTag()