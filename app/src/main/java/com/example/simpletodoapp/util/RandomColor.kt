package com.example.simpletodoapp.util

import android.graphics.Color
import java.util.*

class RandomColor {
    companion object{
        fun generateRandomColor(): Int {
            val random = Random(System.currentTimeMillis());

            val baseColor: Int = Color.WHITE
            val baseRed: Int = Color.red(baseColor)
            val baseGreen: Int = Color.green(baseColor)
            val baseBlue: Int = Color.blue(baseColor)

            val red: Int = (baseRed + random.nextInt(256)) / 2
            val green: Int = (baseGreen + random.nextInt(256)) / 2
            val blue: Int = (baseBlue + random.nextInt(256)) / 2

            return Color.rgb(red, green, blue);
        }
    }
}