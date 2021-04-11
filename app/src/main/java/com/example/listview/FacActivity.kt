package com.example.listview

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listview.structure.MyAdapter
import com.example.listview.structure.Parse_fac
import kotlinx.android.synthetic.main.activity_fac.*

class FacActivity : AppCompatActivity()
{
    var DoubleClick = true
    override fun onResume() {
        super.onResume()
        DoubleClick = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fac)

        labelFac.text = "Выберите факультет:"
        facRecycler.layoutManager = LinearLayoutManager(this)
        val facs = Parse_fac().execute().get()
        facRecycler.adapter = MyAdapter(facs)

        facRecycler.addOnItemTouchListener(MyAdapter.RecyclerItemClickListener(this, facRecycler, object : MyAdapter.RecyclerItemClickListener.OnItemClickListener
        {

            override fun onItemClick(view: View, position: Int)
            {
                if (DoubleClick)
                    createCourse(position)
                DoubleClick = false
            }
            override fun onItemLongClick(view: View?, position: Int)
            {
                if(DoubleClick)
                    createCourse(position)
                DoubleClick = false

            }
        }
        )
        )
    }


    fun createCourse(pos: Int)
    {
        val secondIntent = Intent(this, CourseActivity::class.java)
        secondIntent.putExtra("fac", pos)
        startActivity(secondIntent)
    }
}