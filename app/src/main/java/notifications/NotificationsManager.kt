package notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ga.kps.debitum.R
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
        val builder = NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        return builder
    }

    fun sendNotificationForReminder(title: String, content: String){
        val notificationBuilder = getNotificationBuilder(title,content)
        val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(createNotificationID(),notificationBuilder.build())
    }

    private fun createNotificationID(): Int{
        val date = Date()
        return Integer.parseInt(SimpleDateFormat("ddHHss", Locale.US).format(date))
    }


}