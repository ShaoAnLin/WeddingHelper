package com.wedding.weddinghelper.fragements;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.wedding.weddinghelper.R;
import com.wedding.weddinghelper.activities.JoinMainActivity;
import com.wedding.weddinghelper.activities.OwnMainActivity;
import android.app.ProgressDialog;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
public class JoinSurveyFragment extends Fragment {

    public static JoinSurveyFragment newInstance() {
        Log.d("Survey", "New Instance");
        JoinSurveyFragment fragment = new JoinSurveyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public String weddingInfoObjectId;
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        JoinMainActivity mMainActivity = (JoinMainActivity) activity;
        weddingInfoObjectId = mMainActivity.getWeddingInfoObjectId();
        Log.d("Neal", weddingInfoObjectId);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_edit){
            enableEditSurvey(attendWillingSwitch.isChecked());
            return true;
        }
        else if(id == R.id.action_done){
            saveData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    boolean surveyEditable = false;
    public void disableEditSurvey(){
        name.setEnabled(false);
        phone.setEnabled(false);
        relationSpinner.setEnabled(false);
        attendWillingSwitch.setEnabled(false);
        regionSpinner.setEnabled(false);
        citySpinner.setEnabled(false);
        detailAddress.setEnabled(false);
        attendPeopleAddButton.setEnabled(false);
        attendPeopleMinusButton.setEnabled(false);
        attendEngageSession.setEnabled(false);
        attendMarrySession.setEnabled(false);
        vegetableAddButton.setEnabled(false);
        vegetableMinusButton.setEnabled(false);
        meatAddButton.setEnabled(false);
        meatMinusButton.setEnabled(false);
        message.setEnabled(false);
        surveyEditable = false;
    }
    public void enableEditSurvey(boolean attend){
        name.setEnabled(true);
        phone.setEnabled(true);
        relationSpinner.setEnabled(true);
        attendWillingSwitch.setEnabled(true);
        message.setEnabled(true);
        if (attend){
            regionSpinner.setEnabled(true);
            citySpinner.setEnabled(true);
            detailAddress.setEnabled(true);
            attendPeopleAddButton.setEnabled(true);
            attendPeopleMinusButton.setEnabled(true);
            attendEngageSession.setEnabled(true);
            attendMarrySession.setEnabled(true);
            vegetableAddButton.setEnabled(true);
            vegetableMinusButton.setEnabled(true);
            meatAddButton.setEnabled(true);
            meatMinusButton.setEnabled(true);
        }
        else {
            regionSpinner.setEnabled(false);
            citySpinner.setEnabled(false);
            detailAddress.setEnabled(false);
            attendPeopleAddButton.setEnabled(false);
            attendPeopleMinusButton.setEnabled(false);
            attendEngageSession.setEnabled(false);
            attendMarrySession.setEnabled(false);
            vegetableAddButton.setEnabled(false);
            vegetableMinusButton.setEnabled(false);
            meatAddButton.setEnabled(false);
            meatMinusButton.setEnabled(false);
        }
        surveyEditable = true;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_join_survey_menu, menu);  // Use filter.xml from step 1
        MenuItem theMenuItemEdit = menu.getItem(0);
        theMenuItemEdit.setEnabled(!surveyEditable);
        MenuItem theMenuItemDone = menu.getItem(1);
        theMenuItemDone.setEnabled(!surveyEditable);
    }

    EditText name, phone, detailAddress, message;
    TextView peopleNumber, vegetableNumber, meatNumber;
    Spinner relationSpinner, citySpinner, regionSpinner;
    Button attendPeopleAddButton, attendPeopleMinusButton, meatAddButton, meatMinusButton, vegetableAddButton, vegetableMinusButton;
    ToggleButton attendMarrySession, attendEngageSession;
    Switch attendWillingSwitch;
    Calendar modifyDeadline;
    Toast errorMessageToast;
    LinearLayout weddingSessionLayout;
    String[] city;
    String[][] region;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_join_survey, container, false);
        Log.d("Neal","onCreateView infJoinSurveyFragment");
        name = (EditText)view.findViewById(R.id.survey_name_edit_text);
        phone = (EditText)view.findViewById(R.id.survey_phone_edit_text);
        detailAddress = (EditText) view.findViewById(R.id.survey_address_detail_edit_text);
        message = (EditText)view.findViewById(R.id.survey_message_edit_text);
        peopleNumber = (TextView)view.findViewById(R.id.attend_people_count_text);
        vegetableNumber = (TextView)view.findViewById(R.id.vegetable_count_text);
        meatNumber = (TextView)view.findViewById(R.id.meat_count_text);
        relationSpinner = (Spinner)view.findViewById(R.id.relation_spinner);
        citySpinner = (Spinner)view.findViewById(R.id.city_spinner);
        regionSpinner = (Spinner)view.findViewById(R.id.region_spinner);
        weddingSessionLayout = (LinearLayout)view.findViewById(R.id.wedding_session_layout);
        attendMarrySession = (ToggleButton)view.findViewById(R.id.survey_marry_toggle_button);
        attendEngageSession = (ToggleButton)view.findViewById(R.id.survey_engage_toggle_button);
        attendWillingSwitch = (Switch)view.findViewById(R.id.survey_attend_switch);
        attendWillingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    attendWillingSwitch.setText(getString(R.string.attend));
                    regionSpinner.setEnabled(true);
                    citySpinner.setEnabled(true);
                    detailAddress.setEnabled(true);
                    attendPeopleAddButton.setEnabled(true);
                    attendPeopleMinusButton.setEnabled(true);
                    peopleNumber.setText("1");
                    vegetableAddButton.setEnabled(true);
                    vegetableMinusButton.setEnabled(true);
                    vegetableNumber.setText("0");
                    meatAddButton.setEnabled(true);
                    meatMinusButton.setEnabled(true);
                    meatNumber.setText("0");
                    attendEngageSession.setEnabled(true);
                    attendEngageSession.setChecked(false);
                    attendMarrySession.setEnabled(true);
                    attendMarrySession.setChecked(false);
                }
                else {
                    attendWillingSwitch.setText(getString(R.string.attend_no));
                    regionSpinner.setEnabled(false);
                    citySpinner.setEnabled(false);
                    detailAddress.setEnabled(false);
                    detailAddress.setText("");
                    attendPeopleAddButton.setEnabled(false);
                    attendPeopleMinusButton.setEnabled(false);
                    peopleNumber.setText("0");
                    vegetableAddButton.setEnabled(false);
                    vegetableMinusButton.setEnabled(false);
                    vegetableNumber.setText("0");
                    meatAddButton.setEnabled(false);
                    meatMinusButton.setEnabled(false);
                    meatNumber.setText("0");
                    attendEngageSession.setEnabled(false);
                    attendEngageSession.setChecked(false);
                    attendMarrySession.setEnabled(false);
                    attendMarrySession.setChecked(false);
                }
            }
        });


        String[] m={getString(R.string.groom), getString(R.string.bride)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, m);
        relationSpinner.setAdapter(adapter);
        selectedRegion = 0;
        city = new String[]{"縣市","臺北市","基隆市","新北市","新竹市", "新竹縣", "桃園市", "苗栗縣", "臺中市", "彰化縣", "南投縣", "雲林縣", "嘉義市", "嘉義縣", "臺南市", "高雄市", "屏東縣", "宜蘭縣", "花蓮縣", "臺東縣", "澎湖縣", "金門縣", "連江縣"};
        region = new String[][]{
                {"區域"},
                {"中正區","萬華區","大同區","中山區","松山區","大安區","信義區","內湖區","南港區","士林區","北投區","文山區"},//台北市
                {"仁愛區","中正區","信義區","中山區","安樂區","七堵區","暖暖區"},//基隆市
                {"板橋區","中和區","新莊區","三重區","新店區","土城區","永和區","蘆洲區","汐止區","樹林區","淡水區","三峽區","林口區","鶯歌區","五股區","泰山區", "瑞芳區","八里區","深坑區","三芝區","萬里區","金山區","貢寮區","石門區","雙溪區","石碇區","坪林區","烏來區","平溪區"}, //新北市
                {"北區","東區", "香山區"}, //新竹市
                {"竹北市","竹東鎮","新埔鎮","關西鎮","湖口鄉","新豐鄉","峨眉鄉","寶山鄉","北埔鄉","芎林鄉","橫山鄉","尖石鄉","五峰鄉"}, //新竹縣
                {"桃園區","龜山區","八德區","大溪區","蘆竹區","大園區", "中壢區","楊梅區","平鎮區","龍潭區","觀音區","新屋區","復興區"},//桃園市
                {"苗栗市","頭份市","竹南鎮","卓蘭鎮","後龍鎮","通霄鎮","苑裡鎮", "造橋鄉","西湖鄉","頭屋鄉","公館鄉","銅鑼鄉","三義鄉","大湖鄉","獅潭鄉","三灣鄉","南庄鄉", "泰安鄉"}, //苗栗縣
                {"北屯區","西屯區","大里區","太平區","豐原區","南屯區","北區","南區","西區","潭子區","大雅區","沙鹿區","清水區","龍井區","東區","大甲區","烏日區","神岡區","霧峰區","梧棲區","大肚區","后里區","東勢區 ","外埔區","新社區","大安區","中區","石岡區","和平區"},//台中市
                {"彰化市","員林市"," 和美鎮","鹿港鎮","溪湖鎮","二林鎮","田中鎮","北斗鎮","花壇鄉","芬園鄉","大村鄉","永靖鄉","伸港鄉","線西鄉","福興鄉","秀水鄉","埔心鄉","埔鹽鄉","大城鄉","芳苑鄉","竹塘鄉","社頭鄉","二水鄉","田尾鄉","埤頭鄉","溪州鄉"},//彰化縣
                {"南投市","埔里鎮","草屯鎮","竹山鎮","集集鎮","名間鄉","鹿谷鄉","中寮鄉","魚池鄉","國姓鄉","水里鄉","仁愛鄉","信義鄉"},//南投縣
                {"斗六市","斗南鎮","西螺鎮","虎尾鎮","土庫鎮","北港鎮","莿桐鄉","林內鄉","古坑鄉","大埤鄉","崙背鄉","二崙鄉","麥寮鄉","臺西鄉","東勢鄉","褒忠鄉","四湖鄉","口湖鄉","水林鄉","元長鄉"},//雲林縣
                {"東區","西區"},//嘉義市
                {"太保市","朴子市","布袋鎮","大林鎮","民雄鄉","溪口鄉","新港鄉","六腳鄉","東石鄉","義竹鄉","鹿草鄉","水上鄉","中埔鄉","竹崎鄉","梅山鄉","番路鄉","大埔鄉","阿里山鄉"},//嘉義縣
                {"中西區","東區","南區","北區","安平區","安南區","永康區","歸仁區","新化區","左鎮區","玉井區","楠西區","南化區","仁德區","關廟區","龍崎區","官田區","麻豆區","佳里區","西港區","七股區","將軍區","學甲區","北門區","新營區","後壁區","白河區","東山區","六甲區","下營區","柳營區","鹽水區","善化區","大內區","山上區","新市區","安定區"},//台南市
                {"楠梓區","左營區","鼓山區","三民區","鹽埕區","前金區","新興區","苓雅區","前鎮區","旗津區","小港區","鳳山區","大寮區","鳥松區","林園區","仁武區","大樹區","大社區","岡山區","路竹區","橋頭區","梓官區","彌陀區","永安區","燕巢區","田寮區","阿蓮區","茄萣區","湖內區","旗山區","美濃區","內門區","杉林區","甲仙區","六龜區","茂林區","桃源區","那瑪夏區"},//高雄市
                {"屏東市","潮州鎮","東港鎮","恆春鎮","萬丹鄉","崁頂鄉","新園鄉","林邊鄉","南州鄉","琉球鄉","枋寮鄉","枋山鄉","車城鄉","滿州鄉","高樹鄉","九如鄉","鹽埔鄉","里港鄉","內埔鄉","竹田鄉","長治鄉","麟洛鄉","萬巒鄉","新埤鄉","佳冬鄉","霧台鄉","瑪家鄉","泰武鄉","來義鄉","春日鄉","獅子鄉","牡丹鄉","三地門鄉"},//屏東縣
                {"宜蘭市","頭城鎮","羅東鎮", "蘇澳鎮","礁溪鄉","壯圍鄉","員山鄉","冬山鄉","五結鄉","三星鄉","大同鄉","南澳鄉"},//宜蘭縣
                {"花蓮市","鳳林鎮","玉里鎮","新城鄉","吉安鄉","壽豐鄉","光復鄉","豐濱鄉","瑞穗鄉","富里鄉","秀林鄉","萬榮鄉","卓溪鄉"},//花蓮縣
                {"臺東市","成功鎮","關山鎮","長濱鄉","池上鄉","東河鄉","鹿野鄉","卑南鄉","大武鄉","綠島鄉","太麻里鄉","海端鄉","延平鄉","金峰鄉","達仁鄉","蘭嶼鄉"},//台東縣
                {"馬公市","湖西鄉","白沙鄉","西嶼鄉","望安鄉","七美鄉"},//澎湖縣
                {"金城鎮","金湖鎮","金沙鎮","金寧鄉","烈嶼鄉","烏坵鄉"},//金門縣
                {"南竿鄉","北竿鄉","莒光鄉","東引鄉"}//連江縣
        };

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, city);
        citySpinner.setAdapter(cityAdapter);

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, region[0] );
        regionSpinner.setAdapter(regionAdapter);

        final FragmentActivity theFragmentActivity = this.getActivity();


        citySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //使用者選擇
                selectedRegion = 0;
                return false;
            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = citySpinner.getSelectedItemPosition();
                ArrayAdapter<String> newRegionAdapter = new ArrayAdapter<String>(theFragmentActivity, android.R.layout.simple_spinner_dropdown_item, region[pos] );
                regionSpinner.setAdapter(newRegionAdapter);
                regionSpinner.setSelection(selectedRegion);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        disableEditSurvey();
        return(view);
    }

    int selectedRegion;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Neal","onActivityCreated infJoinSurveyFragment");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("處理中...");
        progressDialog.setTitle(null);
        progressDialog.show();
        final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        ParseQuery query = ParseQuery.getQuery("AttendantList");
        query.whereEqualTo("InstallationID",currentInstallation.getInstallationId());
        query.whereEqualTo("weddingObjectId", weddingInfoObjectId);
        Log.d("Neal",currentInstallation.getInstallationId());
        final FragmentActivity theFragmentActivity = this.getActivity();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject attendInformation, ParseException e) {
                //若無法搜尋到此裝置(errorCode = 101)，表示第一次填寫資料，所有欄位內容清空。
                if (attendInformation == null && e.getCode() == 101) {
                    //開放所有欄位填寫
                    enableEditSurvey(true);
                }
                else {
                    //將取得的資料放入個欄位內。
                    //ToDo:須針對attendInformation內容為null時，作例外處理
                    name.setText(attendInformation.get("Name").toString());
                    phone.setText(attendInformation.get("Phone").toString());

                    String cityAndRegionString = attendInformation.get("AddressRegion").toString();
                    Log.d("Neal","City and RegionString = "+cityAndRegionString);
                    if (cityAndRegionString != null){
                        if (cityAndRegionString.length()!=0) {
                            String cityString = cityAndRegionString.substring(0, 3);
                            String regionString = cityAndRegionString.substring(3, cityAndRegionString.length());
                            int cityIndex = Arrays.asList(city).indexOf(cityString);
                            int regionIndex = Arrays.asList(region[cityIndex]).indexOf(regionString);
                            selectedRegion = regionIndex;
                            citySpinner.setSelection(cityIndex);
                        }
                    }
                    message.setText(attendInformation.get("Notation").toString());
                    peopleNumber.setText(attendInformation.get("PeopleNumber").toString());
                    vegetableNumber.setText(attendInformation.get("VagetableNumber").toString());
                    meatNumber.setText(attendInformation.get("MeatNumber").toString());
                    detailAddress.setText(attendInformation.get("AddressDetail").toString());
                    attendWillingSwitch.setChecked((attendInformation.getInt("AttendingWilling") == 0));
                    attendMarrySession.setChecked((attendInformation.getInt("Session") == 1));
                    attendEngageSession.setChecked((attendInformation.getInt("Session") == 0));
                    relationSpinner.setSelection(attendInformation.getInt("Relation"));
                }
                checkDeadline();
            }
        });

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
    ProgressDialog progressDialog;
    //儲存資料的按鈕action
    public void saveData(){
        boolean contentError = false;
        if (name.getText().length() == 0){
            name.setError("請輸入姓名。");
            name.requestFocus();
            contentError = true;
        }
        if (phone.getText().length() == 0){
            phone.setError("請輸入聯絡電話。");
            phone.requestFocus();
            contentError = true;
        }
        if (attendWillingSwitch.isChecked()){
            if (citySpinner.getSelectedItemPosition() == 0) {
                showWarning("請選擇地址所在縣市。", Toast.LENGTH_LONG);
                return;
            }
            if (regionSpinner.getSelectedItemPosition() == 0) {
                showWarning("請選擇地址所在區域。", Toast.LENGTH_LONG);
                return;
            }
            if (detailAddress.getText().length() == 0){
                detailAddress.setError("請輸入喜帖的寄送地址。");
                detailAddress.requestFocus();
                contentError = true;
            }
            if (peopleNumber.getText().toString().equals("0")){
                showWarning("請輸入參加人次。", Toast.LENGTH_LONG);
                return;
            }
            if (!attendEngageSession.isChecked() && !attendMarrySession.isChecked()){
                showWarning("請選擇參加場次。", Toast.LENGTH_LONG);
                return;
            }
            if ( Integer.parseInt(vegetableNumber.getText().toString())+Integer.parseInt(meatNumber.getText().toString()) != Integer.parseInt(peopleNumber.getText().toString())){
                showWarning("請確認個別飲食習慣的人數。", Toast.LENGTH_LONG);
                return;
            }
        }
        if (!contentError) {
            progressDialog.show();
            final ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
            ParseQuery query = ParseQuery.getQuery("AttendantList");
            query.whereEqualTo("InstallationID", currentInstallation.getInstallationId());
            query.whereEqualTo("weddingObjectId", weddingInfoObjectId);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject attendInformation, ParseException e) {
                    //若無法搜尋到此裝置(errorCode = 101)，表示第一次填寫資料，要加入InstallationID。
                    if (attendInformation == null && e.getCode() == 101) {
                        attendInformation = new ParseObject("AttendantList");
                    }
                    attendInformation.put("InstallationID", currentInstallation.getInstallationId());
                    attendInformation.put("weddingObjectId", "XA6hDoxtXo");
                    attendInformation.put("Name", name.getText().toString());
                    attendInformation.put("Phone", phone.getText().toString());
                    attendInformation.put("weddingObjectId", weddingInfoObjectId);
                    attendInformation.put("AddressRegion", citySpinner.getSelectedItem().toString()+regionSpinner.getSelectedItem().toString());
                    Log.d("Neal","AddressRegion = " + citySpinner.getSelectedItem().toString()+regionSpinner.getSelectedItem().toString());
                    attendInformation.put("AddressDetail", detailAddress.getText().toString());
                    attendInformation.put("Notation", message.getText().toString());
                    attendInformation.put("AttendingWilling", attendWillingSwitch.isChecked() ? 0 : 1);
                    attendInformation.put("VagetableNumber", Integer.parseInt(vegetableNumber.getText().toString()));
                    attendInformation.put("MeatNumber", Integer.parseInt(meatNumber.getText().toString()));
                    attendInformation.put("PeopleNumber", Integer.parseInt(peopleNumber.getText().toString()));
                    attendInformation.put("Relation", relationSpinner.getSelectedItemPosition());
                    if (attendWillingSwitch.isChecked()) {
                        if (attendEngageSession.isChecked() && !attendMarrySession.isChecked()) {
                            attendInformation.put("Session", 0);
                        } else if (!attendEngageSession.isChecked() && attendMarrySession.isChecked()) {
                            attendInformation.put("Session", 1);
                        }
                    } else {
                        attendInformation.put("Session", -1);
                    }
                    attendInformation.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.d("Neal", "saveInbackground.exception = " + e);
                            } else {
                                disableEditSurvey();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            });
        }
    }
    //檢查目前是否可新增或修改資料
    public  void  checkDeadline(){
        ParseQuery query = ParseQuery.getQuery("Information");
        query.getInBackground(weddingInfoObjectId, new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject weddingInformation, ParseException e) {
                //若無法搜尋到此婚宴資訊
                if (e!=null) {
                    //ToDo:錯誤處理
                    Log.d("Neal","query information exception = "+e);
                }
                else {
                    SimpleDateFormat theDateFormat = new SimpleDateFormat("yyyy / MM / dd HH:mm");
                    try {
                        modifyDeadline  = Calendar.getInstance();
                        modifyDeadline.setTime(theDateFormat.parse(weddingInformation.getString("modifyFormDeadline")));
                        Log.d("Neal", "the modifyDeadline = " + modifyDeadline);
                        Calendar currentTime = Calendar.getInstance();
                        if (currentTime.after(modifyDeadline)){
                            //不能新增或修改資料。
                            disableEditSurvey();
                            surveyEditable = false;
                        }
                        Log.d("Neal", "weddingInformation.getBoolean(onlyOneSession = " + weddingInformation.getBoolean("onlyOneSession"));
                        if (weddingInformation.getBoolean("onlyOneSession")){
                            weddingSessionLayout.setVisibility(View.GONE);
                        }
                        else {
                            weddingSessionLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (java.text.ParseException e1) {
                        Log.d("Neal","ParseDateException = "+e1);
                    }
                }
                progressDialog.dismiss();
            }

        });

    }
    //顯示訊息的toast。
    private void showWarning(final String text, final int duration) {
        if (errorMessageToast == null) {
            //如果還沒有用過makeText方法，才使用
            errorMessageToast = android.widget.Toast.makeText(getActivity().getApplicationContext(), text, duration);
        }
        else {
            errorMessageToast.setText(text);
            errorMessageToast.setDuration(duration);
        }
        errorMessageToast.show();
    }
}
