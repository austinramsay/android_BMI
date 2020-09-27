package edu.arizona.cast.austinramsay.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var bmiCalcButton: Button
    private lateinit var clrButton: Button
    private lateinit var rateCalcButton: Button
    private lateinit var heightFtInput: EditText
    private lateinit var heightInInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var statusText: TextView
    private lateinit var indexText: TextView

    // Prepare ViewModel
    private val bmiViewModel: BMIViewModel by lazy {
        ViewModelProviders.of(this).get(BMIViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle the ViewModel
        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val bmiViewModel = provider.get(BMIViewModel::class.java)
        Log.d(TAG, "Got the BMIViewModel: $bmiViewModel.")

        // Get all required buttons, input fields, and text views
        bmiCalcButton = findViewById(R.id.calc_button)
        clrButton = findViewById(R.id.clr_button)
        rateCalcButton = findViewById(R.id.rate_calc_button)
        heightFtInput = findViewById(R.id.ft_input)
        heightInInput = findViewById(R.id.in_input)
        weightInput = findViewById(R.id.weight_input)
        statusText = findViewById(R.id.bmi_status)
        indexText = findViewById(R.id.bmi_index)

        // Set the calculate button listener
        bmiCalcButton.setOnClickListener {

            // Extract all input fields for required info (height and weight)
            val heightFt = heightFtInput.text.toString()
            val heightIn = heightInInput.text.toString()
            val weightLbs = weightInput.text.toString()

            // Verify required info is filled in
            if (heightFt.isBlank() || heightIn.isBlank() || weightLbs.isBlank()) {

                // Display user notification that input is invalid/missing
                Toast.makeText(
                    this,
                    "Please verify input is correct!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                // Provide the view model with the extracted info
                bmiViewModel.update(heightFt, heightIn, weightLbs)

                // Update UI with model data
                syncResults()
            }

        }

        // Heart Rate calculator button - switch to new activity
        rateCalcButton.setOnClickListener {

        }

        // Clear button should set all input fields and output text views to empty
        clrButton.setOnClickListener {
            heightFtInput.text = null
            heightInInput.text = null
            weightInput.text = null
            statusText.text = null
            indexText.text = null
        }

        // Sync to the model's information if the activity was recreated or is new
        syncAll()
    }

    // Sync the UI result fields to the ViewModel's stored data
    private fun syncResults() {
        // BMI status updates
        statusText.text = bmiViewModel.bmiStatus
        statusText.setTextColor(bmiViewModel.bmiStatusColor)

        // BMI index updates
        indexText.text = bmiViewModel.bmiIndexText
    }

    // Sync all UI fields to the ViewModel's stored data
    private fun syncAll() {
        // Set the input fields to stored values
        heightFtInput.setText(bmiViewModel.heightFt)
        heightInInput.setText(bmiViewModel.heightIn)
        weightInput.setText(bmiViewModel.weightLbs)

        // Set result fields to stored values
        syncResults()
    }
}
