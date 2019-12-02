package notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmHelper (val context: Context) {
    lateinit var alarmIntent: PendingIntent
    var alarmManager: AlarmManager? = null

    fun createAlarmForNotifications(hourOfDay: Int, minute: Int, reminderTitle: String, debtTitle: String, reminderID: Int?){
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("ReminderTitle", reminderTitle)
            intent.putExtra("DebtTitle", debtTitle)
            intent.putExtra("ReminderID", reminderID)
            PendingIntent.getBroadcast(context, reminderID!!,intent,PendingIntent.FLAG_ONE_SHOT)
        }

    }
}