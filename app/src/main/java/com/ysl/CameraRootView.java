package com.ysl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

public class CameraRootView extends GLSurfaceView implements Preview.OnPreviewOutputUpdateListener {

    private  CameraRender render;

    public CameraRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //1.使用opengl es 2.0 context
        setEGLContextClientVersion(2);
        Log.e("jbw","CamearRootView");
        //2.设置render
        render = new CameraRender(CameraRootView.this);
        setRenderer(render);//创建GLThread
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        CameraHelper.init((LifecycleOwner) this.getContext(),this);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    @Override
    public void onUpdated(@NonNull Preview.PreviewOutput output) {

        Log.e("jbw","CamearRootView onUpdated");
        render.setSurfaceTexture(output.getSurfaceTexture());
    }
}
