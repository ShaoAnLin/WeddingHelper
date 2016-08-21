package com.wedding.weddinghelper.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wedding.weddinghelper.R;

public class CreateWeddingActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wedding);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.create_wedding_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.create_wedding));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }
        weddingName = (EditText) findViewById(R.id.create_wedding_name_edit_text);

        weddingPassword = (EditText) findViewById(R.id.create_wedding_password_edit_text);
        groomName = (EditText) findViewById(R.id.groom_name_edit_text);
        brideName = (EditText) findViewById(R.id.bride_name_edit_text);
        engageDate = (EditText) findViewById(R.id.engage_time_edit_text);
        engagePlace = (EditText) findViewById(R.id.engage_place_edit_text);
        engageAddress = (EditText) findViewById(R.id.engage_address_edit_text);
        engagePlaceIntroduce = (EditText) findViewById(R.id.engage_website_edit_text);
        marryDate = (EditText) findViewById(R.id.marry_time_edit_text);
        marryPlace = (EditText) findViewById(R.id.marry_place_edit_text);
        marryAddress = (EditText) findViewById(R.id.marry_address_edit_text);
        marryPlaceIntroduce = (EditText) findViewById(R.id.marry_website_edit_text);
        modifyFormDeadline = (EditText) findViewById(R.id.modify_deadline);
        sameDayToggleButton = (ToggleButton) findViewById(R.id.same_day_toggle_button);
        differentDayToggleButton = (ToggleButton)findViewById(R.id.different_day_toggle_button);

        engageDateTextView = (TextView)findViewById(R.id.engage_date_text_view);
        engagePlaceTextView = (TextView)findViewById(R.id.engage_place_text_view);
        engageAddressTextView = (TextView)findViewById(R.id.engage_address_text_view);
        engageWebsiteTextView = (TextView)findViewById(R.id.engage_website_text_view);


        marryDateLinearLayout = (LinearLayout)findViewById(R.id.marry_date_linear_layout);
        marryPlaceLinearLayout = (LinearLayout)findViewById(R.id.marry_place_linear_layout);
        marryAddressLinearLayout = (LinearLayout)findViewById(R.id.marry_address_linear_layout);
        marryWebsiteLinearLayout = (LinearLayout)findViewById(R.id.marry_website_linear_layout);

        sameDayToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sameDayToggleButton.isChecked()){
                    sameDayToggleButton.setChecked(true);
                }
                differentDayToggleButton.setChecked(false);
                marryDateLinearLayout.setVisibility(View.GONE);
                marryPlaceLinearLayout.setVisibility(View.GONE);
                marryAddressLinearLayout.setVisibility(View.GONE);
                marryWebsiteLinearLayout.setVisibility(View.GONE);
                engageDateTextView.setText(getString(R.string.wedding_time));
                engagePlaceTextView.setText(getString(R.string.wedding_place));
                engageAddressTextView.setText(getString(R.string.wedding_address));
                engageWebsiteTextView.setText(getString(R.string.wedding_website));
            }
        });
        differentDayToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!differentDayToggleButton.isChecked()){
                    differentDayToggleButton.setChecked(true);
                }
                sameDayToggleButton.setChecked(false);
                marryDateLinearLayout.setVisibility(View.VISIBLE);
                marryPlaceLinearLayout.setVisibility(View.VISIBLE);
                marryAddressLinearLayout.setVisibility(View.VISIBLE);
                marryWebsiteLinearLayout.setVisibility(View.VISIBLE);
                engageDateTextView.setText(getString(R.string.engage_time));
                engagePlaceTextView.setText(getString(R.string.engage_place));
                engageAddressTextView.setText(getString(R.string.engage_address));
                engageWebsiteTextView.setText(getString(R.string.engage_website));
            }
        });


    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_wedding_action_bar_menu, menu);
        return true;
    }
    EditText weddingName, weddingPassword, groomName, brideName, engageDate, engagePlace, engageAddress, engagePlaceIntroduce, marryDate, marryPlace, marryAddress, marryPlaceIntroduce, modifyFormDeadline;
    String weddingInformationObjectId;
    ToggleButton sameDayToggleButton, differentDayToggleButton;
    LinearLayout marryDateLinearLayout, marryPlaceLinearLayout, marryAddressLinearLayout, marryWebsiteLinearLayout;
    TextView engageDateTextView, engagePlaceTextView, engageAddressTextView, engageWebsiteTextView;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //ToDo:必填欄位提示。
        switch (item.getItemId()) {
            case R.id.done_button:
                Log.d(getClass().getSimpleName(), "Done create wedding");

                ParseQuery checkWeddingExist = ParseQuery.getQuery("Information");
                checkWeddingExist.whereEqualTo("weddingAccount", weddingName.getText().toString());
                checkWeddingExist.whereEqualTo("weddingPassword", weddingPassword.getText().toString());
                checkWeddingExist.countInBackground(new CountCallback() {
                    @Override
                    public void done(int i, ParseException e) {
                        if (e == null){
                            Log.d("Neal", "number of the wedding name = "+i);
                            if (i !=0){
                                Log.d("Neal", "婚宴名稱與通關密語已被他人使用，請用其他婚宴名稱或通關密碼。");
                            }
                            else {
                                final ParseObject weddingInformation = new ParseObject("Information");
                                weddingInformation.put("weddingAccount",weddingName.getText().toString());
                                weddingInformation.put("weddingPassword",weddingPassword.getText().toString());
                                weddingInformation.put("groomName",groomName.getText().toString());
                                weddingInformation.put("brideName",brideName.getText().toString());
                                weddingInformation.put("engageDate",engageDate.getText().toString());
                                weddingInformation.put("engagePlace",engagePlace.getText().toString());
                                weddingInformation.put("engageAddress",engageAddress.getText().toString());
                                weddingInformation.put("engagePlaceIntroduce",engagePlaceIntroduce.getText().toString());
                                weddingInformation.put("marryDate",marryDate.getText().toString());
                                weddingInformation.put("marryPlace",marryPlace.getText().toString());
                                weddingInformation.put("marryAddress",marryAddress.getText().toString());
                                weddingInformation.put("marryPlaceIntroduce",marryPlaceIntroduce.getText().toString());
                                weddingInformation.put("managerAccount",ParseUser.getCurrentUser().getUsername());
                                weddingInformation.put("modifyFormDeadline", modifyFormDeadline.getText().toString());
                                if (sameDayToggleButton.isChecked() && !differentDayToggleButton.isChecked()){
                                    weddingInformation.put("onlyOneSession",true);
                                }
                                else{
                                    weddingInformation.put("onlyOneSession",false);
                                }
                                weddingInformation.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        weddingInformationObjectId = weddingInformation.getObjectId();
                                        //ToDo:建立婚宴成功後就直接進該婚宴頁面。
                                        finish();
                                    }
                                });
                            }
                        }

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
