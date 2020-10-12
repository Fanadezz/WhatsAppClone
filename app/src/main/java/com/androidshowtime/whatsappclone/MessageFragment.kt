package com.androidshowtime.whatsappclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androidshowtime.whatsappclone.databinding.FragmentMessageBinding
import com.androidshowtime.whatsappclone.model.Message
import com.androidshowtime.whatsappclone.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MessageFragment : Fragment() {
    //vals
    private val args: MessageFragmentArgs by navArgs()

    //vars
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var user: User
    private lateinit var binding: FragmentMessageBinding
    private var sendMessage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //retrieve user from Nav Args
        user = args.user


        //initialize binding
        binding = FragmentMessageBinding.inflate(inflater)

        //initialize auth and firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //set actionBar's title
        (activity as AppCompatActivity).supportActionBar?.title = user.name


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

        } else {


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

        val message = Message(from, to, messageString, Date())


        return message
    }


    private fun sendMessage() {

        val message = createMessage()
        firestore.collection("Messages").add(message).addOnSuccessListener { }
    }


}