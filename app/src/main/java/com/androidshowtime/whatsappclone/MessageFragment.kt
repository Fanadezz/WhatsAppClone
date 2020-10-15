package com.androidshowtime.whatsappclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidshowtime.whatsappclone.databinding.FragmentMessageBinding
import com.androidshowtime.whatsappclone.model.Message
import com.androidshowtime.whatsappclone.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.util.*


class MessageFragment : Fragment() {
    //vals
    private val args: MessageFragmentArgs by navArgs()

    //vars
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var user: User
    private lateinit var binding: FragmentMessageBinding
    private lateinit var messageThreadList: MutableList<Message>
    private lateinit var adapter: RecyclerViewAdapter
    private var sendMessage = false


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {

        //retrieve user from Nav Args
        user = args.user

        //initialize auth and firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //initialize message thread list
        messageThreadList = mutableListOf()
        //find messages
        findChats()

        Timber.i("findChats called $messageThreadList")
        //initialize binding
        binding = FragmentMessageBinding.inflate(inflater)


        //set actionBar's title
        (activity as AppCompatActivity).supportActionBar?.title = user.name





       adapter = RecyclerViewAdapter(messageThreadList)




        Timber.i("checking $messageThreadList")

        binding.recyclerView.adapter = adapter


        //send button implementation
        binding.sendButton.setOnClickListener {


            sendMessage()

            //clear editext
            binding.msgBoxEdittext.text.clear()


        }
        return binding.root
    }

    //read Edittext value
    private fun readMessageBox(): String {
        var messageString = ""

        //check if Edittext is Blank

        if (binding.msgBoxEdittext.text.toString().isNotBlank()) {

            messageString = binding.msgBoxEdittext.text.toString()

        }
        else {


            Snackbar.make(
                    binding.root,
                    resources.getString(R.string.messagebox_hint),
                    Snackbar.LENGTH_SHORT
                         )
                    .show()
        }

        return messageString

    }


    //create message

    private fun createMessage(): Message {


        val from = auth.currentUser?.displayName!!
        val to = user.name!!
        val messageString = readMessageBox()


        return Message(from, to, messageString, Date())
    }


    private fun sendMessage() {

        val message = createMessage()
        firestore.collection("Messages").add(message).addOnSuccessListener { }
    }


    private fun findChats() {
        val currentUser = auth.currentUser?.displayName!!
        val user = args.user
        val msgRef = firestore.collection("Messages")

        msgRef.whereEqualTo("from", user.name).get().addOnSuccessListener {

            querySnapshot ->
            for (doc in querySnapshot) {


                val message = doc.toObject(Message::class.java)
                messageThreadList.add(message)
                adapter.notifyDataSetChanged()
                Timber.i("The message are $messageThreadList")
            }


        }.addOnFailureListener {

            Timber.i("Error - $it")
        }


    }

}