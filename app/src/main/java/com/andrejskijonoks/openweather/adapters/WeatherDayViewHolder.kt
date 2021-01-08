package com.andrejskijonoks.openweather.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrejskijonoks.openweather.Daily
import com.andrejskijonoks.openweather.R
import com.andrejskijonoks.openweather.Shared
import kotlin.math.roundToInt

class WeatherDayViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_day_item, parent, false)) {
    private var tvDay: TextView? = null
    private var tvClouds: TextView? = null
    private var tvTempMax: TextView? = null
    private var tvTempMin: TextView? = null


    init {
        tvDay = itemView.findViewById(R.id.tvDay)
        tvClouds = itemView.findViewById(R.id.tvClouds)
        tvTempMax = itemView.findViewById(R.id.tvTempMax)
        tvTempMin = itemView.findViewById(R.id.tvTempMin)
    }

    @SuppressLint("SetTextI18n")
    fun bind(daily: Daily) {

        tvDay?.text = Shared.getDateTime(daily.dt.toString(),"EEE")
        tvClouds?.text =  daily.clouds.toString() + itemView.context.getString(R.string.percent_sign)
        tvTempMax?.text = daily.temp.max.roundToInt().toString() + itemView.context.getString(R.string.temp_sign)
        tvTempMin?.text = daily.temp.min.roundToInt().toString() + itemView.context.getString(R.string.temp_sign)
    }
}