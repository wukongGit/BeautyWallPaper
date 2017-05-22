package com.sunc.beautypaper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CAMERA = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_hello.setOnClickListener {
            checkPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startWallpaper()
                } else {
                    Toast.makeText(this, getString(R.string.need_camera_permission), Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf<String>(Manifest.permission.CAMERA),
                    PERMISSIONS_REQUEST_CAMERA)
        } else {
            startWallpaper()
        }
    }

    fun startWallpaper() {
        val pickWallpaper = Intent(Intent.ACTION_SET_WALLPAPER)
        val chooser = Intent.createChooser(pickWallpaper, getString(R.string.select_wallpaper))
        startActivity(chooser)
    }
}
