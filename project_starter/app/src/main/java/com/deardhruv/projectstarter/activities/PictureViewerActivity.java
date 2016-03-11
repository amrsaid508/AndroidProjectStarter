package com.deardhruv.projectstarter.activities;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.abstracts.AbstractActivity;
import com.deardhruv.projectstarter.adapters.PictureViewerAdapter;
import com.deardhruv.projectstarter.events.model.FileDownloadComleteEvent;
import com.deardhruv.projectstarter.ui.views.CustomViewPager;
import com.deardhruv.projectstarter.utils.FileDownloader;
import com.deardhruv.projectstarter.utils.Helper;
import com.software.shell.fab.ActionButton;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;

public class PictureViewerActivity extends AbstractActivity {

    private static final String LOGTAG = PictureViewerActivity.class.getSimpleName();
    // private static final Logger LOG = new Logger(LOGTAG);

    public static final String EXTRA_IMAGE_URLS = LOGTAG + ".IMAGES";
    public static final String EXTRA_IMAGE_SELECTION = LOGTAG + ".SELECTION";

    private EventBus mEventBus;

    private CustomViewPager mViewImagesPager;
    private int mSelection;
    private List<String> mImageUrls;
    private ActionButton actionBtnAll, actionBtnWhatsapp;
    private CheckBox chkShare;

    private String savedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictureviewer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEventBus = EventBus.getDefault();

        if (getIntent() == null || !getIntent().hasExtra(EXTRA_IMAGE_URLS)
                || !getIntent().hasExtra(EXTRA_IMAGE_SELECTION)) {
            throw new IllegalArgumentException(
                    "Intent should contain image urls and image selection!");
        }

        mImageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        mSelection = getIntent().getIntExtra(EXTRA_IMAGE_SELECTION, 0);

        if (mImageUrls.size() < 1) {
            Toast.makeText(this, R.string.pictureviewer_error_no_pictures, Toast.LENGTH_LONG)
                    .show();
        }

        initSnackBar(PictureViewerActivity.this);
        initUi();

        actionBtnAll.hide();
        actionBtnWhatsapp.hide();
    }

    private void initUi() {
        mViewImagesPager = (CustomViewPager) findViewById(R.id.activity_pictureviewer_viewpager);
        actionBtnAll = (ActionButton) findViewById(R.id.action_button_share_all);
        actionBtnWhatsapp = (ActionButton) findViewById(R.id.action_button_share_whatsapp);
        chkShare = (CheckBox) findViewById(R.id.chk_share);

        actionBtnAll.setRippleEffectEnabled(true);
        actionBtnWhatsapp.setRippleEffectEnabled(true);

        PictureViewerAdapter mPagerAdapter = new PictureViewerAdapter(this, mImageUrls);
        mViewImagesPager.setAdapter(mPagerAdapter);
        mViewImagesPager.setCurrentItem(mSelection);
        mViewImagesPager.setOffscreenPageLimit(mImageUrls.size());

        initListeners();
    }

    private void initListeners() {

        chkShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startDownlodingImage();
                } else {
                    updateSharingActionBar(false); // isChecked
                }
            }
        });

        actionBtnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(savedImagePath)) {
                    shareImage(savedImagePath);
                }
            }
        });

        actionBtnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(savedImagePath)) {
                    shareToWhatsApp(savedImagePath);
                }
            }
        });

        mViewImagesPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // nothing.
            }

            public void onPageScrollStateChanged(int arg0) {
                // nothing.
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // nothing.
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startDownlodingImage() {
        FileDownloader fileDownloader = new FileDownloader(PictureViewerActivity.this);

        if (Helper.isHoneyCombOrHigher()) {
            fileDownloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    mImageUrls.get(mViewImagesPager.getCurrentItem()));
        } else {
            fileDownloader.execute(mImageUrls.get(mViewImagesPager.getCurrentItem()));
        }
    }

    /**
     * EventBus listener. A file is downloaded from this activity/fragment
     *
     * @param event FileDownloadComleteEvent Contains the file path
     */
    public void onEventMainThread(FileDownloadComleteEvent event) {
        // MediaStore.Images.Media.insertImage(ContentResolver cr, Bitmap
        // source, String title, String description);
        // MediaScannerConnection.scanFile(Context context, String[] path, null,
        // null);
        String[] filePaths = new String[]{
                new File(event.getFilePath()).toString()
        };
        MediaScannerConnection.scanFile(PictureViewerActivity.this, filePaths, null, null);
        savedImagePath = event.getFilePath();
        // Uri screenshotUri = Uri.parse(event.getFilePath());
        // shareImage(event.getFilePath());
        updateSharingActionBar(true);
    }

    private void updateSharingActionBar(boolean shouldMakeVisible) {
        // To set hide animation:
        if (shouldMakeVisible) {
            actionBtnAll.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
            actionBtnWhatsapp.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);

            actionBtnAll.playShowAnimation();
            actionBtnWhatsapp.playShowAnimation();

            actionBtnAll.show();
            actionBtnWhatsapp.show();
        } else {
            actionBtnAll.setHideAnimation(ActionButton.Animations.SCALE_DOWN);
            actionBtnWhatsapp.setHideAnimation(ActionButton.Animations.SCALE_DOWN);

            actionBtnAll.playHideAnimation();
            actionBtnWhatsapp.playHideAnimation();

            actionBtnAll.hide();
            actionBtnWhatsapp.hide();
        }

    }

    private void shareImage(String path) {
        Intent shareIntent = new Intent();
        // shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setAction(Intent.ACTION_SEND);
        // shareIntent.setType("image/jpeg");
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));

        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }

    private void shareToWhatsApp(String path) {
        /**
         * Show share dialog BOTH image and text
         */
        Uri imageUri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Target whatsapp:
        shareIntent.setPackage("com.whatsapp");
        //Add text and then Image URI
        shareIntent.putExtra(Intent.EXTRA_TEXT, path);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            showMsg("Whatsapp have not been installed.");
        }
    }
}
