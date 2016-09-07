package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PhotoFragment extends Fragment {
    public static PhotoFragment newInstance() {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public String weddingInfoObjectId;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Neal","activity class = "+activity.getLocalClassName());
        if (activity.getLocalClassName().equals("activities.OwnMainActivity")){
            OwnMainActivity mMainActivity = (OwnMainActivity) activity;
            weddingInfoObjectId = mMainActivity.getWeddingInfoObjectId();
        }
        else {
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
        if(id == R.id.action_camera){
            //ToDo:使用相機拍照，並上傳照片
            //dispatchTakePictureIntent();
            new AlertDialog.Builder(getContext())
                    .setTitle("訊息")
                    .setMessage("功能尚未開放呦！")
                    .setPositiveButton("好！", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
        else if(id == R.id.action_album){
            //ToDo:從相簿裡選擇照片，並上傳照片
            new AlertDialog.Builder(getContext())
                    .setTitle("訊息")
                    .setMessage("功能尚未開放呦！")
                    .setPositiveButton("好！", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    private GridView photoGridView;
    private ParseFile[]photos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        photoGridView = (GridView)view.findViewById(R.id.photo_grid_view);
        ParseQuery query = ParseQuery.getQuery(weddingInfoObjectId+"Photo");
        query.orderByAscending("OriginalPhotoObjectID");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                String [] microPhotoUrls = new String[list.size()];
                String [] miniPhotoUrls = new String[list.size()];
                for (int i = 0 ; i<list.size() ; i++){
                    ParseFile microPhotoFile = list.get(i).getParseFile("microPhoto");
                    microPhotoUrls[i] = microPhotoFile.getUrl();
                    ParseFile miniPhotoFile = list.get(i).getParseFile("miniPhoto");
                    miniPhotoUrls[i] = miniPhotoFile.getUrl();
                }
                photoGridView.setAdapter(new gridViewCustomAdapter(getContext(), microPhotoUrls ));

                PhotoViewActivity.photoUrls = miniPhotoUrls;
                photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                        intent.putExtra(PhotoViewActivity.EXTRA_MESSAGE, position);
                        startActivity(intent);
                    }
                });
            }
        });
        return(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
