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

            createMessage()

            //clear editext
            binding.msgBoxEdittext.text.clear()


        }
        return binding.root
    }


    //create message

    fun createMessage() {


        val sender = getSenderName()
        val messageString = readMessageBox()

        val message = Message(sender, messageString, Date())

        firestore.collection("Messages").add(message).addOnSuccessListener { }

    }


    //read Edittext value
    private fun readMessageBox(): String {
        var msg = ""

        //check if Edittext is Blank

        if (binding.msgBoxEdittext.text.toString().isNotBlank()) {

            msg = binding.msgBoxEdittext.text.toString()
        }
        else {


            Snackbar.make(
                    binding.root,
                    resources.getString(R.string.messagebox_hint),
                    Snackbar.LENGTH_SHORT)
                    .show()
        }

        return msg

    }


    //get sender's name
    fun getSenderName(): String {
       var sender = ""
        val userPhoneNumber = auth.currentUser?.phoneNumber
        firestore.collection("Contacts")
                .whereEqualTo("phoneNumber", userPhoneNumber)
                .get()
                .addOnSuccessListener {

                    for (doc in it) {

                        val contact = doc.toObject(User::class.java)
                        sender = contact.name!!
                    }

                }

        return sender
    }


}