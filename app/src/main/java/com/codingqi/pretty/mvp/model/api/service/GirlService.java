package com.codingqi.pretty.mvp.model.api.service;

import com.codingqi.pretty.mvp.model.entity.GirlJson;
import com.codingqi.pretty.mvp.model.entity.GirlsBean;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by qiyachao on 2017/3/14.
 */
public interface GirlService {
	//    @Headers({HEADER_API_VERSION})
	@GET("api/data/{type}/{count}/{page}")
	Observable<GirlJson<List<GirlsBean>>> getGirls(
			@Path("type") String type,
			@Path("count") int count,
			@Path("page") int perPag
	);


//    @GET("api/data/{type}/{count}/{page}")
//    Call<HttpResult<List<GirlsBean> >> getGirls(
//            @Path("type") String type,
//            @Path("count") int count,
//            @Path("page") int page
}
