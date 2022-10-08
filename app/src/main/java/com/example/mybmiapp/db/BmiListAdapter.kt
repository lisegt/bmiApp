package com.example.mybmiapp.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mybmiapp.R

class BmiListAdapter : ListAdapter<Bmi, BmiListAdapter.BmiHolder>(DiffCallback()) {

    class BmiHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var listener: RecyclerClickListener
    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BmiHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_history, parent, false)
        val bmiHolder = BmiHolder(v)

        val bmiDelete = bmiHolder.itemView.findViewById<ImageView>(R.id.bmi_delete)
        bmiDelete.setOnClickListener {
            listener.onItemRemoveClick(bmiHolder.adapterPosition)
        }

        return bmiHolder
    }

    override fun onBindViewHolder(holder: BmiHolder, position: Int) {
        val currentItem = getItem(position)
        val bmiValue = holder.itemView.findViewById<TextView>(R.id.bmi_record)
        bmiValue.text = currentItem.bmiValue
    }

    class DiffCallback : DiffUtil.ItemCallback<Bmi>() {
        override fun areItemsTheSame(oldItem: Bmi, newItem: Bmi) =
            oldItem.dateAdded == newItem.dateAdded

        override fun areContentsTheSame(oldItem: Bmi, newItem: Bmi) =
            oldItem == newItem
    }
}