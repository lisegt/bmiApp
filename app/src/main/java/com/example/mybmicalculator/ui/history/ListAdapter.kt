package com.example.mybmicalculator.ui.history

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mybmicalculator.R
import com.example.mybmicalculator.data.Bmi
import com.example.mybmicalculator.data.BmiViewModel
import kotlinx.android.synthetic.main.bmi_row.view.*
import org.w3c.dom.Text


class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var bmiList = emptyList<Bmi>()
    private lateinit var mBmiViewModel: BmiViewModel

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
    //For each row of the bmi_table, we create and display a bmi_row layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bmi_row,parent,false))
    }

    //represents each row of bmi_table
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //associates the displayed element to the right row in the database
        val currentItem = bmiList[position]

        //fills the bmi_row component (height + weight + bmiValue + dateAdded) for display in the recyclerView
        holder.itemView.findViewById<TextView>(R.id.height_txt).text = (currentItem.heightValue + " cm")
        holder.itemView.findViewById<TextView>(R.id.weight_txt).text = (currentItem.weightValue + " kg")
        holder.itemView.findViewById<TextView>(R.id.bmiValue_txt).text = currentItem.bmiValue
        holder.itemView.findViewById<TextView>(R.id.dateAdd_txt).text = currentItem.dateAdded


        //setting the background color in the history
        val backgroundCardView = holder.itemView.findViewById<CardView>(R.id.bmi_row)
        when (currentItem.bmiValue.toDouble()) {
            in 16.0..16.9 -> { backgroundCardView.setCardBackgroundColor(Color.rgb(67,163,219)) }
            in 17.0..18.4 -> { backgroundCardView.setCardBackgroundColor(Color.rgb(98,176,179)) }
            in 18.5..24.9 -> { backgroundCardView.setCardBackgroundColor(Color.rgb(161,199,64)) }
            in 25.0..29.9 -> {
                backgroundCardView.setCardBackgroundColor(Color.rgb(254,229,82))
                holder.itemView.findViewById<TextView>(R.id.dateAdd_txt).setTextColor(Color.BLACK)
                holder.itemView.findViewById<TextView>(R.id.height_txt).setTextColor(Color.BLACK)
                holder.itemView.findViewById<TextView>(R.id.weight_txt).setTextColor(Color.BLACK)
                holder.itemView.findViewById<TextView>(R.id.bmiValue_txt).setTextColor(Color.BLACK)
                holder.itemView.findViewById<ImageView>(R.id.bmi_delete).setImageResource(R.drawable.ic_baseline_delete_outline_24)
                }
            in 30.0..34.9 -> {
                backgroundCardView.setCardBackgroundColor(Color.rgb(236,120,29))
                holder.itemView.findViewById<TextView>(R.id.dateAdd_txt).setTextColor(Color.WHITE)
                holder.itemView.findViewById<TextView>(R.id.height_txt).setTextColor(Color.WHITE)
                holder.itemView.findViewById<TextView>(R.id.weight_txt).setTextColor(Color.WHITE)
                holder.itemView.findViewById<TextView>(R.id.bmiValue_txt).setTextColor(Color.WHITE)
                holder.itemView.findViewById<ImageView>(R.id.bmi_delete).setImageResource(R.drawable.ic_delete)
                }
            in 35.0..39.9 -> { backgroundCardView.setCardBackgroundColor(Color.rgb(114,0,25)) }
            else -> {
                if (currentItem.bmiValue.toDouble() < 16.0){
                    backgroundCardView.setCardBackgroundColor(Color.rgb(35,98,176))
                } else {
                    backgroundCardView.setCardBackgroundColor(Color.rgb(79,0,2))
                    holder.itemView.findViewById<TextView>(R.id.dateAdd_txt).setTextColor(Color.WHITE)
                    holder.itemView.findViewById<TextView>(R.id.height_txt).setTextColor(Color.WHITE)
                    holder.itemView.findViewById<TextView>(R.id.weight_txt).setTextColor(Color.WHITE)
                    holder.itemView.findViewById<TextView>(R.id.bmiValue_txt).setTextColor(Color.WHITE)
                    holder.itemView.findViewById<ImageView>(R.id.bmi_delete).setImageResource(R.drawable.ic_delete)
                }
            }
        }

        //when clicking on the trash icon, we call the method deleteBmi
        //which deletes the bmi passed in parameter,
        //corresponding to the row we want to delete in the database
        holder.itemView.bmi_delete.setOnClickListener(){
            mBmiViewModel = ViewModelProvider(it.findFragment()).get(BmiViewModel::class.java)
            deleteBmi(currentItem, it.context)
        }
    }

    //function to delete the clicked bmi
    private fun deleteBmi(currentBmi: Bmi, context: Context){
        //opens a window to confirm the deletion
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes"){_,_ ->
            //access the view model to execute the deleteBmi() method of the repository,
            // which will itself execute deleteBmi() of the DAO and
            // which will generate the deletion of the selected bmi in the DB via an SQL query
            mBmiViewModel.deleteBmi(currentBmi)
            Toast.makeText(context, "BMI successfully removed !", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->
            //nothing occurs
        }
        builder.setTitle("Delete the BMI : ${currentBmi.bmiValue} ?")
        builder.setMessage("Are you sure you want to delete the BMI : ${currentBmi.bmiValue}, " +
                "saved on ${currentBmi.dateAdded} ?")
        builder.create().show()
    }

    override fun getItemCount(): Int {
        return bmiList.size
    }

    fun setData(bmis: List<Bmi>) {
        this.bmiList = bmis
        notifyDataSetChanged()
    }
}

