package com.ysl;

import android.util.Size;

import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;

public class CameraHelper  {

   public  static void init(LifecycleOwner lifecycleOwner, Preview.OnPreviewOutputUpdateListener listener){
       CameraX.bindToLifecycle(lifecycleOwner,getPreview(listener));
    }

    private static Preview getPreview(Preview.OnPreviewOutputUpdateListener listener) {
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetResolution(new Size(720, 1280))
                .setLensFacing(CameraX.LensFacing.BACK)
                .build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(listener);
        return preview;
    }

}
