package com.satragni.randomlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        object:CountDownTimer(5000,1000){
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
                val mIntent = Intent(this@SplashActivity,MainActivity::class.java)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mIntent)
                finish()
            }
        }.start()
    }
}