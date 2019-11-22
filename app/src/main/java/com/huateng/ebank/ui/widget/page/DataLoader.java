package com.huateng.ebank.ui.widget.page;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.ebank.R;

import java.util.List;

/**
 * Created by shanyong on 2017/9/8.
 * brvah  上拉刷新下拉加载 辅助类
 */

public abstract class DataLoader<T> implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mBaseQuickAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int pageNum = 1;
    private int pageSize = 10;
    public static final int REFRESH = 0;
    public static final int LOADMORE = 1;
    private int mTotal;

    private View emptyView;
    private View loadingView;
    private View errorView;

    public DataLoader(Context context, RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter, SwipeRefreshLayout swipeRefreshLayout) {
        this.mRecyclerView = recyclerView;
        this.mBaseQuickAdapter = baseQuickAdapter;
        this.mSwipeRefreshLayout = swipeRefreshLayout;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mBaseQuickAdapter.setOnLoadMoreListener(this, recyclerView);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorBlue));

        LayoutInflater inflater = LayoutInflater.from(context);

        emptyView = inflater.inflate(R.layout.empty_view, (ViewGroup) recyclerView.getParent(), false);
        loadingView = inflater.inflate(R.layout.loading_view, (ViewGroup) recyclerView.getParent(), false);
        errorView = inflater.inflate(R.layout.error_view, (ViewGroup) recyclerView.getParent(), false);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        mBaseQuickAdapter.setEnableLoadMore(false);
        mBaseQuickAdapter.setEmptyView(loadingView);
        pageNum = 1;
        loadDatas(pageNum, pageSize, REFRESH);
    }

    @Override
    public void onLoadMoreRequested() {

        int currentCount = mBaseQuickAdapter.getData().size();
        if (currentCount >= mTotal) {
            mBaseQuickAdapter.loadMoreEnd(false);
        } else {
            //total 大于显示
            mSwipeRefreshLayout.setEnabled(false);
            pageNum++;
            loadDatas(pageNum, pageSize, LOADMORE);
        }
    }

    //获取数据处理
    public void handler(int mode, int total, List<T> datas) {
        this.mTotal = total;
        if (datas != null && datas.size() > 0) {
            if (mode == LOADMORE) {
                mSwipeRefreshLayout.setEnabled(true);
                mBaseQuickAdapter.addData(datas);
                mBaseQuickAdapter.loadMoreComplete();
            } else if (mode == REFRESH) {
                mBaseQuickAdapter.setNewData(datas);
                mSwipeRefreshLayout.setRefreshing(false);
                mBaseQuickAdapter.setEnableLoadMore(true);
            }
        } else {

            //没有内容时禁用下拉
            if (mBaseQuickAdapter.getData().size() <= 0) {
                mSwipeRefreshLayout.setEnabled(false);
            } else {
                mSwipeRefreshLayout.setEnabled(true);
            }

            if (mode == LOADMORE) {
                mSwipeRefreshLayout.setEnabled(true);
                mBaseQuickAdapter.loadMoreComplete();
                mBaseQuickAdapter.loadMoreEnd(false);

            } else if (mode == REFRESH) {
                mSwipeRefreshLayout.setRefreshing(false);
                mBaseQuickAdapter.setNewData(null);
                mBaseQuickAdapter.setEmptyView(emptyView);
                mBaseQuickAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onError() {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if (mBaseQuickAdapter != null) {
            mBaseQuickAdapter.setEmptyView(errorView);
            mBaseQuickAdapter.loadMoreFail();
            pageNum--;
        }
    }

    public void onEmpty() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if (mBaseQuickAdapter != null) {
            mBaseQuickAdapter.getData().clear();
            mBaseQuickAdapter.setEmptyView(emptyView);
            mBaseQuickAdapter.loadMoreComplete();
            mBaseQuickAdapter.notifyDataSetChanged();
        }
    }

    //加载页  每页数量  模式
    public abstract void loadDatas(int pageNum, int pageSize, int mode);

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void release() {
        this.emptyView = null;
        this.loadingView = null;
        this.mBaseQuickAdapter = null;
        this.mSwipeRefreshLayout = null;
        this.mRecyclerView = null;
    }

    public BaseQuickAdapter getAdapter() {
        return mBaseQuickAdapter;
    }
}
