package com.example.mybmiapp.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mybmiapp.R

class BmiListAdapter : ListAdapter<Bmi, BmiListAdapter.BmiViewHolder>(BmisComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BmiViewHolder {
        return BmiViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BmiViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.bmiValue)
    }

    //pour lier un texte à un TextView
    class BmiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bmiItemView: TextView = itemView.findViewById(R.id.bmi_record)

        fun bind(text: String?) {
            bmiItemView.text = text
        }

        companion object {
            //gonfle la mise en page
            fun create(parent: ViewGroup): BmiViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return BmiViewHolder(view)
            }
        }
    }

    class BmisComparator : DiffUtil.ItemCallback<Bmi>() {
        override fun areItemsTheSame(oldItem: Bmi, newItem: Bmi): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Bmi, newItem: Bmi): Boolean {
            return oldItem.bmiValue == newItem.bmiValue
        }
    }
}
