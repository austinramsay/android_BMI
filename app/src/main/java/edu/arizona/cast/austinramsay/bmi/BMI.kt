package edu.arizona.cast.austinramsay.bmi

import kotlin.math.pow

data class BMI(val height_ft: Int,
               val height_in: Int,
               val weight_lbs: Int) {

    val index: Double?
    val status: String?

    companion object {
        const val STATUS_NORMAL = "Normal"
        const val STATUS_OVERWEIGHT = "Overweight"
        const val STATUS_UNDERWEIGHT = "Underweight"
        const val STATUS_OBESE = "Obese"
    }

    // Upon instance creation, calculate the BMI index and status properties using the provided
    // arguments.
    init {
        // Index is calculated by (pounds * 703) / (height in inches)^2
        index = (weight_lbs.toDouble() * 703) / ((height_ft.toDouble() * 12 + height_in).pow(2))

        // Set the BMI overall status rating according to the BMI index
        status = if (index < 18.5) {
            STATUS_UNDERWEIGHT
        } else if (index >= 18.5 && index < 25) {
            STATUS_NORMAL
        } else if (index >= 25 && index < 30) {
            STATUS_OVERWEIGHT
        } else {
            STATUS_OBESE
        }
    }

}