package com.example.listview.structure

import android.content.Context
import android.os.AsyncTask
import com.example.listview.data.DBHelper
import com.example.listview.data.Day
import com.example.listview.data.Lesson
import com.example.listview.data.Repeat
import java.io.IOException

class Parse_fac: AsyncTask<Void, Void, Array<String>>() {

    var mass = emptyArray<String>()
    override fun doInBackground(vararg params: Void?): Array<String> {


        try {

            mass = Parse().fac()

        }
        catch (e: IOException) {
            TODO()
        }

        return mass


    }
}
class Parse_kurs(val chosenfac: Int) : AsyncTask<Void, Void, Array<String>>() {

    var mass = emptyArray<String>()
    override fun doInBackground(vararg params: Void?): Array<String> {


        try {

            mass = Parse().kurs(chosenfac)

        }
        catch (e: IOException) {
            TODO()
        }

        return mass


    }
}
class Parse_group(val chosenfac: Int, val chosenkurs: Int) : AsyncTask<Void, Void, Array<String>>() {

    var mass = emptyArray<String>()
    override fun doInBackground(vararg params: Void?): Array<String> {
        try {

            mass = Parse().group(chosenfac, chosenkurs)

        }
        catch (e: IOException) {
            TODO()
        }

        return mass
    }
}

//Функция работает, когда есть подключение к сети и сайт работает, и вызывается, когда пользователь хочет сменить группу
class paw(val context: Context,val chosenfac: Int, val chosenkurs: Int, val chosengroup: Int) : AsyncTask<Void, Void, Array<String>>() {

    override fun doInBackground(vararg params: Void?): Array<String>
    {
        val rdb = Repeat(context)
        val allV = rdb.allValues
        var fac = -1
        var kurs = -1
        var group = -1
        if (allV.size == 3) {
            fac = allV[0]
            kurs = allV[1]
            group = allV[2]
        }


        val db = DBHelper(context)
        var rMass = emptyArray<String>()
        if (fac != chosenfac || kurs != chosenkurs || group != chosengroup)
        {
            rdb.deleteValue(0)
            rdb.addValue(chosenfac, 0)

            rdb.deleteValue(1)
            rdb.addValue(chosenkurs, 1)

            rdb.deleteValue(2)
            rdb.addValue(chosengroup, 2)

            val massDay = Parse().lessons(chosenfac, chosenkurs, chosengroup)

            val all = db.allDay
            if (all.isNotEmpty())
            {

                for (i in all)
                    db.deleteLesson(i)
                for (day in massDay)
                    for (lesson in day.lessList)
                        db.addLesson(lesson)
            }
            else
            {
                for (day in massDay)
                    for (lesson in day.lessList)
                        db.addLesson(lesson)

            }
        }

        val all = db.allDay
        for (element in all)
            rMass += element.lesson_name
        db.close()
        rdb.close()
        return rMass

    }
}

class fillingWeek(val context: Context): AsyncTask<Void, Void, Array<Day>>()
{
    override fun doInBackground(vararg params: Void?): Array<Day>
    {
        val db = DBHelper(context)
        var dayList = emptyArray<Day>()
        var lessonList: Array<Lesson>
        val all = db.allDay
        for (day in 0..6) {
            lessonList = emptyArray()
            for (lesson in all)
                if (day == lesson.day)
                    lessonList += lesson
            dayList += arrayOf(Day(lessList = lessonList))
        }
        db.close()
        return dayList
    }
}

class DBisNotEmpty(val context: Context): AsyncTask<Void, Void, Boolean>()
{
    override fun doInBackground(vararg params: Void?): Boolean
    {
        val db = DBHelper(context)
        val all = db.allDay
        db.close()
        return all.isNotEmpty()
    }
}

