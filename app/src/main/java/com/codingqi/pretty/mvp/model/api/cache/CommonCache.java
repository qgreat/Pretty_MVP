package com.codingqi.pretty.mvp.model.api.cache;

import com.codingqi.pretty.mvp.model.entity.GirlJson;
import com.codingqi.pretty.mvp.model.entity.GirlsBean;
import com.codingqi.pretty.mvp.model.entity.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import rx.Observable;
/**
 * Time：2017/3/25 下午4:54
 * @author Yachao Qi
 * File Name： CommonCache.java
 * Explain：
 */
public interface CommonCache {

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<User>>> getUsers(Observable<List<User>> oUsers, DynamicKey idLastUserQueried, EvictProvider evictProvider);

    @LifeCache(duration = 5, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<GirlJson<List<GirlsBean>>>> getGirls(Observable<GirlJson<List<GirlsBean>>> oUsers, DynamicKey idLastUserQueried, EvictProvider evictProvider);

}
