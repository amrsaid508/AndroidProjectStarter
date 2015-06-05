package com.deardhruv.projectstarter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.deardhruv.projectstarter.ProjectStarterApplication;
import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.abstracts.AbstractActivity;
import com.deardhruv.projectstarter.adapters.ImageItemDetailAdapter;
import com.deardhruv.projectstarter.events.ApiErrorEvent;
import com.deardhruv.projectstarter.events.ApiErrorWithMessageEvent;
import com.deardhruv.projectstarter.network.ApiClient;
import com.deardhruv.projectstarter.response.model.ImageListResponse;
import com.deardhruv.projectstarter.utils.Dumper;
import com.deardhruv.projectstarter.utils.Helper;
import com.deardhruv.projectstarter.utils.Logger;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import de.greenrobot.event.EventBus;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

//import com.yalantis.phoenix.PullToRefreshView;

public class MainActivity extends AbstractActivity implements OnClickListener, ObservableScrollViewCallbacks {

    private static final String LOGTAG = "MainActivity";
    private static final Logger LOG = new Logger(LOGTAG);

    private static final String IMAGE_LIST_REQUEST_TAG = LOGTAG + ".imageListRequest";
    private static final long REFRESH_DELAY = 2000;

    private EventBus mEventBus;
    private ApiClient mApiClient;

    private Button btnReload, btnUploadFile;
    private ObservableRecyclerView recyclerView;
    //    private PullToRefreshView mPullToRefreshView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.riseAndShine(this);

        setContentView(R.layout.main_activity_layout);
        initUI();

        mEventBus = EventBus.getDefault();
        ProjectStarterApplication app = ((ProjectStarterApplication) getApplication());
        mApiClient = app.getApiClient();

    }

    private void initUI() {
        btnReload = (Button) findViewById(R.id.btnReload);
        btnUploadFile = (Button) findViewById(R.id.btnUploadFile);
        recyclerView = (ObservableRecyclerView) findViewById(R.id.listPhotos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        ShimmerFrameLayout mShimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        mShimmerFrameLayout.setDuration(3000);
        mShimmerFrameLayout.startShimmerAnimation();

        // mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        initListener();
    }

    private void initListener() {
        btnReload.setOnClickListener(this);
        btnUploadFile.setOnClickListener(this);

        recyclerView.setScrollViewCallbacks(this);

        /*mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });*/
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadImages();
            }
        });

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

    private void loadImages() {
        showProgressDialog();
        mApiClient.getImageList(IMAGE_LIST_REQUEST_TAG);
    }

    private void showProgressDialog() {
        btnReload.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void dismissProgressDialog() {
        btnReload.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // ============================================================================================
    // User Clicks and Actions
    // ============================================================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReload:
                loadImages();
                break;

            case R.id.btnUploadFile:
                final Intent intent = new Intent(MainActivity.this, UploadFileActivity.class);
                startActivity(intent);
                break;

            default:
                LOG.i("default case");
                break;
        }
    }

    // ============================================================================================
    // EventBus callbacks
    // ============================================================================================

    /**
     * Response of Image list.
     *
     * @param imageListResponse ImageListResponse
     */
    public void onEventMainThread(ImageListResponse imageListResponse) {
        switch (imageListResponse.getRequestTag()) {
            case IMAGE_LIST_REQUEST_TAG:
                dismissProgressDialog();
                ImageItemDetailAdapter adapter = new ImageItemDetailAdapter(MainActivity.this,
                        imageListResponse.getData().getImageResultList());
                // listPhotos.setAdapter(adapter);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setItemAnimator(new FadeInAnimator());

                adapter.setRecyclerView(recyclerView);

                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
                // scaleAdapter.setFirstOnly(false);
                // scaleAdapter.setInterpolator(new OvershootInterpolator());
                recyclerView.setAdapter(scaleAdapter);

                Dumper.dump(imageListResponse);
                break;

            default:
                break;
        }
    }

    /**
     * EventBus listener. An API call failed. No error message was returned.
     *
     * @param event ApiErrorEvent
     */
    public void onEventMainThread(ApiErrorEvent event) {
        switch (event.getRequestTag()) {
            case IMAGE_LIST_REQUEST_TAG:
                dismissProgressDialog();
                showToast(getString(R.string.error_server_problem));
                break;

            default:
                break;
        }
    }

    /**
     * EventBus listener. An API call failed. An error message was returned.
     *
     * @param event ApiErrorWithMessageEvent Contains the error message.
     */
    public void onEventMainThread(ApiErrorWithMessageEvent event) {
        switch (event.getRequestTag()) {
            case IMAGE_LIST_REQUEST_TAG:
                dismissProgressDialog();
                showToast(event.getResultMsgUser());
                break;

            default:
                break;
        }
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar actionBar = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (actionBar != null && actionBar.isShowing()) {
                actionBar.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (actionBar != null) {
                if (!actionBar.isShowing()) {
                    actionBar.show();
                }
            }
        }
    }
}
