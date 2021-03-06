
package com.deardhruv.projectstarter;

import android.app.Application;
import android.content.Context;

import com.deardhruv.projectstarter.network.ApiClient;
import com.deardhruv.projectstarter.network.JacksonConverter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.Converter;

public class ProjectStarterApplication extends Application {


    private static final int HTTP_TIMEOUT = 20;
    private static final int DISK_IMAGE_CACHE_SIZE = 50 * 1024 * 1024; // 50 MB

    private static Context mContext = null;
    private ApiClient mApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        initImageLoader();
        initApiClient();
//        Fresco.initialize(mContext);
    }

    private void initImageLoader() {
        // UnlimitedDiscCache is used by default. Only the size has to be set.
        // Memory cache is set by default.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .diskCacheSize(DISK_IMAGE_CACHE_SIZE).build();

        ImageLoader.getInstance().init(config);
    }

    /**
     * Initializes the api client which provides the connection to the api. The api client uses
     * {@link OkHttpClient} for the http connection. This allows us to modify the connection
     * properties. The response conversion is done using the {@link JacksonConverter}.
     */
    private void initApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS);

        Client okClient = new OkClient(okHttpClient);
        Converter jsonConverter = new JacksonConverter();
        mApiClient = new ApiClient(okClient, jsonConverter);
    }

    public static Context getAppContext() {
        return mContext;
    }

    public ApiClient getApiClient() {
        return mApiClient;
    }
}
