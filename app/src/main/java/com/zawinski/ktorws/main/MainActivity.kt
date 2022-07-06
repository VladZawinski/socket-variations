package com.zawinski.ktorws.main

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zawinski.ktorws.R
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.sendBtn).setOnClickListener {
            viewModel.startBooking()
        }

        viewModel.listenEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disconnect()
    }
}
