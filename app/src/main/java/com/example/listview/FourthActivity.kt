package com.example.listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listview.structure.MyAdapter
import com.example.listview.structure.paw
import kotlinx.android.synthetic.main.activity_fourth.*

class FourthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        val fac = intent.getIntExtra("fac", -1)
        val kurs = intent.getIntExtra("kurs", -1)
        val group = intent.getIntExtra("group", -1)
        recycler4.layoutManager = LinearLayoutManager(this)
        recycler4.adapter = MyAdapter(paw(this, fac, kurs, group).execute().get())
    }
}
