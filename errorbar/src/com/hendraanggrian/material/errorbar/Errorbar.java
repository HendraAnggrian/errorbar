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

package com.hendraanggrian.material.errorbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * @see com.google.android.material.snackbar.Snackbar
 */
public final class Errorbar extends BaseTransientBottomBar<Errorbar> {

    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @IntRange(from = 1)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    /**
     * Show the Errorbar indefinitely. This means that the Errorbar will be displayed from the time
     * that is {@link #show() shown} until either it is dismissed, or another Errorbar is shown.
     *
     * @see #setDuration
     */
    public static final int LENGTH_INDEFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE;

    /**
     * Show the Errorbar for a short period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = BaseTransientBottomBar.LENGTH_SHORT;

    /**
     * Show the Errorbar for a long period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = BaseTransientBottomBar.LENGTH_LONG;

    /**
     * Callback class for {@link Errorbar} instances.
     * <p>
     * <p>Note: this class is here to provide backwards-compatible way for apps written before the
     * existence of the base {@link BaseTransientBottomBar} class.
     *
     * @see BaseTransientBottomBar#addCallback(BaseCallback)
     */
    public static class Callback extends BaseCallback<Errorbar> {
        /**
         * Indicates that the Errorbar was dismissed via a swipe.
         */
        public static final int DISMISS_EVENT_SWIPE = BaseCallback.DISMISS_EVENT_SWIPE;
        /**
         * Indicates that the Errorbar was dismissed via an action click.
         */
        public static final int DISMISS_EVENT_ACTION = BaseCallback.DISMISS_EVENT_ACTION;
        /**
         * Indicates that the Errorbar was dismissed via a timeout.
         */
        public static final int DISMISS_EVENT_TIMEOUT = BaseCallback.DISMISS_EVENT_TIMEOUT;
        /**
         * Indicates that the Errorbar was dismissed via a call to {@link #dismiss()}.
         */
        public static final int DISMISS_EVENT_MANUAL = BaseCallback.DISMISS_EVENT_MANUAL;
        /**
         * Indicates that the Errorbar was dismissed from a new Errorbar being shown.
         */
        public static final int DISMISS_EVENT_CONSECUTIVE = BaseCallback.DISMISS_EVENT_CONSECUTIVE;

        @Override
        public void onShown(Errorbar sb) {
            // Stub implementation to make API check happy.
        }

        @Override
        public void onDismissed(Errorbar transientBottomBar, @DismissEvent int event) {
            // Stub implementation to make API check happy.
        }
    }

    @Nullable private BaseCallback<Errorbar> callback;

    private Errorbar(
        ViewGroup parent,
        View content,
        com.google.android.material.snackbar.ContentViewCallback contentViewCallback
    ) {
        super(parent, content, contentViewCallback);
    }

    /**
     * Make a Errorbar to display a message
     * <p>
     * <p>Errorbar will try and find a parent view to hold Errorbar's view from the value given to
     * {@code view}. Errorbar will walk up the view tree trying to find a suitable parent, which is
     * defined as a {@link CoordinatorLayout} or the window decor's content view, whichever comes
     * first.
     * <p>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Errorbar to enable certain
     * features, such as swipe-to-dismiss and automatically moving of widgets.
     *
     * @param view     The view to find a parent from.
     * @param text     The text to show. Can be formatted text.
     * @param duration How long to display the message. Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    @NonNull
    public static Errorbar make(
        @NonNull View view,
        @NonNull CharSequence text,
        @Duration int duration
    ) {
        final ViewGroup parent = findSuitableParent(view);
        if (parent == null) {
            throw new IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view.");
        }

        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final ErrorbarContentLayout content = (ErrorbarContentLayout) inflater.
            inflate(R.layout.design_layout_errorbar_include, parent, false);
        final Errorbar errorbar = new Errorbar(parent, content, content);
        errorbar.setText(text);
        errorbar.setDuration(duration);
        // hack Snackbar's view container
        errorbar.view.setPadding(0, 0, 0, 0);
        errorbar.view.setBackgroundColor(getColor(context, android.R.attr.windowBackground));
        errorbar.view.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return errorbar;
    }

    @ColorInt
    private static int getColor(@NonNull Context context, @AttrRes int resId) {
        TypedArray a = context.getTheme().obtainStyledAttributes(null, new int[]{resId}, 0, 0);
        if (!a.hasValue(0)) {
            throw new Resources.NotFoundException();
        }
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    @NonNull
    private static ViewGroup findSuitableParent(@NonNull View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return (ViewGroup) view;
                } else {
                    // It's not the content view but we'll use it as our fallback
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
    }

    private ErrorbarContentLayout getContentLayout() {
        return (ErrorbarContentLayout) view.getChildAt(0);
    }

    /**
     * Clear background.
     */
    public Errorbar noBackground() {
        ErrorbarUtils.clearImage(getContentLayout().getBackgroundView());
        return this;
    }

    /**
     * Set background from drawable resource.
     */
    public Errorbar setBackground(@DrawableRes int resId) {
        ErrorbarUtils.setImageResource(getContentLayout().getBackgroundView(), resId);
        return this;
    }

    /**
     * Set background from uri.
     */
    public Errorbar setBackground(@NonNull Uri uri) {
        ErrorbarUtils.setImageURI(getContentLayout().getBackgroundView(), uri);
        return this;
    }

    /**
     * Set background from drawable.
     */
    public Errorbar setBackground(@NonNull Drawable drawable) {
        ErrorbarUtils.setImageDrawable(getContentLayout().getBackgroundView(), drawable);
        return this;
    }

    /**
     * Set background from icon.
     */
    @RequiresApi(23)
    public Errorbar setBackground(@NonNull Icon icon) {
        ErrorbarUtils.setImageIcon(getContentLayout().getBackgroundView(), icon);
        return this;
    }

    /**
     * Set background from tint.
     */
    @RequiresApi(21)
    public Errorbar setBackground(@NonNull ColorStateList tint) {
        ErrorbarUtils.setImageTintList(getContentLayout().getBackgroundView(), tint);
        return this;
    }

    /**
     * Set a background from bitmap.
     */
    public Errorbar setBackground(@NonNull Bitmap bitmap) {
        ErrorbarUtils.setImageBitmap(getContentLayout().getBackgroundView(), bitmap);
        return this;
    }

    /**
     * Set a background from color.
     */
    public Errorbar setBackgroundColor(@ColorInt int color) {
        ErrorbarUtils.setBackgroundColor(getContentLayout().getBackgroundView(), color);
        return this;
    }

    /**
     * Set content margin each side.
     */
    public Errorbar setContentMargin(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        ((ViewGroup.MarginLayoutParams) getContentLayout().getContainerView().getLayoutParams())
            .setMargins(left, top, right, bottom);
        return this;
    }

    /**
     * Set content left margin.
     */
    public Errorbar setContentMarginLeft(@Px int left) {
        ((ViewGroup.MarginLayoutParams) getContentLayout().getContainerView().getLayoutParams())
            .leftMargin = left;
        return this;
    }

    /**
     * Set content top margin.
     */
    public Errorbar setContentMarginTop(@Px int top) {
        ((ViewGroup.MarginLayoutParams) getContentLayout().getContainerView().getLayoutParams())
            .topMargin = top;
        return this;
    }

    /**
     * Set content right margin.
     */
    public Errorbar setContentMarginRight(@Px int right) {
        ((ViewGroup.MarginLayoutParams) getContentLayout().getContainerView().getLayoutParams())
            .rightMargin = right;
        return this;
    }

    /**
     * Set content bottom margin.
     */
    public Errorbar setContentMarginBottom(@Px int bottom) {
        ((ViewGroup.MarginLayoutParams) getContentLayout().getContainerView().getLayoutParams())
            .bottomMargin = bottom;
        return this;
    }

    /**
     * Clear image.
     */
    public Errorbar noImage() {
        ErrorbarUtils.clearImage(getContentLayout().getImageView());
        return this;
    }

    /**
     * Set image from drawable resource.
     */
    public Errorbar setImage(@DrawableRes int resId) {
        ErrorbarUtils.setImageResource(getContentLayout().getImageView(), resId);
        return this;
    }

    /**
     * Set image from uri.
     */
    public Errorbar setImage(@NonNull Uri uri) {
        ErrorbarUtils.setImageURI(getContentLayout().getImageView(), uri);
        return this;
    }

    /**
     * Set image from drawable.
     */
    public Errorbar setImage(@NonNull Drawable drawable) {
        ErrorbarUtils.setImageDrawable(getContentLayout().getImageView(), drawable);
        return this;
    }

    /**
     * Set image from icon.
     */
    @RequiresApi(23)
    public Errorbar setImage(@NonNull Icon icon) {
        ErrorbarUtils.setImageIcon(getContentLayout().getImageView(), icon);
        return this;
    }

    /**
     * Set image from bitmap.
     */
    public Errorbar setImage(@NonNull Bitmap bitmap) {
        ErrorbarUtils.setImageBitmap(getContentLayout().getImageView(), bitmap);
        return this;
    }

    /**
     * Set text to this Errorbar.
     */
    public Errorbar setText(@NonNull CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            getContentLayout().getTextView().setVisibility(View.VISIBLE);
            getContentLayout().getTextView().setText(text);
        }
        return this;
    }

    /**
     * Set text from string resource.
     */
    public Errorbar setText(@StringRes int resId) {
        return setText(getContext().getText(resId));
    }

    /**
     * Sets the text color.
     */
    public Errorbar setTextColor(@ColorInt int color) {
        getContentLayout().getTextView().setTextColor(color);
        return this;
    }

    /**
     * Sets the text color.
     */
    public Errorbar setTextColor(@NonNull ColorStateList tint) {
        getContentLayout().getTextView().setTextColor(tint);
        return this;
    }

    /**
     * Set button text and its click listener.
     */
    public Errorbar setAction(
        @Nullable CharSequence text,
        @NonNull final View.OnClickListener listener
    ) {
        final TextView tv = getContentLayout().getActionView();

        if (TextUtils.isEmpty(text) || listener == null) {
            tv.setVisibility(View.GONE);
            tv.setOnClickListener(null);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
            tv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(view);
                        // Now dismiss the Errorbar
                        dispatchDismiss(BaseCallback.DISMISS_EVENT_ACTION);
                    }
                });
        }
        return this;
    }

    /**
     * Set button text from string resource and its click listener.
     */
    public Errorbar setAction(@StringRes int resId, @NonNull View.OnClickListener listener) {
        return setAction(getContext().getText(resId), listener);
    }

    /**
     * Sets the text color of the action specified in {@link Errorbar#setAction(CharSequence, View.OnClickListener)}.
     */
    public Errorbar setActionTextColor(@ColorInt int color) {
        getContentLayout().getActionView().setTextColor(color);
        return this;
    }

    /**
     * Sets the text color of the action specified in {@link Errorbar#setAction(CharSequence, View.OnClickListener)}.
     */
    public Errorbar setActionTextColor(@NonNull ColorStateList tint) {
        getContentLayout().getActionView().setTextColor(tint);
        return this;
    }
}
