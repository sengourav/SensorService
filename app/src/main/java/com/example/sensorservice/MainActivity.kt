package com.example.sensorservice

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.sensorservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),SensorEventListener {
    lateinit var binding: ActivityMainBinding
    //lateinit var stop:Button
   // lateinit var save:Button
    lateinit var sensorManager: SensorManager
    lateinit var gyroSensor: Sensor
    var x1:Float = 0.0f;var x2:Float = 0.0f;var y1:Float=0.0f;var y2:Float=0.0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP->{
                x2=event.x
                y2=event.y
                if (x1<x2){
                    val intent:Intent = Intent(this,MainActivity4::class.java)
                    startActivity(intent)
                }
                else{
                    val i:Intent = Intent(this,MainActivity2::class.java)
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

        sensorManager= getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        binding.button.setOnClickListener {
//            changeActivity()
//        }
        setContentView(R.layout.activity_main)
        sensorManager= getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroSensor= sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(this,gyroSensor,SensorManager.SENSOR_DELAY_NORMAL)

        val save:Button = findViewById(R.id.collect_data)
        val stop:Button=findViewById(R.id.stop_collecting)

        save.setOnClickListener {
            val serviceIntent =Intent(this,BackgroundService::class.java)
            startService(serviceIntent)
        }
        stop.setOnClickListener {
            val serviceIntent =Intent(this,BackgroundService::class.java)
            stopService(serviceIntent)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type==Sensor.TYPE_GYROSCOPE){
            val gyroText:TextView=findViewById(R.id.Gyro_Sensor)
            gyroText.text = "Gyro Value \nx= ${event!!.values[0]}\n\n" + "y = ${event.values[1]}\n\n"+"z = ${event.values[2]}"}

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}