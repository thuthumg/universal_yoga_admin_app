package com.smh.ttm.universalyogaadminapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.DayOfWeekItem

class DayOfWeekAdapter(
    private val days: List<DayOfWeekItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DayOfWeekAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(day: DayOfWeekItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.tvDay)

        fun bind(day: DayOfWeekItem) {
            dayText.text = day.day
            itemView.setBackgroundColor(if (day.isSelected) Color.YELLOW else Color.TRANSPARENT)

            itemView.setOnClickListener {
                listener.onItemClick(day)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_of_week, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size
}
