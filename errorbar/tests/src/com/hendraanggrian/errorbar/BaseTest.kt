package com.hendraanggrian.errorbar

import android.os.Build
import android.os.CountDownTimer
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.v7.widget.Toolbar
import android.view.View
import com.hendraanggrian.errorbar.test.R
import android.widget.ProgressBar
import androidx.core.view.isVisible

abstract class BaseTest {

    fun setTitle(title: String) = object : ViewAction {
        override fun getConstraints() = isAssignableFrom(Toolbar::class.java)
        override fun getDescription() = "setTitle($title)"
        override fun perform(uiController: UiController, view: View) {
            (view as Toolbar).title = title
        }
    }

    fun delay(millis: Long) = object : ViewAction {
        override fun getConstraints() = isAssignableFrom(ProgressBar::class.java)
        override fun getDescription() = "delay($millis)"
        override fun perform(uiController: UiController, view: View) {
            val progressBar = view as ProgressBar
            progressBar.isVisible = true
            progressBar.progress = 100
            object : CountDownTimer(millis, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressBar.setProgress((progressBar.max * millisUntilFinished / millis).toInt(), true)
                    } else {
                        progressBar.progress = (progressBar.max * millisUntilFinished / millis).toInt()
                    }
                }

                override fun onFinish() {
                    progressBar.isVisible = false
                }
            }.start()
            uiController.loopMainThreadForAtLeast(millis)
        }
    }
}