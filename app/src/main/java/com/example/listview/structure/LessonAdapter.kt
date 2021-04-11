package com.example.listview.structure

import android.view.View
import com.example.listview.data.Lesson
import kotlinx.android.synthetic.main.column_lesson.view.*

fun LessonAdapter(view: View, content: Lesson) {
    view.lessonNumber.text = content.lesson_number.toString()
    view.lessonType.text = content.type
    view.lessonName.text = content.lesson_name
    view.subgroupNumber.text = content.subgroups
    view.prepodName.text = content.prepod_name
    view.audit.text = content.audit
}
