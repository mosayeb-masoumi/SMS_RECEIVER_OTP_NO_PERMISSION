package com.example.sms_receiver_no_permission.kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver : BroadcastReceiver()  {

    private var smsBroadcastReceiverListener: SmsBroadcastReceiverListener? = null

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action === SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras
            val smsRetrieverStatus = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (smsRetrieverStatus!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val messageIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    smsBroadcastReceiverListener!!.onSuccess(messageIntent)
                }
                CommonStatusCodes.TIMEOUT -> smsBroadcastReceiverListener!!.onFailure()
            }
        }





//        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
//            val extras = intent.extras
//            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
//            when (status!!.statusCode) {
//                CommonStatusCodes.SUCCESS -> {
//                    val sms = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
//                    sms?.let {
//                        // val p = Pattern.compile("[0-9]+") check a pattern with only digit
//                        val p = Pattern.compile("\\d+")
//                        val m = p.matcher(it)
//                        if (m.find()) {
//                            val otp = m.group()
//                            if (otpListener != null) {
//                                otpListener!!.onOTPReceived(otp)
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    interface SmsBroadcastReceiverListener {
        fun onSuccess(intent: Intent?)
        fun onFailure()
    }

}