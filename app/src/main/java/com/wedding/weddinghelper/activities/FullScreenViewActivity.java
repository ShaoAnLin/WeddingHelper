package com.wedding.weddinghelper.activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

    private PopupWindow popupWindow;

    private long downloadId;
    private DownloadManager manager;
    private DownloadManager.Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (CustomizedViewPager) findViewById(R.id.pager);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        ArrayList<String> photoList = new ArrayList(Arrays.asList(PhotoFragment.miniPhotoUrls));
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, photoList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        fullScreen();
    }

    public void setPagingEnabled(boolean enabled){
        viewPager.setPagingEnabled(enabled);
    }

    // TODO: dismiss popup when clicking the screen other than the window
    public void callDownloadPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.image_viewer_popup, null);
        popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        Button mDownloadButton = ((Button) popupView.findViewById(R.id.download_button));
        if (mDownloadButton != null){
            mDownloadButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "相片下載中...", Toast.LENGTH_SHORT).show();
                    // TODO: download mini photo now, should be original photo
                    int position = viewPager.getCurrentItem();
                    download(PhotoFragment.miniPhotoUrls[position]);
                    popupWindow.dismiss();
                }
            });
        }
        Button mCalcelButton = ((Button) popupView.findViewById(R.id.cancel_button));
        if (mCalcelButton != null){
            mCalcelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
        }
    }

    private void download(String url){
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = manager.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            try {
                                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uriString));
                                PhotoUtils.saveImage(bmp);
                                Toast.makeText(getApplicationContext(), "相片下載完成!", Toast.LENGTH_SHORT).show();
                                manager.remove(downloadId);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        request = new DownloadManager.Request(Uri.parse(url));
        downloadId = manager.enqueue(request);
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