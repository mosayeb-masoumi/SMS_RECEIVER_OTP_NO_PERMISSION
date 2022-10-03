package com.example.sms_receiver_no_permission.java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;

import com.example.sms_receiver_no_permission.R;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Result;

public class MainActivityJava extends AppCompatActivity {

    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    AppCompatEditText edtOtp;


    //    https://www.youtube.com/watch?v=bUqQ1pRmQnI


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);

        edtOtp = findViewById(R.id.edtOtp);
        startSmartUserConsent();
    }

    private void startSmartUserConsent() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);
    }

    private void registerBroadcastReceiver(){
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
               startActivityForResult(intent , REQ_USER_CONSENT);
            }

            @Override
            public void onFailure() {
            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver , intentFilter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_USER_CONSENT){

//            if((requestCode == RESULT_OK) && (data !=null)){
            if(data != null){
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }

    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");  // 6 is the length of sms
        Matcher matcher = otpPattern.matcher(message);
        if(matcher.find()){
            edtOtp.setText(matcher.group(0));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        registerBroadcastReceiver();
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(smsBroadcastReceiver);
//    }
}