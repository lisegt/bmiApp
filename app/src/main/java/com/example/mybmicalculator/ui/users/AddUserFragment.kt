package com.example.mybmicalculator.ui.users

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mybmicalculator.R
import com.example.mybmicalculator.data.User
import com.example.mybmicalculator.data.UserViewModel
import kotlinx.android.synthetic.main.fragment_add_user.*
import kotlinx.android.synthetic.main.fragment_add_user.view.*

class AddUserFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_user, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.add_btn.setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val firstName = addFirstName_txt.text.toString()
        val lastName = addLastName_txt.text.toString()
        val height = addHeight_txt.text.toString()

        if (inputCheck(firstName, lastName, height)) {
            // Create User Object
            val user = User(0, firstName, lastName, height.toDouble())
            // Add Data to Database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "${firstName} ${lastName} successfully added !", Toast.LENGTH_LONG).show()
            // Navigate back
            findNavController().navigate(R.id.action_addUserFragment_to_UsersFragment)
        }
    }

    //function that checks the validity of user inputs
    private fun inputCheck(firstName: String, lastName: String, height: String) : Boolean{

        return when {
            //if the user has not entered his height or weight, a toast is displayed
            firstName.isEmpty() -> {
                Toast.makeText(context, getString(R.string.first_name_empty), Toast.LENGTH_SHORT).show()
                return false
            }
            lastName.isEmpty() -> {
                Toast.makeText(context, getString(R.string.last_name_empty), Toast.LENGTH_SHORT).show()
                return false
            }

            height.isEmpty() -> {
                Toast.makeText(context, getString(R.string.height_empty), Toast.LENGTH_SHORT).show()
                return false
            }
            else -> {
                return true
            }
        }
    }


}