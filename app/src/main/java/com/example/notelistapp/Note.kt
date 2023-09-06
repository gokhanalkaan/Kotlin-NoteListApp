package com.example.notelistapp

import java.io.Serializable
import java.sql.Date
import java.util.*

data class Note(var id:Int? ,val title:String,var uid:String= UUID.randomUUID().toString(),var alarmRequestCode:Int=0,  var isChecked:Boolean=false):Serializable