package com.andrejskijonoks.openweather

import android.annotation.SuppressLint
import android.app.Activity
import android.view.WindowManager
import java.text.SimpleDateFormat
import java.util.*

class Shared {

    companion object StaticContent {

        @SuppressLint("SimpleDateFormat")
        fun getDateTime(s: String, pattern: String): String? {
            return try {
                val sdf = SimpleDateFormat(pattern)
                val netDate = Date(s.toLong() * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        }

        fun changeStatusBar(activity: Activity){
            activity.window?.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }
}