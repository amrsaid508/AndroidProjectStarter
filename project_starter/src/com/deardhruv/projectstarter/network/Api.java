
package com.deardhruv.projectstarter.network;

import com.deardhruv.projectstarter.response.model.ImageListResponse;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * The API interface for retrofit calls. This interface defines all the api
 * calls made by the app. Note: You have to pass a null value for a parameter to
 * be skipped. Parameter values are URL encoded by default. TODO: We may have to
 * encode the parameter values before passing them to the api calls here
 * although they are URL encoded by retrofit automatically. We have to test this
 * thoroughly, especially for calls like CustCenterAuthenticate where special
 * characters can be passed in the password.
 */
public interface Api {
	static final String APP_ID = "";
	static final String USER_ID = "";
	static final String BASE_PARAMS = "?application=" + APP_ID + "&userid=" + USER_ID;
	static final String SECRET_KEY = "";

	/*
	 * @GET("/image_list_json" + BASE_PARAMS) void getImageList(
	 * @Query("adnumber") String adnumber,
	 * @Query("deviceid") String deviceid, Callback<ImageListResponse>
	 * callback);
	 */

	/**
	 * Retrive list of images.
	 */
	@GET("/image_list_json")
	void getImageList(Callback<ImageListResponse> callback);

}
