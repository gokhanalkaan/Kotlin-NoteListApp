package com.example.notelistapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SQLiteHelper(var context: Context?):SQLiteOpenHelper(context, DATABASE_NAME,null,1) {


    companion object {

        private val DATABASE_NAME = "NotesDB"


        private val DATABASE_VERSION = 1


        val TABLE_NAME = "Note"


        val ID_COL = "id"
        val UID_COL = "uid"

        val TITLE_COl = "title"


        val ISCHECKED_COL = "IsChecked"
        val ALARM_REQUST_CODE = "alarmRequestCode"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        var createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE_COl + " VARCHAR(256)," +
                UID_COL + " VARCHAR(256)," +
                ALARM_REQUST_CODE + " INTEGER,"+
                ISCHECKED_COL + " BINARY)"


        db?.execSQL(createTable);
        println("createeddd")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    fun addNote(note: Note): Boolean {


        val values = ContentValues()
        val checked: Int = when (note.isChecked) {
            true -> 1
            false -> 0
        }

        values.put(TITLE_COl, note.title)
        values.put(UID_COL, note.uid)
        values.put(ISCHECKED_COL, checked)
        values.put(ALARM_REQUST_CODE,note.alarmRequestCode)


        val db = this.writableDatabase


        var sonuc = db.insert(TABLE_NAME, null, values)
        println("sonuccc:" + sonuc)
        if (sonuc == (-1).toLong()) {
            db.close()
            return false
        } else {
            db.close()
            return true
        }




    }

    @SuppressLint("Range")
    fun getNotes(): MutableList<Note> {
        val db = this.readableDatabase
        var noteList: MutableList<Note> = mutableListOf();

        // below code returns a cursor to
        // read data from the database
        var sonuc = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

        if (sonuc.moveToFirst()) {
            do {
                val checked = when (sonuc.getString(sonuc.getColumnIndex(ISCHECKED_COL)).toInt()) {
                    1 -> true
                    0 -> false
                    else -> false
                }
                var note = Note(
                    sonuc.getString(sonuc.getColumnIndex(ID_COL)).toInt(),
                    sonuc.getString(sonuc.getColumnIndex(TITLE_COl)),
                    sonuc.getString(sonuc.getColumnIndex(UID_COL)),
                        sonuc.getString(sonuc.getColumnIndex(ALARM_REQUST_CODE)).toInt(),

                    checked
                )

                noteList.add(note);

            } while (sonuc.moveToNext())
        }
        return noteList;


    }

    fun deleteAllNotes() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

    fun deleteItem(uid: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "uid=?", arrayOf(uid))
        db.close()
    }

    fun updateNote(currNote: Note) {
        val db = writableDatabase
        val values = ContentValues()


        //  values.put(ID_COL, currNote.id);
        //  values.put(UID_COL, currNote.uid);
        values.put(ALARM_REQUST_CODE, currNote.alarmRequestCode);
        values.put(ISCHECKED_COL, currNote.isChecked);
        // values.put(TRACKS_COL, courseTracks);

        db.update(TABLE_NAME, values, "uid=?", arrayOf(currNote.uid));
        db.close();

    }



    @SuppressLint("Range")
    fun getSingleNote(noteUid: String): Note {
       lateinit var note: Note
        val db = this.readableDatabase
        var sonuc = db.rawQuery(
            "SELECT * FROM " + TABLE_NAME + " WHERE "+ UID_COL+ " = "+ noteUid,
            null
        )

        if (sonuc.moveToFirst()) {
            val checked = when (sonuc.getString(sonuc.getColumnIndex(ISCHECKED_COL)).toInt()) {
                1 -> true
                0 -> false
                else -> false
            }
            note = Note(
                sonuc.getString(sonuc.getColumnIndex(ID_COL)).toInt(),
                sonuc.getString(sonuc.getColumnIndex(TITLE_COl)),
                sonuc.getString(sonuc.getColumnIndex(UID_COL)),
                    sonuc.getString(sonuc.getColumnIndex(ALARM_REQUST_CODE)).toInt(),

                checked
            )


        }
        db.close()
        return note


    }
}