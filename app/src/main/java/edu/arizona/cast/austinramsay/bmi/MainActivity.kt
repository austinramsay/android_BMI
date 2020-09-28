package edu.arizona.cast.austinramsay.bmi

import android.app.Activity
import android.content.Context
import android.content.Intent
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
private const val REQUEST_AGE = 0

class MainActivity : AppCompatActivity() {

    private lateinit var calcButton: Button
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
        calcButton = findViewById(R.id.calc_button)
        clrButton = findViewById(R.id.clr_button)
        rateCalcButton = findViewById(R.id.rate_calc_button)
        heightFtInput = findViewById(R.id.ft_input)
        heightInInput = findViewById(R.id.in_input)
        weightInput = findViewById(R.id.weight_input)
        statusText = findViewById(R.id.bmi_status)
        indexText = findViewById(R.id.bmi_index)

        // Set the calculate button listener
        calcButton.setOnClickListener {

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
            // If the BMI ViewModel contains a saved age returned from the heart calculator previously,
            // then send the saved age into the heart rate activity to restore it.
            val savedAge = bmiViewModel.age ?: ""

            val intent = HeartRateActivity.newIntent(this@MainActivity, savedAge)
            startActivityForResult(intent, REQUEST_AGE)
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

    // Upon the heart rate calculator activity completing, determine if the user entered an age
    // and calculated a result. If so, we can extract that age from the intent extra.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            // Nothing to be done, age wasn't set
            return
        }

        if (requestCode == REQUEST_AGE) {
            // Rate was calculated with an age input, extract the age that was set
            bmiViewModel.age = data?.getStringExtra(EXTRA_AGE_SET)
        }
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
