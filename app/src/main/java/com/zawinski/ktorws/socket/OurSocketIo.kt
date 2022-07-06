package com.zawinski.ktorws.socket

import io.socket.client.Socket
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * @see https://socket.io/blog/native-socket-io-and-android/#using-socket-in-activity-and-fragment
 * Socket Io is pretty straightforward,
 * you can just use on, send, off, connect, disconnect with the touch of a hand.
 * socket.io will do the tedious stuffs.
 * And with the help of kotlin callbackFlow, you can transform event change callbacks to flow easily
 */
class OurSocketIo @Inject constructor(
    private val socket: Socket
) : OurSocket {

    override suspend fun send(event: String, jsonString: String) {
        socket.emit(event, jsonString)
    }

    override fun on(event: String): Flow<String> = callbackFlow {
        socket.on(event) {
            trySend(it.toString())
        }
        awaitClose {
            socket.off()
        }
    }

    override fun connect() {
        socket.connect()
    }

    override fun disconnect() {
        socket.disconnect()
    }
}

suspend fun Socket.onEvent(event: String) = suspendCancellableCoroutine<String> { continuation ->
    on(event) {
        Timber.d(event)
        continuation.resume(it.toString())
    }
}
