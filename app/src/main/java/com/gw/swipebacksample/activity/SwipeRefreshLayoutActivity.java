package com.gw.swipebacksample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gw.swipebacksample.R;
import com.gw.swipebacksample.base.BaseSwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongWen on 17/8/31.
 */

public class SwipeRefreshLayoutActivity extends BaseSwipeBackActivity {
    private float density;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<String> dataList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_swipe_refresh_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        density = getResources().getDisplayMetrics().density;
        super.onCreate(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                SwipeRefreshLayoutActivity.this,
                                "The refresh is finished!",
                                Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add("Test" + i);
        }
        mRecyclerView.setAdapter(adapter);
    }

    private RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(SwipeRefreshLayoutActivity.this);
            textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (40 * density)));
            textView.setGravity(Gravity.CENTER);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv.setText(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    };

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }
}
