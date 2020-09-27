package edu.arizona.cast.austinramsay.bmi

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

private const val TAG = "HeartRateViewModel"

class HeartRateViewModel : ViewModel() {

    var age: Int? = null
    var maxHeartRate: Int? = null
    var targetHeartRateMin: Double? = null
    var targetHeartRateMax: Double? = null
    var heartRateTargetText: String? = null
    var heartRateMaxText: String? = null
    val minRateColor: Int = Color.GREEN
    val maxRateColor: Int = Color.RED


    fun update(age: Int) {

        this.age = age

        // Catch any number format exceptions that might occur
        try {
            val HeartRate = HeartRate(age)

            this.maxHeartRate = HeartRate.maxHeartRate
            this.targetHeartRateMax = HeartRate.targetHeartRateMax
            this.targetHeartRateMin = HeartRate.targetHeartRateMin

            // Set the heart rate target and max status strings according to extracted data
            val df = DecimalFormat("#.0")
            this.heartRateMaxText = String.format("Heart rate should be under: %d", this.maxHeartRate)
            this.heartRateTargetText = String.format("Ideal heart rate range is between: %s and %s", df.format(this.targetHeartRateMin), df.format(this.targetHeartRateMax))

        } catch (e: NumberFormatException) {
            Log.d(TAG, "NumberFormatException occurred with heart rate input values.")
        }
    }

}
