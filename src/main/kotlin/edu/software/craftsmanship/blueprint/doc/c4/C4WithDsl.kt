package edu.software.craftsmanship.blueprint.doc.c4

import com.structurizr.analysis.ComponentFinder
import com.structurizr.analysis.StructurizrAnnotationsComponentFinderStrategy
import com.structurizr.api.StructurizrClient
import com.structurizr.dsl.StructurizrDslParser
import com.structurizr.util.WorkspaceUtils
import java.io.File

fun main(args: Array<String>) {
    if (args.size == 4) {
        val workspaceId = args[0].toLong()
        val apiKey = args[1]
        val apiSecret = args[2]
        val dslPath = args[3]

        // get C4 workspace from DSL file
        runCatching {
            val parser = StructurizrDslParser()
            parser.parse(File(dslPath))
            parser.workspace
        }
            .onFailure { println("C4 model could not be read, cause is $it") }
            .onSuccess { println("C4 model has been successfully loaded.") }
            .getOrNull()?.let { workspace ->

                // find predefined blueprint api container
                workspace.model.getSoftwareSystemWithName("ARCH system")
                    ?.getContainerWithName("Blueprint API")
                    ?.let { application ->
                        println("Blueprint API C4 container found.")

                        // C4 component scanning
                        ComponentFinder(
                            application,
                            "edu.software.craftsmanship.blueprint",
                            StructurizrAnnotationsComponentFinderStrategy()
                        ).findComponents()

                        // Create C4 component view
                        workspace.views.createComponentView(
                            application,
                            "blueprint-components",
                            "All the components inside the Blueprint API"
                        ).also {
                            it.setExternalSoftwareSystemBoundariesVisible(true)
                            it.addAllElements()
                        }
                    }

                WorkspaceUtils.printWorkspaceAsJson(workspace)

                StructurizrClient(apiKey, apiSecret).putWorkspace(workspaceId, workspace)
            }
    } else throw IllegalArgumentException("Some arguments are missing, expected ones are: <workspaceId>, <apiKey>, <apiSecret>")
}

