package com.andrejskijonoks.openweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrejskijonoks.openweather.Current

class WeatherHourAdapter(private val list: List<Current>)
    : RecyclerView.Adapter<WeatherHourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHourViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WeatherHourViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: WeatherHourViewHolder, position: Int) {
        val current: Current = list[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int = list.size

}