package edu.obya.blueprint.client.c4

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
    if (args.isNotEmpty()) {
        with(Workspace("Obya", "A set of system architectures represented with the C4 model")) {

            model.impliedRelationshipsStrategy = CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy()

            model.addPerson("User", "API client")

            with(model.addSoftwareSystem("blueprint-system", "A small system with one service and one database")) {

                val webApi = addContainer(
                    "blueprint-api",
                    "Provides client management endpoints",
                    "Kotlin/Webflux/REST"
                ) { withTags(CustomTags.Api) }

                addContainer(
                    "Database",
                    "Stores client data",
                    "PostgreSQL"
                ) { withTags(CustomTags.Database) }

                ComponentFinder(
                    webApi,
                    "edu.obya.blueprint",
                    StructurizrAnnotationsComponentFinderStrategy()
                ).findComponents()

                views.createSystemContextView(
                    this,
                    "blueprint-context",
                    "The context of the blueprint system"
                ).also {
                    it.isEnterpriseBoundaryVisible = true
                    it.addAllElements()
                }

                views.createContainerView(
                    this,
                    "blueprint-containers",
                    "The containers inside the blueprint system"
                ).also {
                    it.externalSoftwareSystemBoundariesVisible = true
                    it.addAllElements()
                }

                views.createComponentView(
                    webApi,
                    "blueprint-api-components",
                    "The components inside the Blueprint API container"
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

            StructurizrClient().putWorkspace(args[0].toLong(), this)
        }
    } else throw IllegalArgumentException("Some arguments are missing, expected ones are: <workspaceId>")
}

private object CustomTags {
    const val Database = "database"
    const val Api = "api"
}
