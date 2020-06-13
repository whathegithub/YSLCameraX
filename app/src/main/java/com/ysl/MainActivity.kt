package com.ysl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.camera_capture_button).setOnClickListener {
            Toast.makeText(this,"shutter !",Toast.LENGTH_SHORT).show()
        }
    }
}