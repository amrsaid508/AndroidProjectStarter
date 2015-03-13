
package com.deardhruv.projectstarter.network;

import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.converter.Converter;

import com.deardhruv.projectstarter.BuildConfig;
import com.deardhruv.projectstarter.events.RequestFinishedEvent;
import com.deardhruv.projectstarter.requests.AbstractApiRequest;
import com.deardhruv.projectstarter.requests.model.ImageListRequest;
import com.deardhruv.projectstarter.utils.Helper;

import de.greenrobot.event.EventBus;

/**
 * Provides request functions for all api calls. This class maintains a map of
 * running requests to provide request cancellation.
 */
public class ApiClient {
	private static final String LOGTAG = ApiClient.class.getSimpleName();

	public static final String WS_SCHEME = "https://";
	public static final String WS_HOSTNAME = "raw.githubusercontent.com";
	public static final String WS_SUFFIX_FOLDER = "/DearDhruv/AndroidProjectStarter/master";

	private static final String BASE_URL = WS_SCHEME + WS_HOSTNAME + WS_SUFFIX_FOLDER;
	// https://raw.githubusercontent.com/DearDhruv/AndroidProjectStarter/master/image_list_json

	/** Makes the api calls. */
	private Api mApi;

	/** The list of running requests. Used to cancel requests. */
	private Map<String, AbstractApiRequest> requests;

	/**
	 * Initializes the api interface which will handle the api calls. Also
	 * initializes an empty hash map of <request tag, request> pairs. Registers
	 * with EventBus to receive the {@link RequestFinishedEvent}.
	 * 
	 * @param client A retrofit {@link Client} implementation which will be used
	 *            as the Http Client.
	 * @param converter A retrofit {@link Converter} implementation used for the
	 *            api response conversion to POJOs.
	 */
	public ApiClient(Client client, Converter converter) {
		RestAdapter restAdapter = new RestAdapter.Builder().setClient(client).setEndpoint(BASE_URL)
				.setConverter(converter).setLogLevel(LogLevel.BASIC).setLog(new RestAdapter.Log() {
					public void log(String msg) {
						if (BuildConfig.DEBUG) {
							Helper.logLongStrings(LOGTAG, msg);
						}
					}
				}).build();

		mApi = restAdapter.create(Api.class);
		requests = new HashMap<String, AbstractApiRequest>();
		EventBus.getDefault().register(this);
	}

	// ============================================================================================
	// Request functions
	// ============================================================================================

	/**
	 * Execute a request to retrieve the update message. See
	 * {@link Api#getImageList(retrofit.Callback)} for parameter details.
	 * 
	 * @param requestTag The tag for identifying the request.
	 */
	public void getImageList(String requestTag) {
		ImageListRequest request = new ImageListRequest(mApi, requestTag);
		requests.put(requestTag, request);
		request.execute();
	}

	// ============================================================================================
	// Public functions
	// ============================================================================================

	/**
	 * Look up the event with the passed tag in the event list. If the request
	 * is found, cancel it and remove it from the list.
	 * 
	 * @param requestTag Identifies the request.
	 * @return True if the request was cancelled, false otherwise.
	 */
	public boolean cancelRequest(String requestTag) {
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
	 * Returns true if the request with the passed tag is in the list of running
	 * requests, false otherwise.
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
		requests.remove(event.getRequestTag());
	}

}
