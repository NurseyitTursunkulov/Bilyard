package com.example.sonyadmin.login


import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.sonyadmin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var viewModel: LoginViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private val TAG: String = LoginFragment::class.java.simpleName

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var currentUser = auth.currentUser
        if (currentUser != null) {
//            if (currentUser.isEmailVerified) {
            Navigation.findNavController(next_button).navigate(R.id.action_loginFragment_to_listOfGamesFragment)
            Log.d(TAG, "createUserWithEmail:success ${auth.currentUser?.displayName}")
//            }
        }
        // Set an error if the password is less than 8 characters.
        next_button.setOnClickListener { myView ->
            if (!isPasswordValid(password_edit_text.text)) {
                password_text_input.error = getString(R.string.shr_error_password)
            } else {
                password_text_input.error = null // Clear the error

                activity?.let {
                    auth.signInWithEmailAndPassword(
                        user_name_edit_text.text.toString(),
                        password_edit_text.text.toString()
                    )
                        .addOnCompleteListener(it) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                currentUser = auth.currentUser
                                Navigation.findNavController(myView)
                                    .navigate(R.id.action_loginFragment_to_listOfGamesFragment)
//                        updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    context, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
//                        updateUI(null)
                            }

                            // ...
                        }
                }

//                Navigation.findNavController(next_button).navigate(R.id.action_loginFragment_to_listOfGamesFragment)
            }
        }

        // Clear the error once more than 8 characters are typed.
        password_edit_text.setOnKeyListener { _, _, _ ->
            if (isPasswordValid(password_edit_text.text)) {
                password_text_input.error = null //Clear the error
            }
            false
        }
//        next_button.setOnClickListener { view ->


//        }

        Log.d(LoginFragment::class.java.simpleName, "$currentUser")

//        button2.setOnClickListener {
//            activity?.let { it1 ->
//                auth.createUserWithEmailAndPassword("nurs@mail.ru", "12345678")
//                    .addOnCompleteListener(it1) { task ->
//                        if (task.isSuccessful) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success")
//                            Navigation.findNavController(button2).navigate(R.id.action_loginFragment_to_listOfGamesFragment)
//                            currentUser = auth.currentUser
////                            updateUI(user)
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                            Toast.makeText(
//                                context, "Authentication failed.",
//                                Toast.LENGTH_SHORT
//                            ).show()
////                            updateUI(null)
//                        }
//
//                        // ...
//                    }
//            }
//        }
//        }
    }

    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 8
    }


}
