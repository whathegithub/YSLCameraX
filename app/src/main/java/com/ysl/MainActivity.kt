package com.ysl

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null

    companion object {     //相当于static
        const val TAG = "MainActivity"
        val REQUIRE_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
        const val REQUEST_CAMERA_CODE = 10
        var outputDirectory : File? = null
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun allPermissionGrant(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        camera_capture_button.setOnClickListener {
            if (allPermissionGrant()) {
                capture()
            }
        }
        outputDirectory = getOutputDirectory()
        //1.检查权限
        //2.configration usecase
        if (allPermissionGrant()) {
            initNessarySetting()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRE_PERMISSION, REQUEST_CAMERA_CODE)
        }


    }

    private fun initNessarySetting() {
        //1.获取ProcessCameraProvider

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        //2.检查provider可用性   ,可用后会通过addListener回调
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val cameraSelector = CameraSelector.Builder().requireLensFacing(
            CameraSelector.LENS_FACING_BACK
        ).build()

        /**
         * 4.配置 Usecase
         * 1.Preview
         * 2.ImageCapture
         * 3.ImageAnalyse
         * 4.VideoCapture
         */
        val preview = Preview.Builder().build()//preview
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .build()

        /**
         * 3.绑定到生命周期
         * 注意第三个参数 usecase 为可变参数
         */
        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        preview.setSurfaceProvider(viewFinder.createSurfaceProvider(camera.cameraInfo))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback(),
                ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.d(MainActivity.TAG, "onError-----:" + exception.message)
                }
            })
    }


    class ImageCallback : ImageCapture.OnImageCapturedCallback() {

    }


    fun switchCamera(cameraId: Int) {

    }

}