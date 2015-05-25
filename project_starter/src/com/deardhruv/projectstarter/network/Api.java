
package com.deardhruv.projectstarter.network;

import com.deardhruv.projectstarter.response.model.ImageListResponse;
import com.deardhruv.projectstarter.response.model.UploadFileResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * The API interface for retrofit calls. This interface defines all the api
 * calls made by the app. Note: You have to pass a null value for a parameter to
 * be skipped. Parameter values are URL encoded by default.
 * TODO: We may have to encode the parameter values before passing them to the api calls here
 * although they are URL encoded by retrofit automatically. We have to test this
 * thoroughly, especially for calls like CustCenterAuthenticate where special
 * characters can be passed in the password.
 */
public interface Api {
	static final String APP_ID = "";
	static final String USER_ID = "";
	static final String BASE_PARAMS = "?application=" + APP_ID + "&userid=" + USER_ID;
	static final String SECRET_KEY = "";

	/**
	 * Retrive list of images.
	 */
//	@GET("/image_list_json")
	@GET("/image_list_json")
	void getImageList(Callback<ImageListResponse> callback);

	/**
	 * Upload file to server
	 * 
	 * @param file
	 * @param callback
	 */
	@Multipart
	@POST("/upload.php")
	void uploadFile(
			@Part("file") TypedFile file,
			Callback<UploadFileResponse> callback);

}
