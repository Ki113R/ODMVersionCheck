package com.simonmicro.odmversioncheck

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create a new notification channel for later use
        //TODO May move this into the receiver section
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel("default", "default", NotificationManager.IMPORTANCE_DEFAULT) //IMPORTANCE_LOW to mark notifications as silent
            val notificationManager = this.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        //Register helper hook on help view button
        val helpBtn: FloatingActionButton = findViewById(R.id.helpBtn)
        helpBtn.setOnClickListener {
            val uri: Uri = Uri.parse(readSysProp("ro.odm.download")?: "https://developer.sony.com/develop/open-devices/downloads/software-binaries")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        //Update the views data fields
        val expVersionText: TextView = findViewById(R.id.expVersion)
        val gotVersionText: TextView = findViewById(R.id.gotVersion)
        val expVersion = getExpectedODMVersion()
        val gotVersion = getCurrentODMVersion()
        if(expVersion != null)
            expVersionText.text = expVersion!!.raw
        if(gotVersion != null)
            gotVersionText.text = gotVersion!!.raw
        if(expVersion != null || (gotVersion != null && expVersion!!.raw != gotVersion!!.raw))
            gotVersionText.setTextColor(Color.RED)
    }
}