package com.example.listview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.share

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val appSettingPrefs = getSharedPreferences("AppSettingPrefs", 0)
        val darkTheme = findViewById<SwitchCompat>(R.id.darkTheme)
        val notifications = findViewById<SwitchCompat>(R.id.notifications)

        val isNightModeOn = appSettingPrefs.getBoolean("NightMode", false)
        val isNotification = appSettingPrefs.getBoolean("Notification", true)

        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        fun saveData()
        {
            val editor = appSettingPrefs.edit()
            editor.putBoolean("NightMode", darkTheme.isChecked)
            editor.putBoolean("Notifications", notifications.isChecked)
            editor.apply()
        }


        fun loadData()
        {
            darkTheme.isChecked = appSettingPrefs.getBoolean("NightMode", false)
            notifications.isChecked = appSettingPrefs.getBoolean("Notification", true)
        }

        loadData()

        darkTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            saveData()
            loadData()
        }

    }


}