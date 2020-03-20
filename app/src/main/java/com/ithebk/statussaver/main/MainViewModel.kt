package com.ithebk.statussaver.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ithebk.statussaver.BuildConfig
import com.ithebk.statussaver.data.STATUS_TYPE
import com.ithebk.statussaver.data.Status
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val _imageList = MutableLiveData<List<Status>>()
    val imageList: LiveData<List<Status>>
        get() = _imageList;

    fun init() {
        val mainPath: String? = context.getExternalFilesDir(null)?.absolutePath
        if (mainPath != null) {
            val extraPortion = ("Android/data/" + BuildConfig.APPLICATION_ID
                    + File.separator + "files")
            val validPath = mainPath.replace(extraPortion, "") + "WhatsApp/Media/.Statuses"
            val files: MutableList<File> = File(validPath).listFiles().toMutableList()
            // files.filter { it.extension == "jpg" }
            files.sortByDescending { it.lastModified() }
            val statusList: MutableList<Status> = mutableListOf()
            files.iterator().forEach {
                statusList.add(
                    Status(
                        it.absolutePath,
                        STATUS_TYPE.IMAGE
                    )
                );
            }
            println("Printing size:"+statusList.size)
            _imageList.postValue(statusList);

        }
    }
}