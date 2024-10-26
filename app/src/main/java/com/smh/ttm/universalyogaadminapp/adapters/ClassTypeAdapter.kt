package com.smh.ttm.universalyogaadminapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.ClassTypeItem

class ClassTypeAdapter(
    private val classTypes: List<ClassTypeItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ClassTypeAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(classType: ClassTypeItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val classTypeTextView: TextView = itemView.findViewById(R.id.tvClassType)

        fun bind(classType: ClassTypeItem) {
            classTypeTextView.text = classType.type // Set the class type text

            // Change background color based on selection state
            if (classType.isSelected) {
                itemView.setBackgroundColor(Color.YELLOW) // Change to your selected color
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT) // Default color
            }

            itemView.setOnClickListener {
                listener.onItemClick(classType) // Trigger the listener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(classTypes[position]) // Bind data to ViewHolder
    }

    override fun getItemCount(): Int = classTypes.size // Return the item count
}
