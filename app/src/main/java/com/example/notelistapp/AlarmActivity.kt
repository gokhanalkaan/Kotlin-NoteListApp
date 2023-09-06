package com.example.notelistapp

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView


class AlarmActivity : AppCompatActivity() {
    lateinit var ringtone: Ringtone
    lateinit var sqLiteHelper: SQLiteHelper
    lateinit var noteUid:String
    lateinit var receivedNote:String

    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        sqLiteHelper= SQLiteHelper(this)
        val receivedIntent=intent
       noteUid= receivedIntent!!.getStringExtra("alarmNote").toString()
         receivedNote=receivedIntent!!.getStringExtra("alarmNoteTitle").toString()
println("receivednoteeee"+receivedNote)


        setContentView(R.layout.activity_alarm)
  val alarmText=findViewById<TextView>(R.id.noteTitleTv)
        alarmText.setText(receivedNote)


        var alarmUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone=RingtoneManager.getRingtone(this,alarmUri)
        ringtone.play()

    }

    fun closeAlarm(view: View){
        if (ringtone.isPlaying){
            ringtone.stop()


        }

        sqLiteHelper.deleteItem(noteUid)

        val intent=Intent(this,MainActivity::class.java)
        this.startActivity(intent)


    }
}