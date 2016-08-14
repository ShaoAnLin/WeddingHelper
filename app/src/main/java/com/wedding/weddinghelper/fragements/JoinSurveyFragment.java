package com.wedding.weddinghelper.fragements;

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.wedding.weddinghelper.R;

public class JoinSurveyFragment extends Fragment {

    public static JoinSurveyFragment newInstance() {
        Log.d("Survey", "New Instance");
        JoinSurveyFragment fragment = new JoinSurveyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    EditText name, phone, addressRegion, detailAddress, message;
    TextView peopleNumber, vegetableNumber, meatNumber;
    Spinner relationSpinner;
    Button attendPeopleAddButton, attendPeopleMinusButton, meatAddButton, meatMinusButton, vegetableAddButton, vegetableMinusButton, surveySaveButton;
    ToggleButton attendMarrySession, attendEngageSession;
    Switch attendWillingSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_join_survey, container, false);

        name = (EditText)view.findViewById(R.id.survey_name_edit_text);
        phone = (EditText)view.findViewById(R.id.survey_phone_edit_text);
        addressRegion = (EditText)view.findViewById(R.id.survey_address_city_edit_text);
        detailAddress = (EditText) view.findViewById(R.id.survey_address_detail_edit_text);
        message = (EditText)view.findViewById(R.id.survey_message_edit_text);
        peopleNumber = (TextView)view.findViewById(R.id.attend_people_count_text);
        vegetableNumber = (TextView)view.findViewById(R.id.vegetable_count_text);
        meatNumber = (TextView)view.findViewById(R.id.meat_count_text);
        relationSpinner = (Spinner)view.findViewById(R.id.relation_spinner);


        attendMarrySession = (ToggleButton)view.findViewById(R.id.survey_marry_toggle_button);
        attendEngageSession = (ToggleButton)view.findViewById(R.id.survey_engage_toggle_button);
        attendWillingSwitch = (Switch)view.findViewById(R.id.survey_attend_switch);
        attendWillingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    attendWillingSwitch.setText(getString(R.string.attend));
                }
                else {
                    attendWillingSwitch.setText(getString(R.string.attend_no));
                }
            }
        });


        String[] m={getString(R.string.groom), getString(R.string.bride)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, m);
        relationSpinner.setAdapter(adapter);


        final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        ParseQuery query = ParseQuery.getQuery("AttendantList");
        query.whereEqualTo("InstallationID",currentInstallation.getInstallationId());
        query.whereEqualTo("weddingObjectId", "XA6hDoxtXo");//ToDo:暫時使用
        Log.d("Neall",currentInstallation.getInstallationId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject attendInformation, ParseException e) {
                //若無法搜尋到此裝置(errorCode = 101)，表示第一次填寫資料，所有欄位內容清空。
                if (attendInformation == null && e.getCode() == 101) {
                    //所有欄位內容清空
                }
                else {
                    //將取得的資料放入個欄位內。
                    //ToDo:須針對attendInformation內容為null時，作例外處理
                    name.setText(attendInformation.get("Name").toString());
                    phone.setText(attendInformation.get("Phone").toString());
                    addressRegion.setText(attendInformation.get("AddressRegion").toString());
                    message.setText(attendInformation.get("Notation").toString());
                    peopleNumber.setText(attendInformation.get("PeopleNumber").toString());
                    vegetableNumber.setText(attendInformation.get("VagetableNumber").toString());
                    meatNumber.setText(attendInformation.get("MeatNumber").toString());
                    detailAddress.setText(attendInformation.get("AddressDetail").toString());
                    attendWillingSwitch.setChecked((attendInformation.getInt("AttendingWilling") == 0));
                    attendMarrySession.setChecked((attendInformation.getInt("Session") == 1));
                    attendEngageSession.setChecked(!attendMarrySession.isChecked());
                    relationSpinner.setSelection(attendInformation.getInt("Relation"));
                }
            }
        });

        attendPeopleAddButton = (Button) view.findViewById(R.id.attend_people_add_button);
        attendPeopleAddButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                addPeopleNumber();
            }
        });
        attendPeopleMinusButton = (Button) view.findViewById(R.id.attend_people_minus_button);
        attendPeopleMinusButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                minusPeopleNumber();
            }
        });

        meatAddButton = (Button)view.findViewById(R.id.meat_add_button);
        meatAddButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                addMeatNumber();
            }
        });
        meatMinusButton = (Button)view.findViewById(R.id.meat_minus_button);
        meatMinusButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                minusMeatNumber();
            }
        });

        vegetableAddButton = (Button)view.findViewById(R.id.vegetable_add_button);
        vegetableAddButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                addVegetableNumber();
            }
        });
        vegetableMinusButton = (Button)view.findViewById(R.id.vegetable_minus_button);
        vegetableMinusButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                minusVegetableNumber();
            }
        });





        attendMarrySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!attendMarrySession.isChecked()){
                    attendMarrySession.setChecked(true);
                }
                attendEngageSession.setChecked(false);
            }
        });
        attendEngageSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!attendEngageSession.isChecked()){
                    attendEngageSession.setChecked(true);
                }
                attendMarrySession.setChecked(false);
            }
        });

        surveySaveButton = (Button)view.findViewById(R.id.survey_save_button);
        surveySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        return(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    //增加參加人數的按鈕action
    public void addPeopleNumber(){
        int currentPeopleNumber = Integer.parseInt(peopleNumber.getText().toString());
        peopleNumber.setText(Integer.toString(currentPeopleNumber+1));
    }
    //減少參加人數的按鈕action
    public void minusPeopleNumber(){
        int currentPeopleNumber = Integer.parseInt(peopleNumber.getText().toString());
        if (currentPeopleNumber == 0){
            return;
        }
        else {
            currentPeopleNumber--;
            peopleNumber.setText(Integer.toString(currentPeopleNumber));
        }
        int currentMeatNumber = Integer.parseInt(meatNumber.getText().toString());
        int currentVegetableNumber = Integer.parseInt(vegetableNumber.getText().toString());
        if (currentMeatNumber+currentVegetableNumber>currentPeopleNumber){
            meatNumber.setText("0");
            vegetableNumber.setText("0");
        }
    }
    //增加葷食人數的按鈕action
    public void addMeatNumber(){
        int currentMeatNumber = Integer.parseInt(meatNumber.getText().toString());
        int currentVegetableNumber = Integer.parseInt(vegetableNumber.getText().toString());
        int currentPeopleNumber = Integer.parseInt(peopleNumber.getText().toString());
        if (currentMeatNumber+currentVegetableNumber>=currentPeopleNumber){
            return;
        }
        else {
            meatNumber.setText(Integer.toString(currentMeatNumber + 1));
        }
    }
    //減少葷食人數的按鈕action
    public void minusMeatNumber(){
        int currentMeatNumber = Integer.parseInt(meatNumber.getText().toString());
        if (currentMeatNumber == 0){
            return;
        }
        else {
            meatNumber.setText(Integer.toString(currentMeatNumber - 1));
        }
    }
    //增加素食人數的按鈕action
    public void addVegetableNumber(){
        int currentMeatNumber = Integer.parseInt(meatNumber.getText().toString());
        int currentVegetableNumber = Integer.parseInt(vegetableNumber.getText().toString());
        int currentPeopleNumber = Integer.parseInt(peopleNumber.getText().toString());
        if (currentMeatNumber+currentVegetableNumber>=currentPeopleNumber){
            return;
        }
        vegetableNumber.setText(Integer.toString(currentVegetableNumber+1));
    }
    //減少素食人數的按鈕action
    public void minusVegetableNumber(){
        int currentVegetableNumber = Integer.parseInt(vegetableNumber.getText().toString());
        if (currentVegetableNumber == 0){
            return;
        }
        else {
            vegetableNumber.setText(Integer.toString(currentVegetableNumber - 1));
        }
    }
    //儲存資料的按鈕action
    public void saveData(){
        final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        ParseQuery query = ParseQuery.getQuery("AttendantList");
        query.whereEqualTo("InstallationID",currentInstallation.getInstallationId());
        query.whereEqualTo("weddingObjectId", "XA6hDoxtXo");//ToDo:暫時使用
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject attendInformation, ParseException e) {
                //若無法搜尋到此裝置(errorCode = 101)，表示第一次填寫資料，要加入InstallationID。
                if (attendInformation == null && e.getCode() == 101) {
                    attendInformation = new ParseObject("AttendantList");
                }
                attendInformation.put("InstallationID",currentInstallation.getInstallationId());
                attendInformation.put("weddingObjectId","XA6hDoxtXo");
                attendInformation.put("Name",name.getText().toString());
                attendInformation.put("Phone",phone.getText().toString());
                attendInformation.put("weddingObjectId","XA6hDoxtXo"); //ToDo:暫時使用
                attendInformation.put("AddressRegion",addressRegion.getText().toString());
                attendInformation.put("AddressDetail",detailAddress.getText().toString());
                attendInformation.put("Notation",message.getText().toString());
                attendInformation.put("AttendingWilling", attendWillingSwitch.isChecked()?0:1);
                attendInformation.put("VagetableNumber",Integer.parseInt(vegetableNumber.getText().toString()));
                attendInformation.put("MeatNumber",Integer.parseInt(meatNumber.getText().toString()));
                attendInformation.put("PeopleNumber",Integer.parseInt(peopleNumber.getText().toString()));
                attendInformation.put("Relation",relationSpinner.getSelectedItemPosition());
                Log.d("Neal", "relationSpinner.SelectedItemPosition = " + relationSpinner.getSelectedItemPosition());
                if (attendEngageSession.isChecked() && !attendMarrySession.isChecked()){
                    attendInformation.put("Session",0);
                }
                else if (!attendEngageSession.isChecked() && attendMarrySession.isChecked()){
                    attendInformation.put("Session",1);
                }
                attendInformation.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!=null){
                            Log.d("Neal","saveInbackground.exception = " +e);
                        }
                    }
                });
            }
        });
    }
}
