package com.codingqi.pretty.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.codingqi.pretty.R;
import com.codingqi.pretty.mvp.model.entity.GirlsBean;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.jess.arms.widget.imageloader.glide.GlideImageConfig;

import butterknife.BindView;
import common.WEApplication;


/**
 * Created by jess on 9/4/16 12:56
 * Contact with jess.yan.effort@gmail.com
 */
public class GirlItemHolder extends BaseHolder<GirlsBean> {

    @Nullable
    @BindView(R.id.iv_avatar)
    ImageView mAvater;

    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    private final WEApplication mApplication;

    public GirlItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到Application的地方,拿到AppComponent,从而得到用Dagger管理的单例对象
        mApplication = (WEApplication) itemView.getContext().getApplicationContext();
        mImageLoader = mApplication.getAppComponent().imageLoader();
    }

    @Override
    public void setData(GirlsBean data, int position) {


        mImageLoader.loadImage(mApplication, GlideImageConfig
                .builder()
                .url(data.getUrl())
                .imageView(mAvater)
                .build());
    }


    @Override
    protected void onRelease() {
        mImageLoader.clear(mApplication, GlideImageConfig.builder()
                .imageViews(mAvater)
                .build());
    }
}
