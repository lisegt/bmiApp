package com.example.mybmiapp.ui.calculator

import android.graphics.Color
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
import com.google.android.material.color.MaterialColors.getColor
import com.mikhaellopez.circularprogressbar.CircularProgressBar
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
        val bmiStatus = view.findViewById<TextView>(R.id.bmi_status)
        val recalculateButton = view.findViewById<Button>(R.id.button_recalculate)

        val progressBar = view.findViewById<CircularProgressBar>(R.id.progress_bar)
        var progressValue = 50f

        //quand on arrive sur la page certains éléments ne sont pas visibles
        progressBar.visibility = View.GONE
        recalculateButton.visibility = View.GONE
        bmi.visibility = View.GONE
        bmiStatus.visibility = View.GONE

        calculateButton.setOnClickListener(){
            var weightUser = 0.0
            var heightUser = 0.0

            //si l'utilisateur n'a pas rentré sa taille, on affiche un toast
            if ((height.text.toString().isEmpty())){
                val toastHeight = Toast.makeText(context, getString(R.string.height_empty), Toast.LENGTH_SHORT)
                toastHeight.show()
            } else {
                var testTypeHeight = height.text.toString().toDoubleOrNull()
                //on vérifie si l'utilisateur a rentré un nombre
                //si ce n'est pas un nombre, on affiche un toast
                if (testTypeHeight == null){
                    val toastHeightError = Toast.makeText(context, getString(R.string.height_type_error), Toast.LENGTH_SHORT)
                    toastHeightError.show()
                } else {
                    //si c'est un nombre, on convertit la taille en mètres
                    heightUser = height.text.toString().toDouble() / 100
                }
            }

            //si l'utilisateur n'a pas rentré son poids, on affiche un toast
            if (weight.text.toString().isEmpty()){
                val toastWeight = Toast.makeText(context, getString(R.string.weight_empty), Toast.LENGTH_SHORT)
                toastWeight.show()
            } else {
                var testTypeWeight = weight.text.toString().toDoubleOrNull()
                //si le poids rentré n'est pas un nombre, on affiche un toast
                if (testTypeWeight == null){
                    val toastWeightError = Toast.makeText(context, getString(R.string.weight_type_error), Toast.LENGTH_SHORT)
                    toastWeightError.show()
                } else {
                    weightUser = weight.text.toString().toDouble()
                }
            }

            if (weightUser > 0.0 && heightUser > 0.0){
                //on arrondit le résultat à 1 décimale
                var bmiUser = (weightUser / heightUser.pow(2) * 10.0).roundToInt() / 10.0
                bmi.text = bmiUser.toString()
                progressValue = bmiUser.toFloat()*100/40

                // Set Progress
                progressBar.setProgressWithAnimation(progressValue, 1000)
                //équivalent d'un switch case en java
                //on modifier la couleur de la progress bar en fonction de la valeur du BMI
                when (bmiUser) {
                    in 16.0..16.9 -> {progressBar.progressBarColor = Color.rgb(67,163,219) ; bmiStatus.text = "Underweight \n(Moderate thinness) " }
                    in 17.0..18.4 -> {progressBar.progressBarColor = Color.rgb(98,176,179) ; bmiStatus.text = "Underweight \n(Mild thinness)" }
                    in 18.5..24.9 -> {progressBar.progressBarColor = Color.rgb(161,199,64) ; bmiStatus.text = "Normal Range" }
                    in 25.0..29.9 -> {progressBar.progressBarColor = Color.rgb(254,229,82) ; bmiStatus.text = "Overweight \n(Pre-obese)" }
                    in 30.0..34.9 -> {progressBar.progressBarColor = Color.rgb(236,120,29) ; bmiStatus.text = "Obese \n(Class I)" }
                    in 35.0..39.9 -> {progressBar.progressBarColor = Color.rgb(114,0,25) ; bmiStatus.text = "Obese \n(Class II)" }
                    else -> {
                        if (bmiUser < 16.0){
                            // Set ProgressBar Color
                            progressBar.progressBarColor = Color.rgb(35,98,176)
                            bmiStatus.text = "Obese (Class III)"
                        } else {
                            progressBar.progressBarColor = Color.rgb(114,0,25)
                            bmiStatus.text = "Underweight \n(Severe thinness)"
                        }
                    }
                }
                //on affiche la progress bar
                progressBar.visibility = View.VISIBLE

                //on modifie les élements affichés
                message_welcome.text = getString(R.string.bmi_calculated_message)
                bmi.visibility = View.VISIBLE
                bmiStatus.visibility = View.VISIBLE

                recalculateButton.visibility = View.VISIBLE
                calculateButton.visibility = View.GONE
            }
        }
        //si on veut recalculer un nouvel IMC, on doit vider les champs d'input en cliquant sur le bouton recalculate
        recalculateButton.setOnClickListener(){

            message_welcome.text = getString(R.string.welcome)
            //on réinitialise les champs d'input
            height.text.clear()
            weight.text.clear()

            //on remet la progress bar à 0
            progressBar.progress = 0f
            progressBar.visibility = View.GONE
            calculateButton.visibility = View.VISIBLE
            bmi.visibility = View.GONE
            bmiStatus.visibility = View.GONE
            recalculateButton.visibility = View.GONE
        }

        //personnalisation de la progress bar
        progressBar.apply {
            // Set Progress Max
            progressMax = 100f

            // Set background ProgressBar Color
            backgroundProgressBarColorStart = Color.rgb(35,98,176)
            backgroundProgressBarColorEnd = Color.rgb(114,0,25)
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 16f // in DP

            // Other
            roundBorder = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}