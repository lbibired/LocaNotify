package com.example.locanotify.views

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locanotify.R
import com.example.locanotify.room.Notification
import com.example.locanotify.utils.NotificationClickDeleteInterface
import com.example.locanotify.utils.NotificationClickInterface
import com.example.locanotify.utils.NotificationRVAdapter
import com.example.locanotify.viewmodel.NotificationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), NotificationClickInterface,
    NotificationClickDeleteInterface {

    // on below line we are creating a variable
    // for our recycler view, exit text, button and viewmodel.
    lateinit var viewModal: NotificationViewModel
    lateinit var notificationsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )
        setContentView(R.layout.activity_main)
        notificationsRV = findViewById(R.id.notificationRV)
        addFAB = findViewById(R.id.idFAB)


        notificationsRV.layoutManager = LinearLayoutManager(this)


        val notificationRVAdapter = NotificationRVAdapter(this, this, this)


        notificationsRV.adapter = notificationRVAdapter


        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NotificationViewModel::class.java)

        viewModal.allNotifications.observe(this, Observer { list ->
            list?.let {
                notificationRVAdapter.updateList(it)
            }
        })
        addFAB.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNotification::class.java)
            startActivity(intent)
            this.finish()
        }

    }

    override fun onNotificationClick(notification: Notification) {
        val intent = Intent(this@MainActivity, AddNotification::class.java)
        intent.putExtra("NotificationType", "Edit")
        intent.putExtra("NotificationTitle", notification.notificationTitle)
        intent.putExtra("NotificationDescription", notification.notificationDescription)
        intent.putExtra("NotificationId", notification.id)
        intent.putExtra("NotificationLatitude", notification.latitude.toString())
        intent.putExtra("NotificationLongitude", notification.longitude.toString())

        startActivity(intent)
        this.finish()
    }

    override fun onDeleteIconClick(notification: Notification) {
        viewModal.deleteNotification(notification)
        Toast.makeText(this, "${notification.notificationTitle} Deleted", Toast.LENGTH_LONG).show()
    }










}