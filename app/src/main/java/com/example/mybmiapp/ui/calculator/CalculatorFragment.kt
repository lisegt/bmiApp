package com.example.mybmiapp.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mybmiapp.R
import com.example.mybmiapp.databinding.FragmentCalculatorBinding
import kotlin.math.pow
import kotlin.math.roundToInt

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calculatorViewModel =
            ViewModelProvider(this).get(CalculatorViewModel::class.java)

        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCalculator
        calculatorViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message_welcome = view.findViewById<TextView>(R.id.text_calculator)
        val weight = view.findViewById<EditText>(R.id.weight_input)
        val height = view.findViewById<EditText>(R.id.height_input)

        val calculateButton = view.findViewById<Button>(R.id.button_calculate)
        val bmi = view.findViewById<TextView>(R.id.bmi_result)
        val recalculateButton = view.findViewById<Button>(R.id.button_recalculate)

        //quand on arrive sur la page certains éléments ne sont pas visibles
        recalculateButton.visibility = View.GONE
        bmi.visibility = View.GONE

        calculateButton.setOnClickListener(){
            var weightUser = 0.0
            var heightUser = 0.0

            //si l'utilisateur n'a pas rentré sa taille, on affiche un toast
            if ((height.text.toString().isEmpty())){
                val toastHeight = Toast.makeText(context, getString(R.string.height_empty), Toast.LENGTH_SHORT)
                toastHeight.show()
            } else {
                //on convertit la taille en mètres
                heightUser = height.text.toString().toDouble() / 100
            }

            //si l'utilisateur n'a pas rentré son poids, on affiche un toast
            if (weight.text.toString().isEmpty()){
                val toastWeight = Toast.makeText(context, getString(R.string.weight_empty), Toast.LENGTH_SHORT)
                toastWeight.show()
            } else {
                weightUser = weight.text.toString().toDouble()
            }

            if (weightUser > 0.0 && heightUser > 0.0){
                //on arrondit le résultat à 1 décimale
                var bmiUser = (weightUser / heightUser.pow(2) * 10.0).roundToInt() / 10.0
                bmi.text = bmiUser.toString()
                message_welcome.text = getString(R.string.bmi_calculated_message)
                bmi.visibility = View.VISIBLE
                recalculateButton.visibility = View.VISIBLE
                calculateButton.visibility = View.GONE
            }
        }

        recalculateButton.setOnClickListener(){

            message_welcome.text = getString(R.string.welcome)
            //on vide les champs d'input
            height.text.clear()
            weight.text.clear()

            calculateButton.visibility = View.VISIBLE
            bmi.visibility = View.GONE
            recalculateButton.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}