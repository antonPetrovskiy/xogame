package com.game.xogame.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.game.xogame.views.authentication.ConfirmPhoneActivity;

public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            StringBuilder sms = new StringBuilder();
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        sms.append(msgBody);

                    }

                    Toast toast = Toast.makeText(context,
                            sms, Toast.LENGTH_SHORT);
                    toast.show();
                    parseSMS(sms, context);
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }

    public void parseSMS(StringBuilder s, Context c){
        String code;
        code = s.substring(0,6);
        Toast toast = Toast.makeText(c,
                code, Toast.LENGTH_SHORT);
        toast.show();
        ConfirmPhoneActivity.codeText.setText(code);
    }
}