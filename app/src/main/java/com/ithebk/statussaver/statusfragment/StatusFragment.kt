package com.ithebk.statussaver.statusfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ithebk.statussaver.R

class StatusFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_status, container, false)
        println("Key1:" + (arguments?.getInt("Key1") ?: "No data"))
        return root
    }
}
