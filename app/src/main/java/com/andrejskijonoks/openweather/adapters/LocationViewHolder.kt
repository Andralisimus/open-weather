package com.andrejskijonoks.openweather.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrejskijonoks.openweather.R
import com.andrejskijonoks.openweather.activities.LocationListActivity
import com.andrejskijonoks.openweather.activities.MainActivity
import com.andrejskijonoks.openweather.database.OWLocation
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LocationViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_location_item, parent, false)) {
    private var tvLocationName: TextView? = null
    private var btnOptions: Button? = null


    init {
        tvLocationName = itemView.findViewById(R.id.tvLocationName)
        btnOptions = itemView.findViewById(R.id.btnOptions)
    }

    fun bind(owLocation: OWLocation, activity: Activity) {
        tvLocationName?.text = owLocation.name
        btnOptions?.setOnClickListener {
            showPopup(it,owLocation,activity)
        }
    }

    private fun showPopup(view: View, owLocation: OWLocation, activity: Activity){
        val popup = PopupMenu(activity,view)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.details -> {
                    activity.startActivity(MainActivity.newIntent(activity, LatLng(owLocation.latitude,owLocation.longitude),owLocation.name))
                    true
                }
                R.id.delete -> {
                    CoroutineScope(IO).launch {
                        MainActivity.database!!.owLocationDao().delete(owLocation)
                        val snackbar = Snackbar.make(view,activity.getString(R.string.delete_success), Snackbar.LENGTH_SHORT)
                        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                        snackbar.show()
                        val a = activity as LocationListActivity

                        if(LocationListActivity.currentLocationName!!.equals(owLocation.name)){
                            a.locationWasDeleted = true
                        }
                        a.getLocations()
                    }
                    true
                }
                else -> false
            }
        }

        popup.inflate(R.menu.popup_menu)

        try {
            val fieldWPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldWPopup.isAccessible = true
            val mPopup = fieldWPopup.get(popup)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java).invoke(mPopup,true)
        } catch (e: Exception){
            Log.e("WeatherAdapter","Error showing menu icons.", e)
        } finally {
            popup.show()
        }
    }

}