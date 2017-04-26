package com.hendraanggrian.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hendraanggrian.errorview.R;
import com.hendraanggrian.errorview.State;
import com.hendraanggrian.errorview.VisibilityUtils;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class ErrorView extends FrameLayout {

    @NonNull private final ImageView imageViewBackground;
    @NonNull private final ImageView imageViewLogo;
    @NonNull private final TextView textView;
    @NonNull private final Button button;
    private final int hideId;

    @DrawableRes int errorBackground;
    @DrawableRes int errorLogo;
    @Nullable String errorText;
    @Nullable String errorButtonText;
    @Nullable OnClickListener errorListener;

    @DrawableRes int emptyBackground;
    @DrawableRes int emptyLogo;
    @Nullable String emptyText;
    @Nullable String emptyButtonText;
    @Nullable OnClickListener emptyListener;

    State state;
    @Nullable RecyclerView recyclerView;
    @Nullable RecyclerView.AdapterDataObserver observer;

    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.errorview, this, true);
        imageViewBackground = (ImageView) findViewById(R.id.imageview_errorview_background);
        imageViewLogo = (ImageView) findViewById(R.id.imageview_errorview_logo);
        textView = (TextView) findViewById(R.id.textview_errorview);
        button = (Button) findViewById(R.id.button_errorview);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ErrorView, 0, 0);
        try {
            errorBackground = array.getResourceId(R.styleable.ErrorView_errorBackground, -1);
            errorLogo = array.getResourceId(R.styleable.ErrorView_errorLogo, R.drawable.ic_errorview_cloud);
            errorText = array.getString(R.styleable.ErrorView_errorText);
            errorButtonText = array.getString(R.styleable.ErrorView_errorButtonText);
            emptyBackground = array.getResourceId(R.styleable.ErrorView_emptyBackground, -1);
            emptyLogo = array.getResourceId(R.styleable.ErrorView_emptyLogo, -1);
            emptyText = array.getString(R.styleable.ErrorView_emptyText);
            emptyButtonText = array.getString(R.styleable.ErrorView_emptyButtonText);
            hideId = array.getResourceId(R.styleable.ErrorView_hideId, -1);
        } finally {
            array.recycle();
        }
        setState(State.HIDDEN);
    }

    public void registerRecyclerView(@NonNull final RecyclerView recyclerView) {
        if (recyclerView.getAdapter() == null)
            throw new IllegalStateException("set adapter to this RecyclerView before registering!");
        this.recyclerView = recyclerView;
        this.observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (state != State.ERROR)
                    setState(recyclerView.getAdapter().getItemCount() == 0
                            ? State.EMPTY
                            : State.HIDDEN);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                onChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                onChanged();
            }
        };
        recyclerView.getAdapter().registerAdapterDataObserver(observer);
    }

    public void setState(@NonNull State state) {
        switch (state) {
            case ERROR:
                if (hideId != -1)
                    VisibilityUtils.setVisible(((View) getParent()).findViewById(hideId), false);
                if (recyclerView != null)
                    VisibilityUtils.setVisible(recyclerView, false);
                VisibilityUtils.setVisible(this, true);
                VisibilityUtils.setImage(imageViewBackground, errorBackground);
                VisibilityUtils.setImage(imageViewLogo, errorLogo);
                VisibilityUtils.setText(textView, errorText);
                VisibilityUtils.setText(button, errorButtonText);
                if (errorListener != null)
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            errorListener.onClick(ErrorView.this);
                            setState(State.HIDDEN);
                        }
                    });
                break;
            case EMPTY:
                if (hideId != -1)
                    VisibilityUtils.setVisible(((View) getParent()).findViewById(hideId), false);
                if (recyclerView != null)
                    VisibilityUtils.setVisible(recyclerView, false);
                VisibilityUtils.setVisible(this, true);
                VisibilityUtils.setImage(imageViewBackground, emptyBackground);
                VisibilityUtils.setImage(imageViewLogo, emptyLogo);
                VisibilityUtils.setText(textView, emptyText);
                VisibilityUtils.setText(button, emptyButtonText);
                button.setOnClickListener(emptyListener);
                break;
            case HIDDEN:
                if (hideId != -1)
                    VisibilityUtils.setVisible(((View) getParent()).findViewById(hideId), true);
                if (recyclerView != null)
                    VisibilityUtils.setVisible(recyclerView, true);
                VisibilityUtils.setVisible(this, false);
                break;
        }
    }
}