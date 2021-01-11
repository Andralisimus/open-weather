package com.andrejskijonoks.openweather.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrejskijonoks.openweather.Current
import com.andrejskijonoks.openweather.R
import com.andrejskijonoks.openweather.Shared
import com.andrejskijonoks.openweather.adapters.LocationAdapter
import com.andrejskijonoks.openweather.database.OWLocation
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class LocationListActivity : AppCompatActivity() {

    companion object StaticContent {

        var currentLocationName : String? = null

        fun newIntent(context: Context, currentLocayionName : String): Intent{
            this.currentLocationName = currentLocationName
            return Intent(context, LocationListActivity::class.java)
        }
    }

    var locationWasDeleted : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)
        Shared.changeStatusBar(this)
        setOnClickListeners()
        getLocations()
    }

    private suspend fun updateList(locations : List<OWLocation>){
        withContext(Main){
            findViewById<RecyclerView>(R.id.recyclerView).apply {
                layoutManager = LinearLayoutManager(this@LocationListActivity)
                adapter = LocationAdapter(this@LocationListActivity,locations)
            }
        }
    }

    private fun setOnClickListeners(){
        findViewById<ConstraintLayout>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
        findViewById<ConstraintLayout>(R.id.btnAddLocation).setOnClickListener {
            startActivity(Intent(this,MapActivity::class.java))
        }
    }

    fun getLocations(){
        CoroutineScope(Dispatchers.IO).launch {
            val locations = MainActivity.database!!.owLocationDao().getLocations()
            updateList(locations)
        }
    }

    override fun onBackPressed() {
        if(locationWasDeleted){
            val intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}