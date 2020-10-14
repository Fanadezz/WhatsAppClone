package com.androidshowtime.whatsappclone.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    val from: String? = null,
    val to:String? = null,
    val messageString: String? = null,
    @ServerTimestamp
    val
    timestamp: Date? = null)