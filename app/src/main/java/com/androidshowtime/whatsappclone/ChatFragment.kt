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
import com.google.firebase.ktx.Firebase


class ChatFragment : Fragment() {

    //vars
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth:FirebaseAuth



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

        //implement list onClick
        binding.listView.setOnItemClickListener { _, _, i, _ ->

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

    private fun getAllContacts() {
        val currentUserPhoneCredential = auth.currentUser?.phoneNumber!!

        firestore.collection("Contacts")
            .whereNotEqualTo("phoneNumber", currentUserPhoneCredential)
            .get().addOnSuccessListener { querySnapshot ->
            for (doc in querySnapshot) {

                val user = doc.toObject(User::class.java)

                contactsList.add(user)
                contactsNames.add(user.name!!)

            }
            adapter.notifyDataSetChanged()
        }

    }


}