package com.androidshowtime.whatsappclone

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidshowtime.whatsappclone.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.getInstance
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber


class LoginFragment : Fragment(), FirebaseAuth.AuthStateListener, FirebaseAuth.IdTokenListener {
    //vars
    private lateinit var auth: FirebaseAuth
    private lateinit var phoneNumber: String


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
        (activity as AppCompatActivity).actionBar?.title = "Welcome"

        //login Button implementation

        binding.loginButton.setOnClickListener {


            //check whether a user is already signed in from a previous session
            if (auth.currentUser != null) {
                //already signed in


                // navigate to ChatFragment
                findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToProfileFragment(phoneNumber)
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
                                "https://example.com"
                                                   )
                        .build()

                /*kick off the FirebaseUI sign in flow, call startActivityForResult()
                          on the sign in Intent you built */
                startForResult.launch(intent)
            }


        }

        return binding.root
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
    override fun onAuthStateChanged(auth: FirebaseAuth) {
        //current user is signed in

        //non-null check/
        auth.currentUser?.let { currentUser ->

            //initialize phone number from the current user
            phoneNumber = currentUser.phoneNumber.toString()

            /*use metadata to check if it is the first time user signs-in*/
            val metadata = currentUser.metadata!!

            //new user - last login equals creation timestamp
            if (metadata.creationTimestamp == metadata.lastSignInTimestamp) {
                //you can navigate to a welcome screen
                findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToProfileFragment(
                                phoneNumber))
            }
            //existing user
            else {
                //if user is existing, move on
                findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToChatFragment())



            }
        }





    }

    override fun onIdTokenChanged(auth: FirebaseAuth) {
        Timber.i("onIdToken Called")
        auth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            Timber.i("The Token from onIdToken is ${it.token}")

        }

    }

    //Xindy has the cutest smile, hajiskii sukari making her so easy to like.

    //Ako Left-Sided n like other Lefties she is amazing n full of brilliant ideas

    // Cindy is a slice of heaven to me


}