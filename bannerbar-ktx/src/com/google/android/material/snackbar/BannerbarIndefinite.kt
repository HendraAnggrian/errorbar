@file:JvmMultifileClass
@file:JvmName("BannerbarKt")

package com.google.android.material.snackbar

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes

/**
 * Display [Bannerbar] with [Bannerbar.LENGTH_INDEFINITE] duration.
 *
 * @param text the text message.
 */
inline fun View.bannerbar(@NonNull text: CharSequence, configuration: Bannerbar.() -> Unit): Bannerbar =
    Bannerbar.make(this, text, Bannerbar.LENGTH_INDEFINITE)
        .apply {
            configuration()
            show()
        }

/**
 * Display [Bannerbar] with [Bannerbar.LENGTH_INDEFINITE] duration.
 *
 * @param textId the text message.
 */
inline fun View.bannerbar(@StringRes textId: Int, configuration: Bannerbar.() -> Unit): Bannerbar =
    Bannerbar.make(this, textId, Bannerbar.LENGTH_INDEFINITE)
        .apply {
            configuration()
            show()
        }

/**
 * Display [Bannerbar] with [Bannerbar.LENGTH_INDEFINITE] duration.
 *
 * @param icon option on the left side of message.
 * @param text the text message.
 */
inline fun View.bannerbar(
    @NonNull icon: Drawable,
    @NonNull text: CharSequence,
    configuration: Bannerbar.() -> Unit
): Bannerbar = Bannerbar.make(this, text, Bannerbar.LENGTH_INDEFINITE)
    .setIcon(icon)
    .apply {
        configuration()
        show()
    }

/**
 * Display [Bannerbar] with [Bannerbar.LENGTH_INDEFINITE] duration.
 *
 * @param icon option on the left side of message.
 * @param textId the text message.
 */
inline fun View.bannerbar(
    @NonNull icon: Drawable,
    @StringRes textId: Int,
    configuration: Bannerbar.() -> Unit
): Bannerbar = Bannerbar.make(this, textId, Bannerbar.LENGTH_INDEFINITE)
    .setIcon(icon)
    .apply {
        configuration()
        show()
    }

/**
 * Display [Bannerbar] with [Bannerbar.LENGTH_INDEFINITE] duration.
 *
 * @param iconId option on the left side of message.
 * @param text the text message.
 */
inline fun View.bannerbar(
    @DrawableRes iconId: Int,
    @NonNull text: CharSequence,
    configuration: Bannerbar.() -> Unit
): Bannerbar = Bannerbar.make(this, text, Bannerbar.LENGTH_INDEFINITE)
    .setIcon(iconId)
    .apply {
        configuration()
        show()
    }

/**
 * Display [Bannerbar] with [Bannerbar.LENGTH_INDEFINITE] duration.
 *
 * @param iconId option on the left side of message.
 * @param textId the text message.
 */
inline fun View.bannerbar(
    @DrawableRes iconId: Int,
    @StringRes textId: Int,
    configuration: Bannerbar.() -> Unit
): Bannerbar = Bannerbar.make(this, textId, Bannerbar.LENGTH_INDEFINITE)
    .setIcon(iconId)
    .apply {
        configuration()
        show()
    }