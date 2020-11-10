package com.ysl;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener  {
    private CameraRootView rootView;
    private SurfaceTexture mSurfaceTexture;
    float[] mtx = new float[16];
    private CameraFilter cameraFilter;
    private int textureId;

    public CameraRender(CameraRootView rootView, SurfaceTexture surfaceTexture){
        this.rootView = rootView;
        this.mSurfaceTexture = surfaceTexture;
    }

    /**
     * 第一次创建时调用
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        int textures[] = new int[1];
//        GLES20.glGenTextures(1, textures, 0);
//        int textureId = textures[0];
//        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES,textureId);
        mSurfaceTexture.attachToGLContext(textureId);
        mSurfaceTexture.setOnFrameAvailableListener(this);


        GLES20.glClearColor(0,0,0,0); // 清屏
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); // 清屏
        cameraFilter = new CameraFilter(rootView.getContext());

//        GLES20.glGenTextures(1,textures,0);
//        textureId = textures[0];//textureId可以看成一个texture
//        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES,textureId);
//        SurfaceTexture surfaceTexture = new SurfaceTexture(textureId);
//        surfaceTexture.setOnFrameAvailableListener(this);
//        Surface surface = new Surface(surfaceTexture);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //请求执行一次onDrawFrame

        Log.e("jbw","onFrameAvailable");
        rootView.requestRender();
    }
    /**
     * surface大小变化时调用
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);// 确定视口大小
    }

    /**
     * 绘制
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
       mSurfaceTexture.updateTexImage();
       cameraFilter.onDraw(mtx,textureId);
    }

}
