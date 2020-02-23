package com.men.imclent.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.men.imclent.R;

public class ContactLayout extends RelativeLayout {
    private RecyclerView recyclerView;
    private TextView tv_float;
    private SlideBar slidebar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ContactLayout(Context context) {
        this(context,null);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        View.inflate(getContext(), R.layout.contact_layout, this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        tv_float = (TextView) findViewById(R.id.tv_float);
        slidebar = (SlideBar) findViewById(R.id.sb_slideBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sb_swipeRefresh);
//        swipeRefreshLayout.setOnRefreshListener();
    }

     public void setAdapter(RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
     }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }
}
