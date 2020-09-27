package edu.arizona.cast.austinramsay.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "HeartRateActivity"

class HeartRateActivity : AppCompatActivity() {

    private lateinit var calcButton: Button
    private lateinit var clrButton: Button
    private lateinit var returnBmiButton: Button
    private lateinit var ageInput: EditText
    private lateinit var heartRateMaxResult: TextView
    private lateinit var heartRateTargetResult: TextView

    // Prepare ViewModel
    private val heartRateViewModel: HeartRateViewModel by lazy {
        ViewModelProviders.of(this).get(HeartRateViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart_rate)

        // Handle the ViewModel
        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val heartRateViewModel = provider.get(HeartRateViewModel::class.java)
        Log.d(TAG, "Got the HeartRateViewModel: $heartRateViewModel.")

        // Get all required buttons, input fields, and text views
        calcButton = findViewById(R.id.calc_button)
        clrButton = findViewById(R.id.clr_button)
        returnBmiButton = findViewById(R.id.return_bmi_button)
        ageInput = findViewById(R.id.age_input)
        heartRateMaxResult = findViewById(R.id.heart_rate_max)
        heartRateTargetResult = findViewById(R.id.heart_rate_target)

        calcButton.setOnClickListener {
            // Calculate heart rate and display results

            // Extract all input fields for required info (height and weight)
            val age = ageInput.text.toString()

            // Verify required info is filled in
            if (age.isBlank()) {

                // Display user notification that input is invalid/missing
                Toast.makeText(
                    this,
                    "Please verify input is correct!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                // Provide the view model with the extracted info
                heartRateViewModel.update(age.toInt())

                // Update UI with model data
                syncResults()
            }
        }

        clrButton.setOnClickListener {
            // Clear input fields and result fields
            ageInput.text = null
            heartRateMaxResult.text = null
            heartRateTargetResult.text = null
        }

        returnBmiButton.setOnClickListener {
            finish()
        }

        syncAll()
    }

    // Sync the UI result fields to the ViewModel's stored data
    private fun syncResults() {
        // Heart rate target and maximum value updates
        heartRateMaxResult.text = heartRateViewModel.heartRateMaxText
        heartRateMaxResult.setTextColor(heartRateViewModel.maxRateColor)

        heartRateTargetResult.text = heartRateViewModel.heartRateTargetText
        heartRateTargetResult.setTextColor(heartRateViewModel.optimalRateColor)
    }

    // Sync all UI fields to the ViewModel's stored data
    private fun syncAll() {
        // Set the input fields to stored values
        ageInput.setText(
            if (heartRateViewModel.age == null)
                null
            else
                heartRateViewModel.age.toString()
        )

        // Set result fields to stored values
        syncResults()
    }
}
