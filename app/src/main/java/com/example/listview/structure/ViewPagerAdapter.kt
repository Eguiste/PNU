package com.example.listview.structure

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.listview.FullActivity
import com.example.listview.MainActivity

// Создание конструктора, создание переменных для заголовков и количества вкладок
class ViewPagerAdapter (val context: Context, fm: FragmentManager, private var Titles: Array<CharSequence>, private var numOfTabs: Int) : FragmentStatePagerAdapter(fm)
{
    //Этот метод возвращает фрагмент для каждой позиции в View Pager
    override fun getItem(position: Int): Fragment {

        val dayList = fillingWeek(context).execute().get()
        return if (position == 0)
        {
            //Первый фрагмент
            MainActivity(context,  dayList)
        }
        else
        {
            // Второй фрагмент
            FullActivity(dayList)
        }
    }

    // Метод возвращает заголовок вкладки по индексу в TabStrip
    override fun getPageTitle(position: Int): CharSequence? {
        return Titles[position]
    }

    // Этот метод возвращает число вкладок для TabStrip
    override fun getCount(): Int {
        return numOfTabs
    }
}