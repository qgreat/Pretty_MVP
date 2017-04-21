package com.codingqi.pretty.mvp.presenter;

import android.app.Application;

import com.codingqi.pretty.mvp.contract.GirlGalleryContract;
import com.codingqi.pretty.mvp.model.GirlsModel;
import com.jess.arms.base.AppManager;
import com.jess.arms.mvp.BasePresenter;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * Time：2017/3/25 下午4:47
 *
 * @author Yachao Qi
 *         File Name： GirlGalleryPresenter.java
 *         Explain：
 */
public class GirlGalleryPresenter extends BasePresenter<GirlGalleryContract.Model<GirlsModel>, GirlGalleryContract.View> {
    private Application application;
    private RxErrorHandler handler;

    private AppManager appManager;
    public GirlGalleryPresenter(GirlGalleryContract.Model<GirlsModel> model, GirlGalleryContract.View rootView, RxErrorHandler handler, AppManager appManager, Application application) {
        super(model, rootView);
        this.application =application;
        this.appManager=appManager;
        this.handler=handler;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        application=null;
        appManager = null;
        handler =null;
    }
}
