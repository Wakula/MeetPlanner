package com.example.meetplanner.overview

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.meetplanner.R
import com.example.meetplanner.databinding.OverviewListItemBinding
import com.example.meetplanner.network.OverviewMeetingProperty
import kotlinx.android.synthetic.main.overview_list_item.view.*
import java.time.LocalDateTime

//fun formatDatetime(datetime: LocalDateTime) {
//    val year = datetime.
//    val day =
//    val month
//    val time
//}

class OverviewAdapter(private val onClickListener: OnClickListener):
    ListAdapter<OverviewMeetingProperty, OverviewAdapter.OverviewMeetingPropertyViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewMeetingPropertyViewHolder {
        return OverviewMeetingPropertyViewHolder(
            OverviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OverviewMeetingPropertyViewHolder, position: Int) {
        val property = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(property)
        }
        holder.bind(property)
    }

    class OverviewMeetingPropertyViewHolder(val binding: OverviewListItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(property: OverviewMeetingProperty) {
            binding.property = property
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<OverviewMeetingProperty>() {
        override fun areItemsTheSame(oldItem: OverviewMeetingProperty, newItem: OverviewMeetingProperty): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: OverviewMeetingProperty, newItem: OverviewMeetingProperty): Boolean {
            return oldItem.id == newItem.id
        }
    }
    class OnClickListener(val clickListener: (meetingProperty: OverviewMeetingProperty) -> Unit) {
        fun onClick(meetingProperty: OverviewMeetingProperty) = clickListener(meetingProperty)
    }
}
