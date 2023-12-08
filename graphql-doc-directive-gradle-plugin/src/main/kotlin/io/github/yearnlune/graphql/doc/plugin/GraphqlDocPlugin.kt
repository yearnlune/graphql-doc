package io.github.yearnlune.graphql.doc.plugin

import io.github.yearnlune.graphql.doc.plugin.BuildProperties.OUTPUT_RESOURCE_DIRECTORY
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import java.io.File
import java.nio.file.Paths

class GraphqlDocPlugin : Plugin<Project>{

    override fun apply(target: Project) {
        target.afterEvaluate {
            initializePlugin(target)
        }
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