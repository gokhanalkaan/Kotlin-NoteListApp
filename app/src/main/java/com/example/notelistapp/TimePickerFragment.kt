import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.notelistapp.*
import java.util.*
import android.app.TimePickerDialog.OnTimeSetListener as OnTimeSetListener1

class TimePickerFragment( var note: Note?,var imgView: ImageView? ) : DialogFragment(), OnTimeSetListener1 {
    var c :Calendar= Calendar.getInstance()
    lateinit var sqlHelper:SQLiteHelper
   public  var reqCode:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqlHelper=SQLiteHelper(context)


       println("noteeee:"+note)

    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)




        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
       var  requestCode=UniqueNumberHelper().produceUid()
        note!!.alarmRequestCode=requestCode




        println("reqcodeeee:"+requestCode)


        val intent=Intent(context, AlarmReceiver::class.java)

        intent.putExtra("note",note!!.uid)
        intent.putExtra("noteTitle",note!!.title)

        sqlHelper.updateNote(note!!)
        imgView!!.setImageResource(R.drawable.ic_action_name)





        val pendingIntent=PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)







        reqCode=0

        val alarmManager: AlarmManager? =requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager!![AlarmManager.RTC_WAKEUP, c.timeInMillis]=pendingIntent



    }
}