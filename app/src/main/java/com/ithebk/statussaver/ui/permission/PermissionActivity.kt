package com.ithebk.statussaver.ui.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ithebk.statussaver.R
import com.ithebk.statussaver.ui.main.MainActivity
import com.ithebk.statussaver.util.Utility
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {
    private var PERMISSION_CODE: Int = 1
    private val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private var hasPermission :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        frame_permission_setting.visibility = View.GONE
        initPermission()
        bt_permission_settings.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            finish()
        }
    }
    private fun startActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun initPermission() {
        if(!Utility.hasPermissions(applicationContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE)
        }
        else {
            startActivity()
        }
    }



    private fun isSelectedNeverAskAgainPermissions(permissions: Array<out String>): Boolean {
        for (permission in permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if(Utility.hasPermissions(applicationContext, PERMISSIONS)) {
                startActivity()
            }
            else if(isSelectedNeverAskAgainPermissions(PERMISSIONS)){
                frame_permission_setting.visibility = View.VISIBLE
            }
            else {
                initPermission()
            }
        }
    }
}
