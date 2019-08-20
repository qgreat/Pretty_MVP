package com.codingqi.pretty.mvp.model.api.service;

import com.codingqi.pretty.mvp.model.entity.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 存放关于用户的一些api
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public interface UserService {

	String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

	@Headers({HEADER_API_VERSION})
	@GET("/users")
	Observable<List<User>> getUsers(
			@Query("since") int lastIdQueried,
			@Query("per_page") int perPage
	);


}
