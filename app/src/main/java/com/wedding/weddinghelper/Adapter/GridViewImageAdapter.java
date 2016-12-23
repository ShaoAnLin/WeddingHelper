package com.wedding.weddinghelper.Adapter;

/**
 * Created by linshaoan on 2016/10/15.
 * Reference: http://www.androidhive.info/2013/09/android-fullscreen-image-slider-with-swipe-and-pinch-zoom-gestures/
 */

import com.squareup.picasso.Picasso;
import com.wedding.weddinghelper.activities.FullScreenViewActivity;
import com.wedding.weddinghelper.activities.JoinMainActivity;
import com.wedding.weddinghelper.fragements.PhotoFragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

//TODO: make sure it also works for both Own and Join
public class GridViewImageAdapter extends BaseAdapter {

    private Activity _activity;
    private ArrayList<String> _filePaths = new ArrayList<String>();
    private int imageWidth;

    public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths, int imageWidth) {
        this._activity = activity;
        this._filePaths = filePaths;
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return this._filePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return this._filePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(_activity);
        } else {
            imageView = (ImageView) convertView;
        }

        String url = (String) _filePaths.get(position);
        Picasso.with(this._activity)
                .load(url)
                .resize(500, 500) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                //.fit().centerInside()
                .into(imageView);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
        //imageView.setOnClickListener(new OnImageClickListener(position));
        imageView.setPadding(10, 10, 10, 10);

        return imageView;
    }

    /*class OnImageClickListener implements OnClickListener {
        int _postion;

        public OnImageClickListener(int position) {
            this._postion = position;
        }

        @Override
        public void onClick(View v) {
            // TODO: The upper left and lower left image cannot be selected correctly
            if (PhotoFragment.mDownload) {
                ImageView imageView = (ImageView) v;
                if (PhotoFragment.mDownloadList[this._postion]) {
                    imageView.setBackgroundColor(Color.TRANSPARENT);
                    PhotoFragment.mDownloadList[this._postion] = false;
                }
                else {
                    imageView.setBackgroundColor(Color.RED);
                    PhotoFragment.mDownloadList[this._postion] = true;
                }
            } else {
                Intent i = new Intent(_activity, FullScreenViewActivity.class);
                i.putExtra("position", _postion);
                _activity.startActivity(i);
            }
        }
    }*/
}
