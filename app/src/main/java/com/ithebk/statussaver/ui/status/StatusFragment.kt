package com.ithebk.statussaver.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ithebk.statussaver.BuildConfig
import com.ithebk.statussaver.R
import com.ithebk.statussaver.data.STATUS_TYPE
import com.ithebk.statussaver.data.Status
import java.io.File

class StatusFragment : Fragment() {
    private lateinit var textStatus :TextView
    private lateinit var frameEmpty :LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_status, container, false)
        val targetId = (arguments?.getInt("target_id") ?: 0)
        val statusListStr: String? = arguments?.getString("status_list")
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        frameEmpty = root.findViewById(R.id.frame_empty_status)
        textStatus = root.findViewById(R.id.text_view_status)
        if (context != null) {
            recyclerView.adapter = StatusAdapter(getStatusList(statusListStr, targetId).toMutableList(), context!!, targetId, getMainPath())
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.setHasFixedSize(true)
        }
        return root
    }

    private fun showStatus(value: Int) {
        textStatus.text = value.toString() + " status found"
        if(value == 0) {
            frameEmpty.visibility = View.VISIBLE
        }
        else {
            frameEmpty.visibility = View.GONE

        }
    }

    private fun getMainPath(): String? {
        if(context!=null) {
            val mainPath: String? = context!!.getExternalFilesDir(null)?.absolutePath
            if (mainPath != null) {
                val extraPortion = ("Android/data/" + BuildConfig.APPLICATION_ID
                        + File.separator + "files")
                return mainPath.replace(extraPortion, "")
            }
        }
        return null;
    }
    private fun getStatusList(statusListStr: String?, targetId: Int): List<Status> {
        val turnsType = object : TypeToken<List<Status>>() {}.type
        var list = Gson().fromJson<List<Status>>(statusListStr, turnsType)
        if (targetId == R.id.navigation_images) {
            list = list.filter { it.type == STATUS_TYPE.IMAGE }

        } else if(targetId == R.id.navigation_videos) {
            list = list.filter { it.type == STATUS_TYPE.VIDEO }
        }
        showStatus(list.size)
        return list
    }

    companion object {
        private const val STATUS_LIST = "status_list"
        private const val TARGET_ID = "target_id"

        fun newInstance(
            statusList: String,
            targetId: Int
        ) = StatusFragment().apply {
            arguments = bundleOf(
                STATUS_LIST to statusList,
                TARGET_ID to targetId
            )
        }
    }
}
