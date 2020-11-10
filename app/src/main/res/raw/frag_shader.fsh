#extension GL_OES_EGL_image_external : require

precision mediump float; //使所有没有显示表明精度的变量都按照设定好的默认精度来处理.

varying vec2 aCoord;

uniform samplerExternalOES vTexture; //

void main() {
    gl_FragColor = texture2D(vTexture,aCoord);
}