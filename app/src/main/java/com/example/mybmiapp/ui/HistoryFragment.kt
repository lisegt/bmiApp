package com.example.mybmiapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybmiapp.R
import com.example.mybmiapp.databinding.FragmentCalculatorBinding
import com.example.mybmiapp.databinding.FragmentHistoryBinding
import com.example.mybmiapp.db.BmiListAdapter

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ajout du recyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = BmiListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}