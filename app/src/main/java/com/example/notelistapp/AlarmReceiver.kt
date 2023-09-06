package com.example.notelistapp

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random


const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
class AlarmReceiver():BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val receivedIntent=intent
        val note= receivedIntent!!.getStringExtra("note")
        val noteTitle= receivedIntent!!.getStringExtra("noteTitle")

        val builder = NotificationCompat.Builder(context!!, "notification1")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(noteTitle)
                .setContentText("It is time for: "+noteTitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)



                .setAutoCancel(true)

        with(NotificationManagerCompat.from(context!!)) {

            notify(Random.nextInt(0,1000), builder.build())
        }


        println("noteeeetitleeee:"+noteTitle)


        println("deletedd:"+receivedIntent!!.getStringExtra("noteTitle"))

        println(note)
        println(noteTitle)
        println("alarmm receiverrrrrrr"+note)
      val myintent=Intent(context,AlarmActivity::class.java)

        myintent.putExtra("alarmNote",note)
        myintent.putExtra("alarmNoteTitle",noteTitle)

        myintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(myintent)

    }
}