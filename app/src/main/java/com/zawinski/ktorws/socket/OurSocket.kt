package com.zawinski.ktorws.socket

import kotlinx.coroutines.flow.Flow

interface OurSocket {
    suspend fun send(event: String, jsonString: String)
    /**
     * Used string to simplify,
     * you can just inject converter to SocketImpls or do it by hand,
     * your choice.
     */
    fun on(event: String): Flow<String>
    fun connect()
    fun disconnect()
}
