package com.example.mybmicalculator.ui.users

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mybmicalculator.R
import com.example.mybmicalculator.data.Bmi
import com.example.mybmicalculator.data.BmiViewModel
import com.example.mybmicalculator.data.User
import com.example.mybmicalculator.data.UserViewModel
import com.example.mybmicalculator.ui.history.ListAdapter
import kotlinx.android.synthetic.main.bmi_row.view.*
import kotlinx.android.synthetic.main.bmi_row.view.bmi_delete
import kotlinx.android.synthetic.main.user_row.view.*

class UserAdapter: RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mBmiViewModel: BmiViewModel

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
    //For each row of the user_table, we create and display a user_row layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        )
    }

    //represents each row of user_table
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //associates the displayed element to the right row in the database
        val currentItem = userList[position]

        //fills the bmi_row component (firstName + lastName) for display in the recyclerView
        holder.itemView.first_name_txt.text = currentItem.firstName
        holder.itemView.last_name_txt.text = currentItem.lastName

        //when clicking on the trash icon, we call the method deleteUser
        //which deletes the user passed in parameter (and all of his BMIs),
        //corresponding to the row we want to delete in the database
        holder.itemView.user_delete.setOnClickListener(){
            mUserViewModel = ViewModelProvider(it.findFragment()).get(UserViewModel::class.java)
            mBmiViewModel = ViewModelProvider(it.findFragment()).get(BmiViewModel::class.java)
            deleteUser(currentItem, it.context)
        }

        //share the current item between the Users view and the calculator
        holder.itemView.userLayout.setOnClickListener(){
            val action = UsersFragmentDirections.actionUsersFragmentToFirstFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    //function to delete the clicked user
    private fun deleteUser(currentUser: User, context: Context){
        //opens a window to confirm the deletion
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes"){_,_ ->
            //access the view model to execute the deleteUser() method of the repository,
            // which will itself execute deleteUser() of the DAO and
            // which will generate the deletion of the selected user in the DB via an SQL query
            mBmiViewModel.deleteAllBmisByUser(currentUser.userId)
            mUserViewModel.deleteUser(currentUser)
            Toast.makeText(context, "User : ${currentUser.firstName} ${currentUser.lastName} successfully removed with all his/her BMIs!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->
            //nothing occurs
        }
        builder.setTitle("Delete the user : ${currentUser.firstName} ${currentUser.lastName} ?")
        builder.setMessage("Are you sure you want to delete the user : ${currentUser.firstName} ${currentUser.lastName}, and all his/her BMIs ?")
        builder.create().show()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(users: List<User>) {
        this.userList = users
        notifyDataSetChanged()
    }

}

