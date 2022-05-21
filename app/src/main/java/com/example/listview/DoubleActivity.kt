package com.example.listview

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.listview.structure.SlidingTabLayout
import com.example.listview.structure.ViewPagerAdapter
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlin.math.roundToInt

class DoubleActivity : AppCompatActivity()
 {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_double)

        val titles = arrayOf<CharSequence>("Сегодня", "Неделя")
        val countOfTabs = 2

        // Создание toolbar и установка его вместо actionbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val settings = toolbar.settings
        settings.setOnClickListener{
            val intent = Intent(baseContext, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Создание ViewPagerAdapter
        val adapter = ViewPagerAdapter(this, supportFragmentManager, titles, countOfTabs)


        // Инициализация ViewPager и настройка адаптера
        val pager = findViewById<ViewPager>(R.id.slideViewPager)

        pager.adapter = adapter
        pager.pageMargin = dpToPx(5)
        
        // Инициализация SlidingTabLayout
        val tabs = findViewById<SlidingTabLayout>(R.id.tabs)
        tabs.setDistributeEvenly(true) // Равномерное расположение вкладок по доступной ширине

        // Настройка цвета для индикатора scrollBar в TabView
        tabs.setCustomTabColorizer(object : SlidingTabLayout.TabColorizer {
            override fun getIndicatorColor(position: Int): Int {
                return resources.getColor(R.color.horizontalScrollBar)
            }
        })

        // Установка ViewPager для SlidingTabsLayout
        tabs.setViewPager(pager)
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

}