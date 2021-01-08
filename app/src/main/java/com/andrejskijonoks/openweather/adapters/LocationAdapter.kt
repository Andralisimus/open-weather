package com.andrejskijonoks.openweather.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrejskijonoks.openweather.database.OWLocation

class LocationAdapter(activity: Activity, private val list: List<OWLocation>) : RecyclerView.Adapter<LocationViewHolder>() {

    private val context = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LocationViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val owLocation: OWLocation = list[position]
        holder.bind(owLocation,context)
    }

    override fun getItemCount(): Int = list.size

}