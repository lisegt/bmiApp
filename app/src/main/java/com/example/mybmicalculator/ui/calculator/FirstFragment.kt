package com.example.mybmicalculator.ui.calculator

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mybmicalculator.R
import com.example.mybmicalculator.data.Bmi
import com.example.mybmicalculator.data.BmiViewModel
import com.example.mybmicalculator.data.User
import com.example.mybmicalculator.data.UserViewModel
import com.example.mybmicalculator.databinding.FragmentFirstBinding
import com.example.mybmicalculator.ui.users.UsersFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var mBmiViewModel: BmiViewModel
    private lateinit var mUserViewModel: UserViewModel
    private var _binding: FragmentFirstBinding? = null

    //to retrieve the current user we have selected and pre-fill his height in the height input
    private val args by navArgs<FirstFragmentArgs>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBmiViewModel = ViewModelProvider(this).get(BmiViewModel::class.java)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        //pre-fill the height input
        val currentHeight = mUserViewModel.getCurrentHeight(args.currentUser.userId)
        view.height_input.setText(currentHeight.toString())

        //Add help icon in the top navigation menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //when we click on the right floating action button, the history view is displayed
        view.findViewById<FloatingActionButton>(R.id.floatingActionButtonToHistory).setOnClickListener {
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(args.currentUser)
            findNavController().navigate(action)
        }

        //when we click on the left floating action button, the users view is displayed
        view.findViewById<FloatingActionButton>(R.id.floatingActionButtonToUsers).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_UsersFragment)
        }

        //initialization of variables
        val message_welcome = view.findViewById<TextView>(R.id.text_calculator)
        val weight = view.findViewById<EditText>(R.id.weight_input)
        val height = view.findViewById<EditText>(R.id.height_input)

        val calculateButton = view.findViewById<Button>(R.id.button_calculate)
        val bmi = view.findViewById<TextView>(R.id.bmi_result)
        val bmiStatus = view.findViewById<TextView>(R.id.bmi_status)
        val recalculateButton = view.findViewById<Button>(R.id.button_recalculate)

        val progressBar = view.findViewById<CircularProgressBar>(R.id.progress_bar)
        var progressValue = 50f

        //when we arrive on the main page (calculator) some elements are not visible
        progressBar.visibility = View.GONE
        recalculateButton.visibility = View.GONE
        bmi.visibility = View.GONE
        bmiStatus.visibility = View.GONE

        //displays the last saved data
        //loadLastInput(height,weight)

        //function to calculate and display the bmi
        fun calculateBmi(){
            var weightUser = 0.0
            var heightUser = 0.0

            //we check that all user entries are valid
            if (checkInput(height, weight)){
                weightUser = weight.text.toString().toDouble()
                //we convert the height into meters
                heightUser = height.text.toString().toDouble() / 100

                //the result is rounded to one decimal place
                var bmiUser = (weightUser / heightUser.pow(2) * 10.0).roundToInt() / 10.0
                bmi.text = bmiUser.toString()

                //PROGRESS BAR
                //the progress bar is considered full if the bmi reaches 40
                progressValue = bmiUser.toFloat()*100/40

                // Set Progress
                progressBar.setProgressWithAnimation(progressValue, 1000)

                //equivalent of a switch case in java
                //change the color of the progress bar and the status according to the BMI value
                when (bmiUser) {
                    in 16.0..16.9 -> {progressBar.progressBarColor = Color.rgb(67,163,219) ;
                        bmiStatus.text = getString(R.string.underweight_moderate) }
                    in 17.0..18.4 -> {progressBar.progressBarColor = Color.rgb(98,176,179) ;
                        bmiStatus.text = getString(R.string.underweight_mild) }
                    in 18.5..24.9 -> {progressBar.progressBarColor = Color.rgb(161,199,64) ;
                        bmiStatus.text = getString(R.string.normal_range) }
                    in 25.0..29.9 -> {progressBar.progressBarColor = Color.rgb(254,229,82) ;
                        bmiStatus.text = getString(R.string.overweight) }
                    in 30.0..34.9 -> {progressBar.progressBarColor = Color.rgb(236,120,29) ;
                        bmiStatus.text = getString(R.string.obese_1) }
                    in 35.0..39.9 -> {progressBar.progressBarColor = Color.rgb(114,0,25) ;
                        bmiStatus.text = getString(R.string.obese_2) }
                    else -> {
                        if (bmiUser < 16.0){
                            progressBar.progressBarColor = Color.rgb(35,98,176)
                            bmiStatus.text = getString(R.string.underweight_severe)
                        } else {
                            progressBar.progressBarColor = Color.rgb(79,0,2)
                            bmiStatus.text = getString(R.string.obese_3)
                        }
                    }
                }
                //we display the progress bar
                progressBar.visibility = View.VISIBLE

                //we modify the displayed items
                message_welcome.text = getString(R.string.bmi_calculated_message)
                bmi.visibility = View.VISIBLE
                bmiStatus.visibility = View.VISIBLE
                recalculateButton.visibility = View.VISIBLE
                calculateButton.visibility = View.GONE

                //we save the last input data
                //saveLastInput(height, weight)

                //we add the bmi to the Database
                insertDataToDatabase(height.text.toString(), weight.text.toString(), bmiUser.toString())

                updateHeight(height.text.toString().toDouble())
            }
        }

        calculateButton.setOnClickListener(){

            //we calculate the BMI and display the progress bar
            calculateBmi()
        }

        //if we want to recalculate a new BMI, we reset the progress bar and
        // call the calculateBmi() function
        // by clicking on the recalculate button
        recalculateButton.setOnClickListener(){

            //reset the progress bar to 0
            progressBar.progress = 0f

            //we calculate the BMI and display the progress bar
            calculateBmi()
        }

        //progress bar customization
        progressBar.apply {
            // Set Progress Max
            progressMax = 100f

            // Set background ProgressBar Color
            backgroundProgressBarColorStart = Color.argb(80, 35,98,176)
            backgroundProgressBarColorEnd = Color.argb(80,114,0,25)
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.RIGHT_TO_LEFT

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 16f // in DP

            // Other
            roundBorder = true
        }
    }

    private fun updateHeight(height: Double) {

        //we update if the height is not empty and if it is different from the current height
        if (args.currentUser.height != height) {
            // Update Current Height
            mUserViewModel.updateHeight(args.currentUser.userId, height)
            Toast.makeText(requireContext(), "Height updated Successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    //function that adds a bmi and its save date in the database
    private fun insertDataToDatabase(heightValue:String, weightValue:String,bmiValue:String) {

        //on récupère la date d'enregistrement = date actuelle
        val currentDate: Date = Calendar.getInstance().getTime()
        //we define a date format to display the day, month, year and time
        //the time is retrieved in order to display the results in the descending order of the recording date
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm:ss", Locale.getDefault())
        //we convert the date into a string so that it can be inserted into the database
        val formattedDate: String = dateFormat.format(currentDate)

        // Create Bmi Object
        val bmi = Bmi(0, heightValue, weightValue, bmiValue,formattedDate, args.currentUser.userId)
        // Add Data to Database
        mBmiViewModel.addBmi(bmi)
    }

    //function that checks the validity of user inputs
    private fun checkInput(height: EditText, weight: EditText) : Boolean{
        val height_string = height.text.toString()
        val weight_string = weight.text.toString()

        val testTypeHeight = height.text.toString().toDoubleOrNull()
        val testTypeWeight = weight.text.toString().toDoubleOrNull()

        return when {
            //if the user has not entered his height or weight, a toast is displayed
            height_string.isEmpty() -> {
                Toast.makeText(context, getString(R.string.height_empty), Toast.LENGTH_SHORT).show()
                return false
            }
            weight_string.isEmpty() -> {
                Toast.makeText(context, getString(R.string.weight_empty), Toast.LENGTH_SHORT).show()
                return false
            }

            //if the user has not entered a number, a toast is displayed
            //the toast should never appear because we have restricted the type of the EditText
            // to a decimal number
            testTypeHeight == null -> {
                Toast.makeText(context, getString(R.string.height_type_error), Toast.LENGTH_SHORT).show()
                return false
            }
            testTypeWeight == null -> {
                Toast.makeText(context, getString(R.string.weight_type_error), Toast.LENGTH_SHORT).show()
                return false
            }

            //if the user has entered a negative number, a toast is displayed
            //the toast should never appear because we have restricted the type of the EditText
            // to a decimal number, unsigned
            (height_string.toDouble() <= 0.0) -> {
                Toast.makeText(context, getString(R.string.height_negative), Toast.LENGTH_SHORT).show()
                return false
            }
            (weight_string.toDouble() <= 0.0) -> {
                Toast.makeText(context, getString(R.string.weight_negative), Toast.LENGTH_SHORT).show()
                return false
            }
            else -> {
                return true
            }
        }
    }

    //to make the help icon appear on the view
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.help_menu, menu)
    }

    //when clicking on the help can icon, we display information about BMI
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_help){
            displayInformation()
        }
        return super.onOptionsItemSelected(item)
    }

    //function that displays information about BMI
    private fun displayInformation() {
        //opens a window to confirm the deletion
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Understood !"){_,_ ->
        }
        builder.setTitle("What is a BMI ?")
        builder.setMessage("Body mass index or BMI is a statistical index using a person's weight and height to provide " +
                "an estimate of body fat in males and females of any age. " +
                "It is calculated by taking a person's weight, in kilograms, divided by their height, in meters squared, " +
                "or BMI = weight (in kg)/ height^2 (in m^2). The number generated from this equation is then " +
                "the individual's BMI number. The National Institute of Health (NIH) now uses BMI to define a person " +
                "as underweight, normal weight, overweight, or obese instead of traditional height vs. weight charts. ")
        builder.create().show()
    }

    //function that saves the last inputs
    private fun saveLastInput(height : EditText, weight:EditText){
        //we get the values entered by the user
        val lastHeight = height.text.toString()
        val lastWeight = weight.text.toString()

        val sharedPreferences : SharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        val editor = sharedPreferences.edit()
        //we save them by storing them in the SharedPreferences using the key values
        editor.apply(){
            putString("HEIGHT_KEY", lastHeight)
            putString("WEIGHT_KEY", lastWeight)
        }.apply()
    }

    //function that loads the last inputs and pre-fills the input fields
    private fun loadLastInput(height : EditText, weight : EditText){
        val sharedPreferences : SharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);

        //we retrieve the saved values from SharedPreferences using the key values
        val savedHeight = sharedPreferences.getString("HEIGHT_KEY", null)
        val savedWeight = sharedPreferences.getString("WEIGHT_KEY", null)

        //we pre-fill the user inputs
        height.setText(savedHeight)
        weight.setText(savedWeight)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}