package com.ithebk.statussaver.ui.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ithebk.statussaver.R
import com.ithebk.statussaver.data.STATUS_TYPE
import com.ithebk.statussaver.data.Status
import kotlinx.android.synthetic.main.status_item_view.view.*

class StatusAdapter(private val dataSets: List<Status>, private val context: Context) :
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
        Glide.with(context).load(dataSets.get(position).path).into(holder.itemView.image_view)
        if(dataSets.get(position).type == STATUS_TYPE.VIDEO) {
            holder.itemView.image_type_video.visibility = View.VISIBLE
        }
        else {
            holder.itemView.image_type_video.visibility = View.GONE
        }
    }
}