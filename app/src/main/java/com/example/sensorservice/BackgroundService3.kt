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
import androidx.core.app.NotificationCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class BackgroundService3: Service() ,SensorEventListener{
    lateinit var sensor: Sensor
    lateinit var sensorManager: SensorManager
    lateinit var file: File
    lateinit var fileWriter: FileWriter
    lateinit var bufferedWriter: BufferedWriter
    lateinit var jsonArray: JSONArray
    lateinit var jsonObject: JSONObject
    val CHANNELID = "MySensorService2Channel"
    var NAME = "MySecondChannel"

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        jsonArray= JSONArray()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val nchannel = NotificationChannel(CHANNELID,NAME,NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(nchannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent : Intent = Intent(this,MainActivity3::class.java)
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)
        val notification:Notification = NotificationCompat.Builder(this,CHANNELID)
            .setContentTitle("Collect data")
            .setContentText("Coleecting Accel data")
            .setSmallIcon(R.drawable.sun)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(4,notification)
        return START_NOT_STICKY
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            jsonObject = JSONObject()
            try {
                jsonObject.put("x", p0!!.values[0])
                jsonObject.put("y", p0!!.values[1])
                jsonObject.put("z", p0!!.values[2])
            }catch (e: JSONException){
                e.printStackTrace()
            }
            jsonArray.put(jsonObject)
            val userString = jsonArray.toString()
            file = File(applicationContext.externalCacheDir, "Accel.json")
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