package com.ithebk.statussaver.ui.status

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ithebk.statussaver.R
import com.ithebk.statussaver.data.STATUS_TYPE
import com.ithebk.statussaver.data.Status
import kotlinx.android.synthetic.main.status_item_view.view.*
import java.io.*
import java.util.*


class StatusAdapter(
    private val dataSets: MutableList<Status>,
    private val context: Context,
    private val targetId: Int,
    private val mainPath: String?
) :
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

    private fun view(file: File, statusType: STATUS_TYPE) {
        var fileData : Uri = FileProvider.getUriForFile(
            context,
            context.packageName, file
        )
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(fileData, if (statusType == STATUS_TYPE.IMAGE) "image/*" else "video/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
           shareIntent
        )
    }

    private fun shareImage(uriToImage: String, statusType: STATUS_TYPE) {
        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            context.packageName, File(uriToImage)
        )
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = if (statusType == STATUS_TYPE.IMAGE) "image/jpg" else "video/mp4"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.resources.getText(R.string.send_to)
            )
        )
    }

    private fun deleteFile(file: File, position: Int) {
        if (file.exists()) {
            file.delete()
            Toast.makeText(context, "File has been deleted", Toast.LENGTH_SHORT).show()
            dataSets.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    private fun copyFile(source: File) {
        println("Date before copy::" + Date(System.currentTimeMillis()))
        val dest: File = File(
            mainPath + "WhatsAppStatus/" + source.name + "." + source.extension
        )
        if (source.exists()) {
            if (!dest.exists()) {
                val inputStream: InputStream = FileInputStream(source)
                val outputStream: OutputStream = FileOutputStream(dest)
                val buf = ByteArray(1024)
                var len: Int = inputStream.read(buf)
                while (len > 0) {
                    outputStream.write(buf, 0, len)
                    len = inputStream.read(buf)
                }
                inputStream.close()
                outputStream.close()
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Already exits", Toast.LENGTH_SHORT).show()
            }
        }
        println("Date after copy::" + Date(System.currentTimeMillis()))
    }

    override fun getItemCount() = dataSets.size

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val statusData: Status = dataSets.get(position)
        Glide.with(context).load(statusData.path).into(holder.itemView.image_view)
        if (statusData.type == STATUS_TYPE.VIDEO) {
            holder.itemView.image_type_video.visibility = View.VISIBLE
        } else {
            holder.itemView.image_type_video.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            view(File(statusData.path), statusData.type)
        }

        if (targetId == R.id.navigation_saved) {
            holder.itemView.fab_download.visibility = View.GONE
            holder.itemView.fab_delete.visibility = View.VISIBLE
        } else {
            holder.itemView.fab_download.visibility = View.VISIBLE
            holder.itemView.fab_delete.visibility = View.GONE
        }
        holder.itemView.fab_share.setOnClickListener {
            shareImage(statusData.path, statusData.type)
        }
        holder.itemView.fab_download.setOnClickListener {
            copyFile(File(statusData.path))
        }
        holder.itemView.fab_delete.setOnClickListener {
            deleteFile(File(statusData.path), position)
        }
    }
}