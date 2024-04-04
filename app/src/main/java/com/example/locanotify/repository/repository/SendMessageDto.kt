package com.example.locaanotify.repository

import com.example.locaanotify.room.Notification

data class SendMessageDto(
    val to: String?,
    val notification: Notification
)
