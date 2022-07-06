package com.zawinski.ktorws.di

import com.zawinski.ktorws.socket.KtorSocketClient
import com.zawinski.ktorws.socket.OurSocket
import com.zawinski.ktorws.socket.OurSocketIo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {
    @Binds
    @Named(SOCKET_IO)
    fun bindSocketIoClient(impl: OurSocketIo): OurSocket

    @Binds
    @Named(KTOR)
    fun bindKtorClient(impl: KtorSocketClient): OurSocket
}

const val SOCKET_IO = "SocketIo"
const val KTOR = "Ktor"