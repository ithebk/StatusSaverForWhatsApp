package com.ithebk.statussaver.statusfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ithebk.statussaver.R
import com.ithebk.statussaver.data.Status

class StatusAdapter(private val dataSets: List<Status>) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {
    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        return StatusViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.status_item_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataSets.size

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {

    }
}