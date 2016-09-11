package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.app.ProgressDialog;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PhotoFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private static final int TAKE_PHOTO = 2;
    public String weddingInfoObjectId;

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
            //ToDo:使用相機拍照，並上傳照片
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
            startActivityForResult(intent, TAKE_PHOTO);
            return true;
        } else if (id == R.id.action_album) {
            //ToDo:從相簿裡選擇照片，並上傳照片
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private GridView photoGridView;
    private ParseFile[] photos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        photoGridView = (GridView) view.findViewById(R.id.photo_grid_view);
        getPhotoPreviewList();

        return (view);
    }

    public void getPhotoPreviewList(){
        showProgressPar();
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
        Bitmap scaledBmp=null;
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri Selected_Image_Uri = data.getData();
            try {
                Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Selected_Image_Uri);
                // scale the image for display
                final float MAX_PIXEL = 800;
                float width = (float) captureBmp.getWidth();
                float height = (float) captureBmp.getHeight();
                float w = (width > height ? MAX_PIXEL : width / height * MAX_PIXEL);
                float h = (height > width ? MAX_PIXEL : height / width * MAX_PIXEL);
                scaledBmp = Bitmap.createScaledBitmap(captureBmp, (int)w, (int)h, true);
                ImageView mImageView = (ImageView) getActivity().findViewById(R.id.testImageView);
                mImageView.setImageBitmap(scaledBmp);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                scaledBmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                uploadPhoto(bytes);
            }catch (Exception e){}
        }
        else if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            Uri Selected_Image_Uri = data.getData();
            try {
                Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Selected_Image_Uri);
                // scale the image for display
                final float MAX_PIXEL = 800;
                float width = (float) captureBmp.getWidth();
                float height = (float) captureBmp.getHeight();
                float w = (width > height ? MAX_PIXEL : width / height * MAX_PIXEL);
                float h = (height > width ? MAX_PIXEL : height / width * MAX_PIXEL);
                scaledBmp = Bitmap.createScaledBitmap(captureBmp, (int)w, (int)h, true);
                ImageView mImageView = (ImageView) getActivity().findViewById(R.id.testImageView);
                mImageView.setImageBitmap(scaledBmp);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                scaledBmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                //File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Imagename13.jpg");
                //Log.d("Neal","File path = "+f.getPath()+"  " +f.getAbsolutePath());
                uploadPhoto(bytes);

            }catch (Exception e){
                Log.d("Neal","Bitmap exception = " +e.toString());
            }
        }
        else {
            Log.d("Shawn", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.d("Shawn", "User cancelled");
                    break;
                default:
                    break;

            }
        }
    }

    public void uploadPhoto(ByteArrayOutputStream bytes){
        showProgressPar();
        final ParseFile fileUploadingToParse = new ParseFile("microPhoto.jpg",bytes.toByteArray());
        fileUploadingToParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                final ParseObject photoOriginal = new ParseObject(weddingInfoObjectId+"OriginalPhoto");
                photoOriginal.put("Photo", fileUploadingToParse);
                photoOriginal.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseObject photoThumbnail = new ParseObject(weddingInfoObjectId+"Photo");
                        photoThumbnail.put("OriginalPhotoObjectID",photoOriginal.getObjectId());
                        photoThumbnail.put("microPhoto",fileUploadingToParse);
                        photoThumbnail.put("miniPhoto", fileUploadingToParse);
                        photoThumbnail.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                progressDialog.dismiss();
                                Log.d("Neal", "Done saving 3 files!");
                                getPhotoPreviewList();
                            }
                        });
                    }
                });
            }
        });
    }
    ProgressDialog progressDialog;
    public void showProgressPar(){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("處理中...");
        progressDialog.setTitle(null);
        progressDialog.show();
    }
}
