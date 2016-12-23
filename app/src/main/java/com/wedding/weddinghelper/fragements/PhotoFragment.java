package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
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
import com.wedding.weddinghelper.Adapter.GridViewImageAdapter;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.Util.PhotoUtils;
import com.wedding.weddinghelper.activities.DownloadPhotosActivity;
import com.wedding.weddinghelper.activities.FullScreenViewActivity;
import com.wedding.weddinghelper.activities.JoinMainActivity;
import com.wedding.weddinghelper.activities.OwnMainActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PhotoFragment extends Fragment {

    private static final int TAKE_PHOTO = 1;
    private static final int PICK_IMAGE = 2;
    private ProgressDialog progressDialog;
    private GridView photoGridView;
    private Bitmap originalBmp;

    public String weddingInfoObjectId;

    private PhotoUtils utils;
    private int columnWidth;

    static public String [] miniPhotoUrls = new String[0];
    static public String [] microPhotoUrls = new String[0];

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
        } else if (id == R.id.action_download ){
            Intent intent = new Intent(getActivity(), DownloadPhotosActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        photoGridView = (GridView) view.findViewById(R.id.photo_grid_view);
        getPhotoPreviewList();
        return (view);
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

    public void getPhotoPreviewList() {
        showProgressBar("相片載入中...");
        ParseQuery query = ParseQuery.getQuery(weddingInfoObjectId + "Photo");
        query.orderByAscending("OriginalPhotoObjectID");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                microPhotoUrls = new String[list.size()];
                miniPhotoUrls = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ParseFile microPhotoFile = list.get(i).getParseFile("microPhoto");
                    microPhotoUrls[i] = microPhotoFile.getUrl();
                    ParseFile miniPhotoFile = list.get(i).getParseFile("miniPhoto");
                    miniPhotoUrls[i] = miniPhotoFile.getUrl();
                }
                utils = new PhotoUtils(getActivity());
                InitilizeGridLayout();
                ArrayList<String> imagePaths = new ArrayList(Arrays.asList(microPhotoUrls));
                GridViewImageAdapter adapter = new GridViewImageAdapter(getActivity(), imagePaths, columnWidth);
                photoGridView.setAdapter(adapter);

                photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent i = new Intent(getActivity(), FullScreenViewActivity.class);
                        i.putExtra("position", position);
                        getActivity().startActivity(i);
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
        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            Toast.makeText(getActivity().getApplicationContext(), "相片上傳中...", Toast.LENGTH_SHORT).show();
            Uri Selected_Image_Uri = data.getData();
            try {
                originalBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Selected_Image_Uri);
                uploadPhoto();
            } catch (Exception e) {
                Log.d("Neal", "Bitmap exception = " + e.toString());
            }
        } else if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Toast.makeText(getActivity().getApplicationContext(), "相片上傳中...", Toast.LENGTH_SHORT).show();
            Uri Selected_Image_Uri = data.getData();
            try {
                originalBmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Selected_Image_Uri);
                uploadPhoto();
            } catch (Exception e) {
            }
        }
    }

    private void uploadPhoto() {
        ByteArrayOutputStream bytesOri = PhotoUtils.getByteArrayFromBmp(originalBmp, PhotoUtils.PHOTO_SCALE.ORIGIN);
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
        ByteArrayOutputStream bytesMini = PhotoUtils.getByteArrayFromBmp(originalBmp, PhotoUtils.PHOTO_SCALE.MINI);
        final ParseFile miniPhotoUploadingToParse = new ParseFile("miniPhoto.jpg", bytesMini.toByteArray());
        miniPhotoUploadingToParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ByteArrayOutputStream bytesMicro = PhotoUtils.getByteArrayFromBmp(originalBmp, PhotoUtils.PHOTO_SCALE.MICRO);
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
}
