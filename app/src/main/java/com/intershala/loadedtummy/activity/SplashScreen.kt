package com.intershala.loadedtummy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.intershala.loadedtummy.R

class SplashScreen : AppCompatActivity() {

        /*--    ANIMATED SPLASH SCREEN  --*/
        lateinit var handler : Handler
        private val splashScreenTimeOut =1000L

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.splash_screen)

            handler  = Handler()
            handler.postDelayed({
                startActivity(Intent(this, Login::class.java))
                finish()
            }, splashScreenTimeOut)
        }

    }