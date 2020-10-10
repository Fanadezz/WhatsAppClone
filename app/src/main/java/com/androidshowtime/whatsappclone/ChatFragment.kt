package com.androidshowtime.whatsappclone

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidshowtime.whatsappclone.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ChatFragment : Fragment() {

    //vars
    private lateinit var firestore: FirebaseFirestore
    private lateinit var contactsList: MutableList<User>
    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        val binding = FragmentChatBinding.inflate(inflater)

        //show menu
        setHasOptionsMenu(true)

        //initialize firestore

        firestore = FirebaseFirestore.getInstance()

        //initialize list
        contactsList = mutableListOf()

        //initialize adapter

        adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, contactsList)

        //populate listView

        binding.listView.adapter = adapter
getAllDocs()
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

    private fun getAllDocs() {

        firestore.collection("Contacts").get().addOnSuccessListener { querySnapshot ->
            for (doc in querySnapshot) {


                

                /*Timber.i("The doc is ${doc.id}")
                contactsList.add(doc.id)*/

            }
            adapter.notifyDataSetChanged()
        }

    }


}