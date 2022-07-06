package com.zawinski.ktorws.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
class SocketModule {
    @Provides
    fun provideSocketIoSocket(
        socketUrl: String
    ): Socket {
        return IO.socket(socketUrl)
    }

    @Provides
    fun provideSocketUrl(): String =
        "http://10.0.2.2:3000"

    @Provides
    fun provideKtorWsClient(): HttpClient {
        return HttpClient() {
            install(WebSockets)
        }
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
