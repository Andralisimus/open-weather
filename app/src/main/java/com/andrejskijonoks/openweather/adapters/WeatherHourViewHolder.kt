package com.andrejskijonoks.openweather.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andrejskijonoks.openweather.Current
import com.andrejskijonoks.openweather.R
import com.andrejskijonoks.openweather.Shared
import kotlin.math.roundToInt

class WeatherHourViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_hour_item, parent, false)) {

    private var tvTime: TextView? = null
    private var tvTemp: TextView? = null
    private var tvClouds: TextView? = null
    private var tvHumidity: TextView? = null
    private var tvWindSpeed: TextView? = null
    private var tvFeelsLike: TextView? = null
    private var tvPressure: TextView? = null
    private var tvUvi: TextView? = null

    init {
        tvTime = itemView.findViewById(R.id.tvTime)
        tvTemp = itemView.findViewById(R.id.tvTemp)
        tvClouds = itemView.findViewById(R.id.tvClouds)
        tvHumidity = itemView.findViewById(R.id.tvHumidity)
        tvWindSpeed = itemView.findViewById(R.id.tvWindSpeed)
        tvFeelsLike = itemView.findViewById(R.id.tvFeelsLike)
        tvPressure = itemView.findViewById(R.id.tvPressure)
        tvUvi = itemView.findViewById(R.id.tvUvi)
    }

    @SuppressLint("SetTextI18n")
    fun bind(current: Current) {
        tvTime?.text = Shared.getDateTime(current.dt.toString(),"HH:mm")
        tvTemp?.text = current.temp.roundToInt().toString() + itemView.context.getString(R.string.temp_sign)
        tvClouds?.text = current.clouds.toString() + itemView.context.getString(R.string.percent_sign)
        tvHumidity?.text = current.humidity.toString() + itemView.context.getString(R.string.percent_sign)
        tvWindSpeed?.text = current.wind_speed.toString()
        tvFeelsLike?.text = current.feels_like.roundToInt().toString() + itemView.context.getString(R.string.temp_sign)
        tvPressure?.text = current.pressure.toString()
        tvUvi?.text = current.uvi.toString()
    }
}