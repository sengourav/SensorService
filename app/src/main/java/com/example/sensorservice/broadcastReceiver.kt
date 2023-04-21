package com.example.sensorservice

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import aws.smithy.kotlin.runtime.http.engine.callContext

class broadcastReceiver:BroadcastReceiver(){
    val CHANNEL_ID = "MySensorServiceChannel"
    override fun onReceive(p0: Context, p1: Intent?) {
        var intent:Intent= Intent(p0,MainActivity5::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK   or Intent.FLAG_ACTIVITY_NEW_TASK)
        var pendingIntent:PendingIntent=PendingIntent.getActivity(p0,0,intent,0)
        val notification = NotificationCompat.Builder(p0,CHANNEL_ID)
            .setContentTitle("Survey")
            .setContentText("Click to complete the survey")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.sun)
        with(NotificationManagerCompat.from(p0)) {
            // notificationId is a unique int for each notification that you must define
            notify(6, notification.build())
        }

    }

}