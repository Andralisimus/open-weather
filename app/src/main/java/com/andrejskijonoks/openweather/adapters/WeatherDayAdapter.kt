package com.andrejskijonoks.openweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrejskijonoks.openweather.Daily

class WeatherDayAdapter(private val list: List<Daily>)
    : RecyclerView.Adapter<WeatherDayViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WeatherDayViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: WeatherDayViewHolder, position: Int) {
        val daily: Daily = list[position]
        holder.bind(daily)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(
                position
            )
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}