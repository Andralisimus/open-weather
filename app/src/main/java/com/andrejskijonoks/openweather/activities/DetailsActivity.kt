package com.andrejskijonoks.openweather.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrejskijonoks.openweather.Current
import com.andrejskijonoks.openweather.R
import com.andrejskijonoks.openweather.adapters.WeatherHourAdapter

class DetailsActivity : AppCompatActivity() {

    companion object StaticContent {

        private var hourlyList : List<Current>? = null
        private var locationName : String? = null

        fun newIntent(context: Context,hourlyList: List<Current>,locationName: String): Intent{

            this.hourlyList = hourlyList
            this.locationName = locationName
            return Intent(context, DetailsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        findViewById<TextView>(R.id.tvLocationName).text = locationName

        changeStatusBar()
        updateUI()
    }

    private fun changeStatusBar(){
        this.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun updateUI(){
        findViewById<ConstraintLayout>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<RecyclerView>(R.id.hourRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@DetailsActivity)
            adapter = WeatherHourAdapter(hourlyList!!)
        }
    }
}