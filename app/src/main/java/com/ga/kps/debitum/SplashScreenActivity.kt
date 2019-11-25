package com.ga.kps.debitum

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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