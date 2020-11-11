package com.ysl;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class CameraFilter  {

    private int vPosition;
    private int vCoord;
    private int vTexture;
    private int program;
    private int vMatrix;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;

    public CameraFilter(Context context) {
        String fragShader = OpenGLUtils.readRawTextFile(context, R.raw.frag_shader);
        String vertexShader = OpenGLUtils.readRawTextFile(context, R.raw.vert_shader);

        program = OpenGLUtils.loadProgram(vertexShader, fragShader);
        GLES20.glUseProgram(program);

         vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        vCoord = GLES20.glGetAttribLocation(program, "vCoord");
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");


        //坐标
        vertexBuffer = ByteBuffer.allocateDirect(OpenGLUtils.VERTEX.length*4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(OpenGLUtils.VERTEX);


        textureBuffer = ByteBuffer.allocateDirect(OpenGLUtils.TEXTURE.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textureBuffer.clear();
        textureBuffer.put(OpenGLUtils.TEXTURE);
    }

    public void onDraw(float[] mtx, int texture) {
        //传递坐标,顶点坐标,确定形状
       vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,vertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        //传递纹理坐标,贴图
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,0,textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        //用来激活一个用来显示的画框
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture);
        GLES20.glUniform1i(vTexture,0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0 ,4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
    }

    public void release() {
        GLES20.glDeleteProgram(program);
    }
}
