package com.androidshowtime.whatsappclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidshowtime.whatsappclone.model.Message
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/*Add RecyclerViewAdapter class responsible for displaying message items. The Adapter
Class extends RecyclerView.Adapter and we pass in a ViewHolder Class*/
class RecyclerViewAdapter(private val messageDataSet: MutableList<Message>) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>() {


    //inflate the recyclerView's row xml layout and return a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {

        //called a number of times depending on number of rows for initial display n the
        //number of rows needed to manage recycling
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_layout, parent, false)

        val view = parent.inflate(R.layout.recycler_view_layout)
        //return an instance of the inner ViewHolderClass
        return ViewHolderClass(itemView)
    }

    //extension function on ViewGroup class
    private fun ViewGroup.inflate(layout: Int): View {

        return LayoutInflater.from(context).inflate(layout, this, false)
    }

    //bind or maps data to the children
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        //it is called for each row
        val message = messageDataSet[position]
        holder.fromTextView.text = message.from
        holder.msgTextView.text = message.messageString
        holder.timeStampTextView.text = message.timestamp?.dateToString("HH:mm a E")

    }


//extension function on a date class
    private fun Date.dateToString(format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }

    //represents number of rows in your recycler view
    override fun getItemCount(): Int {

        Timber.i("inside onCreateViewHolder")
        return messageDataSet.size
    }

    //inner class for holding views which takes a View item in its constructor

    //manages and keeps track of the children
    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //itemView is the recyclerView row used to get children
        val fromTextView: TextView = itemView.findViewById(R.id.fromTextView)
        val timeStampTextView: TextView = itemView.findViewById(R.id.timeStampTextView)
        val msgTextView = itemView.findViewById(R.id.msgTextView) as TextView

    }


}