package com.androidshowtime.whatsappclone.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    val sender: String,
    val messageString: String,
    @ServerTimestamp
    val
    timestamp: Date)