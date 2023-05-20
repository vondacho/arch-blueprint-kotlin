package edu.obya.blueprint.client.adapter.rest

import org.springframework.util.Base64Utils

object TestWebUser {
    const val TEST_USER_NAME = "test"
    const val TEST_USER_PASSWORD = "test"
    const val TEST_ADMIN_NAME = "admin"
    const val TEST_ADMIN_PASSWORD = "admin"
}

object TestWebUserTokens {
    val TEST_USER_TOKEN = tokenOf(TestWebUser.TEST_USER_NAME, TestWebUser.TEST_USER_PASSWORD)
    val TEST_ADMIN_TOKEN = tokenOf(TestWebUser.TEST_ADMIN_NAME, TestWebUser.TEST_ADMIN_PASSWORD)

    private fun tokenOf(username: String, password: String) =
        "Basic ${Base64Utils.encodeToString("$username:$password".toByteArray())}"
}
