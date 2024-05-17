package com.test.landmarkremark.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.test.landmarkremark.R

class LoadingDialog(context: Context) : Dialog(context) {

	companion object {
		private var instance: LoadingDialog? = null
		fun showLoading(context: Context?) {
			if (context == null) return
			CoroutineScope(Dispatchers.Main).launch {
				if (instance == null || instance?.isShowing == false) {
					instance = LoadingDialog(context).apply { show() }
				}
			}
		}

		fun dismissLoading() {
			CoroutineScope(Dispatchers.Main).launch {
				instance?.dismiss()
				instance = null
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.dialog_loading)
//        progressBar.progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
		window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		setCancelable(false)
	}
}