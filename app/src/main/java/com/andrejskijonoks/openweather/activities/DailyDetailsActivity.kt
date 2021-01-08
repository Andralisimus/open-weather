package com.andrejskijonoks.openweather.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.andrejskijonoks.openweather.Daily
import com.andrejskijonoks.openweather.R
import com.andrejskijonoks.openweather.Shared
import kotlin.math.roundToInt

class DailyDetailsActivity : AppCompatActivity() {

    companion object StaticContent {

        private var daily : Daily? = null
        private var locationName : String? = null

        fun newIntent(context: Context, daily: Daily, locationName: String): Intent {

            this.daily = daily
            this.locationName = locationName
            return Intent(context, DailyDetailsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_details)

        Shared.changeStatusBar(this)
        updateUI()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(){
        findViewById<ConstraintLayout>(R.id.btnBack).setOnClickListener { onBackPressed() }
        findViewById<TextView>(R.id.tvDate).text = Shared.getDateTime(daily!!.dt.toString(),"MMM d EE")
        findViewById<TextView>(R.id.tvLocationName).text = locationName
        findViewById<TextView>(R.id.tvTempDay).text = daily!!.temp.day.roundToInt().toString() + this.getString(R.string.temp_sign)
        findViewById<TextView>(R.id.tvTempNight).text = daily!!.temp.night.roundToInt().toString() + this.getString(R.string.temp_sign)
        findViewById<TextView>(R.id.tvTempMorn).text = daily!!.temp.morn.roundToInt().toString() + this.getString(R.string.temp_sign)
        findViewById<TextView>(R.id.tvTempEve).text = daily!!.temp.eve.roundToInt().toString() + this.getString(R.string.temp_sign)
        findViewById<TextView>(R.id.tvFeelsDay).text = daily!!.feels_like.day.roundToInt().toString() + this.getString(R.string.temp_sign)
        findViewById<TextView>(R.id.tvFeelsNight).text =  daily!!.feels_like.night.roundToInt().toString() + this.getString(R.string.temp_sign)
        findViewById<TextView>(R.id.tvFeelsMorn).text =  daily!!.feels_like.morn.roundToInt().toString() + this.getString(R.string.temp_sign)
        findViewById<TextView>(R.id.tvFeelsEve).text = daily!!.feels_like.eve.roundToInt().toString() + this.getString(R.string.temp_sign)
        findViewById<TextView>(R.id.tvWind).text = daily!!.wind_speed.toString()
        findViewById<TextView>(R.id.tvClouds).text = daily!!.clouds.toString() + this.getString(R.string.percent_sign)
        findViewById<TextView>(R.id.tvHumidity).text = daily!!.humidity.toString() + this.getString(R.string.percent_sign)
    }
}