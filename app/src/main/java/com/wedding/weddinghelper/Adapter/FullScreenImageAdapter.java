package com.wedding.weddinghelper.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.Util.TouchImageView;

import java.util.ArrayList;

/**
 * Created by linshaoan on 2016/10/15.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        //Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        String url = (String) _imagePaths.get(position);
        Context context = this._activity.getApplicationContext();
        Picasso.with(context)
                .load(url)
                .into(imgDisplay);

        // the bitmap must not be too large. Otherwise, it crashes
        // the following codes scale the image down
        /*float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        float MIN_PIXEL = 400;
        float w = (width < height ? MIN_PIXEL : width / height * MIN_PIXEL);
        float h = (height < width ? MIN_PIXEL : height / width * MIN_PIXEL);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) w, (int) h, true);

        imgDisplay.setImageBitmap(scaledBitmap);*/

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
