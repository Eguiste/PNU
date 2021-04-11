package com.example.listview.data

import com.example.listview.structure.parity

class Day(var lessList: Array<Lesson>)
{
    fun currentLessons(): Array<Lesson>
    {
        var todayLessonList = emptyArray<Lesson>()
        for (lesson in lessList)
        {
            if (lesson.even_parity == 2 || lesson.even_parity == parity())
                todayLessonList += arrayOf(lesson)
        }
        return todayLessonList
    }
    fun countOfLesson(): Int
    {
        var numberLessonList = emptyArray<Int>()
        for (lesson in lessList)
        {
            if (lesson.even_parity == 2 || lesson.even_parity == parity())
                if (lesson.lesson_number !in numberLessonList) {
                    numberLessonList += arrayOf(lesson.lesson_number)
                }
        }
        return numberLessonList.size
    }
}