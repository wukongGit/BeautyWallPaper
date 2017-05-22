package com.sunc.beautypaper

/**
 * Description:
 * Date: 2017-05-15 09:40
 * Author: suncheng
 */
import android.hardware.Camera
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder

class BeautyWallpaper : WallpaperService() {
    override fun onCreateEngine(): WallpaperService.Engine {
        return CameraEngine()
    }

    internal inner class CameraEngine : WallpaperService.Engine(), Camera.PreviewCallback {
        private var camera: Camera? = null

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)

            startPreview()
            setTouchEventsEnabled(true)
        }

        override fun onTouchEvent(event: MotionEvent) {
            super.onTouchEvent(event)
        }

        override fun onDestroy() {
            super.onDestroy()
            stopPreview()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            if (visible) {
                startPreview()
            } else {
                stopPreview()
            }
        }

        /**
         * 开始预览
         */
        fun startPreview() {
            camera = Camera.open()
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
        }

        /**
         * 停止预览
         */
        fun stopPreview() {
            if (camera != null) {
                camera!!.stopPreview()
                camera!!.setPreviewCallback(null)
                camera!!.release()
                camera = null
            }
        }

        override fun onPreviewFrame(bytes: ByteArray, camera: Camera) {
            camera.addCallbackBuffer(bytes)
        }
    }
}

