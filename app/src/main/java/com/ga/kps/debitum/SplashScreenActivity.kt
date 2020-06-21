package com.ga.kps.debitum

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
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