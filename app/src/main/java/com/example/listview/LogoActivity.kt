package com.example.listview

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

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
                val intent = Intent(this, DoubleActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }

}