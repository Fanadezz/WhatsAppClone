package com.androidshowtime.whatsappclone

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.androidshowtime.whatsappclone.databinding.FragmentMessageBinding
import com.androidshowtime.whatsappclone.model.Message
import com.androidshowtime.whatsappclone.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //retrieve user from Nav Args
        user = args.user

        //initialize auth and firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //initialize binding
        binding = FragmentMessageBinding.inflate(inflater)

        //initialize message thread list
        messageThreadList = mutableListOf()


        //set actionBar's title
        (activity as AppCompatActivity).supportActionBar?.title = user.name


        //instantiate the RecyclerViewAdapter and pass in the messages list
        adapter = RecyclerViewAdapter(messageThreadList)
        //find messages
        findChats()
        //set the RecyclerViewAdapter to the recyclerView
        binding.recyclerView.adapter = adapter


        //enable sendButtonClick when enter button is hit
        binding.msgBoxEdittext.setOnKeyListener { v, keyCode, event ->

            //use when-expression
            when {
                //check if it is the Enter-Key, Check if Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {

                    //trigger sendButtonClick Programatically
                    binding.sendButton.performClick()

                    //Focus on Edittext
                    binding.msgBoxEdittext.requestFocus()

                    return@setOnKeyListener true
                }

                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_UP)) -> {

                    //request for focus when enter button is released
                    binding.msgBoxEdittext.requestFocus()
                    return@setOnKeyListener true

                }

                //else branch
                else -> false
            }

        }

        //show cursor on the edittext when texts are loaded
        binding.msgBoxEdittext.requestFocus()
        binding.msgBoxEdittext.addTextChangedListener(this)
        //send button implementation
        binding.sendButton.setOnClickListener {
            //sending the message
            sendMessage()

            //make recycler view scroll to show the bottommost text
            binding.recyclerView.scrollToPosition(messageThreadList.size - 1)

            //clear editext
            binding.msgBoxEdittext.text.clear()


        }


        //menu = true
        setHasOptionsMenu(true)
        //return the View Object
        return binding.root
    }

    //Read from the Chat Box
    private fun readMessageBox(): String {

        //read Edittext value
        return binding.msgBoxEdittext.text.toString()
    }


    //create message
    private fun createMessage(): Message {

        //create Message Object passing in the current user, active chat and the message
        val from = auth.currentUser?.displayName!!
        val to = user.name!!
        val messageString = readMessageBox()

        val message = Message(from, to, messageString, Date())


        return message

    }


    private fun sendMessage() {

        val message = createMessage()

        //check if the message is empty
        val messageString = message.messageString
        if (messageString == "") {


            Snackbar.make(binding.root, resources.getString(R.string.empty_message), Snackbar.LENGTH_SHORT).show()


        }
        else {
            firestore.collection("Messages")
                    .add(message)
                    .addOnSuccessListener { Timber.i("Message sent") }
                    .addOnFailureListener { Timber.i("it") }

            messageThreadList.add(message)
            adapter.notifyDataSetChanged()
        }

    }


    private fun findChats() {
        val currentUser = auth.currentUser?.displayName!!
        val chatMate = args.user
        Timber.i("The guy is: $chatMate")
        val msgRef1 = firestore.collection("Messages")

        msgRef1.whereEqualTo("from", chatMate.name).whereEqualTo("to", currentUser)

                .get().addOnSuccessListener { querySnapshot ->

                    //iterate to find massages documents from cloud firestore
                    for (doc in querySnapshot) {
                        //convert the document to a Message Custom Object
                        val message = doc.toObject(Message::class.java)
                        messageThreadList.add(message)


                    }
                    // adapter.notifyDataSetChanged()

                }.addOnFailureListener { Timber.i("Error on msgRef1 - $it") }


        val msgRef2 = firestore.collection("Messages")

        msgRef2.whereEqualTo("from", currentUser).whereEqualTo("to", chatMate.name)
                //.orderBy("timestamp", Query.Direction.ASCENDING)
                .get().addOnSuccessListener { querySnapshot ->
                    //iterate to find massages documents from cloud firestore
                    for (doc in querySnapshot) {

                        //convert the document to a Message Custom Object
                        val message = doc.toObject(Message::class.java)
                        messageThreadList.add(message)

                    }


                    //make recycler view scroll to show the last text message
                    binding.recyclerView.scrollToPosition(messageThreadList.size - 1)


                    //sort messages by date
                    sortMessagesByDate(messageThreadList)
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener { Timber.i("Error on msgRef2 - $it") }


    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //change button color when text is added to signify message is ready for sending
        binding.sendButton.background = Color.CYAN.toDrawable()
        binding.sendButton.scaleX = 0.8f
        binding.sendButton.scaleY = 0.8f

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}


    //sort method - takes a list to be sorted
    private fun sortMessagesByDate(messages: MutableList<Message>) {

        //sort messages by Date
        messages.sortWith(compareBy { it.timestamp })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.msg_overflow, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,findNavController())
    }

}