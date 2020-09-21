package edu.arizona.cast.austinramsay.bmi

import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

private const val TAG = "BMIViewModel"

class BMIViewModel : ViewModel() {

    var heightFt: String? = null
    var heightIn: String? = null
    var weightLbs: String? = null
    var bmiIndex: String? = null
    var bmiStatus: String? = null

    fun updateBMI(height_ft: String, height_in: String, weight_lbs: String) {

        this.heightFt = height_ft
        this.heightIn = height_in
        this.weightLbs = weight_lbs

        // Catch any number format exceptions that might occur
        try {
            val bmi = BMI(height_ft.toInt(), height_in.toInt(), weight_lbs.toInt())

            this.bmiStatus = bmi.status

            // Set the BMI index text view using formatted output of the BMI index calculation
            val df = DecimalFormat("#.0")
            this.bmiIndex = df.format(bmi.index)

        } catch (e: NumberFormatException) {
            Log.d(TAG, "NumberFormatException occurred with BMI input values.")
        }
    }

}