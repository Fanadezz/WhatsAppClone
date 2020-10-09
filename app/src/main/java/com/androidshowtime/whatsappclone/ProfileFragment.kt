package com.androidshowtime.whatsappclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.androidshowtime.whatsappclone.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
//vals
    private val args:ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //create binding
        val binding = FragmentProfileBinding.inflate(inflater)

        //set title
        (activity as AppCompatActivity).supportActionBar?.title = "My Profile"

        //set phone number
        binding.phoneNumberTexView.text = args.phoneNumber

        // return root
        return binding.root
    }


}