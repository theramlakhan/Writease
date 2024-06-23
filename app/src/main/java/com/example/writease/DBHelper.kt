package com.example.writease

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "NotesDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Notes"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COL_ID TEXT PRIMARY KEY, $COL_TITLE TEXT, $COL_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }


    fun insertNote(note: Note) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, note.id)
        contentValues.put(COL_TITLE, note.title)
        contentValues.put(COL_CONTENT, note.content)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Recycle")
    fun getAllNotes(): ArrayList<Note> {
        val noteList = ArrayList<Note>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getString(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            noteList.add(Note(id, title, content))
        }
        cursor.close()
        db.close()
        return noteList
    }

    fun deleteNote(id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "id=?", arrayOf(id))
        db.close()
    }

    fun updateNote(id: String, title: String, content: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_TITLE, title)
        values.put(COL_CONTENT, content)
        db.update(TABLE_NAME, values, "id=?", arrayOf(id))

    }
}