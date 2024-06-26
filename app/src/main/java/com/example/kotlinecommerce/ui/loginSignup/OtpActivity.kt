package com.example.kotlinecommerce.ui.loginSignup

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinecommerce.R
import com.example.kotlinecommerce.data.UserData
import com.example.kotlinecommerce.databinding.ActivityOtpBinding
import com.example.kotlinecommerce.ui.OTPStatus
import com.example.kotlinecommerce.ui.launchHome
import com.example.kotlinecommerce.viewModels.OtpViewModel
import com.google.android.material.snackbar.Snackbar


class OtpActivity : AppCompatActivity() {
	private lateinit var binding: ActivityOtpBinding

	private lateinit var viewModel: OtpViewModel

	private lateinit var fromWhere: String

	class OtpViewModelFactory(
		private val application: Application, private val uData: UserData
	) : ViewModelProvider.Factory {
		@Suppress("UNCHECKED_CAST")
		override fun <T : ViewModel?> create(modelClass: Class<T>): T {
			if (modelClass.isAssignableFrom(OtpViewModel::class.java)) {
				return OtpViewModel(application, uData) as T
			}
			throw IllegalArgumentException("Unknown ViewModel Class")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityOtpBinding.inflate(layoutInflater)
		val uData: UserData? = intent.getParcelableExtra("uData")
		fromWhere = intent.getStringExtra("from").toString()
		if (uData != null) {
			val viewModelFactory = OtpViewModelFactory(application, uData)
			viewModel =
				ViewModelProvider(this, viewModelFactory).get(OtpViewModel::class.java)

			viewModel.verifyPhoneOTPStart(uData.mobile, this)
		}
		setViews()

		setObservers()
		setContentView(binding.root)
	}

	private fun setObservers() {
		viewModel.otpStatus.observe(this) {
			when (it) {
				OTPStatus.WRONG -> binding.otpVerifyError.visibility = View.VISIBLE
				else -> binding.otpVerifyError.visibility = View.GONE
			}
		}

		viewModel.isUserLoggedIn.observe(this) {
			if (it == true) {
				if (fromWhere == getString(R.string.signup_fragment_label)) {
					viewModel.signUp()
				} else {
					val rememberMe = intent.getBooleanExtra("loginRememberMe", false)
					viewModel.login(rememberMe)
				}
				launchHome(this)
				finish()
			}
		}

		viewModel.isOTPSent.observe(this) {
			if(it == true) {
				binding.loaderLayout.loaderCard.visibility = View.GONE
				val contextView = binding.loaderLayout.loaderCard
				Snackbar.make(contextView, R.string.otp_sent_msg, Snackbar.LENGTH_SHORT).show()
			}
		}
	}

	private fun setViews() {
		binding.otpVerifyError.visibility = View.GONE
		binding.loaderLayout.loaderCard.visibility = View.VISIBLE
		binding.loaderLayout.loadingMessage.text = getString(R.string.sending_otp_msg)
		binding.loaderLayout.circularLoader.showAnimationBehavior
		binding.otpVerifyBtn.setOnClickListener {
			onVerify()
		}
	}

	private fun onVerify() {
		val otp = binding.otpOtpEditText.text.toString()
		viewModel.verifyOTP(otp)

	}
}