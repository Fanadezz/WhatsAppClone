package com.androidshowtime.whatsappclone

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidshowtime.whatsappclone.databinding.IncomingMsgLayoutBinding
import com.androidshowtime.whatsappclone.databinding.OutgoingMsgLayoutBinding
import com.androidshowtime.whatsappclone.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/*Add RecyclerViewAdapter class responsible for displaying message items. The Adapter
Class extends RecyclerView.Adapter and we pass in a ViewHolder Class*/
class RecyclerViewAdapter(private val chatMessageThread: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    /*  //inflate the recyclerView's row xml layout and return a ViewHolder
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

      //maps data to the children
      override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

          //it is called for each row
          val message = messageDataSet[position]
          holder.fromTextView.text = message.from
          holder.msgTextView.text = message.messageString
          holder.timeStampTextView.text = message.timestamp?.dateToString("hh:mm a E dd-MMM")

      }
  */

    //extension function on a date class
    private fun Date.dateToString(format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return dateFormatter.format(this)
    }

    //represents number of rows in your recycler view
    override fun getItemCount(): Int {

        Timber.i("inside onCreateViewHolder")
        return chatMessageThread.size
    }

    //inner class for holding views which takes a View item in its constructor

    //manages and keeps track of the children
    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //itemView is the recyclerView row used to get children
        val fromTextView: TextView = itemView.findViewById(R.id.fromTextView)
        val timeStampTextView: TextView = itemView.findViewById(R.id.timeStampTextView)
        val msgTextView = itemView.findViewById(R.id.msgTextView) as TextView

    }


    ///view holder

    inner class InComingViewHolder(var itemBinding: IncomingMsgLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    inner class OutGoingViewHolder(var itemBinding: OutgoingMsgLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun getItemViewType(position: Int): Int {
        val user = chatMessageThread[position].from!!

        //0 - sender == currentUser -> message is outGoing
        //1 - sender != currentUser -> message is inComing
        return if (FirebaseAuth.getInstance().currentUser?.displayName == user) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if (viewType == 1) {
            val inComingBinding: IncomingMsgLayoutBinding =
                DataBindingUtil.inflate(inflater, R.layout.incoming_msg_layout, parent, false)
            return InComingViewHolder(inComingBinding)
        } else {

            val outB = OutgoingMsgLayoutBinding.inflate(inflater)
            val outBinding: OutgoingMsgLayoutBinding =
                DataBindingUtil.inflate(inflater, R.layout.outgoing_msg_layout, parent, false)
            return OutGoingViewHolder(outBinding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewType = getItemViewType(position)
        val chat = chatMessageThread[position]

        //for incoming chat
        if (viewType == 1) {
            (holder as InComingViewHolder).itemBinding.msg = chat

            //hide sender's name if previous chat came from the same sender
            holder.itemBinding.msgFrom.visibility =
                if (chat.from == getTheRecentSender(position)) GONE else VISIBLE
        }
        //for outgoing chat
        else {
            (holder as OutGoingViewHolder).itemBinding.msg = chat

            //hide sender's name if previous chat came from the same sender
            holder.itemBinding.msgFrom.visibility =
                if (chat.from == getTheRecentSender(position)) GONE else VISIBLE
        }
    }


    private fun getTheRecentSender(position: Int): String? {


        val previousMsg = position - 1
        //return message at index 0 if lastMessagePosition is negative
        val chat = chatMessageThread[if (previousMsg < 0) 0 else previousMsg]
        //get and return the sender's name of the last message
        return chat.from
    }

}