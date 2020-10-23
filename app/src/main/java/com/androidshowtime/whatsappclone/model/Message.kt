package com.androidshowtime.whatsappclone.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    val from: String? = null,
    val to:String? = null,
    val messageString: String? = null,
    @ServerTimestamp
    val
    timestamp: Date? = null){


    override fun toString(): String {
        return "Message[from: $from \n to: $to \n messageText: $messageString \n time: $timestamp"
    }
}