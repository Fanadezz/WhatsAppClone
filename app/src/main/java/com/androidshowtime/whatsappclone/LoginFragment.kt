package com.androidshowtime.whatsappclone

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidshowtime.whatsappclone.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.getInstance
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment(), FirebaseAuth.AuthStateListener {
    //vars
    private lateinit var auth: FirebaseAuth


    private val startForResult =
            registerForActivityResult(
                    ActivityResultContracts
                            .StartActivityForResult()
                                     ) { result: ActivityResult ->


                if (result.resultCode == Activity.RESULT_OK) {
                    // Handle the Intent


                }


            }

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {
        //initialize auth
        auth = FirebaseAuth.getInstance()

        //create binding
        binding = FragmentLoginBinding.inflate(inflater)


        //login Button implementation

        binding.loginButton.setOnClickListener {
            //check whether a user is already signed in from a previous session
            if (auth.currentUser != null) {
                //already signed in
                // navigate to second activity/fragment
                findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToChatFragment()
                                            )

            }

            //not signed in
            else {


//set up default number
                val phoneConfigWithDefaultNumber =
                        IdpConfig.PhoneBuilder()
                                .setDefaultNumber("+2547234458")
                                .build()


                val providers = arrayListOf(phoneConfigWithDefaultNumber)

                val intent = getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com",
                                "https://example.com")
                        .build()

                /*kick off the FirebaseUI sign in flow, call startActivityForResult()
                          on the sign in Intent you built */
                startForResult.launch(intent)
            }


            /* val whitelistedCountries = mutableListOf<String>()
                whitelistedCountries.add("+254")



                val phoneConfigWithWhitelistedCountries = PhoneBuilder()
                        .setWhitelistedCountries(whitelistedCountries)
                        .build()


                //val providers = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())
                val providers = arrayListOf(phoneConfigWithWhitelistedCountries)
*/

            /* .setAvailableProviders(providers)
                                    .setIsSmartLockEnabled(true)
                                    .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                                    .setAlwaysShowSignInMethodScreen(true)*/


        }


        return binding.root
    }


    //start signin proces

    fun signin() {


        //choose Authentication providers
        val providers = arrayListOf(
                IdpConfig.PhoneBuilder().build(),
                IdpConfig.EmailBuilder().build(),
                IdpConfig.TwitterBuilder().build()
                                   )


        /*//create and launch sign-in intent
        ActivityResultContracts.StartActivityForResult(AuthUI.getInstance()
                                                               .createSignInIntentBuilder()
                                                               .setAvailableProviders(providers)
                                                               .build(), 12)*/


    }


    override fun onStop() {
        super.onStop()

        /*FirebaseUI performs auth operations internally which may trigger the
        listener before the flow is complete.*/

        //unregister AuthStateListener before launching the FirebaseUI flow
        auth.removeAuthStateListener(this)
    }

    override fun onStart() {
        super.onStart()

        //register AuthStateListener after the flow returns.
        auth.addAuthStateListener(this)
    }

    //FirebaseAuth.AuthStateListener
    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        //user current signed in
        if (firebaseAuth.currentUser != null) {

            // navigate to second activity/fragment
            findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToChatFragment()
                                        )
        }
        //If user is not signed in then start sign-in process here
        else {


        }

    }


    /* //navigate to second activity/fragment
                findNavController().navigate(LoginFragmentDirections.
                actionLoginFragmentToChatFragment())
    */


    /*   firebaseAuth.currentUser!!.getIdToken(true).addOnSuccessListener {

                Timber.i("The token is: ${it.token}")
            }.addOnFailureListener {
                Timber.i("Error - Could not get the token $it")

            }*/

}