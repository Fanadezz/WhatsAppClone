package com.androidshowtime.whatsappclone.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    val from: String,
    val to:String,
    val messageString: String,
    @ServerTimestamp
    val
    timestamp: Date)