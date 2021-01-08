package com.andrejskijonoks.openweather.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.andrejskijonoks.openweather.R
import com.andrejskijonoks.openweather.Shared
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException


class MapActivity : AppCompatActivity() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var chosenCords: LatLng

    private lateinit var etLocation: EditText
    private lateinit var etLocationName: EditText
    private lateinit var btnFindByText: Button
    private lateinit var btnFindByMap: Button
    private lateinit var clError: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Shared.changeStatusBar(this)
        findViews()
        initMap()
        setListeners()
        getUserLocation()
    }

    private fun initMap(){
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment

        mapFragment.getMapAsync {
            it.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.dark_map_style))

            it.setOnMapClickListener { latLng ->
                it.clear()
                it.addMarker(MarkerOptions().position(latLng)).setIcon(
                    bitmapDescriptorFromVector(this, R.drawable.ic_location)
                )
                chosenCords = latLng
                btnFindByMap.isEnabled = true
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation(){
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if(location != null){
                val currentLocation = LatLng(location.latitude, location.longitude)
                mapFragment.getMapAsync {
                    it.isMyLocationEnabled = true
                    it.uiSettings.isZoomGesturesEnabled = true
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
                }
            }
        }
    }

    private fun getLatLngFromName(location: String) : LatLng? {
        return if (Geocoder.isPresent()) {
            try {
                val gc = Geocoder(this)
                val addresses = gc.getFromLocationName(location, 1)
                if(addresses == null || addresses.size == 0) return null
                val address = addresses[0]

                if(address.hasLatitude() && address.hasLongitude()){
                    return LatLng(address.latitude, address.longitude)
                } else null

            } catch (e: IOException) {
                return null
            }
        } else null
    }

    private fun setListeners(){
        btnFindByMap.setOnClickListener {
            enableButtons(false)
            val locationName = etLocationName.text.toString()
            startActivity(MainActivity.newIntent(this, chosenCords, locationName))
        }
        btnFindByText.setOnClickListener {
            enableButtons(false)
            val locationName = etLocationName.text.toString()
            val location = etLocation.text.toString()
            val latLng = getLatLngFromName(location)
            if(latLng != null){
                val name = if(locationName.isEmpty()) location else locationName
                startActivity(MainActivity.newIntent(this, latLng, name))
            } else {
                btnFindByText.isEnabled = true
                clError.visibility = View.VISIBLE
            }
        }
        etLocation.addTextChangedListener {
            btnFindByText.isEnabled = true
        }
    }

    private fun findViews(){
        etLocation = findViewById(R.id.etLocation)
        etLocationName = findViewById(R.id.etLocationName)
        btnFindByText = findViewById(R.id.btnFindByText)
        btnFindByMap = findViewById(R.id.btnFindByMap)
        clError = findViewById(R.id.clError)
    }

    private fun enableButtons(condition : Boolean){
        btnFindByText.isEnabled = condition
        btnFindByMap.isEnabled = condition
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(
                intrinsicWidth,
                intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}