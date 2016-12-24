package com.wedding.weddinghelper.activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.wedding.weddinghelper.Adapter.FullScreenImageAdapter;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.Util.CustomizedViewPager;
import com.wedding.weddinghelper.Util.PhotoUtils;
import com.wedding.weddinghelper.fragements.PhotoFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FullScreenViewActivity extends Activity {

    private FullScreenImageAdapter adapter;
    private CustomizedViewPager viewPager;
    private ImageButton downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (CustomizedViewPager) findViewById(R.id.pager);
        downloadButton = (ImageButton) findViewById(R.id.fullscreen_download_button);
        downloadButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "相片下載中...", Toast.LENGTH_SHORT).show();
                        // TODO: download mini photo now, should be original photo
                        int position = viewPager.getCurrentItem();
                        JoinMainActivity.download(position);
                    }
                }
        );

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        ArrayList<String> photoList = new ArrayList(Arrays.asList(PhotoFragment.miniPhotoUrls));
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, photoList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        fullScreen();
    }

    public void setPagingEnabled(boolean enabled) {
        viewPager.setPagingEnabled(enabled);
    }

    public void fullScreen() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}