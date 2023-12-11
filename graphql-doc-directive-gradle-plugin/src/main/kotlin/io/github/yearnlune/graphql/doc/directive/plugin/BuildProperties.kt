package io.github.yearnlune.graphql.doc.directive.plugin

object BuildProperties {

    const val INTERFACE_NAME = "graphql-doc-directive-core"

    const val EXTRACT_GRAPHQL_DOC_DIRECTIVE_TASK = "extractGraphqlDocDirective"

    const val COPY_GRAPHQL_DOC_DIRECTIVE_TASK = "copyGraphqlDocDirective"

    const val APPLY_GRAPHQL_DOC_DIRECTIVE_TASK = "applyGraphqlDocDirective"

    const val OUTPUT_DIRECTORY = "generated/resources/graphqlDoc"

    const val OUTPUT_RESOURCE_DIRECTORY = "$OUTPUT_DIRECTORY/resources"

    const val OUTPUT_GRAPHQL_RESOURCE_DIRECTORY = "$OUTPUT_RESOURCE_DIRECTORY/graphql"

    const val EXTRACTED_GRAPHQL_DIRECTORY = "$OUTPUT_DIRECTORY/graphql"
}
