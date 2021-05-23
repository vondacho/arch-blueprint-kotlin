package edu.software.craftsmanship.blueprint.doc.c4

import cc.catalysts.structurizr.kotlin.addContainer
import com.structurizr.Workspace
import com.structurizr.analysis.ComponentFinder
import com.structurizr.analysis.StructurizrAnnotationsComponentFinderStrategy
import com.structurizr.api.StructurizrClient
import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Tags
import com.structurizr.util.WorkspaceUtils
import com.structurizr.view.Shape

fun main(args: Array<String>) {
    if (args.size == 3) {
        val workspaceId = args[0].toLong()
        val apiKey = args[1]
        val apiSecret = args[2]

        with(Workspace("ARCH-workspace", "C4 model for Clean architecture education")) {

            model.impliedRelationshipsStrategy = CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy()

            model.addPerson("User", "A user of ARCH-system")

            with(model.addSoftwareSystem("ARCH-system", "One fictive system")) {

                val webApi = addContainer(
                    "Blueprint API",
                    "Provides client management endpoints",
                    "Kotlin/Webflux/REST"
                ) { withTags(CustomTags.Api) }

                addContainer("Service", "Consumes Blueprint API")

                addContainer(
                    "Database",
                    "Stores client information",
                    "PostgreSQL"
                ) { withTags(CustomTags.Database) }

                ComponentFinder(
                    webApi,
                    "edu.software.craftsmanship.blueprint",
                    StructurizrAnnotationsComponentFinderStrategy()
                ).findComponents()

                views.createSystemContextView(
                    this,
                    "arch-context",
                    "The context of the ARCH-system"
                ).also {
                    it.isEnterpriseBoundaryVisible = true
                    it.addAllElements()
                }

                views.createContainerView(
                    this,
                    "arch-containers",
                    "All the containers inside the ARCH-system"
                ).also {
                    it.externalSoftwareSystemBoundariesVisible = true
                    it.addAllElements()
                }

                views.createComponentView(
                    webApi,
                    "blueprint-components",
                    "All the components inside the Blueprint API"
                ).also {
                    it.setExternalSoftwareSystemBoundariesVisible(true)
                    it.addAllElements()
                }

                with(views.configuration.styles) {
                    addElementStyle(Tags.ELEMENT).color("#ffffff")
                    addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd")
                    addElementStyle(Tags.CONTAINER).background("#438dd5")
                    addElementStyle(Tags.COMPONENT).background("#85bbf0").color("#000000")
                    addElementStyle(Tags.PERSON).background("#08427b").shape(Shape.Person)
                    addElementStyle(CustomTags.Database).shape(Shape.Cylinder)
                }
            }

            WorkspaceUtils.printWorkspaceAsJson(this)

            StructurizrClient(apiKey, apiSecret).putWorkspace(workspaceId, this)
        }
    } else throw IllegalArgumentException("Some arguments are missing, expected ones are: <workspaceId>, <apiKey>, <apiSecret>")
}

private object CustomTags {
    const val Database = "database"
    const val Api = "api"
}
