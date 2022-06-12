package com.example.listview.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/*class Saver{
    fun Save(day: Day)
    {
        var raspDB = SQLiteDatabase.openOrCreateDatabase("/data/data/com.togu/databases/rasp.db",null)
        //raspDB.execSQL("CREATE TABLE IF NOT EXISTS week (ind INT, lesson_name VARCHAR(100), lesson_number INT, type VARCHAR(100), prepod_name VARCHAR(100), subgroups VARCHAR(100), audit VARCHAR(100), even_parity INT)")

        for(i in 0 until 1)//day.lesson_list.size)
        {
            var row = ContentValues()
            row.put("ind", day.index)
            row.put("lesson_name", day.lesson_list[i].lesson_name)
            row.put("lesson_number", day.lesson_list[i].lesson_number)
            row.put("type", day.lesson_list[i].type)
            row.put("prepod_name", day.lesson_list[i].prepod_name)
            row.put("subgroups", day.lesson_list[i].subgroups)
            row.put("audit", day.lesson_list[i].audit)
            row.put("even_parity", day.lesson_list[i].even_parity)
            raspDB.insert("week", null, row)
        }
        raspDB.close()


    }
}*/
class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER)
{
    companion object {
        private val DATABASE_VER = 1
        private val DATABASE_NAME = "LESSONS.db"

        //table
        private val TABLE_NAME = "Lesson"
        private val COL_ID = "Id"
        private val COL_DAY = "Day"
        private val COL_LESSON_NAME = "Lesson_name"
        private val COL_LESSON_NUMBER = "Lesson_number"
        private val COL_TYPE = "Type"
        private val COL_PREPOD_NAME = "Prepod_name"
        private val COL_SUBGROUP = "Subgroups"
        private val COL_AUDIT = "Audit"
        private val COL_EVEN_PARITY = "Even_parity"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERE =  ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER,$COL_DAY INTEGER,$COL_LESSON_NAME TEXT,$COL_LESSON_NUMBER INTEGER,$COL_TYPE TEXT,$COL_PREPOD_NAME TEXT,$COL_SUBGROUP TEXT,$COL_AUDIT TEXT,$COL_EVEN_PARITY INTEGER)")
        db!!.execSQL(CREATE_TABLE_QUERE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    val
            allDay: Array<Lesson>
    get()
    {
        var lstLessons = emptyArray<Lesson>()
        val selectQuere = "SELECT * FROM $TABLE_NAME"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuere, null)
        if(cursor.moveToFirst())
        {
            do
            {
                val lesson = Lesson(id = cursor.getInt(cursor.getColumnIndex(COL_ID)),
                                    day=cursor.getInt(cursor.getColumnIndex(COL_DAY)),
                                    lesson_name = cursor.getString(cursor.getColumnIndex(COL_LESSON_NAME)),
                                    lesson_number = cursor.getInt(cursor.getColumnIndex(COL_LESSON_NUMBER)),
                                    type = cursor.getString(cursor.getColumnIndex(COL_TYPE)),
                                    prepod_name = cursor.getString(cursor.getColumnIndex(COL_PREPOD_NAME)),
                                    subgroups = cursor.getString(cursor.getColumnIndex(COL_SUBGROUP)),
                                    audit = cursor.getString(cursor.getColumnIndex(COL_AUDIT)),
                                    even_parity = cursor.getInt(cursor.getColumnIndex(COL_EVEN_PARITY)))


                lstLessons += lesson
            } while(cursor.moveToNext())


        }
        db.close()
        return lstLessons
    }

    fun addLesson(lesson: Lesson)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, lesson.id)
        values.put(COL_DAY, lesson.day)
        values.put(COL_LESSON_NAME, lesson.lesson_name)
        values.put(COL_LESSON_NUMBER, lesson.lesson_number)
        values.put(COL_TYPE, lesson.type)
        values.put(COL_PREPOD_NAME, lesson.prepod_name)
        values.put(COL_SUBGROUP, lesson.subgroups)
        values.put(COL_AUDIT, lesson.audit)
        values.put(COL_EVEN_PARITY, lesson.even_parity)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateLesson(lesson: Lesson): Int
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, lesson.id)
        values.put(COL_DAY, lesson.day)
        values.put(COL_LESSON_NAME, lesson.lesson_name)
        values.put(COL_LESSON_NUMBER, lesson.lesson_number)
        values.put(COL_TYPE, lesson.type)
        values.put(COL_PREPOD_NAME, lesson.prepod_name)
        values.put(COL_SUBGROUP, lesson.subgroups)
        values.put(COL_AUDIT, lesson.audit)
        values.put(COL_EVEN_PARITY, lesson.even_parity)

        return db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(lesson.id.toString()))

    }

    fun deleteLesson(lesson: Lesson)
    {
        val db = this.writableDatabase

        db.delete(TABLE_NAME,"$COL_ID=?", arrayOf(lesson.id.toString()))
        db.close()
    }


}
class Repeat(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER)
{
    companion object {
        private val DATABASE_VER = 1
        private val DATABASE_NAME = "Repeat.db"

        //table
        private val TABLE_NAME = "Choose"
        private  val COL_ID = "Id"
        private val COL_VALUE = "Value"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERE =  ("CREATE TABLE $TABLE_NAME ($COL_VALUE INTEGER,$COL_ID INTEGER)")
        db!!.execSQL(CREATE_TABLE_QUERE)

    }
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    val allValues: Array<Int>
        get() {
            var lstValues = emptyArray<Int>()
            val selectQuere = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuere, null)
            if (cursor.moveToFirst()) {
                do {
                    lstValues += cursor.getInt(cursor.getColumnIndex(COL_VALUE))
                } while (cursor.moveToNext())


            }
            db.close()
            return lstValues
        }
    fun addValue(value: Int, id: Int)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_VALUE, value)
        values.put(COL_ID, id)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateValue(value: Int, id: Int): Int
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_VALUE, value)
        values.put(COL_ID, id)

        return db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(id.toString()))

    }

    fun deleteValue(id: Int)
    {
        val db = this.writableDatabase

        db.delete(TABLE_NAME,"$COL_ID=?", arrayOf(id.toString()))
        db.close()
    }
}