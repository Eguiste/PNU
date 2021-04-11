package com.example.listview.structure

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.listview.R
import kotlinx.android.synthetic.main.row_main.view.*

class MyAdapter(private val listcontent: Array<String>): RecyclerView.Adapter<CustomViewHolder>(){

    override fun getItemCount(): Int {
        return listcontent.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.row_main, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val content = listcontent[position]
        holder.itemView.block.text = content
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



class CustomViewHolder(v: View): RecyclerView.ViewHolder(v){

}