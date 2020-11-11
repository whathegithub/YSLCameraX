package com.ysl;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Camera extends AppCompatActivity {

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
        Button takePhoto = findViewById(R.id.camera_capture_button);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (!allPermissionGrant()) {
            ActivityCompat.requestPermissions(this, REQUIRE_PERMISSION, REQUEST_CAMERA_CODE);
        }
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
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults[0] == 0) {
//                startPreview();
            } else {
                finish();
            }
        }
    }


}
