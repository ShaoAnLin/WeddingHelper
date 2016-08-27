package com.wedding.weddinghelper.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wedding.weddinghelper.R;

public class CreateWeddingActivity extends AppCompatActivity
        implements View.OnClickListener {

    boolean isEditWedding;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wedding);


        isEditWedding = getIntent().getBooleanExtra("isEditWedding",false);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.create_wedding_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            if (isEditWedding){
                getSupportActionBar().setTitle("編輯婚宴資訊");
            }
            else {
                getSupportActionBar().setTitle(getString(R.string.create_wedding));
            }
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
        //若是編輯婚宴，則先取回原本資料。
        if (isEditWedding){
            weddingInformationObjectId = getIntent().getStringExtra("weddingInfoObjectId");
            ParseQuery query = ParseQuery.getQuery("Information");
            query.getInBackground(weddingInformationObjectId,new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject weddingInformation, ParseException e) {
                    weddingName.setText(weddingInformation.getString("weddingAccount"));
                    weddingPassword.setText(weddingInformation.getString("weddingPassword"));
                    groomName.setText(weddingInformation.getString("groomName"));
                    brideName.setText(weddingInformation.getString("brideName"));
                    engageDate.setText(weddingInformation.getString("engageDate"));
                    engagePlace.setText(weddingInformation.getString("engagePlace"));
                    engageAddress.setText(weddingInformation.getString("engageAddress"));
                    engagePlaceIntroduce.setText(weddingInformation.getString("engagePlaceIntroduce"));
                    marryDate.setText(weddingInformation.getString("marryDate"));
                    marryPlace.setText(weddingInformation.getString("marryPlace"));
                    marryAddress.setText(weddingInformation.getString("marryAddress"));
                    marryPlaceIntroduce.setText(weddingInformation.getString("marryPlaceIntroduce"));
                    modifyFormDeadline.setText(weddingInformation.getString("modifyFormDeadline"));
                    if (weddingInformation.getBoolean("onlyOneSession")){
                        sameDayToggleButton.setChecked(true);
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
                    else {
                        sameDayToggleButton.setChecked(false);
                        differentDayToggleButton.setChecked(true);
                        marryDateLinearLayout.setVisibility(View.VISIBLE);
                        marryPlaceLinearLayout.setVisibility(View.VISIBLE);
                        marryAddressLinearLayout.setVisibility(View.VISIBLE);
                        marryWebsiteLinearLayout.setVisibility(View.VISIBLE);
                        engageDateTextView.setText(getString(R.string.engage_time));
                        engagePlaceTextView.setText(getString(R.string.engage_place));
                        engageAddressTextView.setText(getString(R.string.engage_address));
                        engageWebsiteTextView.setText(getString(R.string.engage_website));
                    }
                }
            });
        }


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
        //ToDo:必填欄位提示，未完善。
        switch (item.getItemId()) {
            case R.id.done_button:
                boolean contentError = false;
                if (TextUtils.isEmpty(weddingName.getText().toString())){
                    weddingName.setError("請輸入婚宴名稱。");
                    contentError = true;
                }
                if (TextUtils.isEmpty(weddingPassword.getText().toString())){
                    weddingPassword.setError("請輸入通關密語。");
                    contentError = true;
                }
                if (TextUtils.isEmpty(groomName.getText().toString())){
                    groomName.setError("請輸入新郎姓名。");
                    contentError = true;
                }
                if (TextUtils.isEmpty(brideName.getText().toString())){
                    brideName.setError("請輸入新娘姓名。");
                    contentError = true;
                }
                if (sameDayToggleButton.isChecked() && !differentDayToggleButton.isChecked()) {
                    if (TextUtils.isEmpty(engageDate.getText().toString())) {
                        engageDate.setError("請輸入婚宴日期。");
                        contentError = true;
                    }
                    if (TextUtils.isEmpty(engagePlace.getText().toString())) {
                        engagePlace.setError("請輸入婚宴餐廳名稱。");
                        contentError = true;
                    }
                    if (TextUtils.isEmpty(engageAddress.getText().toString())) {
                        engageAddress.setError("請輸入婚宴餐廳地址。");
                        contentError = true;
                    }
                }
                else{
                    if (TextUtils.isEmpty(engageDate.getText().toString())) {
                        engageDate.setError("請輸入訂婚日期。");
                        contentError = true;
                    }
                    if (TextUtils.isEmpty(engagePlace.getText().toString())) {
                        engagePlace.setError("請輸入訂婚餐廳名稱。");
                        contentError = true;
                    }
                    if (TextUtils.isEmpty(engageAddress.getText().toString())) {
                        engageAddress.setError("請輸入訂婚餐廳地址。");
                        contentError = true;
                    }
                    if (TextUtils.isEmpty(marryDate.getText().toString())){
                        marryDate.setError("請輸入結婚日期。");
                        contentError = true;
                    }
                    if (TextUtils.isEmpty(marryPlace.getText().toString())){
                        marryPlace.setError("請輸入結婚餐廳名稱。");
                        contentError = true;
                    }
                    if (TextUtils.isEmpty(marryAddress.getText().toString())){
                        marryAddress.setError("請輸入結婚餐廳地址。");
                        contentError = true;
                    }
                }
                if (TextUtils.isEmpty(modifyFormDeadline.getText().toString())){
                    modifyFormDeadline.setError("請輸入填寫出席意願期限。");
                    contentError = true;
                }

                if (!contentError) {
                    if (!isEditWedding) {
                        Log.d(getClass().getSimpleName(), "Done create wedding");
                        ParseQuery checkWeddingExist = ParseQuery.getQuery("Information");
                        checkWeddingExist.whereEqualTo("weddingAccount", weddingName.getText().toString());
                        checkWeddingExist.whereEqualTo("weddingPassword", weddingPassword.getText().toString());
                        checkWeddingExist.countInBackground(new CountCallback() {
                            @Override
                            public void done(int i, ParseException e) {
                                if (e == null) {
                                    Log.d("Neal", "number of the wedding name = " + i);
                                    if (i != 0) {
                                        new AlertDialog.Builder(context)
                                                .setTitle("訊息")
                                                .setMessage("婚宴名稱與通關密語已被他人使用，請用其他婚宴名稱或通關密碼。")
                                                .setPositiveButton("好！", null)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                        Log.d("Neal", "婚宴名稱與通關密語已被他人使用，請用其他婚宴名稱或通關密碼。");
                                    } else {
                                        final ParseObject weddingInformation = new ParseObject("Information");
                                        weddingInformation.put("weddingAccount", weddingName.getText().toString());
                                        weddingInformation.put("weddingPassword", weddingPassword.getText().toString());
                                        weddingInformation.put("groomName", groomName.getText().toString());
                                        weddingInformation.put("brideName", brideName.getText().toString());
                                        weddingInformation.put("engageDate", engageDate.getText().toString());
                                        weddingInformation.put("engagePlace", engagePlace.getText().toString());
                                        weddingInformation.put("engageAddress", engageAddress.getText().toString());
                                        weddingInformation.put("engagePlaceIntroduce", engagePlaceIntroduce.getText().toString());
                                        weddingInformation.put("marryDate", marryDate.getText().toString());
                                        weddingInformation.put("marryPlace", marryPlace.getText().toString());
                                        weddingInformation.put("marryAddress", marryAddress.getText().toString());
                                        weddingInformation.put("marryPlaceIntroduce", marryPlaceIntroduce.getText().toString());
                                        weddingInformation.put("managerAccount", ParseUser.getCurrentUser().getUsername());
                                        weddingInformation.put("modifyFormDeadline", modifyFormDeadline.getText().toString());
                                        if (sameDayToggleButton.isChecked() && !differentDayToggleButton.isChecked()) {
                                            weddingInformation.put("onlyOneSession", true);
                                        } else {
                                            weddingInformation.put("onlyOneSession", false);
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
                    }
                    else {
                        //ToDo: 將修改過後的內容上傳。
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
