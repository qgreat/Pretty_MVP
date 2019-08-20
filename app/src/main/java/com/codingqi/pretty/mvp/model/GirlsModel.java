package com.codingqi.pretty.mvp.model;

import com.codingqi.pretty.mvp.contract.GirlsContract;
import com.codingqi.pretty.mvp.contract.UserContract;
import com.codingqi.pretty.mvp.model.api.cache.CacheManager;
import com.codingqi.pretty.mvp.model.api.service.ServiceManager;
import com.codingqi.pretty.mvp.model.entity.GirlJson;
import com.codingqi.pretty.mvp.model.entity.GirlsBean;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by jess on 9/4/16 10:56
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
public class GirlsModel extends BaseModel<ServiceManager, CacheManager> implements GirlsContract.Model {
    public static final int USERS_PER_PAGE = 10;

    @Inject
    public GirlsModel(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

    @Override
    public Observable<GirlJson<List<GirlsBean>>> getGirls(int lastIdQueried, boolean update) {
        Observable<GirlJson<List<GirlsBean>>> users = mServiceManager.getGirlService()
                .getGirls("福利",USERS_PER_PAGE,lastIdQueried);
//                .getUsers(lastIdQueried, USERS_PER_PAGE);
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return mCacheManager.getCommonCache()
                .getGirls(users
                        , new DynamicKey(lastIdQueried)
                        , new EvictDynamicKey(update))

                .flatMap(new Func1<Reply<GirlJson<List<GirlsBean>>>, Observable<GirlJson<List<GirlsBean>>>>() {
                    @Override
                    public Observable<GirlJson<List<GirlsBean>>> call(Reply<GirlJson<List<GirlsBean>>> listReply) {
                    io.rx_cache.Source source= listReply.getSource();
                        return Observable.just(listReply.getData());
                    }
                });
    }

}
