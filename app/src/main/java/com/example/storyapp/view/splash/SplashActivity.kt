package com.example.storyapp.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.storyapp.R
import com.example.storyapp.data.preferences.LoginPreferences
import com.example.storyapp.data.preferences.dataStore
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val motionLayout = findViewById<MotionLayout>(R.id.motionLayout)

        motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                // Do nothing
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                // Do nothing
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                navigateToNextScreen()
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                // Do nothing
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            motionLayout.transitionToEnd()
        }, 1500L)
    }

    private fun navigateToNextScreen() {
        runBlocking {
            val userLogin = withContext(Dispatchers.IO) {
                LoginPreferences.getInstance(this@SplashActivity.dataStore).getLoginStatus().first()
            }
            if (userLogin == true) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                startActivity(Intent(applicationContext, WelcomeActivity::class.java))
            }
            finish()
        }
    }
}
