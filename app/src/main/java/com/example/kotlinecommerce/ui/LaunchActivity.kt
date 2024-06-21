package com.example.kotlinecommerce.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinecommerce.R
import com.example.kotlinecommerce.data.ShoppingAppSessionManager

import com.example.kotlinecommerce.ui.loginSignup.LoginSignupActivity


@SuppressLint("CustomSplashScreen")
class LaunchActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)
		setLaunchScreenTimeOut()
	}

	private fun setLaunchScreenTimeOut() {
		Looper.myLooper()?.let {
			Handler(it).postDelayed({
				startPreferredActivity()
			}, TIME_OUT)
		}
	}

	private fun startPreferredActivity() {
		val sessionManager = ShoppingAppSessionManager(this)
		if (sessionManager.isLoggedIn()) {
			launchHome(this)
			finish()
		} else {
			val intent = Intent(this, LoginSignupActivity::class.java)
			startActivity(intent)
			finish()
		}
	}

	companion object {
		private const val TIME_OUT: Long = 1500
	}
}