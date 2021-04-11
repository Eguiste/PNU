package com.example.listview.structure

import android.util.Log
import com.example.listview.data.Day
import com.example.listview.data.Lesson
import org.jsoup.Jsoup


val html = Jsoup.connect("http://pnu.edu.ru/rasp/groups/")
    .userAgent("Mozilla/0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
    .get()
val main = html.select("div[class=container main-inner]")


class Parse()
{
    fun fac(): Array<String> {
        var list_fac = emptyArray<String>()
        val fac = main.select("p[class=btn-slide inst_name]")

        for (i in 0 until fac.size) {
            list_fac += arrayOf(fac.eq(i).select("b").text())


        }

        return list_fac
    }

    fun kurs(c_fac: Int): Array<String>
    {
        var list_kurs = emptyArray<String>()
        val kurs = main.select("table[class=rasp-group-list]").eq(c_fac).select("th")
        for (i in 0 until kurs.size) {
            list_kurs += arrayOf(kurs.eq(i).text())


        }

        return list_kurs
    }
    fun group(c_fac: Int, c_kurs: Int): Array<String>
    {
        var list_group = emptyArray<String>()
        val group = main.select("table[class=rasp-group-list]").eq(c_fac).select("td").eq(c_kurs).select("li")
        for (i in 0 until group.size) {
            list_group += arrayOf(group.eq(i).text())
        }

        return list_group
    }
    fun lessons(c_fac: Int, c_kurs: Int, c_group: Int): Array<Day>
    {

        val url = "http://pnu.edu.ru/rasp/groups/" + main.select("table[class=rasp-group-list]").eq(c_fac).select("td").eq(c_kurs).select("li").eq(c_group).select("a").attr("href")
        val html_week = Jsoup.connect(url)
            .userAgent("Mozilla/0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
            .get()
        val lessons = html_week.select("div[class=tab_content]")
        val dayPars = lessons.select("h3[class=rasp-weekday-title]")
        var day = emptyArray<Int>()
        for (i in 0 until dayPars.size)
        {
            when(dayPars.eq(i).text())
            {
                "Понедельник" -> day += listOf(0)
                "Вторник" -> day += listOf(1)
                "Среда" -> day += listOf(2)
                "Четверг" -> day += listOf(3)
                "Пятница" -> day += listOf(4)
                "Суббота" -> day += listOf(5)
            }
        }

        var newLesson: Lesson
        var newDay: Day
        var listDay = emptyArray<Day>()
        var listLessons = emptyArray<Lesson>()
        var buff = String()
        var l = 0
        for (i in day.indices)
        {
            for (j in 0 until lessons.select("table[class=rasp]").eq(i).select("tr").size)
            {
                l++
                //Получение подгруппы
                val subgroups = lessons.select("table[class=rasp]").eq(i).select("tr").eq(j).select("td[class=time-discipline]").select("span[class=event-subgroup]").text()
                //Получение типа пары
                var type = lessons.select("table[class=rasp]").eq(i).select("tr").eq(j).select("td[class=time-discipline]").select("span[class=event-type]").text()
                if (type == "д.зач пр")
                    type = type.split(" ")[1]
                if (type == "")
                    type = "пр"
                //Получение названия пары
                var dic = lessons.select("table[class=rasp]").eq(i).select("tr").eq(j).select("td[class=time-discipline]")
                dic.select("span[class=event-subgroup]").remove()
                dic.select("span[class=event-type]").remove()
                dic.select("div[class=visible-xs hidden-print]").remove()
                val lessonName = dic.text()
                //Получение номера пары
                var lessonNumber: Int
                val num = lessons.select("table[class=rasp]").eq(i).select("tr").eq(j)
                val numc = num.select("td[class=time-hour]").text().split(" ")[0]
                if (numc != "")
                {
                    lessonNumber =  numc.toInt()
                    buff = numc
                }
                else {
                    if (num.select("td[class=time-hour time-hour-reduced]").text().split(" ")[0] != "") {
                        lessonNumber =  num.select("td[class=time-hour time-hour-reduced]").text().split(" ")[0].toInt()
                        buff = num.select("td[class=time-hour time-hour-reduced]").text().split(" ")[0]
                    }
                    else {
                        lessonNumber = buff.toInt()
                    }
                }
                //Получение фамилии препода
                val prepod_name = lessons.select("table[class=rasp]").eq(i).select("tr").eq(j).select("td[class=time-prepod]").select("a").text()

                //Получение номера аудитории
                val audit = lessons.select("table[class=rasp]").eq(i).select("tr").eq(j).select("td[class=time-room]").select("a").text()
                //Определение четности пары
                var even_parity = -1
                var p = lessons.select("table[class=rasp]").eq(i).select("tr").eq(j).select("td")
                if (p.eq(1).attr("class") == "time-discipline")
                {
                    p = p.eq(0)
                }
                else
                {
                    p = p.eq(1)
                }
                when(p.attr("class").split("-")[2])
                {
                    "0" -> {even_parity = 2}
                    "1" -> {even_parity = 1}
                    "2" -> {even_parity = 0}
                }
                val auditSplit = audit.split(" ")
                if(auditSplit.size > 1) {
                    val prepodSplit = prepod_name.split(". ")
                    val prepod1 = prepodSplit[0].plus(".").plus(prepodSplit[1]).plus(".")
                    val prepod2 = prepodSplit[2].plus(".").plus(prepodSplit[3])
                    newLesson = Lesson(lesson_name = lessonName, lesson_number = lessonNumber, audit = auditSplit[0], even_parity = even_parity, prepod_name = prepod1, type = type, subgroups = subgroups, day=day[i], id=l)
                    listLessons += listOf(newLesson)
                    newLesson = Lesson(lesson_name = lessonName, lesson_number = lessonNumber, audit = auditSplit[1], even_parity = even_parity, prepod_name = prepod2, type = type, subgroups = subgroups, day=day[i], id=l)
                    listLessons += listOf(newLesson)
                }
                else{
                newLesson = Lesson(lesson_name = lessonName, lesson_number = lessonNumber, audit = audit, even_parity = even_parity, prepod_name = prepod_name, type = type, subgroups = subgroups, day=day[i], id=l)
                listLessons += listOf(newLesson)
                    }
            }
            newDay = Day(lessList = listLessons)
            listDay += listOf(newDay)
            listLessons = emptyArray<Lesson>()
        }
        return listDay
    }

}


