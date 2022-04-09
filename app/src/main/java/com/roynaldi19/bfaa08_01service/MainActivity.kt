package com.roynaldi19.bfaa08_01service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.roynaldi19.bfaa08_01service.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding

    private var serviceBound = false
    private lateinit var boundService: MyBoundService

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBoundService.MyBinder
            boundService = myBinder.getService
            serviceBound = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartService.setOnClickListener {
            val startServiceIntent = Intent(this, MyService::class.java)
            startService(startServiceIntent)

        }

        binding.btnStartJobIntentService.setOnClickListener {
            val startIntentService = Intent(this, MyJobIntentService::class.java)
            startIntentService.putExtra(MyJobIntentService.EXTRA_DURATION, 5000L)
            MyJobIntentService.enqueueWork(this, startIntentService)

        }

        binding.btnStartBoundService.setOnClickListener {
            val boundServiceIntent = Intent(this, MyBoundService::class.java)
            bindService(boundServiceIntent, serviceConnection, BIND_AUTO_CREATE)

        }

        binding.btnStopBoundService.setOnClickListener {
            unbindService(serviceConnection)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound) {
            unbindService(serviceConnection)
        }
    }
}