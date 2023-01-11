package com.example.mybmicalculator.ui.users

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybmicalculator.R
import com.example.mybmicalculator.data.BmiViewModel
import com.example.mybmicalculator.data.UserViewModel
import kotlinx.android.synthetic.main.fragment_second.view.*
import kotlinx.android.synthetic.main.fragment_users.view.*

class UsersFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mBmiViewModel: BmiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        // RecyclerView
        val adapter = UserAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_users)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mUserViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            adapter.setData(user)
        })

        //BmiViewModel
        mBmiViewModel = ViewModelProvider(this).get(BmiViewModel::class.java)


        view.floatingActionButtonToAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_UsersFragment_to_addUserFragment)
        }

        //Add trash icon in the top navigation menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //to make the trash icon appear on the view
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    //when clicking on the trash can icon, we delete all the saved users
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteAllUsers()
        }
        return super.onOptionsItemSelected(item)
    }

    //function that deletes all the users stored in the database
    private fun deleteAllUsers() {
        //opens a window to confirm the deletion
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes"){_,_ ->
            //we access the viewModel to execute the deleteAllUsers() method of the repository,
            // which will itself execute deleteAllUsers() of the DAO and
            // will generate the deletion of all the users in the DB with an SQL query
            mBmiViewModel.deleteAllBmis()
            mUserViewModel.deleteAllUsers()
            Toast.makeText(context, "All users have successfully been removed !", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->
            //nothing occurs
        }
        builder.setTitle("Delete all the users ?")
        builder.setMessage("Are you sure you want to delete all the users ?")
        builder.create().show()
    }
}