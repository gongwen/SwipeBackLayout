package com.gw.swipebacksample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gw.swipeback.SwipeBackLayout;
import com.gw.swipebacksample.R;
import com.gw.swipebacksample.base.BaseSwipeBackActivity;

/**
 * Created by GongWen on 17/8/24.
 */

public class CommonActivity extends BaseSwipeBackActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (mSwipeBackLayout.getDirectionMode()) {
            case SwipeBackLayout.FROM_LEFT:
                fromLeftRb.setChecked(true);
                break;
            case SwipeBackLayout.FROM_TOP:
                fromTopRb.setChecked(true);
                break;
            case SwipeBackLayout.FROM_RIGHT:
                fromRightRb.setChecked(true);
                break;
            case SwipeBackLayout.FROM_BOTTOM:
                fromBottomRb.setChecked(true);
                break;
        }
    }
}
