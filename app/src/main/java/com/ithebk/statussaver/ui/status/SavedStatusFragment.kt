package com.ithebk.statussaver.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ithebk.statussaver.BuildConfig
import com.ithebk.statussaver.R
import com.ithebk.statussaver.data.STATUS_TYPE
import com.ithebk.statussaver.data.Status
import java.io.File

class SavedStatusFragment : Fragment() {
    private lateinit var textStatus: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_status, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        textStatus = root.findViewById(R.id.text_view_status)
        textStatus.visibility = View.GONE
        if (context != null) {
            recyclerView.adapter =
                StatusAdapter(getStatusList().toMutableList(), context!!, R.id.navigation_saved, getMainPath())
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.setHasFixedSize(true)
        }
        return root
    }

    private fun showStatus(value: Int) {
        textStatus.text = value.toString() + " status found"
    }

    private fun getMainPath(): String? {
        if (context != null) {
            val mainPath: String? = context!!.getExternalFilesDir(null)?.absolutePath
            if (mainPath != null) {
                val extraPortion = ("Android/data/" + BuildConfig.APPLICATION_ID
                        + File.separator + "files")
                return mainPath.replace(extraPortion, "")
            }
        }
        return null;
    }

    private fun getStatusList(): List<Status> {
        val mainPath: String? = context?.getExternalFilesDir(null)?.absolutePath
        val savedStatusList: MutableList<Status> = mutableListOf()
        if (mainPath != null) {
            val extraPortion = ("Android/data/" + BuildConfig.APPLICATION_ID
                    + File.separator + "files")
            val validPath = mainPath.replace(extraPortion, "")
            val folderSaved: File = File(validPath + "WhatsAppStatus")
            folderSaved.mkdirs()
            if (folderSaved.listFiles() != null) {
                val files: MutableList<File> = folderSaved.listFiles().toMutableList()
                files.sortByDescending { it.lastModified() }
                files.iterator().forEach {
                    var extension: STATUS_TYPE? = null;
                    if (it.extension == "jpg") {
                        extension = STATUS_TYPE.IMAGE;
                    } else if (it.extension == "mp4") {
                        extension = STATUS_TYPE.VIDEO
                    }
                    if (extension != null) {
                        savedStatusList.add(
                            Status(
                                it.absolutePath,
                                extension
                            )
                        );
                    }

                }
            }

        }
        showStatus(savedStatusList.size)
        return savedStatusList
    }
}
