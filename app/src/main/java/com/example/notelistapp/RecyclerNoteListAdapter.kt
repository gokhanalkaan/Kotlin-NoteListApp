package com.example.notelistapp

import TimePickerFragment
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView


class RecyclerNoteListAdapter(private val notes: MutableList<Note>): RecyclerView.Adapter<RecyclerNoteListAdapter.NoteVH>() {
    class NoteVH(itemView: View):RecyclerView.ViewHolder(itemView) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {

      val itemView=LayoutInflater.from(parent.context).inflate(R.layout.rv_note_item, parent, false)
        return NoteVH(itemView)
    }



    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        val currNote=notes[position]


        val title= holder.itemView.findViewById<TextView>(R.id.rvTitle)
        var  checked= holder.itemView.findViewById<CheckBox>(R.id.rvChecked)
        val noteAlarm=holder.itemView.findViewById<ImageView>(R.id.alarmImgView)
        title.text=currNote.title
        checked.isChecked=currNote.isChecked

        checkCompleted(title, checked.isChecked)
        checkAlarmSetted(noteAlarm, currNote.alarmRequestCode)
        val context: Context = holder.itemView.context
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            showDialog(context, currNote)
            true
        })
        holder.itemView.setOnClickListener(){


            println("currnote:" + currNote)// use as per your need


        }
        noteAlarm.setOnClickListener(){
            if(currNote.alarmRequestCode==0){

                println("currnote:" + currNote)// use as per your need





                TimePickerFragment(currNote, noteAlarm).show((context as AppCompatActivity).supportFragmentManager, "timePicker")


                 println("calistim")

                 currNote.alarmRequestCode=UniqueNumberHelper().lastNumber



              notifyDataSetChanged()



            }

            else{
                deleteAlarm(context, currNote)
                currNote.alarmRequestCode=0

               val sqlhelper= SQLiteHelper(context)
                sqlhelper.updateNote(currNote)
                notifyItemChanged(position)
            }
        }

        checked.setOnCheckedChangeListener{ _, isChecked ->
            val sql:SQLiteHelper=SQLiteHelper(context)
            checkCompleted(title, isChecked)
            checked.isChecked=isChecked
            currNote.isChecked=isChecked
            sql.updateNote(currNote)

        }

    }

    private fun deleteAlarm(context: Context, currNote: Note) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
                currNote.alarmRequestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager: AlarmManager? = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager!!.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDialog(context: Context, currNote: Note): Boolean {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Note")
        builder.setMessage("Are you sure? Do you really want to delete?")



        builder.setPositiveButton("Yes") { dialog, which ->
            val sql:SQLiteHelper=SQLiteHelper(context)

           notes.removeIf{
               it.uid==currNote.uid

           }

            notifyDataSetChanged()
           sql.deleteItem(currNote.uid)

        }

        builder.setNegativeButton("No") { dialog, which ->
            Toast.makeText(context,
                    "Cancelled", Toast.LENGTH_SHORT).show()
        }


        builder.show()
        return true
    }

    private fun checkCompleted(title: TextView, checked: Boolean) {

        if(checked){
            title.paintFlags=title.paintFlags or STRIKE_THRU_TEXT_FLAG

        }
        else{
            title.paintFlags=title.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()

        }

    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    private fun checkAlarmSetted(imgView: ImageView, alarmReqCode: Int) {

        if(alarmReqCode.toInt()==0){

            imgView.setImageResource(R.drawable.ic_add_alarm)


        }
        else{

                    imgView.setImageResource(R.drawable.ic_action_name)

        }

    }

    override fun getItemCount(): Int {
      return notes.size;
    }

    fun addNote(newTodo: Note) {
        notes.add(newTodo);
        notifyItemInserted(notes.size - 1)

        print(notes)

    }

    fun deleteAll(context:Context) {

       
        for (item: Note in notes){
            deleteAlarm(context,item)
        }
         notes.clear();


        notifyDataSetChanged()
    }
}
