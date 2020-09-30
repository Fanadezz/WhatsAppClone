package com.androidshowtime.whatsappclone

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.androidshowtime.whatsappclone.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.PhoneBuilder
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment(),FirebaseAuth.AuthStateListener {

    private lateinit var auth: FirebaseAuth

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->





        if (result.resultCode == Activity.RESULT_OK) {
           // val intent = result.intent
            // Handle the Intent




        }





    }


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

        //login Button implementation

        binding.loginButton.setOnClickListener {

            val whitelistedCountries: MutableList<String> = ArrayList()
            whitelistedCountries.add("+254")


            val phoneConfigWithWhitelistedCountries = PhoneBuilder()
                    .setWhitelistedCountries(whitelistedCountries)
                    .build()




            //val providers = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())
            val providers = arrayListOf(phoneConfigWithWhitelistedCountries)



            val intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                    .setAlwaysShowSignInMethodScreen(true)
                    .build()

            startForResult.launch(intent)



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


        /*//create and launch sign-in intent
        ActivityResultContracts.StartActivityForResult(AuthUI.getInstance()
                                                               .createSignInIntentBuilder()
                                                               .setAvailableProviders(providers)
                                                               .build(), 12)*/


    }


    override fun onStart() {
        super.onStart()

        auth.addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()

        auth.removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        TODO("Not yet implemented")
    }


}