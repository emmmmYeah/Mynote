package com.example.mynote.edu.swu

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters


class MyWorker(context: Context, params: WorkerParameters?) : Worker(context, params!!) {

    val TAG="@@service"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val context = applicationContext
        val channelId = "测试渠道"
        val notification: Notification = Notification.Builder(context, channelId)
            .setContentTitle("备忘录")
            .setContentText("记得完成今天的待办哦~")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.btn_default_small)
            .build()
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, "测试渠道名称", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(1123, notification);


        Log.d("~~~", "do something")
        return Result.success()
    }




}
