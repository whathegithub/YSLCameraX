package com.ysl

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.camera2.internal.Camera2CameraInfoImpl
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    var curCameraId : Int = CameraSelector.LENS_FACING_BACK

    companion object {     //相当于static
        const val TAG = "MainActivity"
        val REQUIRE_PERMISSION = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        const val REQUEST_CAMERA_CODE = 10
        var outputDirectory: File? = null
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEvent()
        outputDirectory = getOutputDirectory()

        //1.获取ProcessCameraProvider
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        //1.检查权限
        //2.configration usecase
        if (allPermissionGrant()) {
            switchCamera(CameraSelector.LENS_FACING_BACK)
        } else {
            ActivityCompat.requestPermissions(this, REQUIRE_PERMISSION, REQUEST_CAMERA_CODE)
        }
    }

    fun switchCamera(cameraId: Int) {
        //2.检查provider可用性   ,可用后会通过addListener回调
        cameraProviderFuture?.addListener(Runnable {
            val cameraProvider = cameraProviderFuture?.get()
            val cameraSelector = CameraSelector.Builder().requireLensFacing(
                cameraId
            ).build()
            Log.d(TAG,"cameraID switchCamera:"+cameraId)

            bindPreview(cameraSelector, cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }


    fun bindPreview(selector: CameraSelector, cameraProvider: ProcessCameraProvider?) {
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

        //before rebind need unbind
        cameraProvider?.unbindAll()


        /**
         * 3.绑定到生命周期
         * 注意第三个参数 usecase 为可变参数
         */
        val  camera = cameraProvider?.bindToLifecycle(this, selector, preview, imageCapture)

        //5.camera绑定到preview
        preview.setSurfaceProvider(viewFinder.createSurfaceProvider(camera?.cameraInfo))
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_CODE) {
            Log.d(TAG, "grantresult:" + grantResults.get(0))

            if (grantResults.get(0) == 0) {
                switchCamera(CameraSelector.LENS_FACING_BACK)
            } else {
                finish()
            }
        }
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
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initEvent() {
        camera_capture_button.setOnClickListener {
            if (allPermissionGrant()) {
                capture()
            }
        }
        camera_switch_button.setOnClickListener {
            when(curCameraId){
                CameraSelector.LENS_FACING_BACK-> curCameraId = CameraSelector.LENS_FACING_FRONT
                else -> curCameraId = CameraSelector.LENS_FACING_BACK
            }
            switchCamera(curCameraId)
        }
    }

}