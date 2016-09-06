package com.wedding.weddinghelper.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wedding.weddinghelper.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateWeddingActivity extends AppCompatActivity
        implements View.OnClickListener {

    boolean isEditWedding;
    final Context context = this;
    ProgressDialog progressDialog;
    ParseObject currentWeddingInformation;
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("處理中...");
        progressDialog.setTitle(null);

        weddingName = (EditText) findViewById(R.id.create_wedding_name_edit_text);

        weddingPassword = (EditText) findViewById(R.id.create_wedding_password_edit_text);
        groomName = (EditText) findViewById(R.id.groom_name_edit_text);
        brideName = (EditText) findViewById(R.id.bride_name_edit_text);
        engagePlace = (EditText) findViewById(R.id.engage_place_edit_text);
        engageAddress = (EditText) findViewById(R.id.engage_address_edit_text);
        engagePlaceIntroduce = (EditText) findViewById(R.id.engage_website_edit_text);
        marryPlace = (EditText) findViewById(R.id.marry_place_edit_text);
        marryAddress = (EditText) findViewById(R.id.marry_address_edit_text);
        marryPlaceIntroduce = (EditText) findViewById(R.id.marry_website_edit_text);
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

        pickEngageDate = (Button)findViewById(R.id.pick_engage_date);
        pickEngageTime = (Button)findViewById(R.id.pick_engage_time);

        pickMarryDate = (Button)findViewById(R.id.pick_marry_date);
        pickMarryTime = (Button)findViewById(R.id.pick_marry_time);
        pickModifyDeadlineDate = (Button)findViewById(R.id.pick_modify_deadline_date);
        pickModifyDeadlineTime = (Button)findViewById(R.id.pick_modify_deadline_time);

        View.OnClickListener pickDateListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(view);
            }
        };
        View.OnClickListener pickTimeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(view);
            }
        };

        pickEngageDate.setOnClickListener(pickDateListener);
        pickEngageTime.setOnClickListener(pickTimeListener);

        pickMarryDate.setOnClickListener(pickDateListener);
        pickMarryTime.setOnClickListener(pickTimeListener);

        pickModifyDeadlineDate.setOnClickListener(pickDateListener);
        pickModifyDeadlineTime.setOnClickListener(pickTimeListener);




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
            progressDialog.show();
            weddingInformationObjectId = getIntent().getStringExtra("weddingInfoObjectId");
            ParseQuery query = ParseQuery.getQuery("Information");
            query.getInBackground(weddingInformationObjectId,new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject weddingInformation, ParseException e) {
                    currentWeddingInformation = weddingInformation;
                    weddingName.setText(weddingInformation.getString("weddingAccount"));
                    weddingPassword.setText(weddingInformation.getString("weddingPassword"));
                    groomName.setText(weddingInformation.getString("groomName"));
                    brideName.setText(weddingInformation.getString("brideName"));
                    pickEngageDate.setText(weddingInformation.getString("engageDate").substring(0,14));
                    pickEngageTime.setText(weddingInformation.getString("engageDate").substring(15));
                    engagePlace.setText(weddingInformation.getString("engagePlace"));
                    engageAddress.setText(weddingInformation.getString("engageAddress"));
                    engagePlaceIntroduce.setText(weddingInformation.getString("engagePlaceIntroduce"));
                    pickMarryDate.setText(weddingInformation.getString("marryDate").substring(0,14));
                    pickMarryTime.setText(weddingInformation.getString("marryDate").substring(15));
                    marryPlace.setText(weddingInformation.getString("marryPlace"));
                    marryAddress.setText(weddingInformation.getString("marryAddress"));
                    marryPlaceIntroduce.setText(weddingInformation.getString("marryPlaceIntroduce"));
                    pickModifyDeadlineDate.setText(weddingInformation.getString("modifyFormDeadline").substring(0,14));
                    pickModifyDeadlineTime.setText(weddingInformation.getString("modifyFormDeadline").substring(15));
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
                    progressDialog.dismiss();
                }
            });
        }


    }

    public void pickDate(View view){
        final Button theButton = (Button)view;
        int mYear, mMonth, mDay;

        String theDateString = theButton.getText().toString();
        Calendar c = Calendar.getInstance();
        if (!theDateString.equals("請選擇日期")){
            SimpleDateFormat form = new SimpleDateFormat("yyyy / MM / dd");
            java.util.Date theDate = null;
            try {
                theDate = form.parse(theDateString);
                c.setTime(theDate);
            } catch (java.text.ParseException e) {
                Log.d("Neal","Parse Date Error");
            }
        }
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        // 跳出日期選擇器
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 完成選擇，顯示日期
                        theButton.setText(year + " / " + String.format("%02d", (monthOfYear + 1)) + " / " + String.format("%02d", dayOfMonth));
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public  void pickTime(View view){
        // 設定初始時間
        final Button theButton = (Button) view;
        int mHour, mMinute;

        String theDateString = theButton.getText().toString();
        Calendar c = Calendar.getInstance();
        if (!theDateString.equals("請選擇時間")){
            SimpleDateFormat form = new SimpleDateFormat("HH:mm");
            java.util.Date theDate = null;
            try {
                theDate = form.parse(theDateString);
                c.setTime(theDate);
            } catch (java.text.ParseException e) {
                Log.d("Neal","Parse Date Error");
            }
        }
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // 跳出時間選擇器
        TimePickerDialog tpd = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // 完成選擇，顯示時間
                        theButton.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                    }
                }, mHour, mMinute, false);
        tpd.show();
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
    EditText weddingName, weddingPassword, groomName, brideName, engagePlace, engageAddress, engagePlaceIntroduce, marryPlace, marryAddress, marryPlaceIntroduce;
    String weddingInformationObjectId;
    ToggleButton sameDayToggleButton, differentDayToggleButton;
    LinearLayout marryDateLinearLayout, marryPlaceLinearLayout, marryAddressLinearLayout, marryWebsiteLinearLayout;
    TextView engageDateTextView, engagePlaceTextView, engageAddressTextView, engageWebsiteTextView;
    Button pickEngageDate, pickEngageTime, pickMarryDate, pickMarryTime, pickModifyDeadlineDate, pickModifyDeadlineTime;
    Toast errorMessageToast;
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
                if (!sameDayToggleButton.isChecked() && !differentDayToggleButton.isChecked()){
                    showWarning("請選擇婚宴形式。", Toast.LENGTH_LONG);
                    return true;
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
                    if (pickEngageDate.getText().equals("日期")) {
                        showWarning("請選擇婚宴日期。", Toast.LENGTH_LONG);
                        return true;
                    }
                    else if (pickEngageTime.getText().equals("時間")) {
                        showWarning("請選擇婚宴時間。", Toast.LENGTH_LONG);
                        return true;
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
                    if (pickEngageDate.getText().equals("日期")) {
                        showWarning("請選擇訂婚日期。", Toast.LENGTH_LONG);
                        return true;
                    }
                    else if (pickEngageTime.getText().equals("時間")) {
                        showWarning("請選擇訂婚時間。", Toast.LENGTH_LONG);
                        return true;
                    }
                    if (TextUtils.isEmpty(engagePlace.getText().toString())) {
                        engagePlace.setError("請輸入訂婚餐廳名稱。");
                        contentError = true;
                    }
                    if (TextUtils.isEmpty(engageAddress.getText().toString())) {
                        engageAddress.setError("請輸入訂婚餐廳地址。");
                        contentError = true;
                    }
                    if (pickMarryDate.getText().equals("日期")) {
                        showWarning("請選擇結婚日期。", Toast.LENGTH_LONG);
                        return true;
                    }
                    else if (pickMarryTime.getText().equals("時間")) {
                        showWarning("請選擇結婚時間。", Toast.LENGTH_LONG);
                        return true;
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
                if (pickModifyDeadlineDate.getText().equals("日期")) {
                    showWarning("請選擇\"填寫出席意願期限\"日期。", Toast.LENGTH_LONG);
                    return true;
                }
                else if (pickModifyDeadlineTime.getText().equals("時間")) {
                    showWarning("請選擇\"填寫出席意願期限\"時間。", Toast.LENGTH_LONG);
                    return true;
                }

                if (!contentError) {
                    progressDialog.show();
                    if (!isEditWedding) {
                        Log.d(getClass().getSimpleName(), "Done create wedding");
                        ParseQuery checkWeddingExist = ParseQuery.getQuery("Information");
                        checkWeddingExist.whereEqualTo("weddingAccount", weddingName.getText().toString());
                        checkWeddingExist.whereEqualTo("weddingPassword", weddingPassword.getText().toString());
                        checkWeddingExist.countInBackground(new CountCallback() {
                            @Override
                            public void done(int i, ParseException e) {
                                progressDialog.dismiss();
                                if (e == null) {
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
                                        weddingInformation.put("engageDate", pickEngageDate.getText().toString()+" "+pickEngageTime.getText().toString());
                                        weddingInformation.put("engagePlace", engagePlace.getText().toString());
                                        weddingInformation.put("engageAddress", engageAddress.getText().toString());
                                        weddingInformation.put("engagePlaceIntroduce", engagePlaceIntroduce.getText().toString());
                                        weddingInformation.put("marryDate", pickMarryDate.getText().toString()+" "+pickMarryTime.getText().toString());
                                        weddingInformation.put("marryPlace", marryPlace.getText().toString());
                                        weddingInformation.put("marryAddress", marryAddress.getText().toString());
                                        weddingInformation.put("marryPlaceIntroduce", marryPlaceIntroduce.getText().toString());
                                        weddingInformation.put("managerAccount", ParseUser.getCurrentUser().getUsername());
                                        weddingInformation.put("modifyFormDeadline", pickModifyDeadlineDate.getText().toString()+" "+pickModifyDeadlineTime.getText().toString());
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
                        currentWeddingInformation.put("weddingAccount", weddingName.getText().toString());
                        currentWeddingInformation.put("weddingPassword", weddingPassword.getText().toString());
                        currentWeddingInformation.put("groomName", groomName.getText().toString());
                        currentWeddingInformation.put("brideName", brideName.getText().toString());
                        currentWeddingInformation.put("engageDate", pickEngageDate.getText().toString()+" "+pickEngageTime.getText().toString());
                        currentWeddingInformation.put("engagePlace", engagePlace.getText().toString());
                        currentWeddingInformation.put("engageAddress", engageAddress.getText().toString());
                        currentWeddingInformation.put("engagePlaceIntroduce", engagePlaceIntroduce.getText().toString());
                        currentWeddingInformation.put("marryDate", pickMarryDate.getText().toString()+" "+pickMarryTime.getText().toString());
                        currentWeddingInformation.put("marryPlace", marryPlace.getText().toString());
                        currentWeddingInformation.put("marryAddress", marryAddress.getText().toString());
                        currentWeddingInformation.put("marryPlaceIntroduce", marryPlaceIntroduce.getText().toString());
                        currentWeddingInformation.put("managerAccount", ParseUser.getCurrentUser().getUsername());
                        currentWeddingInformation.put("modifyFormDeadline", pickModifyDeadlineDate.getText().toString()+" "+pickModifyDeadlineTime.getText().toString());
                        if (sameDayToggleButton.isChecked() && !differentDayToggleButton.isChecked()) {
                            currentWeddingInformation.put("onlyOneSession", true);
                        } else {
                            currentWeddingInformation.put("onlyOneSession", false);
                        }
                        currentWeddingInformation.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(context)
                                        .setTitle("訊息")
                                        .setMessage("修改完成！")
                                        .setPositiveButton("好！", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        });
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //顯示訊息的toast。
    private void showWarning(final String text, final int duration) {
        if (errorMessageToast == null) {
            //如果還沒有用過makeText方法，才使用
            errorMessageToast = android.widget.Toast.makeText(this, text, duration);
        }
        else {
            errorMessageToast.setText(text);
            errorMessageToast.setDuration(duration);
        }
        errorMessageToast.show();
    }
}
