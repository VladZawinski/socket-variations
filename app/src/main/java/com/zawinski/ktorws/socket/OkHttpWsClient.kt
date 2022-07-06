package com.zawinski.ktorws.socket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import okhttp3.*
import javax.inject.Inject

/**
 * Just leave it away as we need to implement tedious stuffs.
 */
class OkHttpWsClient @Inject constructor(
    private val client: OkHttpClient,
    private val socketUrl: String
) : OurSocket {
    override suspend fun send(event: String, jsonString: String) {
        val request = Request.Builder().url(socketUrl).build()
    }

    override fun on(event: String): Flow<String> = flow {
        val request = Request.Builder().url(socketUrl).build()
        client.newWebSocketAsync(request).collectLatest {
            when (it) {
                is WebSocketState.OnMessage -> {
                    // Filter things out here
                }
                is WebSocketState.Open -> {
                    // Open event
                }
            }
        }
    }

    /**
     * As you need to enqueue request manually,
     * connect method is unnecessary
     */
    override fun connect() {
    }

    override fun disconnect() {
        client
    }
}

fun OkHttpClient.newWebSocketAsync(request: Request) = callbackFlow<WebSocketState> {
    newWebSocket(
        request,
        object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                trySend(WebSocketState.Open(webSocket, response))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                trySend(WebSocketState.OnMessage(webSocket, text))
            }
        }
    )
}

sealed class WebSocketState {
    data class Open(val socket: WebSocket, val response: Response) : WebSocketState()
    data class OnMessage(val socket: WebSocket, val text: String) : WebSocketState()
}
