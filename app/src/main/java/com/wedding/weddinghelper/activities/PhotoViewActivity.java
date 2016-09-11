package com.wedding.weddinghelper.activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wedding.weddinghelper.R;

public class PhotoViewActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private GestureDetectorCompat mDetector;

    ProgressDialog progressDialog;
    ImageView mImageView;
    public static String [] photoUrls;
    public static final String EXTRA_MESSAGE = "PHOTO_EXTRA";

    private static float downX = 0;
    private static float downY = 0;
    private static float upX = 0;
    private static float upY = 0;

    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("處理中...");
        progressDialog.setTitle(null);
        mImageView = (ImageView) findViewById(R.id.photo_image_view);
        if (mImageView != null){
            mImageView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(event);
                    return true;
                }
            });
        }
        progressDialog.show();
        mPosition = getIntent().getIntExtra(EXTRA_MESSAGE, 0);
        showImage();

        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                float x = Math.abs(upX - downX);
                float y = Math.abs(upY - downY);
                double z = Math.sqrt(x*x+y*y);
                int jiaodu = Math.round((float)(Math.asin(y/z)/Math.PI*180));

                if (upY < downY && jiaodu > 45) {
                    // swipe up
                }else if(upY > downY && jiaodu > 45) {
                    // swipe down
                }else if(upX < downX && jiaodu <= 45) {
                    // swipe left
                    showNextPhoto();
                }else if(upX > downX && jiaodu <= 45) {
                    // swipe right
                    showPreviousPhoto();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        return true;
    }
    @Override
    public void onLongPress(MotionEvent event) {
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }
    @Override
    public void onShowPress(MotionEvent event) {
    }
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }
    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return true;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return true;
    }

    private void showNextPhoto(){
        if (mPosition < (photoUrls.length - 1)){
            mPosition++;
            progressDialog.show();
            showImage();
        }
    }
    private void showPreviousPhoto(){
        if (mPosition > 0){
            mPosition--;
            progressDialog.show();
            showImage();
        }
    }

    private void showImage(){
        String url = photoUrls[mPosition];
        Picasso.with(getApplicationContext())
                .load(url)
                .centerInside()
                .fit()
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError() {
                    }
                });
    }
}
