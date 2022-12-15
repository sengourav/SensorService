package com.example.sensorservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.widget.TextView
import androidx.core.app.NotificationCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
//import java.text.DateFormat
//import java.text.SimpleDateFormat
import java.util.*

class BackgroundService: Service(),SensorEventListener {
    lateinit var sensor: Sensor
    lateinit var sensorManager: SensorManager
    lateinit var jsonArray: JSONArray
    lateinit var jsonObject: JSONObject
    lateinit var file:File
    lateinit var fileWriter: FileWriter
    lateinit var bufferedWriter: BufferedWriter
    val CHANNEL_ID = "MySensorServiceChannel"
    var name = "MyOtherChannel"



    override fun onBind(p0: Intent?): IBinder? {
     return null
    }

    override fun onCreate() {
        super.onCreate()
        jsonArray = JSONArray()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        createNotificationChannel()


    }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               val mChannel =
                    NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }

        }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent:Intent = Intent(this,MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)
        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Collect Data")
            .setContentText("Collecting gyro data")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.sun)
            .build()
        startForeground(1,notification)
        return START_NOT_STICKY

    }
    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor?.type==Sensor.TYPE_GYROSCOPE) {
            jsonObject = JSONObject()
            try {
                jsonObject.put("x", p0!!.values[0])
                jsonObject.put("y", p0!!.values[1])
                jsonObject.put("z", p0!!.values[2])
//                val simple: DateFormat = SimpleDateFormat(
//                    "dd MMM yyyy HH:mm:ss"
//                )
//                val result = Date(p0!!.timestamp/1000000L)
//                jsonObject.put("timestamp", p0!!.timestamp/1000000L)
                jsonObject.put("timestamp(ms)",System.currentTimeMillis() + (p0!!.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000L)

            }catch (e:JSONException){
                e.printStackTrace()
            }

            jsonArray.put(jsonObject)
            val userString = jsonArray.toString()
            file = File(applicationContext.externalCacheDir, "gyro.json")
            fileWriter = FileWriter(file)
            bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(userString)
            bufferedWriter.close()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

}