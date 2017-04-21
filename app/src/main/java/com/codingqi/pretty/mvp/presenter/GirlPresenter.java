package com.codingqi.pretty.mvp.presenter;

import android.app.Application;
import android.widget.Toast;

import com.codingqi.pretty.mvp.contract.UserContract;
import com.codingqi.pretty.mvp.model.entity.GirlJson;
import com.codingqi.pretty.mvp.model.entity.GirlsBean;
import com.codingqi.pretty.mvp.ui.adapter.GirlAdapter;
import com.jess.arms.base.AppManager;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Time：2017/3/25 下午4:47
 * @author Yachao Qi
 * File Name： GirlPresenter.java
 * Explain：
 */

@ActivityScope
public class GirlPresenter extends BasePresenter<UserContract.Model, UserContract.View> {
    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<GirlsBean> mUsers = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private int lastUserId = 1;
    private boolean isFirst = true;

    @Inject
    public GirlPresenter(UserContract.Model model, UserContract.View rootView, RxErrorHandler handler
            , AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
        mAdapter = new GirlAdapter(mUsers);
        mRootView.setAdapter(mAdapter);//设置Adapter
    }

    public void requestUsers(final boolean pullToRefresh) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
            }
        }, mRootView.getRxPermissions(), mRootView, mErrorHandler);

        if (pullToRefresh) lastUserId = 1;//xia拉刷新默认只请求第一页

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        boolean isEvictCache = pullToRefresh;//是否驱逐缓存,为ture即不使用缓存,每次上拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst){//默认在第一次上拉刷新时使用缓存
            isFirst = false;
            isEvictCache = false;
        }
        isEvictCache = true;

        mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.showLoading();//显示上拉刷新的进度条net error
                        else
                            mRootView.startLoadMore();//显示下拉加载更多的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.hideLoading();//隐藏上拉刷新的进度条
                        else
                            mRootView.endLoadMore();//隐藏下拉加载更多的进度条
                    }
                })
                .compose(RxUtils.<List<GirlsBean>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<GirlJson<List<GirlsBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(GirlJson<List<GirlsBean>> jsonData) {


                        if(jsonData.getError()==false){
                            List<GirlsBean> users=jsonData.getData();
                            lastUserId = lastUserId+1;//记录最后一个id,用于下一次请求
                            if (pullToRefresh){
                                mUsers.clear();//如果是上拉刷新则清空列表
                            }
                            for (GirlsBean user : users) {
                                mUsers.add(user);
                            }
                            mAdapter.notifyDataSetChanged();//通知更新数据
                        }
                        else {
                            Toast.makeText(mApplication,"数据错误",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mUsers = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }
}
