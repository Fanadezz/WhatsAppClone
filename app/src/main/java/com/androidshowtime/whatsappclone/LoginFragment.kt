package com.androidshowtime.whatsappclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidshowtime.whatsappclone.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //create binding
        val binding = FragmentLoginBinding.inflate(inflater)


        //check whether a user is already signed in from a previous session

        if (auth.currentUser != null) {


            //already signed in
        } else {

            //not signed in
        }


        return binding.root
    }


}