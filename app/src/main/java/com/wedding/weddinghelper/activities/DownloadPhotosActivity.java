package com.wedding.weddinghelper.activities;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.wedding.weddinghelper.Adapter.GridViewImageAdapter;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.Util.PhotoUtils;
import com.wedding.weddinghelper.fragements.PhotoFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DownloadPhotosActivity extends AppCompatActivity {

    private PhotoUtils utils;
    private int columnWidth;

    private GridView photoGridView;
    private Button mCancel, mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_photos);

        mCancel = (Button) findViewById(R.id.download_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mConfirm = (Button) findViewById(R.id.download_confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinMainActivity.downloadPhotoList();
                Toast.makeText(getApplicationContext(), "相片下載中...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        photoGridView = (GridView) findViewById(R.id.download_photo_grid_view);
        getPhotoListFromPhotoFragment();
    }

    private void getPhotoListFromPhotoFragment() {
        utils = new PhotoUtils(this);
        InitilizeGridLayout();
        ArrayList<String> imagePaths = new ArrayList(Arrays.asList(PhotoFragment.microPhotoUrls));
        GridViewImageAdapter adapter = new GridViewImageAdapter(this, imagePaths, columnWidth);
        photoGridView.setAdapter(adapter);
        PhotoFragment.mDownloadList = new boolean[PhotoFragment.microPhotoUrls.length];

        photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ImageView imageView = (ImageView) v;
                if (PhotoFragment.mDownloadList[position]) {
                    imageView.setBackgroundColor(Color.TRANSPARENT);
                    PhotoFragment.mDownloadList[position] = false;
                } else {
                    imageView.setBackgroundColor(Color.RED);
                    PhotoFragment.mDownloadList[position] = true;
                }
            }
        });
    }

    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PhotoUtils.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((PhotoUtils.NUM_OF_COLUMNS + 1) * padding)) / PhotoUtils.NUM_OF_COLUMNS);

        photoGridView.setNumColumns(PhotoUtils.NUM_OF_COLUMNS);
        photoGridView.setColumnWidth(columnWidth);
        photoGridView.setStretchMode(GridView.NO_STRETCH);
        photoGridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        photoGridView.setHorizontalSpacing((int) padding);
        photoGridView.setVerticalSpacing((int) padding);
    }
}
