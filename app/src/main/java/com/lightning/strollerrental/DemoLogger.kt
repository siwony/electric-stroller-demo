package com.lightning.strollerrental

import android.util.Log

object DemoLogger {
    private const val TAG = "StrollerDemo"

    fun logScreenView(screenName: String) {
        Log.d(TAG, "ScreenView: $screenName")
    }

    fun logClick(buttonName: String) {
        Log.d(TAG, "Click: $buttonName")
    }

    fun logFlow(step: Int, screen: String) {
        Log.d(TAG, "Flow: step=$step, screen=$screen")
    }

    fun logFlowError(code: String, atStep: Int) {
        Log.e(TAG, "FlowError: code=$code, atStep=$atStep")
    }
}
