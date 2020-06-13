package com.ysl

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {     //相当于static
        const val TAG = "MainActivity"
        val REQUIRE_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
        const val REQUEST_CAMERA_CODE = 10
    }

    private fun allPermissionGrant(): Boolean {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        camera_capture_button.setOnClickListener {
            Toast.makeText(this, "shutter !", Toast.LENGTH_SHORT).show()
        }

        //1.检查权限
        //2.configration usecase
        if (allPermissionGrant()) {
            initNessarySetting()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRE_PERMISSION, REQUEST_CAMERA_CODE)
        }

    }

    private fun initNessarySetting() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.Builder().requireLensFacing(
                CameraSelector.LENS_FACING_BACK
            ).build()

            /**
             * Usecase
             * 1.Preview
             * 2.ImageCapture
             * 3.ImageAnalyse
             * 4.VideoCapture
             */
            val preview = Preview.Builder().build()//preview
            val imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY).build()

            //注意第三个参数 usecase 为可变参数
            val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            preview.setSurfaceProvider(viewFinder.createSurfaceProvider(camera.cameraInfo))
        },ContextCompat.getMainExecutor(this))
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_CODE) {
            Log.d(TAG, "grantresult:" + grantResults.get(0))

            if (grantResults.get(0) == 0) {
                initNessarySetting()
            } else {
                finish()
            }
        }

    }


    private fun capture() {

    }

}