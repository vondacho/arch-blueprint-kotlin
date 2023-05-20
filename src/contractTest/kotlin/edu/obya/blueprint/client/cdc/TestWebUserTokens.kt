package edu.obya.blueprint.client.cdc

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object TestWebUser {
    const val TEST_USER_NAME = "test"
    const val TEST_USER_PASSWORD = "test"
    const val TEST_ADMIN_NAME = "admin"
    const val TEST_ADMIN_PASSWORD = "admin"
}

object TestWebUserTokens {
    val TEST_USER_TOKEN = tokenOf(TestWebUser.TEST_USER_NAME, TestWebUser.TEST_USER_PASSWORD)
    val TEST_ADMIN_TOKEN = tokenOf(TestWebUser.TEST_ADMIN_NAME, TestWebUser.TEST_ADMIN_PASSWORD)

    @OptIn(ExperimentalEncodingApi::class)
    private fun tokenOf(username: String, password: String): String {
        val token = Base64.encode("$username:$password".toByteArray())
        return "Basic $token"
    }
}
