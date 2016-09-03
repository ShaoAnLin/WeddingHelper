package com.wedding.weddinghelper.Adapter;

/**
 * Created by Neal on 2016/9/3.
 */
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.wedding.weddinghelper.R;
import com.squareup.picasso.Picasso;
public class gridViewCustomAdapter extends BaseAdapter {

    //int[] data={R.drawable.couple,R.drawable.create_wedding};
    private Context context;

    private  String[] photoUrls;
    public gridViewCustomAdapter(Context context, String[] thePhotoUrls){
        this.context = context;
        photoUrls = thePhotoUrls;
    }

    @Override
    public int getCount() {
        return photoUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return photoUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image = (ImageView)convertView;
        if (image == null) {
            image = new ImageView(context);
        }
        String url = (String) getItem(position);

        Picasso.with(context)
                .load(url)
                .resize(200,200)
                .centerCrop()
                .into(image);
        return image;
    }
}
