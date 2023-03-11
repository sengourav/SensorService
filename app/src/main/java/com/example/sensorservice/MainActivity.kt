
package com.example.sensorservice

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.example.sensorservice.databinding.ActivityMainBinding
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity(),SensorEventListener {
    lateinit var binding: ActivityMainBinding
    private var alarmMgr: AlarmManager?=null
    private lateinit var alarmIntent: PendingIntent
    val CHANNEL_ID = "MySensorServiceChannel"
    var name = "MyOtherChannel"

    //lateinit var stop:Button
    // lateinit var save:Button
    lateinit var sensorManager: SensorManager
    lateinit var gyroSensor: Sensor
    var x1: Float = 0.0f;
    var x2: Float = 0.0f;
    var y1: Float = 0.0f;
    var y2: Float = 0.0f;

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                y2 = event.y
                if (x1 < x2) {
                    val intent: Intent = Intent(this, MainActivity4::class.java)
                    startActivity(intent)
                } else {
                    val i: Intent = Intent(this, MainActivity2::class.java)
                    startActivity(i)
                }
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        configureAmplify()
        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        intent = Intent(this, broadcastReceiver::class.java)
        alarmIntent=PendingIntent.getBroadcast(this,0,intent,0)
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY,12)
            set(Calendar.MINUTE,40)
        }
        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            //AlarmManager.INTERVAL_DAY,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        binding.button.setOnClickListener {
//            changeActivity()
//        }
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL)

        val save: Button = findViewById(R.id.collect_data)
        val stop: Button = findViewById(R.id.stop_collecting)

        save.setOnClickListener {
            val serviceIntent = Intent(this, BackgroundService::class.java)
            startService(serviceIntent)
        }
        stop.setOnClickListener {
            val serviceIntent = Intent(this, BackgroundService::class.java)
            stopService(serviceIntent)

            uploadFile()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            val gyroText: TextView = findViewById(R.id.Gyro_Sensor)
            gyroText.text =
                "Gyro Value \nx= ${event!!.values[0]}\n\n" + "y = ${event.values[1]}\n\n" + "z = ${event.values[2]}"
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    private fun configureAmplify() {
        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)

            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }

    private fun uploadFile() {
        val exampleFile = File(applicationContext.externalCacheDir, "gyro.json")
        Amplify.Storage.uploadFile("Gyroscope", exampleFile,
            { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
            { Log.e("MyAmplifyApp", "Upload failed", it) }
        )
    }

    public fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }


}
