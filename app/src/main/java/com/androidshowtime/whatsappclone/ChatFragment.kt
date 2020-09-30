package com.androidshowtime.whatsappclone

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidshowtime.whatsappclone.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth


class ChatFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {
        val binding = FragmentChatBinding.inflate(inflater)

        //show menu
        setHasOptionsMenu(true)

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


}