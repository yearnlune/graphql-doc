package io.github.yearnlune.graphql.doc.directive.plugin

import io.github.yearnlune.graphql.doc.directive.plugin.BuildProperties.APPLY_GRAPHQL_DOC_DIRECTIVE_TASK
import io.github.yearnlune.graphql.doc.directive.plugin.BuildProperties.COPY_GRAPHQL_DOC_DIRECTIVE_TASK
import io.github.yearnlune.graphql.doc.directive.plugin.BuildProperties.EXTRACT_GRAPHQL_DOC_DIRECTIVE_TASK
import io.github.yearnlune.graphql.doc.directive.plugin.BuildProperties.OUTPUT_RESOURCE_DIRECTORY
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import java.io.File
import java.nio.file.Paths

class GraphqlDocDirectivePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        registerTasks(target)

        target.afterEvaluate {
            initializePlugin(target)
        }
    }

    private fun registerTasks(project: Project) {
        registerExtractInterface(project)
        registerCopyInterface(project)
        registerApplyGraphqlDocDirective(project)
    }

    private fun registerExtractInterface(project: Project) {
        project.tasks.register(EXTRACT_GRAPHQL_DOC_DIRECTIVE_TASK, ExtractGraphqlDocDirective::class.java)
        project.getTasksByName(EXTRACT_GRAPHQL_DOC_DIRECTIVE_TASK, false)

        project.plugins.apply(JavaPlugin::class.java)
    }

    private fun registerCopyInterface(project: Project) {
        project.tasks.register(COPY_GRAPHQL_DOC_DIRECTIVE_TASK, CopyGraphqlDocDirective::class.java)
        project.getTasksByName(COPY_GRAPHQL_DOC_DIRECTIVE_TASK, false)
            .forEach { copyTask ->
                copyTask.dependsOn(EXTRACT_GRAPHQL_DOC_DIRECTIVE_TASK)
            }

        project.plugins.apply(JavaPlugin::class.java)
    }

    private fun registerApplyGraphqlDocDirective(project: Project) {
        project.tasks.register(APPLY_GRAPHQL_DOC_DIRECTIVE_TASK, ApplyGraphqlDocDirective::class.java)
        project.getTasksByName(APPLY_GRAPHQL_DOC_DIRECTIVE_TASK, false)
            .forEach {
                it.dependsOn(COPY_GRAPHQL_DOC_DIRECTIVE_TASK)
                project.getTasksByName("compileJava", false)
                    .map { task -> task.dependsOn(it.path) }
            }

        project.plugins.apply(JavaPlugin::class.java)
    }

    private fun initializePlugin(project: Project) {
        addResourceToSourcesSet(project)
    }

    private fun addResourceToSourcesSet(project: Project) {
        (project.extensions.getByName("sourceSets") as SourceSetContainer)
            .getByName(SourceSet.MAIN_SOURCE_SET_NAME)
            .resources {
                val resourceDir = File(Paths.get(project.buildDir.path, OUTPUT_RESOURCE_DIRECTORY).toUri())
                it.srcDirs(resourceDir)
            }
    }
}