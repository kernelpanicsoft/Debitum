package notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderTitle = intent?.getStringExtra("ReminderTitle")
        val debtTitle = intent?.getStringExtra("DebtTitle")
        val reminderID = intent?.getIntExtra("ReminderID", -1)


        val notificationsManager = NotificationsManager(context!!)
        notificationsManager.sendNotificationForReminder(reminderTitle!!,debtTitle!!, reminderID!!)
    }
}