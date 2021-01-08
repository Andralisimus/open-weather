package com.andrejskijonoks.openweather.activities

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.andrejskijonoks.openweather.APIService
import com.andrejskijonoks.openweather.OneCallResponse
import com.andrejskijonoks.openweather.R
import com.andrejskijonoks.openweather.Shared
import com.andrejskijonoks.openweather.adapters.WeatherDayAdapter
import com.andrejskijonoks.openweather.database.OWDatabase
import com.andrejskijonoks.openweather.database.OWLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var tvFeelsLike: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var tvClouds: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvLocationName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvTemp: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var loader: ConstraintLayout
    private lateinit var btnDetails: ConstraintLayout
    private lateinit var btnChangeLocation: ConstraintLayout
    private lateinit var btnLocationList: ConstraintLayout

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val appId = "9483b92df41d6a2dd1e9d8a8fd061f65"
    private val metric = "metric"
    private val myLocation = "My Location"

    companion object StaticContent {

        var database: OWDatabase? = null
        private var locationCords : LatLng? = null
        private var locationName : String? = null

        fun newIntent(context: Context, locationCords: LatLng?, locationName: String?): Intent {
            this.locationCords = locationCords
            this.locationName = locationName
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Shared.changeStatusBar(this)
        database = Room.databaseBuilder(this,OWDatabase::class.java,"location.db").build()
        findViews()
        getUserLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if(locationCords == null){
            val task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener {
                locationCords = if(it != null){
                    LatLng(it.latitude,it.longitude)
                } else {
                    //Sometimes fusedLocationProviderClient fails to find location for no reason
                    //That's why I will Load hardcoded location. And later user can change it to one from his own locations
                    LatLng(56.949002,24.111375)
                }
                getWeatherByCords()
            }
        } else {
            getWeatherByCords()
        }
    }

    private fun getWeatherByCords(){

        if(isOnline()){
            val instance = APIService.getInstance()

            CoroutineScope(IO).launch {
                val response = instance.getWeatherByCords(locationCords!!.latitude.toString(), locationCords!!.longitude.toString(),appId,metric)

                if(response.isSuccessful){
                    if(locationName == null) locationName = myLocation
                    else if(locationName!!.isEmpty()) locationName = response.body()!!.timezone

                    database!!.owLocationDao().upsert(OWLocation(locationName!!, locationCords!!.latitude, locationCords!!.longitude))
                    updateUI(response.body()!!)
                }
            }
        } else error()
    }

    @SuppressLint("SetTextI18n")
    private suspend fun updateUI(response : OneCallResponse){
        withContext(Main){
            tvFeelsLike.text = getString(R.string.feels_like) + response.current.feels_like + getString(R.string.temp_sign)
            tvWindSpeed.text = getString(R.string.wind_speed) + response.current.wind_speed
            tvClouds.text = getString(R.string.clouds) + response.current.clouds + getString(R.string.percent_sign)
            tvHumidity.text = getString(R.string.humidity) + response.current.humidity + getString(R.string.percent_sign)
            tvLocationName.text = locationName
            tvDescription.text = response.current.weatherList[0].description
            tvTemp.text = response.current.temp.roundToInt().toString() + getString(R.string.temp_sign)

            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.HORIZONTAL,false)
            val adapter = WeatherDayAdapter(response.daily)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object:WeatherDayAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    startActivity(DailyDetailsActivity.newIntent(this@MainActivity,response.daily[position], locationName!!))
                    CoroutineScope(IO).cancel()
                }
            })

            btnDetails.setOnClickListener {
                startActivity(DetailsActivity.newIntent(this@MainActivity,response.hourly,locationName!!))
                CoroutineScope(IO).cancel()
            }

            btnChangeLocation.setOnClickListener {
                startActivity(Intent(this@MainActivity,MapActivity::class.java))
                CoroutineScope(IO).cancel()
            }

            btnLocationList.setOnClickListener {
                startActivity((Intent(this@MainActivity,LocationListActivity::class.java)))
                CoroutineScope(IO).cancel()
            }

            weatherLoaded()
            delay(180000) //Syncs with service every 3 min
            sync()
        }
    }

    private fun sync(){
        loader.visibility = View.VISIBLE
        val animationAlpha : ObjectAnimator = ObjectAnimator.ofFloat(loader,"alpha",1f)
        animationAlpha.doOnEnd {
            getWeatherByCords()
        }
        animationAlpha.setDuration(1500).start()
    }

    private fun weatherLoaded(){

        val animationAlpha : ObjectAnimator = ObjectAnimator.ofFloat(loader,"alpha",0f)
        animationAlpha.doOnEnd {
            loader.visibility = View.GONE
        }
        animationAlpha.setDuration(1500).start()
    }

    private fun findViews(){
        tvFeelsLike = findViewById(R.id.tvFeelsLike)
        tvWindSpeed = findViewById(R.id.tvWindSpeed)
        tvClouds = findViewById(R.id.tvClouds)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvLocationName = findViewById(R.id.tvLocationName)
        tvDescription = findViewById(R.id.tvDescription)
        tvTemp = findViewById(R.id.tvTemp)
        recyclerView = findViewById(R.id.dayRecyclerView)
        loader = findViewById(R.id.loader)
        btnDetails = findViewById(R.id.btnDetails)
        btnChangeLocation = findViewById(R.id.btnChangeLocation)
        btnLocationList = findViewById(R.id.btnLocationList)
    }

    private fun isOnline(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return true
        }
        return false
    }

    private fun error(){
        val intent = Intent(this,ErrorActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}

