/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.design.internal

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.design.widget.BaseTransientBottomBar
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.hendraanggrian.errorbar.R

/**
 * Actual content of [android.support.design.widget.Errorbar].
 *
 * @see SnackbarContentLayout
 */
open class ErrorbarContentLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.errorbarStyle
) : FrameLayout(context, attrs, defStyleAttr), BaseTransientBottomBar.ContentViewCallback {

    lateinit var backdropView: ImageView
    lateinit var containerView: ViewGroup
    lateinit var imageView: ImageView
    lateinit var textView: TextView
    lateinit var actionView: Button

    // keep TypedArray a little bit longer because views are binded in onFinishInflate()
    @SuppressLint("CustomViewStyleable")
    private val a = context.obtainStyledAttributes(attrs, R.styleable.ErrorbarLayout,
        defStyleAttr, R.style.Widget_Design_Errorbar)

    override fun onFinishInflate() {
        super.onFinishInflate()

        // bind views
        backdropView = findViewById(R.id.errorbar_backdrop)
        containerView = findViewById(R.id.errorbar_container)
        imageView = findViewById(R.id.errorbar_image)
        textView = findViewById(R.id.errorbar_text)
        actionView = findViewById(R.id.errorbar_action)

        // apply attr and finally recycle
        if (a.hasValue(R.styleable.ErrorbarLayout_backdrop)) {
            backdropView { setImageDrawable(a.getDrawable(R.styleable.ErrorbarLayout_backdrop)) }
        }
        if (a.hasValue(R.styleable.ErrorbarLayout_image)) {
            imageView { setImageDrawable(a.getDrawable(R.styleable.ErrorbarLayout_image)) }
        }
        if (a.hasValue(R.styleable.ErrorbarLayout_android_textAppearance)) {
            @Suppress("DEPRECATION") when (Build.VERSION.SDK_INT) {
                23 -> textView.setTextAppearance(
                    a.getResourceId(R.styleable.ErrorbarLayout_android_textAppearance, 0))
                else -> textView.setTextAppearance(context,
                    a.getResourceId(R.styleable.ErrorbarLayout_android_textAppearance, 0))
            }
        }
        if (a.hasValue(R.styleable.ErrorbarLayout_android_textColor)) {
            textView.setTextColor(a.getColorStateList(R.styleable.ErrorbarLayout_android_textColor))
        }
        if (a.hasValue(R.styleable.ErrorbarLayout_android_textSize)) {
            textView.textSize = a.getDimension(R.styleable.ErrorbarLayout_android_textSize, 0f)
        }
        if (a.hasValue(R.styleable.ErrorbarLayout_actionTextAppearance)) {
            @Suppress("DEPRECATION")
            when (Build.VERSION.SDK_INT) {
                23 -> actionView.setTextAppearance(
                    a.getResourceId(R.styleable.ErrorbarLayout_actionTextAppearance, 0))
                else -> actionView.setTextAppearance(context,
                    a.getResourceId(R.styleable.ErrorbarLayout_actionTextAppearance, 0))
            }
        }
        if (a.hasValue(R.styleable.ErrorbarLayout_actionTextColor)) {
            actionView.setTextColor(a.getColorStateList(R.styleable.ErrorbarLayout_actionTextColor))
        }
        if (a.hasValue(R.styleable.ErrorbarLayout_actionTextSize)) {
            actionView.textSize = a.getDimension(R.styleable.ErrorbarLayout_actionTextSize, 0f)
        }
        a.recycle()
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        backdropView.animateBy(delay, duration, true)
        imageView.animateBy(delay, duration, true)
        // inherited from Snackbar
        textView.animateBy(delay, duration, true, true)
        actionView.animateBy(delay, duration, true)
    }

    override fun animateContentOut(delay: Int, duration: Int) {
        backdropView.animateBy(delay, duration, false)
        imageView.animateBy(delay, duration, false)
        // inherited from Snackbar
        textView.animateBy(delay, duration, false, true)
        actionView.animateBy(delay, duration, false)
    }

    private companion object {

        fun View.animateBy(
            delay: Int,
            duration: Int,
            animateIn: Boolean,
            condition: Boolean = visibility == VISIBLE
        ) {
            if (condition) {
                alpha = if (animateIn) 0.0f else 1.0f
                animate()
                    .alpha(if (animateIn) 1.0f else 0.0f)
                    .setDuration(duration.toLong())
                    .setStartDelay(delay.toLong())
                    .start()
            }
        }
    }
}