package com.gw.swipebacksample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gw.swipebacksample.R;
import com.gw.swipebacksample.base.BaseSwipeBackActivity;
import com.gw.swipebacksample.fragment.TestFragment;

/**
 * Created by GongWen on 17/8/24.
 */

public class ViewPagerActivity extends BaseSwipeBackActivity {
    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_viewpager;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
    }

    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return TestFragment.newInstance(String.valueOf(position));
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
}
