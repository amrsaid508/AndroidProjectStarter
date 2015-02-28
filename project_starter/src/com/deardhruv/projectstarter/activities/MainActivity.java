
package com.deardhruv.projectstarter.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.deardhruv.projectstarter.ProjectStarterApplication;
import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.abstracts.AbstractActivity;
import com.deardhruv.projectstarter.events.ApiErrorEvent;
import com.deardhruv.projectstarter.events.ApiErrorWithMessageEvent;
import com.deardhruv.projectstarter.network.ApiClient;
import com.deardhruv.projectstarter.response.model.ImageListResponse;
import com.deardhruv.projectstarter.utils.Logger;

import de.greenrobot.event.EventBus;

public class MainActivity extends AbstractActivity {

	private static final String LOGTAG = "MainActivity";
	private static final Logger LOG = new Logger(LOGTAG);

	private static final String IMAGE_LIST_REQUEST_TAG = LOGTAG + ".imageListRequest";

	private EventBus mEventBus;
	ApiClient mApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		mEventBus = EventBus.getDefault();
		ProjectStarterApplication app = ((ProjectStarterApplication) getApplication());
		mApiClient = app.getApiClient();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mApiClient.getImageList(IMAGE_LIST_REQUEST_TAG);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mEventBus.register(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mEventBus.unregister(this);
	}

	/**
	 * Response of Image list.
	 * 
	 * @param imageListResponse ImageListResponse
	 */
	public void onEventMainThread(ImageListResponse imageListResponse) {
		switch (imageListResponse.getRequestTag()) {
			case IMAGE_LIST_REQUEST_TAG:
				showToast(imageListResponse.toString());
				break;

			default:
				break;
		}
	}

	/**
	 * EventBus listener. Called when a api error occurred during one of the api
	 * calls made here.
	 *
	 * @param event ApiErrorEvent
	 */
	public void onEventMainThread(ApiErrorEvent event) {
		switch (event.getRequestTag()) {
			case IMAGE_LIST_REQUEST_TAG:
				showToast(getString(R.string.error_server_problem));
				break;

			default:
				break;
		}
	}

	/**
	 * EventBus listener. Called when a api error occurred during one of the api
	 * calls made here. A error message is passed.
	 *
	 * @param event ApiErrorWithMessageEvent
	 */
	public void onEventMainThread(ApiErrorWithMessageEvent event) {
		switch (event.getRequestTag()) {
			case IMAGE_LIST_REQUEST_TAG:
				showToast(event.getResultMsgUser());
				break;

			default:
				break;
		}
	}
}
