package com.wedding.weddinghelper.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wedding.weddinghelper.R;

public class JoinActivity extends AppCompatActivity
    implements View.OnClickListener {

    final public String NAME = "test";
    final public String PASSWORD = "123";

    private EditText mNameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // initiate action bar
        Toolbar actionBar = (Toolbar) findViewById(R.id.join_wedding_action_bar);
        setSupportActionBar(actionBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_white);
            getSupportActionBar().setTitle(getString(R.string.join_wedding));
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }

        // set close icon on click listener
        if (actionBar != null) {
            actionBar.setNavigationOnClickListener(this);
        }

        mNameView = (EditText) findViewById(R.id.join_wedding_name);
        mPasswordView = (EditText) findViewById(R.id.join_wedding_password);

        Button mSignInButton = (Button) findViewById(R.id.join_wedding_sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //testLogin();
                attemptLogin();
            }
        });
    }

    private void testLogin(){
        Intent showIntent;
        showIntent = new Intent(this, JoinMainActivity.class);
        startActivity(showIntent);
    }

    private void attemptLogin() {

        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean emptyName = false;
        boolean emptyPassword = false;
        final View focusView = null;

        //先判斷組婚宴名稱及通關密語是否為空，若任一個為空，則在文字框旁顯示error。
        // Check for a valid userName.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            emptyName = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            emptyPassword = true;
        }

        //若mNameView為空，則focus在mNameView。若mNameView不為空，但mPasswordView為空，則focus在mPasswordView。
        if (emptyName) {
            mNameView.requestFocus();
        }
        else if (emptyPassword){
            mPasswordView.requestFocus();
        }

        //若皆不為空，才嘗試以此組婚宴名稱及通關密語搜尋。
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            ParseQuery query = ParseQuery.getQuery("Information");
            query.whereEqualTo("weddingAccount", name);
            query.whereEqualTo("weddingPassword", password);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject information, ParseException e) {
                    Log.d("Neal", "Exception = " + e);
                    //有其他錯誤發生，如連線異常、伺服器掛掉等。
                    if (e != null) {
                        mNameView.setError(getString(R.string.error_not_foune));
                        mNameView.requestFocus();
                        if (e.getCode() == 101) {
                            //目前只能判斷是否有婚宴使用此組婚宴名稱及通關密語，因此較適合使用提示視窗告知使用者此組合是錯誤的。

                        }
                        else {
                            //其他錯誤。
                            Log.d("Neal", "Another Exception = " + e + "   " +e.getCode());
                        }
                    }
                    //有搜尋到婚宴資訊，則進入至下一個畫面且夾帶婚宴資訊。
                    else if (information != null) {
                        Log.d("Neal", "WeddingInformation = " + information.get("groomName"));
                        //Intent showMainIntent = new Intent(JoinActivity.this, JoinMainActivity.class);
                        //showMainIntent.putExtra(JoinMainActivity.PAGE_TYPE_KEY, JoinMainActivity.PageType.INFO.name());
                        //startActivity(showMainIntent);
                        testLogin();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
