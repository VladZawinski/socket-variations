package com.zawinski.ktorws.socket

import io.ktor.client.*
import io.ktor.client.plugins.websocket.* // ktlint-disable no-wildcard-imports
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * @see https://ktor.io/docs/getting-started-ktor-client-chat.html
 * In order to subscribe events, you need to long pooling using iteration,
 * You will need to call client.webSocket{} whenever you need to perform ws actions
 */
class KtorSocketClient @Inject constructor(
    private val client: HttpClient,
    private val socketUrl: String
) : OurSocket {
    override suspend fun send(event: String, jsonString: String) {
        client.webSocket(method = HttpMethod.Get, host = socketUrl, path = "/") {
            send(event, jsonString)
        }
    }

    override fun on(event: String): Flow<String> = flow {
        client.webSocket(method = HttpMethod.Get, host = socketUrl, path = "/") {
            while (true) {
                val othersMessage = incoming.receive() as? Frame.Text
                Timber.d("$othersMessage")
            }
        }
    }

    /**
     * As Ktor need to connect before doing any operations,
     * we don't need to pre connect
     */
    override fun connect() {
    }

    /**
     * So do Disconnecting
     */
    override fun disconnect() {
    }
}
