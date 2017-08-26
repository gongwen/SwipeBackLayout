package com.gw.swipeback;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by GongWen on 17/8/24.
 */

public class SwipeBackLayout extends ViewGroup {
    private static final String TAG = "SwipeBackLayout";

    public static final int FROM_LEFT = 0;
    public static final int FROM_TOP = 1;
    public static final int FROM_RIGHT = 2;
    public static final int FROM_BOTTOM = 3;

    @IntDef({FROM_LEFT, FROM_TOP, FROM_RIGHT, FROM_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DirectionMode {
    }

    public int mDirectionMode = FROM_LEFT;

    private final ViewDragHelper mDragHelper;
    private View mDragContentView;
    private View innerScrollView;

    private int width, height;
    private float SWIPE_BACK_FACTOR = 0.5f;
    private float swipeBackFraction;
    private int alpha = 125;
    private float downX, downY;

    public SwipeBackLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
        setSwipeBackListener(defaultSwipeBackListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount > 1) {
            throw new IllegalStateException("SwipeBackLayout must contains only one direct child.");
        }

        if (childCount > 0) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            mDragContentView = getChildAt(0);
            setMeasuredDimension(mDragContentView.getMeasuredWidth(), mDragContentView.getMeasuredHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) return;

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = left + mDragContentView.getMeasuredWidth();
        int bottom = top + mDragContentView.getMeasuredHeight();
        mDragContentView.layout(left, top, right, bottom);

        if (changed) {
            width = getWidth();
            height = getHeight();
        }
        innerScrollView = Util.findAllScrollViews(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(alpha - (int) (alpha * swipeBackFraction), 0, 0, 0);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (MotionEventCompat.getActionMasked(ev) == MotionEvent.ACTION_DOWN) {
            downX = ev.getRawX();
            downY = ev.getRawY();
        }
        boolean handled = mDragHelper.shouldInterceptTouchEvent(ev);
        return handled ? handled : super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void smoothScrollToX(int finalLeft) {
        if (mDragHelper.settleCapturedViewAt(finalLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void smoothScrollToY(int finalTop) {
        if (mDragHelper.settleCapturedViewAt(0, finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDragContentView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mDirectionMode == FROM_LEFT && !Util.canViewScrollRight(innerScrollView, downX, downY, false)) {
                return Math.min(Math.max(left, 0), width);
            } else if (mDirectionMode == FROM_RIGHT && !Util.canViewScrollLeft(innerScrollView, downX, downY, false)) {
                return Math.min(Math.max(left, -width), 0);
            }
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (mDirectionMode == FROM_TOP && !Util.canViewScrollUp(innerScrollView, downX, downY, false)) {
                return Math.min(Math.max(top, 0), height);
            } else if (mDirectionMode == FROM_BOTTOM && !Util.canViewScrollDown(innerScrollView, downX, downY, false)) {
                return Math.min(Math.max(top, -height), 0);
            }
            return super.clampViewPositionVertical(child, top, dy);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            left = Math.abs(left);
            top = Math.abs(top);
            switch (mDirectionMode) {
                case FROM_LEFT:
                case FROM_RIGHT:
                    swipeBackFraction = 1.0f * left / width;
                    break;
                case FROM_TOP:
                case FROM_BOTTOM:
                    swipeBackFraction = 1.0f * top / height;
                    break;
            }
            if (mSwipeBackListener != null) {
                mSwipeBackListener.onViewPositionChanged(mDragContentView, swipeBackFraction, SWIPE_BACK_FACTOR);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (swipeBackFraction >= SWIPE_BACK_FACTOR) {
                switch (mDirectionMode) {
                    case FROM_LEFT:
                        smoothScrollToX(width);
                        break;
                    case FROM_TOP:
                        smoothScrollToY(height);
                        break;
                    case FROM_RIGHT:
                        smoothScrollToX(-width);
                        break;
                    case FROM_BOTTOM:
                        smoothScrollToY(-height);
                        break;
                }
            } else {
                switch (mDirectionMode) {
                    case FROM_LEFT:
                    case FROM_RIGHT:
                        smoothScrollToX(0);
                        break;
                    case FROM_BOTTOM:
                    case FROM_TOP:
                        smoothScrollToY(0);
                        break;
                }
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                if (mSwipeBackListener != null) {
                    if (swipeBackFraction == 0) {
                        mSwipeBackListener.onViewSwipeFinished(mDragContentView, false);
                    } else if (swipeBackFraction == 1) {
                        mSwipeBackListener.onViewSwipeFinished(mDragContentView, true);
                    }
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return width;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return height;
        }
    }

    public void finish() {
        ((Activity) getContext()).finish();
    }

    public void setSwipeBackFactor(float swipeBackFactor) {
        this.SWIPE_BACK_FACTOR = swipeBackFactor;
    }

    public void setAlpha(int alpha) {
        if (alpha > 255) {
            alpha = 255;
        } else if (alpha < 0) {
            alpha = 0;
        }
        this.alpha = alpha;
    }

    public void setDirectionMode(@DirectionMode int direction) {
        mDirectionMode = direction;
    }

    private OnSwipeBackListener mSwipeBackListener;

    private OnSwipeBackListener defaultSwipeBackListener = new OnSwipeBackListener() {
        @Override
        public void onViewPositionChanged(View mView, float swipeBackFraction, float SWIPE_BACK_FACTOR) {
            invalidate();
        }

        @Override
        public void onViewSwipeFinished(View mView, boolean isBackToEnd) {
            if (isBackToEnd) {
                finish();
            } else {
            }
        }
    };

    public void setSwipeBackListener(OnSwipeBackListener mSwipeBackListener) {
        this.mSwipeBackListener = mSwipeBackListener;
    }

    public interface OnSwipeBackListener {

        void onViewPositionChanged(View mView, float swipeBackFraction, float SWIPE_BACK_FACTOR);

        void onViewSwipeFinished(View mView, boolean isBackToEnd);
    }

}
