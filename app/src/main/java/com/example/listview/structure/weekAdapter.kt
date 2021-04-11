package com.example.listview.structure

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.listview.R
import com.example.listview.data.Day
import kotlinx.android.synthetic.main.column_week.view.*

class weekAdapter(private val listcontent: Array<Day>): RecyclerView.Adapter<CustomViewHolder>(){

    override fun getItemCount(): Int {
        return listcontent.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.column_week, parent, false)
        return  CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val content = listcontent[position]
        var nameWeekDay = ""
        var color = -1
        val cntLessons = content.countOfLesson().toString()
        when (content.lessList[0].day)
        {
            0 -> {nameWeekDay = "Пн"; color = Color.parseColor("#F7D800")}
            1 -> {nameWeekDay = "Вт"; color = Color.parseColor("#D979CF")}
            2 -> {nameWeekDay = "Ср"; color = Color.parseColor("#42D60C")}
            3 -> {nameWeekDay = "Чт"; color = Color.parseColor("#F78C00")}
            4 -> {nameWeekDay = "Пт"; color = Color.parseColor("#15DDD6")}
            5 -> {nameWeekDay = "Сб"; color = Color.parseColor("#B713F2")}
        }
        holder.itemView.weekday.text = nameWeekDay
        holder.itemView.day.setCardBackgroundColor(color)
        when (cntLessons) {
            in arrayListOf("2","3","4") -> holder.itemView.countLessons.text = cntLessons.plus(" пары")
            "1" -> holder.itemView.countLessons.text = cntLessons.plus(" пара")
            else -> holder.itemView.countLessons.text = cntLessons.plus(" пар")
        }
        holder.itemView.setOnClickListener {

        }
    }
    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }










    class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val mListener: OnItemClickListener?) : RecyclerView.OnItemTouchListener {

        private val mGestureDetector: GestureDetector

        interface OnItemClickListener {
            fun onItemClick(view: View, position: Int)

            fun onItemLongClick(view: View?, position: Int)
        }

        init {

            mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val childView = recyclerView.findChildViewUnder(e.x, e.y)

                    if (childView != null && mListener != null) {
                        mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
            val childView = view.findChildViewUnder(e.x, e.y)

            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            }

            return false
        }

        override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}}


}