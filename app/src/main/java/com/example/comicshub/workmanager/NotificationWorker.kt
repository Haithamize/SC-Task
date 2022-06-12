package com.example.comicshub.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.comicshub.R
import com.example.comicshub.presentation.viewmodel.NEWEST_COMIC_NUMBER
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

const val VERBOSE_NOTIFICATION_CHANNEL_NAME_P =
    "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION_P =
    "Shows notifications whenever work starts"
const val NOTIFICATION_TITLE_P = "ComicsHub"
const val CHANNEL_ID_P = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID_P = 1
class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(
    context,
    workerParams
) {

    override fun doWork(): Result {
        try {
            val lastCheckedNewestNumber = inputData.getInt(NEWEST_COMIC_NUMBER, 0)
            Log.d("WORKMANAGER prefNumb", "${lastCheckedNewestNumber}")

            fetchNewestComicNumber(lastCheckedNewestNumber)

            return Result.success()
        }catch (e:Exception){
            return Result.failure()
        }

    }

    private fun fetchNewestComicNumber (lastCheckedNewestNumber: Int) {
        val url = "https://xkcd.com/info.0.json"
        val queue = Volley.newRequestQueue(applicationContext)
        val jsonObjectRequest = JsonObjectRequest(
            com.android.volley.Request.Method.GET,
            url,
            null,
            {
                Log.d("json body", it["num"].toString())
                if(lastCheckedNewestNumber < it["num"].toString().toInt()){
                    makeStatusNotification("ComicsHub","A new comic has been released", applicationContext)
                }
            },
            {
                Log.d("error body", it.toString())

            }
        )
        queue.add(jsonObjectRequest)
    }

        private fun makeStatusNotification(title: String, message: String, context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = VERBOSE_NOTIFICATION_CHANNEL_NAME_P
                val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION_P

                val channel = NotificationChannel(
                    CHANNEL_ID_P,
                    name,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.description = description

                // Add the channel
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

                notificationManager?.createNotificationChannel(channel)
            }

            // Create the notification
            val builder = NotificationCompat.Builder(context, CHANNEL_ID_P)
                .setSmallIcon(R.drawable.fav_white_heart)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))

            // Show the notification
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_P, builder.build())
        }

}

