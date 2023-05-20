package edu.obya.blueprint.client.at.steps

import edu.obya.blueprint.client.at.TestWebUser.TEST_ADMIN_NAME
import edu.obya.blueprint.client.at.TestWebUser.TEST_ADMIN_PASSWORD
import edu.obya.blueprint.client.at.TestWebUser.TEST_ANONYMOUS_NAME
import edu.obya.blueprint.client.at.TestWebUser.TEST_ANONYMOUS_PASSWORD
import edu.obya.blueprint.client.at.TestWebUser.TEST_USER_NAME
import edu.obya.blueprint.client.at.TestWebUser.TEST_USER_PASSWORD
import edu.obya.blueprint.common.at.context.TestContext.put
import io.cucumber.java.en.Given

class AccessFixtureSteps {

    @Given("user has read permission")
    fun aUserHasReadPermission() {
        put("userId", TEST_USER_NAME)
        put("userPassword", TEST_USER_PASSWORD)
    }

    @Given("user has write permission")
    fun aUserHasWritePermission() {
        put("userId", TEST_USER_NAME)
        put("userPassword", TEST_USER_PASSWORD)
    }

    @Given("user has remove permission")
    fun aUserHasRemovePermission() {
        put("userId", TEST_ADMIN_NAME)
        put("userPassword", TEST_ADMIN_PASSWORD)
    }

    @Given("user has no permission")
    fun aUserHasNoPermission() {
        put("userId", TEST_ANONYMOUS_NAME)
        put("userPassword", TEST_ANONYMOUS_PASSWORD)
    }

    @Given("user is not authenticated")
    fun aUserIsNotAuthenticated() {
        put("userId", "")
        put("userPassword", "")
    }
}