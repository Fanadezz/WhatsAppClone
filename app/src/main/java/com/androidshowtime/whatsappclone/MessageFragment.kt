package com.androidshowtime.whatsappclone

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androidshowtime.whatsappclone.databinding.FragmentMessageBinding
import com.androidshowtime.whatsappclone.model.Message
import com.androidshowtime.whatsappclone.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_message.*
import timber.log.Timber
import java.util.*


class MessageFragment : Fragment(), TextWatcher {
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
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //retrieve user from Nav Args
        user = args.user

        //initialize auth and firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        //initialize binding
        binding = FragmentMessageBinding.inflate(inflater)
        //initialize message thread list
        messageThreadList = mutableListOf()
        //find messages
        findChats()

        //set actionBar's title
        (activity as AppCompatActivity).supportActionBar?.title = user.name


        //instantiate the RecyclerViewAdapter and pass in the messages list
        adapter = RecyclerViewAdapter(messageThreadList)

        //set the RecyclerViewAdapter to the recyclerView
        binding.recyclerView.adapter = adapter


        Timber.i("inside onCreate and Scroll position is ${messageThreadList.size - 1}")
        //enable sendButtonClick when enter button is hit
        binding.msgBoxEdittext.setOnKeyListener { v, keyCode, event ->

            //use when-expression
            when {
                //check if it is the Enter-Key, Check if Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {

                    //perform an action here e.g. a send message button click
                    binding.sendButton.performClick()
                    binding.msgBoxEdittext.requestFocus()

                    return@setOnKeyListener true
                }

                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_UP)) -> {

                    //request for focus when enter button is released
                    binding.msgBoxEdittext.requestFocus()
                    return@setOnKeyListener true

                }

                //else brance
                else -> false
            }

        }

        //show cursor on the edittext when texts are loaded
        binding.msgBoxEdittext.requestFocus()
        binding.msgBoxEdittext.addTextChangedListener(this)
        //send button implementation
        binding.sendButton.setOnClickListener {

            sendMessage()

            //make recycler view scroll to show the bottommost text
            binding.recyclerView.scrollToPosition(messageThreadList.size - 1)
            Timber.i("inside onClick and Scroll position is ${messageThreadList.size - 1}")
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
                    binding.root, resources.getString(R.string.empty_message), Snackbar.LENGTH_SHORT).show()


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
        firestore.collection("Messages")
                .add(message)
                .addOnSuccessListener { Timber.i("Message sent") }
                .addOnFailureListener { Timber.i("it") }

        messageThreadList.add(message)
        adapter.notifyDataSetChanged()

    }


    private fun findChats() {
        val currentUser = auth.currentUser?.displayName!!
        val chatMate = args.user
        val msgRef = firestore.collection("Messages")

        msgRef.whereEqualTo("from", chatMate.name).whereEqualTo("to", currentUser)


        msgRef.get().addOnSuccessListener {

            querySnapshot ->
            for (doc in querySnapshot) {


                val message = doc.toObject(Message::class.java)
                messageThreadList.add(message)
                //make recycler view scroll to show the last text message
                binding.recyclerView.scrollToPosition(messageThreadList.size - 1)
                adapter.notifyDataSetChanged()
                Timber.i("The message are $messageThreadList")
            }


        }.addOnFailureListener {

            Timber.i("Error - $it")
        }


    }


    

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //change button color when text is added

        binding.sendButton.background = Color.CYAN.toDrawable()
    }

    override fun afterTextChanged(s: Editable?) {

    }




}