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

package android.support.design.widget

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.internal.ErrorbarContentLayout
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ScrollView
import com.hendraanggrian.errorbar.R

/**
 * A larger Snackbar to display error and empty state.
 *
 * @see Snackbar
 */
class Errorbar private constructor(
    parent: ViewGroup,
    content: View,
    contentViewCallback: ContentViewCallback
) : BaseTransientBottomBar<Errorbar>(parent, content, contentViewCallback) {

    companion object {
        const val LENGTH_INDEFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE
        const val LENGTH_SHORT = BaseTransientBottomBar.LENGTH_SHORT
        const val LENGTH_LONG = BaseTransientBottomBar.LENGTH_LONG

        /**
         * Make an Errorbar to display a message.
         *
         * @param view the view to find a parent from.
         * @param text the text to show.  Can be formatted text.
         * @param duration how long to display the message.  Either [LENGTH_SHORT] or [LENGTH_LONG]
         * @see Snackbar.make
         */
        fun make(view: View, text: CharSequence, @Duration duration: Int): Errorbar {
            val parent = findSuitableParent(view)
                ?: throw IllegalStateException("Unable to find suitable parent!")
            val context = parent.context
            val content = LayoutInflater.from(context)
                .inflate(R.layout.design_layout_errorbar_include, parent, false)
                as ErrorbarContentLayout
            return Errorbar(parent, content, content).apply {
                setText(text)
                this.duration = duration
                // hack Snackbar's view container
                mView.setPadding(0, 0, 0, 0)
                mView.setBackgroundColor(context.theme.getColor(android.R.attr.windowBackground))
                mView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        }

        /**
         * Make an Errorbar to display a message.
         *
         * @param view the view to find a parent from.
         * @param resId the resource id of the string resource to use. Can be formatted text.
         * @param duration how long to display the message.  Either [LENGTH_SHORT] or [LENGTH_LONG]
         * @see Snackbar.make
         */
        fun make(view: View, @StringRes resId: Int, @Duration duration: Int): Errorbar =
            make(view, view.resources.getText(resId), duration)

        /**
         * While [Snackbar] prioritizes [CollapsingToolbarLayout] to be its parent,
         * Errorbar accepts any parent capable of holding more than one child.
         */
        private fun findSuitableParent(view: View?): ViewGroup? {
            var _view = view
            do {
                if (_view is ViewGroup) {
                    // ScrollView can only accept one child, therefore not qualified to be an errorbar's parent
                    if (_view !is ScrollView && _view !is NestedScrollView) {
                        return _view
                    }
                }
                if (_view != null) {
                    // loop to get parents
                    val parent = _view.parent
                    _view = if (parent is View) parent else null
                }
            } while (_view != null)
            return null
        }

        @ColorInt
        private fun Resources.Theme.getColor(@AttrRes attr: Int): Int {
            val a = obtainStyledAttributes(null, intArrayOf(attr), 0, 0)
            if (!a.hasValue(0)) throw Resources.NotFoundException()
            val value = a.getColor(0, 0)
            a.recycle()
            return value
        }
    }

    private val layout get() = mView.getChildAt(0) as ErrorbarContentLayout

    /** Set a backdrop from bitmap. */
    fun setBackdropBitmap(backdrop: Bitmap?): Errorbar = apply {
        layout.backdropView.run {
            visibility = backdrop?.let { VISIBLE } ?: GONE
            if (visibility == VISIBLE) setImageBitmap(backdrop)
        }
    }

    /** Set a backdrop from uri. */
    fun setBackdropUri(backdrop: Uri?): Errorbar = apply {
        layout.backdropView.run {
            visibility = backdrop?.let { VISIBLE } ?: GONE
            if (visibility == VISIBLE) setImageURI(backdrop)
        }
    }

    /** Set a backdrop from drawable. */
    fun setBackdropDrawable(backdrop: Drawable?): Errorbar = apply {
        layout.backdropView.run {
            visibility = backdrop?.let { VISIBLE } ?: GONE
            if (visibility == VISIBLE) setImageDrawable(backdrop)
        }
    }

    /** Set a backdrop from drawable resource. */
    fun setBackdropResource(@DrawableRes backdrop: Int): Errorbar = apply {
        layout.backdropView.run {
            visibility = if (backdrop > 0) VISIBLE else GONE
            if (visibility == VISIBLE) setImageResource(backdrop)
        }
    }

    /** Set a backdrop from color state list. */
    fun setBackdropColor(backdrop: ColorStateList?): Errorbar = apply {
        layout.backdropView.run {
            visibility = backdrop?.let { VISIBLE } ?: GONE
            if (visibility == VISIBLE) setImageDrawable(ColorDrawable(backdrop!!.defaultColor))
        }
    }

    /** Set a backdrop from color. */
    fun setBackdropColor(@ColorInt color: Int): Errorbar =
        setBackdropColor(ColorStateList.valueOf(color))

    /** Set a backdrop from color resource. */
    fun setBackdropColorRes(@ColorRes colorRes: Int): Errorbar =
        setBackdropColor(getColor(layout.backdropView.context, colorRes))

    /** Set a backdrop from color attribute. */
    fun setBackdropColorAttr(@AttrRes colorAttr: Int): Errorbar =
        setBackdropColor(layout.backdropView.context.theme.getColor(colorAttr))

    /** Set content margin each side. */
    fun setContentMargin(left: Int, top: Int, right: Int, bottom: Int): Errorbar = apply {
        (layout.containerView.layoutParams as ViewGroup.MarginLayoutParams)
            .setMargins(left, top, right, bottom)
    }

    /** Set content left margin. */
    fun setContentMarginLeft(left: Int): Errorbar = apply {
        (layout.containerView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = left
    }

    /** Set content top margin. */
    fun setContentMarginTop(top: Int): Errorbar = apply {
        (layout.containerView.layoutParams as ViewGroup.MarginLayoutParams).topMargin = top
    }

    /** Set content right margin. */
    fun setContentMarginRight(right: Int): Errorbar = apply {
        (layout.containerView.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = right
    }

    /** Set content bottom margin. */
    fun setContentMarginBottom(bottom: Int): Errorbar = apply {
        (layout.containerView.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = bottom
    }

    /** Set logo from bitmap. */
    fun setLogoBitmap(logo: Bitmap?): Errorbar = apply {
        layout.logoView.run {
            visibility = logo?.let { VISIBLE } ?: GONE
            if (visibility == VISIBLE) setImageBitmap(logo)
        }
    }

    /** Set logo from uri. */
    fun setLogoUri(logo: Uri?): Errorbar = apply {
        layout.logoView.run {
            visibility = logo?.let { VISIBLE } ?: GONE
            if (visibility == VISIBLE) setImageURI(logo)
        }
    }

    /** Set logo from drawable. */
    fun setLogoDrawable(logo: Drawable?): Errorbar = apply {
        layout.logoView.run {
            visibility = logo?.let { VISIBLE } ?: GONE
            if (visibility == VISIBLE) setImageDrawable(logo)
        }
    }

    /** Set logo from drawable resource. */
    fun setLogoResource(@DrawableRes logo: Int): Errorbar = apply {
        layout.logoView.run {
            visibility = if (logo > 0) VISIBLE else GONE
            if (visibility == VISIBLE) setImageResource(logo)
        }
    }

    /** Set text from string resource. */
    fun setText(@StringRes text: Int): Errorbar =
        setText(layout.messageView.resources.getText(text))

    /** Set text to this Errorbar. */
    fun setText(text: CharSequence?): Errorbar = apply {
        layout.messageView.run {
            visibility = if (!text.isNullOrEmpty()) VISIBLE else GONE
            if (visibility == VISIBLE) setText(text)
        }
    }

    /** Set button text from string resource and its click listener. */
    fun setAction(@StringRes resId: Int, action: ((View) -> Unit)?): Errorbar =
        setAction(layout.actionView.context.getText(resId), action)

    /** Set button text and its click listener. */
    fun setAction(text: CharSequence?, action: ((View) -> Unit)?): Errorbar = apply {
        layout.actionView.run {
            visibility = if (!text.isNullOrEmpty() && action != null) VISIBLE else GONE
            when (visibility) {
                VISIBLE -> {
                    setText(text)
                    setOnClickListener {
                        action?.invoke(this)
                        dispatchDismiss(BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION)
                    }
                }
                else -> setOnClickListener(null)
            }
        }
    }

    open class Callback : BaseCallback<Errorbar>() {

        override fun onShown(v: Errorbar) {}

        override fun onDismissed(v: Errorbar, @DismissEvent event: Int) {}

        companion object {
            const val DISMISS_EVENT_SWIPE = BaseCallback.DISMISS_EVENT_SWIPE
            const val DISMISS_EVENT_ACTION = BaseCallback.DISMISS_EVENT_ACTION
            const val DISMISS_EVENT_TIMEOUT = BaseCallback.DISMISS_EVENT_TIMEOUT
            const val DISMISS_EVENT_MANUAL = BaseCallback.DISMISS_EVENT_MANUAL
            const val DISMISS_EVENT_CONSECUTIVE = BaseCallback.DISMISS_EVENT_CONSECUTIVE
        }
    }
}