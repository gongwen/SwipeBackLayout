package com.gw.swipebacksample;

import android.app.Application;

import com.gw.swipeback.tools.WxSwipeBackActivityManager;

/**
 * Created by GongWen on 17/9/4.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WxSwipeBackActivityManager.getInstance().init(this);
    }
}
