package com.example.mybmicalculator.ui.history

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybmicalculator.R
import com.example.mybmicalculator.data.BmiViewModel
import com.example.mybmicalculator.databinding.FragmentSecondBinding
import com.example.mybmicalculator.ui.calculator.FirstFragmentArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    //to retrieve the current user we have selected
    private val args by navArgs<SecondFragmentArgs>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mBmiViewModel: BmiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Add trash icon in the top navigation menu
        setHasOptionsMenu(true)

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        val adapter = ListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // BmiViewModel
        mBmiViewModel = ViewModelProvider(this).get(BmiViewModel::class.java)
        //display of all registered bmi by executing readAllData of the ViewModel,
        // which executes the readAllBmi function of the repository,
        // which itself calls the readAllBmi() method of the Dao,
        // which executes a SQL query on the DB to retrieve all rows
        mBmiViewModel.readBmiByUser(args.currentUserSecond.userId).observe(viewLifecycleOwner, Observer { bmi ->
            adapter.setData(bmi)
        })

        //when we click on the right floating action button, the calculator view is displayed
        view.findViewById<FloatingActionButton>(R.id.floatingActionButtonToCalculator).setOnClickListener {
            //share the current item between the history view and the calculator
            val action = SecondFragmentDirections.actionSecondFragmentToFirstFragment(args.currentUserSecond)
            findNavController().navigate(action)
        }

        //when we click on the left floating action button, the users view is displayed
        view.findViewById<FloatingActionButton>(R.id.floatingActionButtonToUsers).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_UsersFragment)
        }

    }

    //to make the trash icon appear on the view
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    //when clicking on the trash can icon, we delete all the saved BMIs
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteAllBmisByUser()
        }
        return super.onOptionsItemSelected(item)
    }

    //function that deletes all the bmis stored in the database
    private fun deleteAllBmisByUser() {
        //opens a window to confirm the deletion
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes"){_,_ ->
            //we access the viewModel to execute the deleteAllBmis() method of the repository,
            // which will itself execute deleteAllBmi() of the DAO and
            // will generate the deletion of all the bmis in the DB with an SQL query
            mBmiViewModel.deleteAllBmisByUser(args.currentUserSecond.userId)
            Toast.makeText(context, "All your BMIs have successfully been removed !", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->
            //nothing occurs
        }
        builder.setTitle("Delete all the BMIs of ${args.currentUserSecond.firstName} ${args.currentUserSecond.lastName} ?")
        builder.setMessage("Are you sure you want to delete all the BMIs of ${args.currentUserSecond.firstName} ${args.currentUserSecond.lastName} ?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}