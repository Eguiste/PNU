package com.example.listview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listview.structure.MyAdapter
import com.example.listview.structure.Parse_kurs
import kotlinx.android.synthetic.main.activity_course.*


class CourseActivity : AppCompatActivity()
{
    var DoubleClick = true
    override fun onResume() {
        super.onResume()
        DoubleClick = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)


        labelcourse.text = "Выберите курс:"
        val fac = getIntent().getIntExtra("fac", -1)
        courserecycler.layoutManager = LinearLayoutManager(this)
        courserecycler.adapter = MyAdapter(Parse_kurs(fac).execute().get())

        courserecycler.addOnItemTouchListener(
            MyAdapter.RecyclerItemClickListener(
                this,
                courserecycler,
                object : MyAdapter.RecyclerItemClickListener.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {
                        if(DoubleClick)
                            creategroup(fac,position)
                        DoubleClick = false

                    }
                    override fun onItemLongClick(view: View?, position: Int) {
                        if(DoubleClick)
                            creategroup(fac, position)
                        DoubleClick = false
                    }
                })
        )
    }
    fun creategroup(pos_fac: Int, pos_kurs: Int){
        val thirdIntent = Intent(this, GroupActivity::class.java)
        thirdIntent.putExtra("fac", pos_fac)
        thirdIntent.putExtra("kurs", pos_kurs)
        startActivity(thirdIntent)
    }
}