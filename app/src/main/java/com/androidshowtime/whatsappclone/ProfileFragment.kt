package com.androidshowtime.whatsappclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androidshowtime.whatsappclone.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
//vals
    private val args:ProfileFragmentArgs by navArgs()

    //vars
    private lateinit var name:String

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

        //on OK button click

        binding.okayButton.setOnClickListener {
            //get value of the entered name
            if (binding.editTextTextPersonName.text.isNotBlank()){

                name = binding.editTextTextPersonName.text.toString()


            }

            else{



                Snackbar.make(binding.root, "Please Enter your Name", Snackbar.LENGTH_SHORT).show()
            }

            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChatFragment())
        }

        // return root
        return binding.root
    }


}