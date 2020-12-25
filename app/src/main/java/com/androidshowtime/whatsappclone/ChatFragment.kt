package com.androidshowtime.whatsappclone

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidshowtime.whatsappclone.databinding.FragmentChatBinding
import com.androidshowtime.whatsappclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ChatFragment : Fragment() {

    //vars
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth:FirebaseAuth


//I have two lists - one for names and another for users
    private lateinit var contactsList: MutableList<User>
    private lateinit var contactsNames: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {

        //val view = inflater.inflate(R.layout.recycler_view_layout,container,false)
        val binding = FragmentChatBinding.inflate(inflater)

        //show menu
        setHasOptionsMenu(true)

        //initialize firestore and auth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

//set actionBar's title
        (activity as AppCompatActivity).supportActionBar?.title = "Chats"
        //initialize lists
        contactsList = mutableListOf()
        contactsNames = mutableListOf()
        //obtain contacts and add them to the lists
        getAllContacts()
        //initialize adapter

        adapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, contactsNames)

        //populate listView
        binding.listView.adapter = adapter

        //implement listView onClick
        binding.listView.setOnItemClickListener { _, _, i, _ ->


           /*when a name is clicked on the ListView at a specific position the app navigates
            to the DisplayMessage Fragment taking a UserObject corresponding to the
             clicked contact.*/
            findNavController().navigate(ChatFragmentDirections
                                             .actionChatFragmentToMessageFragment(contactsList[i]))

        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            //Log out
            R.id.log_out -> {
                FirebaseAuth.getInstance().signOut()

                //navigate back to login fragment
                findNavController().navigate(ChatFragmentDirections.actionChatFragmentToLoginFragment())
                true
            }


            else -> return super.onOptionsItemSelected(item)
        }


    }


    // method for obtaining contacts and adding them to the lists
    private fun getAllContacts() {
        val currentUserPhoneCredential = auth.currentUser?.phoneNumber!!

        firestore.collection("Contacts")
            .whereNotEqualTo("phoneNumber", currentUserPhoneCredential)
            .get().addOnSuccessListener { querySnapshot ->
            for (doc in querySnapshot) {

                val user = doc.toObject(User::class.java)
     //I have 2 lists one with just the names and another with the User Object
                contactsList.add(user)
                contactsNames.add(user.name!!)

            }
            adapter.notifyDataSetChanged()
        }

    }


}