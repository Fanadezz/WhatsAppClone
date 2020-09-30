package com.androidshowtime.whatsappclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.androidshowtime.whatsappclone.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {
        //initialize auth
        auth = FirebaseAuth.getInstance()

        //create binding
        val binding = FragmentLoginBinding.inflate(inflater)


        //check whether a user is already signed in from a previous session
        if (auth.currentUser != null) {
            //already signed in

        }
        else {

            //not signed in
        }


        return binding.root
    }


    //start signin proces

    fun signin() {


        //choose Authentication providers
        val providers = arrayListOf(
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build())


        //create and launch sign-in intent
        ActivityResultContracts.StartActivityForResult(AuthUI.getInstance()
                                                               .createSignInIntentBuilder()
                                                               .setAvailableProviders(providers)
                                                               .build(), 12)


    }


}