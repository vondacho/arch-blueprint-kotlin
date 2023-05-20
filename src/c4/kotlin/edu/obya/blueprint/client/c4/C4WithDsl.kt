package edu.obya.blueprint.client.c4

import com.structurizr.analysis.ComponentFinder
import com.structurizr.analysis.StructurizrAnnotationsComponentFinderStrategy
import com.structurizr.api.StructurizrClient
import com.structurizr.dsl.StructurizrDslParser
import com.structurizr.util.WorkspaceUtils
import java.io.File

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        runCatching {
            val parser = StructurizrDslParser()
            parser.parse(File(args[0]))
            parser.workspace
        }
            .onFailure { println("C4 model could not be read, cause is $it") }
            .onSuccess { println("C4 model has been successfully loaded.") }
            .getOrNull()?.let { workspace ->

                // find predefined blueprint api container
                workspace.model.getSoftwareSystemWithName("blueprint-system")
                    ?.getContainerWithName("blueprint-api")
                    ?.let { application ->
                        println("Blueprint API C4 container found.")

                        // C4 annotated component scanning
                        ComponentFinder(
                            application,
                            "edu.obya.blueprint",
                            StructurizrAnnotationsComponentFinderStrategy()
                        ).findComponents()

                        // Create C4 component view
                        workspace.views.createComponentView(
                            application,
                            "blueprint-api-components",
                            "The components inside the Blueprint API container"
                        ).also {
                            it.setExternalSoftwareSystemBoundariesVisible(true)
                            it.addAllElements()
                        }
                    }

                WorkspaceUtils.printWorkspaceAsJson(workspace)

                if (args.size > 1) {
                    runCatching {
                        StructurizrClient().putWorkspace(args[1].toLong(), workspace)
                    }
                        .onFailure { println("C4 model could not be uploaded, cause is $it") }
                        .onSuccess { println("C4 model has been successfully uploaded.") }
                }
            }
    } else throw IllegalArgumentException("Some arguments are missing, expected ones are: <dslPath> <workspaceId> <apiKey> <apiSecret>")
}

