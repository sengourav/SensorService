package com.example.sensorservice

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class BackgroundService2 :Service(),SensorEventListener{
    lateinit var sensor: Sensor
    lateinit var sensorManager: SensorManager
    lateinit var file: File
    lateinit var fileWriter: FileWriter
    lateinit var bufferedWriter: BufferedWriter
    lateinit var jsonArray: JSONArray
    lateinit var jsonObject: JSONObject
    val CHANNEL_ID = "MySensorServiceChannel"
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        jsonArray = JSONArray()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL)

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent:Intent = Intent(this,MainActivity2::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)
        val notification= NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Collect Data")
            .setContentText("Collecting light data")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.sun)
            .build()

        startForeground(2,notification)
        return START_NOT_STICKY

    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor?.type == Sensor.TYPE_LIGHT) {
            jsonObject = JSONObject()
            try {
                jsonObject.put("light value", p0!!.values[0])
               // jsonObject.put("timestamp", p0!!.timestamp/1000000L)
                jsonObject.put("timestamp(ms)",System.currentTimeMillis() + (p0!!.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000L)
            }catch (e: JSONException){
                e.printStackTrace()
            }
            jsonArray.put(jsonObject)
            var userString = jsonArray.toString()
            file = File(applicationContext.externalCacheDir, "light.json")
            fileWriter = FileWriter(file)
            bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(userString)
            bufferedWriter.close()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()

    }
}