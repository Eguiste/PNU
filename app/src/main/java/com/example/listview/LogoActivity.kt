package com.example.listview

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.listview.data.DBHelper

class LogoActivity: AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?)
        {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_logo)

            val appSettingPrefs = getSharedPreferences("AppSettingPrefs", 0)
            val isNightModeOn = appSettingPrefs.getBoolean("NightMode", false)
            if (isNightModeOn)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            Handler().postDelayed(
            {
                val db = DBHelper(this)
                val sizeDB = db.allDay.size
                if (sizeDB == 0)
                {
                    val intent = Intent(this, FacActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val intent = Intent(this, DoubleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 2000)
        }

}