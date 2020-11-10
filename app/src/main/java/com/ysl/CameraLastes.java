package com.ysl;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class CameraLastes extends AppCompatActivity {

    private String[] REQUIRE_PERMISSION = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int REQUEST_CAMERA_CODE = 10;
    private CameraRootView mRootView;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootView = findViewById(R.id.root_view);

        if (!allPermissionGrant()) {
            ActivityCompat.requestPermissions(this, REQUIRE_PERMISSION, REQUEST_CAMERA_CODE);
        }

        startPreview();
    }

    private boolean allPermissionGrant() {
        return ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void onRequestPermissionsResult(
           int  requestCode,
           String[] permissions,
           int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults[0] == 0) {
                startPreview();
            } else {
                finish();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void startPreview() {
        //选择摄像头
//        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build();
//
//
//        ListenableFuture<ProcessCameraProvider> processCameraProviderFuture = ProcessCameraProvider.getInstance(this);
//        processCameraProviderFuture.addListener(new Runnable() {
//            @Override
//            public void run() {
//                initUseCase();
//
//                try {
//                    ProcessCameraProvider processCameraProvider = processCameraProviderFuture.get();
//                    processCameraProvider.unbindAll();
//                    //设置usecase :(预览Preview,拍照ImageCapture)
//                    processCameraProvider.bindToLifecycle(CameraLastes.this, cameraSelector, getPreview());
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, getMainExecutor());
    }

//    private Preview getPreview() {
//        Preview preview = new Preview.Builder()
//                .build();
//
//        Preview.SurfaceProvider surfaceProvider = new Preview.SurfaceProvider() {
//            @RequiresApi(api = Build.VERSION_CODES.P)
//            @Override
//            public void onSurfaceRequested(@NonNull SurfaceRequest request) {
//                //tmd,这个参数配置的好难
//                request.provideSurface(mRootView.getHolder().getSurface(), ContextCompat.getMainExecutor(mRootView.getContext()), new Consumer<SurfaceRequest.Result>() {
//                    @Override
//                    public void accept(SurfaceRequest.Result result) {
//
//                    }
//                });
//            }
//        };
//        preview.setSurfaceProvider(surfaceProvider);
//        return preview;
//    }


}
