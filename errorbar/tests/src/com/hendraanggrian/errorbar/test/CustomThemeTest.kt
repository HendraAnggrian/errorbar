package com.hendraanggrian.errorbar.test

import android.support.design.widget.Errorbar
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.widget.FrameLayout
import com.hendraanggrian.errorbar.test.activity.CustomThemeActivity
import org.jetbrains.anko.toast
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@RunWith(AndroidJUnit4::class)
class CustomThemeTest : BaseTest() {

    @Rule @JvmField val rule = ActivityTestRule(CustomThemeActivity::class.java)

    @Test
    fun test() {
        onView(withId(R.id.toolbar)).perform(setTitle("Here's an errorbar"))
        onView(withId(R.id.frameLayout)).perform(object : ViewAction {
            override fun getConstraints() = isAssignableFrom(FrameLayout::class.java)
            override fun getDescription() = FrameLayout::class.java.name
            override fun perform(uiController: UiController, view: View) {
                Errorbar.make(view as FrameLayout, "No internet connection.", Errorbar.LENGTH_INDEFINITE)
                        .setAction("Retry", View.OnClickListener { v ->
                            v.context.toast("Clicked!")
                        })
                        .show()
            }
        })
        onView(withId(R.id.progressBar)).perform(delay(5000))
    }
}