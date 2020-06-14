package com.ysl.config

import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig

class PreviewConfig : CameraXConfig.Provider {

    override fun getCameraXConfig(): CameraXConfig {
            return Camera2Config.defaultConfig()
    }

}