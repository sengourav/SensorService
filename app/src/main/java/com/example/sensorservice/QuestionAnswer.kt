package com.example.sensorservice

import android.R
import android.content.res.ColorStateList




class QuestionAnswer {
    public var questions = arrayOf<String>("How are of feeling?","Which place would you like to visit?","What kind of movie do you like?")
    var ques1Option = arrayOf<String>("Awesome","Good","not great","Bad")
    var ques2Option = arrayOf<String>("Himalayas","Ujjain","Karnataka","Goa")
    var question3Options= arrayOf<String>("Thriller","Comedy","Romance","Action")
    var jsonques= arrayOf("Ques1","Ques2","ques3")
    var states = arrayOf(ques1Option, ques2Option,question3Options)



}
