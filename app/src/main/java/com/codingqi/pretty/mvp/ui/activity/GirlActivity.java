package com.codingqi.pretty.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.codingqi.pretty.R;
import com.codingqi.pretty.di.component.DaggerGirlComponent;
import com.codingqi.pretty.di.module.GirlModule;
import com.codingqi.pretty.mvp.contract.GirlsContract;
import com.codingqi.pretty.mvp.contract.UserContract;
import com.codingqi.pretty.mvp.presenter.GirlPresenter;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import common.AppComponent;
import common.WEActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;


public class GirlActivity extends WEActivity<GirlPresenter> implements GirlsContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private RxPermissions mRxPermissions;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerGirlComponent
                .builder()
                .appComponent(appComponent)
                .girlModule(new GirlModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_user, null, false);
    }

    @Override
    protected void initData() {
        mPresenter.requestUsers(true);//打开app时自动加载列表
    }

    @Override
    public void onRefresh() {
        mPresenter.requestUsers(true);
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        UiUtils.configRecycleView(mRecyclerView,  new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        initRecycleView();
        initPaginate();
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.requestUsers(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return false;
                }
            };

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mRxPermissions = null;
        this.mPaginate = null;
    }
}
