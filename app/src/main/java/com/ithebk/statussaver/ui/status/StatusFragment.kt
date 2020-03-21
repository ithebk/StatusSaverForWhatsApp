package com.ithebk.statussaver.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ithebk.statussaver.R
import com.ithebk.statussaver.data.STATUS_TYPE
import com.ithebk.statussaver.data.Status
import kotlinx.android.synthetic.main.fragment_status.*

class StatusFragment : Fragment() {
    private lateinit var textStatus :TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_status, container, false)
        val targetId = (arguments?.getInt("target_id") ?: 0)
        val statusListStr: String? = arguments?.getString("status_list")
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        textStatus = root.findViewById(R.id.text_view_status)
        if (context != null) {
            recyclerView.adapter = StatusAdapter(getStatusList(statusListStr, targetId), context!!)
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.setHasFixedSize(true)
        }
        return root
    }

    private fun showStatus(value: Int) {
        if (value > 0) {
            textStatus.visibility = View.VISIBLE
            textStatus.text = value.toString() + " status found"
        } else {
            textStatus.visibility = View.GONE
        }
    }

    private fun getStatusList(statusListStr: String?, targetId: Int): List<Status> {
        val turnsType = object : TypeToken<List<Status>>() {}.type
        var list = Gson().fromJson<List<Status>>(statusListStr, turnsType)
        if (targetId == R.id.navigation_images) {
            println("Hello1")
            list = list.filter { it.type == STATUS_TYPE.IMAGE }

        } else {
            println("Hello2")
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
