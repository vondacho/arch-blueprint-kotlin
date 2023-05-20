package edu.obya.blueprint.client

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.dependencies.SliceAssignment
import com.tngtech.archunit.library.dependencies.SliceIdentifier
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import edu.obya.blueprint.client.ClientArchitectureTest.Constants.ALL_FEATURE_PACKAGES
import edu.obya.blueprint.client.ClientArchitectureTest.Constants.CLIENT_FEATURE_PACKAGE
import edu.obya.blueprint.client.ClientArchitectureTest.Constants.COMMON_PACKAGE
import edu.obya.blueprint.client.ClientArchitectureTest.Constants.ROOT_PACKAGE
import org.junit.jupiter.api.Test
import java.util.function.Consumer
import java.util.stream.Collectors

internal class ClientArchitectureTest {

    private object Constants {
        const val ROOT_PACKAGE = "edu.obya.blueprint"
        const val ALL_FEATURE_PACKAGES = "$ROOT_PACKAGE.."
        const val CLIENT_FEATURE_PACKAGE = "$ROOT_PACKAGE.client"
        const val COMMON_PACKAGE = "$ROOT_PACKAGE.common"
    }
    /**
     * https://www.archunit.org/userguide/html/000_Index.html#_slices
     */
    @Test
    fun `features are isolated from each other`() {
        val importedClasses = ClassFileImporter().importPackages(ALL_FEATURE_PACKAGES)
        SlicesRuleDefinition.slices().assignedFrom(object : SliceAssignment {
            override fun getIdentifierOf(javaClass: JavaClass): SliceIdentifier {
                return if (javaClass.name.contains(".client.")) {
                    SliceIdentifier.of("client")
                } else SliceIdentifier.ignore()
            }

            override fun getDescription(): String {
                return "only feature packages"
            }
        }).should().notDependOnEachOther().check(importedClasses)
    }

    /**
     * https://www.archunit.org/userguide/html/000_Index.html#_onion_architecture
     */
    @Test
    fun `dependencies are oriented to the center`() {
        val importedClasses = ClassFileImporter().importPackages(ALL_FEATURE_PACKAGES)
        Architectures.onionArchitecture()
            .domainModels("$CLIENT_FEATURE_PACKAGE.domain.model..")
            .domainServices("$CLIENT_FEATURE_PACKAGE.domain.service..")
            .applicationServices(
                "$CLIENT_FEATURE_PACKAGE.application..",
                "$CLIENT_FEATURE_PACKAGE.config..",
                "$COMMON_PACKAGE.config..",
                "$ROOT_PACKAGE.config.."
            )
            .adapter("rest",
                "$CLIENT_FEATURE_PACKAGE.adapter.rest..",
            )
            .adapter("r2dbc", "$CLIENT_FEATURE_PACKAGE.adapter.r2dbc..")
            .check(importedClasses)
    }

    @Test
    fun `output ports are defined as interfaces`() {
        val importedClasses = ClassFileImporter().importPackages(ALL_FEATURE_PACKAGES)
        ArchRuleDefinition.classes()
            .that().haveSimpleNameEndingWith("Repository")
            .should().beInterfaces()
            .check(importedClasses)
        ArchRuleDefinition.classes()
            .that().haveSimpleNameEndingWith("Client")
            .and().haveNameNotMatching("edu.obya.blueprint.client.domain.model.Client")
            .should().beInterfaces()
            .allowEmptyShould(true)
            .check(importedClasses)
    }

    /**
     * https://www.archunit.org/userguide/html/000_Index.html#_composing_class_rules
     */
    @Test
    fun `repository adapters are named and located correctly`() {
        val importedClasses = ClassFileImporter().importPackages(ALL_FEATURE_PACKAGES)
        importedClasses
            .stream()
            .filter { clazz: JavaClass -> clazz.name.endsWith("Repository") }
            .collect(Collectors.toSet())
            .forEach(
                Consumer { clazz: JavaClass ->
                    ArchRuleDefinition.classes()
                        .that().implement(clazz.name)
                        .should().haveSimpleNameEndingWith("Adapter")
                        .andShould().resideInAnyPackage("$CLIENT_FEATURE_PACKAGE.adapter.r2dbc..")
                        .allowEmptyShould(true)
                        .check(importedClasses)
                }
            )
    }

    /**
     * https://www.archunit.org/userguide/html/000_Index.html#_composing_class_rules
     */
    @Test
    fun `client adapters are named and located correctly`() {
        val importedClasses = ClassFileImporter().importPackages(ALL_FEATURE_PACKAGES)
        importedClasses
            .stream()
            .filter { clazz: JavaClass -> clazz.name.endsWith("Client") }
            .collect(Collectors.toSet())
            .forEach(
                Consumer { clazz: JavaClass ->
                    ArchRuleDefinition.classes()
                        .that().implement(clazz.name)
                        .should().haveSimpleNameEndingWith("Adapter")
                        .andShould().resideInAnyPackage("$CLIENT_FEATURE_PACKAGE.adapter.client..")
                        .allowEmptyShould(true)
                        .check(importedClasses)
                }
            )
    }
}
