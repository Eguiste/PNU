package com.example.listview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listview.data.Day
import com.example.listview.structure.LessonAdapter
import com.example.listview.structure.weekAdapter
import kotlinx.android.synthetic.main.column_lesson.view.*
import kotlinx.android.synthetic.main.column_week.view.*
import kotlinx.android.synthetic.main.day_of_current_week.*
import java.util.*


class MainActivity(val cntxt: Context, private val dayList: Array<Day>): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        retainInstance = true
        //задаем разметку фрагменту
        val view: View = inflater.inflate(R.layout.activity_main, container, false)
        var weekDay = Date().day
        if(weekDay == 0)
            weekDay = 7
        var weekDayText = ""

        when (weekDay)
        {
            1 -> {
                weekDayText = "Понедельник"
            }
            2 -> {
                weekDayText = "Вторник"
            }
            3 -> {
                weekDayText = "Среда"
            }
            4 -> {
                weekDayText = "Четверг"
            }
            5 -> {
                weekDayText = "Пятница"
            }
            6 -> {
                weekDayText = "Суббота"
            }
            7 -> {
                weekDayText = "Воскресенье"
            }
        }
        val date = view.findViewById<View>(R.id.date) as TextView
        date.text = weekDayText
        val weekRecycler = view.findViewById<View>(R.id.weekRecycler) as RecyclerView
        weekRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        var cleanDayList = emptyArray<Day>()
        for (day in dayList)
            if (day.lessList.isNotEmpty())
                cleanDayList += arrayOf(day)
        weekRecycler.adapter = weekAdapter(cleanDayList)

        val layout = view.findViewById<LinearLayout>(R.id.LnLayout)

        if(dayList[weekDay - 1].lessList.isNotEmpty()) {
            val todayLessons = dayList[weekDay - 1].currentLessons()
            for (lesson in todayLessons) {
                val columnView = layoutInflater.inflate(R.layout.column_lesson, null)
                LessonAdapter(columnView, lesson)
                layout.addView(columnView)
            }
        }
        else
        {
            val columnView = layoutInflater.inflate(R.layout.no_lessons, null)
            layout.addView(columnView)
        }
        weekRecycler.addOnItemTouchListener(weekAdapter.RecyclerItemClickListener(
            cntxt,
            weekRecycler,
            object : weekAdapter.RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    val day = Dialog(cntxt)
                    day.setContentView(R.layout.day_of_current_week)
                    day.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    var title = view.weekday.text
                    var color = -1
                    var select = -1
                    when (title) {
                        "Пн" -> {
                            title = "Понедельник"; color = Color.parseColor("#F7D800"); select = 0
                        }
                        "Вт" -> {
                            title = "Вторник"; color = Color.parseColor("#D979CF"); select = 1
                        }
                        "Ср" -> {
                            title = "Среда"; color = Color.parseColor("#42D60C"); select = 2
                        }
                        "Чт" -> {
                            title = "Четверг"; color = Color.parseColor("#F78C00"); select = 3
                        }
                        "Пт" -> {
                            title = "Пятница"; color = Color.parseColor("#15DDD6"); select = 4
                        }
                        "Сб" -> {
                            title = "Суббота"; color = Color.parseColor("#B713F2"); select = 5
                        }
                    }
                    day.main.setCardBackgroundColor(color)
                    day.title.text = title
                    val lyt = day.findViewById<LinearLayout>(R.id.lessons)
                    if (dayList[select].lessList.isNotEmpty()) {
                        val todayLessons = dayList[select].currentLessons()
                        for (lesson in todayLessons) {
                            val columnView = layoutInflater.inflate(R.layout.column_lesson, null)

                            LessonAdapter(columnView, lesson)
                            lyt.addView(columnView)
                        }
                    }
                    day.show()
                }

                override fun onItemLongClick(view: View?, position: Int) {

                }
            }
        )
        )

        return view
    }

}







/*
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar


        var weekDay = Date().day
        if(weekDay == 0)
            weekDay = 7
        var weekDayText = ""
        val dayList = fillingWeek(this).execute().get()
        when (weekDay)
        {
            1 -> {weekDayText = "Понедельник"}
            2 -> {weekDayText = "Вторник"}
            3 -> {weekDayText = "Среда"}
            4 -> {weekDayText = "Четверг"}
            5 -> {weekDayText = "Пятница"}
            6 -> {weekDayText = "Суббота"}
            7 -> {weekDayText = "Воскресенье"}
        }
        date.text = weekDayText
        if (DBisNotEmpty(this).execute().get())
        {
            weekRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            var cleanDayList = emptyArray<Day>()
            for(day in dayList)
                if(day.lessList.isNotEmpty())
                    cleanDayList += arrayOf(day)
            weekRecycler.adapter = weekAdapter(cleanDayList)
            val layout = findViewById<LinearLayout>(R.id.LnLayout)
            if(dayList[weekDay-1].lessList.isNotEmpty()) {
                val todayLessons = dayList[weekDay-1].currentLessons()
                for (lesson in todayLessons) {
                    val view = layoutInflater.inflate(R.layout.column_lesson, null)
                    LessonAdapter(view, lesson)
                    layout.addView(view)
                }
            }
            else
            {
                val view = layoutInflater.inflate(R.layout.no_lessons, null)
                layout.addView(view)
            }
        }
        else
        {
            createFac()
        }

    }

    private fun createFac()
    {
        val facIntent = Intent(this, FacActivity::class.java)
        startActivity(facIntent)
    }
}
*/








