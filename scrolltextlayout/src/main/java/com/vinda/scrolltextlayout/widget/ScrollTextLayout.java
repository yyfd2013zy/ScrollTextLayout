package com.vinda.scrolltextlayout.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vinda.scrolltextlayout.R;
import com.vinda.scrolltextlayout.listener.ScrollTextLayoutListener;

/**
 * VPractice2020
 * <p>
 * Created by Vinda on 2020/12/23
 * Copyright © 2020年 BroadVideo. All rights reserved.
 * <p>
 * Describe:
 */
public class ScrollTextLayout extends LinearLayout {
    private String TAG = "ScrollTextView";
    private Context mContext;
    private ScrollTextLayoutParams scrollTextViewParams = new ScrollTextLayoutParams();
    private ObjectAnimator mObjectAnimator;
    private TextView contentShowTextView;
    private int contentShowTextViewWidth;
    private int mViewWidth = 0;
    private boolean viewIsShow = false;
    private ScrollTextLayoutListener scrollTextLayoutListener;

    public ScrollTextLayout(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public ScrollTextLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public ScrollTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public ScrollTextLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (scrollTextViewParams == null) {
            scrollTextViewParams = new ScrollTextLayoutParams();
        }
        if (attrs != null) {
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ScrollTextLayout);
            if (a.getString(R.styleable.ScrollTextLayout_scroll_content) != null) {
                scrollTextViewParams.setContent(a.getString(R.styleable.ScrollTextLayout_scroll_content));
            }
            scrollTextViewParams.setTextSize(a.getInteger(R.styleable.ScrollTextLayout_scroll_text_size, 14));
            scrollTextViewParams.setTextColor(a.getInteger(R.styleable.ScrollTextLayout_scroll_text_clor, Color.BLACK));
            scrollTextViewParams.setScrollDirect(a.getInteger(R.styleable.ScrollTextLayout_scroll_direct, ScrollTextLayoutParams.SCROLL_RIGHT_TO_LEFT));
            scrollTextViewParams.setScrollSpeed(a.getFloat(R.styleable.ScrollTextLayout_scroll_speed, ScrollTextLayoutParams.SCROLL_SPEED_MID));
            a.recycle();
        }
        createTextView();
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    private void createTextView() {
        contentShowTextView = new TextView(mContext);
        contentShowTextView.setTextSize(scrollTextViewParams.getTextSize());
        contentShowTextView.setTextColor(scrollTextViewParams.getTextColor());
        contentShowTextView.setSingleLine(true);
        contentShowTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewIsShow = true;
                mViewWidth = ScrollTextLayout.this.getWidth();
                Log.d(TAG, "\n inner textView.getWidth()" + contentShowTextView.getWidth() + "\n Container Layout width " + mViewWidth);
                if (scrollTextLayoutListener != null) {
                    scrollTextLayoutListener.onViewShowListener();
                }
            }
        });
        getTextViewWidth();
        Log.d(TAG, "contentShowTextViewWidth width " + contentShowTextViewWidth);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(contentShowTextViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        contentShowTextView.setLayoutParams(layoutParams);
        contentShowTextView.setText(scrollTextViewParams.getContent());
    }


    /**
     * 滚动准备
     *
     * @param scrollTextLayoutListener
     */
    public void prepareScroll(ScrollTextLayoutParams params, ScrollTextLayoutListener scrollTextLayoutListener) {
        this.scrollTextLayoutListener = scrollTextLayoutListener;
        this.scrollTextViewParams = params;
        createTextView();
        addView(contentShowTextView);
    }

    /**
     * 滚动准备
     *
     * @param scrollTextLayoutListener
     */
    public void prepareScroll(ScrollTextLayoutListener scrollTextLayoutListener) {
        this.scrollTextLayoutListener = scrollTextLayoutListener;
        addView(contentShowTextView);
    }

    /**
     * 计算出显示当前文字，所需的TextView宽度是多少
     */
    private void getTextViewWidth() {
        Rect bounds = new Rect();
        TextPaint paint = contentShowTextView.getPaint();
        paint.getTextBounds(scrollTextViewParams.getContent(), 0, scrollTextViewParams.getContent().length(), bounds);
        contentShowTextViewWidth = bounds.width();
    }

    /**
     * 手动开启滚动
     *
     * @throws Exception
     */
    public void startScroll() throws Exception {
        if (!viewIsShow) {
            throw new Exception("start failed!, please start scroll in callback of onViewShowListener");
        } else {
            buildInterpolator(contentShowTextView).start();
        }
    }

    public void stopScroll() {
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
            mObjectAnimator = null;
            if (scrollTextLayoutListener != null) {
                scrollTextLayoutListener.onStopScrollListener();
            }
        }
    }

    private ObjectAnimator buildInterpolator(TextView textView) {
        TimeInterpolator mTimeInterpolator;
        if (scrollTextViewParams.getScrollDirect() == ScrollTextLayoutParams.SCROLL_LEFT_TO_RIGHT) {
            mObjectAnimator = ObjectAnimator.ofFloat(textView, "translationX", -contentShowTextViewWidth, mViewWidth);
        } else {
            //scroll right to left!
            mObjectAnimator = ObjectAnimator.ofFloat(textView, "translationX", mViewWidth, -contentShowTextViewWidth);
        }

        mObjectAnimator.setDuration(resultSpeed());
        mObjectAnimator.setRepeatCount(-1);
        mTimeInterpolator = new LinearInterpolator();
        mObjectAnimator.setInterpolator(mTimeInterpolator);
        mObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                //Log.d(TAG, "onAnimationUpdate " + value);
            }
        });
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "onAnimationStart ");
                if (scrollTextLayoutListener != null) {
                    scrollTextLayoutListener.onStartScrollListener();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd ");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG, "onAnimationCancel ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d(TAG, "onAnimationRepeat ");
            }
        });
        return mObjectAnimator;
    }

    private long resultSpeed() {
        return (long) ((mViewWidth + contentShowTextViewWidth) / scrollTextViewParams.getScrollSpeed());
    }
}
