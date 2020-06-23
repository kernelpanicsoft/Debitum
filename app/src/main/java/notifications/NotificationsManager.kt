package notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ga.kps.debitumag.R
import com.ga.kps.debitumag.SplashScreenActivity
import helpcodes.DEFAULT_NOTIFICATION_CHANEL_ID
import java.text.SimpleDateFormat
import java.util.*

class NotificationsManager (val context: Context)  {

     fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notificacions"
            val descriptionText = "Sistema de noficaciones para Debitum"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(DEFAULT_NOTIFICATION_CHANEL_ID, name,importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }


            val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotificationBuilder(title: String, content: String) : NotificationCompat.Builder{
        val intent = Intent(context, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return builder
    }

    fun sendNotificationForReminder(title: String, content: String, reminderID: Int){
        val notificationBuilder = getNotificationBuilder(title,content)
        val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(createNotificationID(),notificationBuilder.build())
    }

    private fun createNotificationID(): Int{
        val date = Date()
        return Integer.parseInt(SimpleDateFormat("ddHHss", Locale.US).format(date))
    }


}