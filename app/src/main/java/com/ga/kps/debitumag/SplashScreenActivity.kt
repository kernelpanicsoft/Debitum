package com.ga.kps.debitumag

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import notifications.NotificationsManager


class SplashScreenActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val notificationChannel = NotificationsManager(this)
        notificationChannel.createNotificationChannel()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}