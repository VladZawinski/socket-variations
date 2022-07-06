package com.zawinski.ktorws.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zawinski.ktorws.di.SOCKET_IO
import com.zawinski.ktorws.socket.OurSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named(SOCKET_IO)
    private val socket: OurSocket
) : ViewModel() {

    init {
        connect()
    }

    fun disconnect() = viewModelScope.launch {
        socket.disconnect()
    }

    fun startBooking() = viewModelScope.launch {
        socket.send("booking_start", "")
    }

    fun sendSomething(text: String) = viewModelScope.launch {
        socket.send("message", text)
    }

    fun listenEvents() = viewModelScope.launch {
        socket.on("starting_booking").collectLatest {
            // Change state
            Timber.d(it)
        }

        socket.on("accepted_booking").collectLatest {
            // Change state
            Timber.d(it)
        }

        socket.on("booking_ended").collectLatest {
            // Change state
            Timber.d(it)
        }
    }

    private fun connect() = viewModelScope.launch {
        socket.connect()
    }
}
