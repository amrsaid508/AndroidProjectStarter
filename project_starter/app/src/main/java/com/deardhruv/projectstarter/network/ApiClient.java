package com.deardhruv.projectstarter.network;

import com.deardhruv.projectstarter.BuildConfig;
import com.deardhruv.projectstarter.events.RequestFinishedEvent;
import com.deardhruv.projectstarter.requests.AbstractApiRequest;
import com.deardhruv.projectstarter.requests.model.ImageListRequest;
import com.deardhruv.projectstarter.requests.model.UploadFileRequest;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.converter.Converter;
import retrofit.mime.TypedFile;

/**
 * Provides request functions for all api calls. This class maintains a map of running requests to
 * provide request cancellation.
 */
public class ApiClient {
    private static final String LOGTAG = ApiClient.class.getSimpleName();

    private static final String WS_SCHEME = "http://";
//    private static final String WS_PREFIX_DOMAIN = "192.168.1.12";
//    private static final String WS_PREFIX_DOMAIN = "deardhruv";
//    private static final String WS_HOSTNAME = ".96.lt/";

    private static final String WS_PREFIX_DOMAIN = "sostarsamaj";
    private static final String WS_HOSTNAME = ".in/";

    private static final String WS_SUFFIX_FOLDER = "api";

    private static final String BASE_URL = WS_SCHEME + WS_PREFIX_DOMAIN + WS_HOSTNAME
            + WS_SUFFIX_FOLDER;

    /**
     * Makes the api calls.
     */
    private final Api mApi;

    /**
     * The list of running requests. Used to cancel requests.
     */
    private final Map<String, AbstractApiRequest> requests;

    /**
     * Initializes the api interface which will handle the api calls. Also initializes an empty hash
     * map of <request tag, request> pairs. Registers with EventBus to receive the {@link
     * RequestFinishedEvent}.
     *
     * @param client    A retrofit {@link Client} implementation which will be used as the Http
     *                  Client.
     * @param converter A retrofit {@link Converter} implementation used for the api response
     *                  conversion to POJOs.
     */
    public ApiClient(Client client, Converter converter) {
        RestAdapter restAdapter = new RestAdapter
                .Builder()
                .setClient(client)
                .setEndpoint(BASE_URL)
                .setConverter(converter)
                .setLogLevel(LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        if (BuildConfig.DEBUG) {
//                            Helper.logLongStrings(LOGTAG, msg);
                        }
                    }
                }).build();

        mApi = restAdapter.create(Api.class);
        requests = new HashMap<>();
        EventBus.getDefault().register(this);
    }

    // ============================================================================================
    // Request functions
    // ============================================================================================

    /**
     * Execute a request to retrieve the update message. See
     * {@link Api#getImageList(retrofit.Callback)}
     * for parameter details.
     *
     * @param requestTag The tag for identifying the request.
     */
    public void getImageList(String requestTag) {
        ImageListRequest request = new ImageListRequest(mApi, requestTag);
        requests.put(requestTag, request);
        request.execute();
    }

    /**
     * Execute a request to retrieve the update message. See {@link Api#uploadFile(TypedFile,
     * String, String, Callback)} for parameter details.
     *
     * @param requestTag The tag for identifying the request.
     */
    public void uploadFile(String requestTag, TypedFile file, String firstName, String lastName) {
        UploadFileRequest request = new UploadFileRequest(mApi, requestTag);
        requests.put(requestTag, request);
        request.execute(requestTag, file, firstName, lastName);
    }

    // ============================================================================================
    // Public functions
    // ============================================================================================

    /**
     * Look up the event with the passed tag in the event list. If the request is found, cancel it
     * and remove it from the list.
     *
     * @param requestTag Identifies the request.
     *
     * @return True if the request was cancelled, false otherwise.
     */
    public boolean cancelRequest(String requestTag) {
        System.gc();
        AbstractApiRequest request = requests.get(requestTag);

        if (request != null) {
            request.cancel();
            requests.remove(requestTag);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the request with the passed tag is in the list of running requests, false
     * otherwise.
     */
    public boolean isRequestRunning(String requestTag) {
        return requests.containsKey(requestTag);
    }

    /**
     * A request has finished. Remove it from the list of running requests.
     *
     * @param event The event posted on the EventBus.
     */
    public void onEvent(RequestFinishedEvent event) {
        System.gc();
        requests.remove(event.getRequestTag());
    }

}
