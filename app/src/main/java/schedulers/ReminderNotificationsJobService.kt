package schedulers

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import java.lang.Exception

class ReminderNotificationsJobService : JobService() {
    val TAG : String = "ExampleJobService"
    var jobCancelled = false
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job Started")
        doBackgroundWork(params)
        return true
    }

    private fun doBackgroundWork(params: JobParameters?){
        Thread(Runnable {
            for(i: Int in 1..100){
                Log.d(TAG, "run: " + i)
                if(jobCancelled){
                    return@Runnable
                }
                try {
                    Thread.sleep(1000)
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

            Log.d(TAG,"JOB FINISHED")
            jobFinished(params,false)
        }).start()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return false
    }
}