package com.wedding.weddinghelper.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by linshaoan on 2016/10/15.
 */
public class PhotoUtils {

    // Grid view constants
    public static final int NUM_OF_COLUMNS = 3;
    public static final int GRID_PADDING = 8; // in dp
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");

    private Context _context;
    public static File mPhotoDirectory;

    public enum PHOTO_SCALE{ ORIGIN, MINI, MICRO };

    public PhotoUtils(Context context) {
        this._context = context;
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    public static ByteArrayOutputStream getByteArrayFromBmp(Bitmap originalBmp, PHOTO_SCALE scale){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        if (scale == PHOTO_SCALE.ORIGIN){
            originalBmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }
        else if (scale == PHOTO_SCALE.MINI){
            Bitmap miniBmp = getScaledBitmap(originalBmp, 1000);
            miniBmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }
        else if (scale == PHOTO_SCALE.MICRO){
            Bitmap microBmp = getScaledBitmap(originalBmp, 400);
            microBmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }
        return bytes;
    }

    public static Bitmap getScaledBitmap(Bitmap originalBmp, float MIN_PIXEL){
        float width = (float) originalBmp.getWidth();
        float height = (float) originalBmp.getHeight();
        float w = (width < height ? MIN_PIXEL : width / height * MIN_PIXEL);
        float h = (height < width ? MIN_PIXEL : height / width * MIN_PIXEL);
        return Bitmap.createScaledBitmap(originalBmp, (int) w, (int) h, true);
    }

    public static void prepareDownloadDirectory() {
        if (Environment.getExternalStorageState() == null) {
            // store internal
            Log.d("Shawn", "NO SD card found");
            mPhotoDirectory = new File(Environment.getDataDirectory() + "/Download/");
            if (!mPhotoDirectory.exists()) {
                mPhotoDirectory.mkdir();
            }
        } else {
            // store in SD card
            Log.d("Shawn", "Have SD card");
            mPhotoDirectory = new File(Environment.getExternalStorageDirectory() + "/Download/");
            if (!mPhotoDirectory.exists()) {
                mPhotoDirectory.mkdir();
            }
        }
    }

    // save image to Download directory
    // TODO: image name?
    public static void saveImage(Bitmap finalBitmap) {
        prepareDownloadDirectory();

        Random generator = new Random();
        int n = generator.nextInt(10000);
        String fname = "Image-" + n;
        File file = new File(mPhotoDirectory, fname + ".jpg");
        n = 1;
        while (file.exists()) {
            file = new File(mPhotoDirectory, fname + "(" + n + ")" + ".jpg");
            n++;
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
