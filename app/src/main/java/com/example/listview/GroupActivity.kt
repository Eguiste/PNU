package com.example.listview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listview.structure.MyAdapter
import com.example.listview.structure.Parse_group
import kotlinx.android.synthetic.main.activity_group.*

class GroupActivity : AppCompatActivity() {
    var DoubleClick = true
    override fun onResume() {
        super.onResume()
        DoubleClick = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        labelgroup.text = "Выберите группу:"
        val fac = getIntent().getIntExtra("fac", -1)
        val kurs = getIntent().getIntExtra("kurs", -1)
        grouprecycler.layoutManager = LinearLayoutManager(this)
        grouprecycler.adapter = MyAdapter(Parse_group(fac, kurs).execute().get())


        grouprecycler.addOnItemTouchListener(
            MyAdapter.RecyclerItemClickListener(
                this,
                grouprecycler,
                object : MyAdapter.RecyclerItemClickListener.OnItemClickListener {

                    override fun onItemClick(view: View, position: Int) {

                        if(DoubleClick)
                            creategroup(fac,kurs, position)
                        DoubleClick = false

                    }
                    override fun onItemLongClick(view: View?, position: Int) {
                        if(DoubleClick)
                            creategroup(fac, kurs, position)
                        DoubleClick = false
                    }
                })
        )
    }
    fun creategroup(pos_fac: Int, pos_kurs: Int, pos_group: Int){
        val fourthIntent = Intent(this, FourthActivity::class.java)
        fourthIntent.putExtra("fac", pos_fac)
        fourthIntent.putExtra("kurs", pos_kurs)
        fourthIntent.putExtra("group", pos_group)
        startActivity(fourthIntent)
    }


}