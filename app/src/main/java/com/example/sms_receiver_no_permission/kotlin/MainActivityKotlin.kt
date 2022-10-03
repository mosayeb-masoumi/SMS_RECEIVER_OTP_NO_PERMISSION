package com.example.sms_receiver_no_permission.kotlin

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sms_receiver_no_permission.databinding.ActivityMainKotlinBinding
import com.example.sms_receiver_no_permission.java.SmsBroadcastReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Pattern


class MainActivityKotlin : AppCompatActivity() {

    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: SmsBroadcastReceiver? = null
    lateinit var binding: ActivityMainKotlinBinding



//    https://www.youtube.com/watch?v=bUqQ1pRmQnI


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSmartUserConsent()

    }

    private fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent) {
                    startActivityForResult(intent, REQ_USER_CONSENT)
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {

//            if((requestCode == RESULT_OK) && (data !=null)){
            if (data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpFromMessage(message!!)
            }
        }
    }

    private fun getOtpFromMessage(message: String) {
        val otpPattern = Pattern.compile("(|^)\\d{6}") // 6 is the length of sms
        val matcher = otpPattern.matcher(message)
        if (matcher.find()) {
            binding.edtOtp.setText(matcher.group(0))
        }
    }


    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }


//    override fun onResume() {
//        super.onResume();
//        registerBroadcastReceiver();
//    }

//    override fun onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(smsBroadcastReceiver);
//    }

}