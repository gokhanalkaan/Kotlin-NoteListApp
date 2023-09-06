package com.example.notelistapp

import android.app.ActionBar
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notelistapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private  lateinit var noteAdapter:RecyclerNoteListAdapter;
    private lateinit var sqlHelper:SQLiteHelper;
    private lateinit var sharedPreferences:SharedPreferences;
    private lateinit var binding:ActivityMainBinding
    var myNotes: MutableList<Note> = mutableListOf();
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // window.decorView.systemUiVisibility= SYSTEM_UI_FLAG_FULLSCREEN
       // supportActionBar?.hide()
         sharedPreferences=this.getSharedPreferences("com.example.notelistapp", Context.MODE_PRIVATE)

        sqlHelper= SQLiteHelper(this)

        checkLastNoteTime()
        myNotes=sqlHelper.getNotes()


        setContentView(R.layout.activity_main)




        findViewById<TextView>(R.id.todayTV).text="                      Today's Notes"
        createNotificationChannel()



        noteAdapter=RecyclerNoteListAdapter(myNotes);

        val layoutManager=LinearLayoutManager(this)
        val rcv= findViewById<RecyclerView>(R.id.rv_note_Items)
        rcv.layoutManager=layoutManager;
        rcv.adapter=noteAdapter;




    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notelistapp"
            val descriptionText = "Channel for notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notification1", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkLastNoteTime() {
      val lastTime=sharedPreferences.getString("lastSaveDate", "")
        Toast.makeText(this,
                currentDateStr(), Toast.LENGTH_SHORT).show()
        if(lastTime != currentDateStr()){
            sqlHelper.deleteAllNotes()
        }
    }


    public   fun saveNote(view: View) {
        showDialog();
    }


    private fun currentDateStr() :String{
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = Date()
        return sdf.format(currentDate)

    }



    public   fun deleteAll(view: View) {
        sqlHelper.deleteAllNotes()
        noteAdapter.deleteAll(this)
    }


    private fun showDialog() {

        val builder = AlertDialog.Builder(this);
        val inflater: LayoutInflater = layoutInflater;
        val dialogLayout: View = inflater.inflate(R.layout.add_note_dialog, null);
        val editText: EditText = dialogLayout.findViewById(R.id.editText);

        with(builder) {
            setTitle("Add a note")
            setPositiveButton("Add") { dialog, which ->

                if(editText.text.isNotEmpty()){

                    val newTodo= Note(null, editText.text.toString());
                var res=sqlHelper.addNote(newTodo)

                    if(res){

                        val currentFormattedDate=currentDateStr()

                        sharedPreferences.edit().putString("lastSaveDate", currentFormattedDate).apply()
                        println(currentFormattedDate)
                        noteAdapter.addNote(newTodo);

                    }

                    else{
                        Toast.makeText(context, "try again", Toast.LENGTH_LONG).show()
                    }


                }





            }

            setNegativeButton("Back") { dialog, which ->




            }

            setView(dialogLayout)
            show();

        }


    }

}