package com.example.listview

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.listview.data.Day
import com.example.listview.structure.LessonAdapter
import kotlinx.android.synthetic.main.column_lesson.view.*
import kotlinx.android.synthetic.main.view_layout.view.*

class FullActivity(private val dayList: Array<Day>) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        retainInstance = true

        //задаем разметку фрагменту
        val view: View = inflater.inflate(R.layout.activity_full, container, false)
        val dayOfWeek = arrayOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота")
        val parityColor = arrayOf(R.drawable.nparity_block, R.drawable.zparity_block, R.drawable.noparity_block)//arrayOf("#438EFF", "#FD7C86", "#FCEB77")
        val parityTextColor = arrayOf("#172D4F", "#4D2629", "#504604")
        val layout = view.findViewById<LinearLayout>(R.id.LnLayout)

        for (day in dayList) {
            val lessList = day.lessList
            if (lessList.isNotEmpty()) {
                val viewLayout = layoutInflater.inflate(R.layout.view_layout, layout,false)
                viewLayout.findViewById<TextView>(R.id.dayOfWeek).text = dayOfWeek[lessList[0].day]
                for (lesson in lessList) {
                    val columnView = layoutInflater.inflate(R.layout.column_lesson, null)
                    LessonAdapter(columnView, lesson)
                    columnView.lessonType.setTextColor(Color.parseColor(parityTextColor[lesson.even_parity]))

                    columnView.indicatorLayout.background = resources.getDrawable(parityColor[lesson.even_parity])

                    viewLayout.linearLayout.addView(columnView)
                }
                layout.addView(viewLayout)
            }
        }

        return view
    }
}