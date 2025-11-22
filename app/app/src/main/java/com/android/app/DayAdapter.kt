package com.android.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.graphics.drawable.GradientDrawable

class DayAdapter(
    private val days: List<Int>,
    private val events: Map<Int, List<String>>
) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.dayText)
        private val eventDotsContainer: LinearLayout = itemView.findViewById(R.id.eventDotsContainer)

        fun bind(day: Int) {
            if (day == 0) {
                dayText.text = ""
                eventDotsContainer.removeAllViews()
            } else {
                dayText.text = day.toString()

                eventDotsContainer.removeAllViews()
                val dayEvents = events[day] ?: emptyList()

                for (eventColor in dayEvents) {
                    val dot = ImageView(itemView.context)

                    val colorInt = when (eventColor) {
                        "blue" -> itemView.context.getColor(android.R.color.holo_blue_bright)
                        "green" -> itemView.context.getColor(android.R.color.holo_green_light)
                        "red" -> itemView.context.getColor(android.R.color.holo_red_light)
                        else -> itemView.context.getColor(android.R.color.transparent)
                    }

                    val drawable = GradientDrawable().apply {
                        shape = GradientDrawable.OVAL
                        setColor(colorInt)
                    }
                    dot.background = drawable

                    val params = LinearLayout.LayoutParams(16, 16)
                    params.marginStart = 3
                    params.marginEnd = 3
                    dot.layoutParams = params

                    eventDotsContainer.addView(dot)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_pacientes, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size
}
