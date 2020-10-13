package com.androidshowtime.whatsappclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androidshowtime.whatsappclone.databinding.FragmentProfileBinding
import com.androidshowtime.whatsappclone.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber


class ProfileFragment : Fragment() {
    //vals
    private val args: ProfileFragmentArgs by navArgs()

    //vars
    private lateinit var name: String
    private lateinit var phoneNumber: String
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //initialize firestore and auth
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        //create binding
        val binding = FragmentProfileBinding.inflate(inflater)

        //set title
        (activity as AppCompatActivity).supportActionBar?.title = "My Profile"
//get phone number form Navigation Arguments
        phoneNumber = args.phoneNumber
        //set phone number
        binding.phoneNumberTexView.text = phoneNumber

        //on OK button click

        binding.okayButton.setOnClickListener {
            //get value of the entered name
            if (binding.editTextTextPersonName.text.isNotBlank()) {

                name = binding.editTextTextPersonName.text.toString()

                //save user's details to firestore
                saveUserDetails(phoneNumber, name)

                //update user's profile
                updateProfileDetails(name)

                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChatFragment())

            } else {


                Snackbar.make(binding.root, "Please Enter your Name", Snackbar.LENGTH_SHORT).show()
            }


        }

        // return root
        return binding.root
    }

    //save user's details in Cloud Firestore
    private fun saveUserDetails(phoneNumber: String, name: String) {
        //create a new user profile
        val user = User(phoneNumber, name)

        //create a firestore reference and save contact into firestore
        val ref = firestore.collection("Contacts")
            .document(name)
            .set(user)
            .addOnSuccessListener {

            }


    }


    private fun updateProfileDetails(name: String) {

        val user = auth.currentUser

        //you can update a user's basic profile info with updateProfile()
        val profileUpdates = userProfileChangeRequest {


            displayName = name

        }

        user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.i("Profile Updated")
            }
        }

    }


}