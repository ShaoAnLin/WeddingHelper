package com.wedding.weddinghelper.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by linshaoan on 2016/10/15.
 */
public class PhotoUtils {

    // Grid view constants
    public static final int NUM_OF_COLUMNS = 3;
    public static final int GRID_PADDING = 8; // in dp
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");

    private Context _context;

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
}
