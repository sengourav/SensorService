package com.example.sensorservice

import android.os.Bundle
import android.os.Environment
import android.util.JsonReader
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*


class MainActivity5 : AppCompatActivity(),View.OnClickListener{
    var index:Int=0
    lateinit var jsonArray: JSONArray
    lateinit var jsonObject: JSONObject
    lateinit var submitButton :Button
    lateinit var question:TextView
    lateinit var ansAButton:Button
    lateinit var ansBButton :Button
    lateinit var ansCButton:Button
    lateinit var ansDButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        loadQuestion()
        jsonObject= JSONObject()
        jsonArray= JSONArray()
        submitButton=findViewById(R.id.button5)
        question=findViewById(R.id.question)
        ansAButton=findViewById(R.id.button1)
        ansBButton=findViewById(R.id.button2)
        ansCButton=findViewById(R.id.button3)
        ansDButton=findViewById(R.id.button4)

        submitButton.setOnClickListener(this)
        ansCButton.setOnClickListener(this)
        ansBButton.setOnClickListener(this)
        ansAButton.setOnClickListener(this)
        ansDButton.setOnClickListener(this)

    }



    fun loadQuestion() {
        submitButton=findViewById(R.id.button5)
        question=findViewById(R.id.question)
        ansAButton=findViewById(R.id.button1)
        ansBButton=findViewById(R.id.button2)
        ansCButton=findViewById(R.id.button3)
        ansDButton=findViewById(R.id.button4)
        this.question.setText(QuestionAnswer().questions[index])
        this.ansAButton.setText(QuestionAnswer().states[index][0])
        this.ansBButton.setText(QuestionAnswer().states[index][1])
        this.ansCButton.setText(QuestionAnswer().states[index][2])
        this.ansDButton.setText(QuestionAnswer().states[index][3])
    }

    override fun onClick(v: View?) {
        val b :Button = v as Button
        if(b!!.id==R.id.button5){
            index=index+1
            if(index==QuestionAnswer().questions.size){
                Toast.makeText(this,"Thank you for completing the Survey",Toast.LENGTH_SHORT).show()
                //jsonArray.put(jsonObject)
                //val jsonParser: jdk.nashorn.internal.parser.JSONParser =
                 //   jdk.nashorn.internal.parser.JSONParser()
                val externalStorageState=Environment.getExternalStorageState()
                if (externalStorageState==Environment.MEDIA_MOUNTED) {

                    val filename: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                    val d = File(applicationContext.externalCacheDir, "Data")
                    if (!d.exists()) {
                        d.mkdirs()
                    }

                    jsonArray.put(jsonObject)
//                file.flush()
//                file.close()
                    val examplefile = File(d, "survey$filename.json")
                    val fileWriter = FileWriter(examplefile, true)
                    val bufferedWriter = BufferedWriter(fileWriter)
                    bufferedWriter.append(jsonArray.toString())
                    bufferedWriter.close()

            }}
            else
                loadQuestion()
        }
        else if(b!!.id==R.id.button1){
            jsonObject.put(QuestionAnswer().jsonques[index],v.text)
        }
        else if(b!!.id==R.id.button2){
            jsonObject.put(QuestionAnswer().jsonques[index],v.text)
        }
        else if(b!!.id==R.id.button3){
            jsonObject.put(QuestionAnswer().jsonques[index],v.text)
        }else{
            jsonObject.put(QuestionAnswer().jsonques[index],v.text)
        }

    }
}
