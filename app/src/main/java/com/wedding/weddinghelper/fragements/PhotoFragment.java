package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.wedding.weddinghelper.Adapter.gridViewCustomAdapter;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.JoinMainActivity;
import com.wedding.weddinghelper.activities.OwnMainActivity;
import com.wedding.weddinghelper.activities.PhotoViewActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class PhotoFragment extends Fragment {

    private static final int TAKE_PHOTO = 1;
    private static final int PICK_IMAGE = 2;
    public String weddingInfoObjectId;
    private ProgressDialog progressDialog;
    private GridView photoGridView;
    private File mPhotoDirectory;
    private Bitmap originalBmp;

    private enum PHOTO_SCALE{ ORIGIN, MINI, MICRO };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Neal", "activity class = " + activity.getLocalClassName());
        if (activity.getLocalClassName().equals("activities.OwnMainActivity")) {
            OwnMainActivity mMainActivity = (OwnMainActivity) activity;
            weddingInfoObjectId = mMainActivity.getWeddingInfoObjectId();
        } else {
            JoinMainActivity mMainActivity = (JoinMainActivity) activity;
            weddingInfoObjectId = mMainActivity.getWeddingInfoObjectId();
        }
        Log.d("Neal", weddingInfoObjectId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.photo_list_menu, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            // 使用相機拍照，並上傳照片
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
            startActivityForResult(intent, TAKE_PHOTO);
            return true;
        } else if (id == R.id.action_album) {
            // 從相簿裡選擇照片，並上傳照片
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        photoGridView = (GridView) view.findViewById(R.id.photo_grid_view);
        getPhotoPreviewList();
        prepareDownloadDirectory();

        return (view);
    }

    public void getPhotoPreviewList() {
        showProgressBar("相片載入中...");
        ParseQuery query = ParseQuery.getQuery(weddingInfoObjectId + "Photo");
        query.orderByAscending("OriginalPhotoObjectID");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                String[] microPhotoUrls = new String[list.size()];
                String[] miniPhotoUrls = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ParseFile microPhotoFile = list.get(i).getParseFile("microPhoto");
                    microPhotoUrls[i] = microPhotoFile.getUrl();
                    ParseFile miniPhotoFile = list.get(i).getParseFile("miniPhoto");
                    miniPhotoUrls[i] = miniPhotoFile.getUrl();
                }
                photoGridView.setAdapter(new gridViewCustomAdapter(getContext(), microPhotoUrls));

                PhotoViewActivity.photoUrls = miniPhotoUrls;
                photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                        intent.putExtra(PhotoViewActivity.EXTRA_MESSAGE, position);
                        startActivity(intent);
                    }
                });
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity().getApplicationContext(), "相片上傳中...", Toast.LENGTH_SHORT).show();
        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            Uri Selected_Image_Uri = data.getData();
            try {
                originalBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Selected_Image_Uri);
                uploadPhoto();
            } catch (Exception e) {
                Log.d("Neal", "Bitmap exception = " + e.toString());
            }
        } else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri Selected_Image_Uri = data.getData();
            try {
                originalBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Selected_Image_Uri);
                uploadPhoto();
            } catch (Exception e) {
            }
        }
    }

    private void uploadPhoto() {
        ByteArrayOutputStream bytesOri = getByteArrayFromBmp(PHOTO_SCALE.ORIGIN);
        final ParseFile fileUploadingToParse = new ParseFile("originPhoto.jpg", bytesOri.toByteArray());
        fileUploadingToParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                final ParseObject photoOriginal = new ParseObject(weddingInfoObjectId + "OriginalPhoto");
                photoOriginal.put("Photo", fileUploadingToParse);
                photoOriginal.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        // upload micro, mini photo
                        uploadCompressedPhoto(photoOriginal.getObjectId());
                    }
                });
            }
        });
    }

    // called from uploadPhoto()
    private void uploadCompressedPhoto(final String originalPhotoId) {
        ByteArrayOutputStream bytesMini = getByteArrayFromBmp(PHOTO_SCALE.MINI);
        final ParseFile miniPhotoUploadingToParse = new ParseFile("miniPhoto.jpg", bytesMini.toByteArray());
        miniPhotoUploadingToParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ByteArrayOutputStream bytesMicro = getByteArrayFromBmp(PHOTO_SCALE.MICRO);
                final ParseFile microPhotoUploadingToParse = new ParseFile("microPhoto.jpg", bytesMicro.toByteArray());
                microPhotoUploadingToParse.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseObject photoThumbnail = new ParseObject(weddingInfoObjectId + "Photo");
                        photoThumbnail.put("OriginalPhotoObjectID", originalPhotoId);
                        photoThumbnail.put("miniPhoto", miniPhotoUploadingToParse);
                        photoThumbnail.put("microPhoto", microPhotoUploadingToParse);
                        photoThumbnail.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("Neal", "Done saving 3 files!");
                                getPhotoPreviewList();
                            }
                        });
                    }
                });
            }
        });
    }

    private void showProgressBar(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage(msg);
        progressDialog.setTitle(null);
        progressDialog.show();
    }

    // save image to Download directory
    private void saveImage(Bitmap finalBitmap) {
        Random generator = new Random();
        int n = generator.nextInt(10000);
        String fname = "Image-" + n;
        if (!mPhotoDirectory.exists()) {
            mPhotoDirectory.mkdir();
        }
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

    private void prepareDownloadDirectory() {
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

    private ByteArrayOutputStream getByteArrayFromBmp(PHOTO_SCALE scale){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        if (scale == PHOTO_SCALE.ORIGIN){
            originalBmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }
        else if (scale == PHOTO_SCALE.MINI){
            Bitmap miniBmp = getScaledBitmap(500);
            miniBmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }
        else if (scale == PHOTO_SCALE.MICRO){
            Bitmap microBmp = getScaledBitmap(200);
            microBmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        }
        return bytes;
    }

    private Bitmap getScaledBitmap(float MIN_PIXEL){
        float width = (float) originalBmp.getWidth();
        float height = (float) originalBmp.getHeight();
        float w = (width < height ? MIN_PIXEL : width / height * MIN_PIXEL);
        float h = (height < width ? MIN_PIXEL : height / width * MIN_PIXEL);
        return Bitmap.createScaledBitmap(originalBmp, (int) w, (int) h, true);
    }
}
