package com.gw.swipebacksample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gw.swipebacksample.activity.CommonActivity;
import com.gw.swipebacksample.activity.HorizontalScrollViewActivity;
import com.gw.swipebacksample.activity.ListViewActivity;
import com.gw.swipebacksample.activity.NestedScrollViewActivity;
import com.gw.swipebacksample.activity.RecyclerViewActivity;
import com.gw.swipebacksample.activity.ScrollViewActivity;
import com.gw.swipebacksample.activity.ViewPagerActivity;
import com.gw.swipebacksample.activity.WebViewActivity;
import com.gw.swipebacksample.base.BaseToolBarActivity;

public class MainActivity extends BaseToolBarActivity implements View.OnClickListener {
    private Button btnCommon;
    private Button btnScrollView;
    private Button btnHorizontalScrollView;
    private Button btnNestedScrollView;
    private Button btnRecyclerView;
    private Button btnListView;
    private Button btnViewPager;
    private Button btnWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnCommon = (Button) findViewById(R.id.btnCommon);
        btnCommon.setOnClickListener(this);

        btnScrollView = (Button) findViewById(R.id.btnScrollView);
        btnScrollView.setOnClickListener(this);

        btnHorizontalScrollView = (Button) findViewById(R.id.btnHorizontalScrollView);
        btnHorizontalScrollView.setOnClickListener(this);

        btnNestedScrollView = (Button) findViewById(R.id.btnNestedScrollView);
        btnNestedScrollView.setOnClickListener(this);

        btnListView = (Button) findViewById(R.id.btnListView);
        btnListView.setOnClickListener(this);

        btnRecyclerView = (Button) findViewById(R.id.btnRecyclerView);
        btnRecyclerView.setOnClickListener(this);

        btnWebView = (Button) findViewById(R.id.btnWebView);
        btnWebView.setOnClickListener(this);

        btnViewPager = (Button) findViewById(R.id.btnViewPager);
        btnViewPager.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCommon:
                startActivity(CommonActivity.class);
                break;
            case R.id.btnScrollView:
                startActivity(ScrollViewActivity.class);
                break;
            case R.id.btnHorizontalScrollView:
                startActivity(HorizontalScrollViewActivity.class);
                break;
            case R.id.btnNestedScrollView:
                startActivity(NestedScrollViewActivity.class);
                break;
            case R.id.btnListView:
                startActivity(ListViewActivity.class);
                break;
            case R.id.btnRecyclerView:
                startActivity(RecyclerViewActivity.class);
                break;
            case R.id.btnViewPager:
                startActivity(ViewPagerActivity.class);
                break;
            case R.id.btnWebView:
                startActivity(WebViewActivity.class);
                break;
        }
    }

    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(MainActivity.this, clazz));
    }
}
