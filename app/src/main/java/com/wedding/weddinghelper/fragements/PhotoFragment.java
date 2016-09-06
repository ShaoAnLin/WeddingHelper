package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
            return true;
        }
        else if(id == R.id.action_album){
            //ToDo:從相簿裡選擇照片，並上傳照片
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
