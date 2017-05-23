package com.sunc.beautypaper

import android.Manifest
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import rx.functions.Action1

class MainActivity : AppCompatActivity() {
    private var isHandlePermission: Boolean = false
    private var isHasPermission: Boolean = false
    private var isSaveInstanceState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_hello.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        val action = Action1<kotlin.Boolean> {
            isHasPermission = PermissionsChecker.isHasCameraPermission()
            if (isSaveInstanceState) {
                // 不能进行UI操作,需要在onresume的时候处理
                isHandlePermission = true
            } else {
                if (isHasPermission) {
                    startWallpaper()
                } else {
                    showPermissionAlertDialog()
                }
            }
        }
        RxPermissions.getInstance(this).request(Manifest.permission.CAMERA).subscribe(action)
    }

    private fun showPermissionAlertDialog() {
        val dialog = AlertDialog.Builder(this,
                android.support.v7.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle(getString(R.string.need_camera_permission)).setMessage(getString(R.string.open_camera_permission_later)).setPositiveButton("去设置") { dialogInterface, i ->
            dialogInterface.dismiss()
            try {
                val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
                val pkg = "com.android.settings"
                val cls = "com.android.settings.applications.InstalledAppDetails"
                intent.component = ComponentName(pkg, cls)
                intent.data = Uri.parse("package:" + getPackageName())
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

            }
        }.setNegativeButton("我知道了") { dialogInterface, i ->
            dialogInterface.dismiss()
            onBackPressed()
        }.create()
        dialog.show()
    }

    fun startWallpaper() {
        val pickWallpaper = Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)
        pickWallpaper.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, ComponentName(this, BeautyWallpaper::class.java))
        startActivity(pickWallpaper)
    }
}
