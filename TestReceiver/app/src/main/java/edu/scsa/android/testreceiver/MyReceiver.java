package edu.scsa.android.testreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) { //


        Bundle myB = intent.getExtras(); // intent가 내부적으로 bundle 안에 값을 넣는다.
        Object[] objArr = (Object[]) myB.get("pdus"); //pdus -> 데이터를 꺼낼때 사용한다고...
        // 다양한 크기로 담겨서 넘어오겠지만, 간단하게 테스트 할거니까 SmsMessage로 넣고
        SmsMessage[] smsArr = new SmsMessage[objArr.length];//보내고 싶으면 SmsManager

        smsArr[0] = SmsMessage.createFromPdu((byte[]) objArr[0]);

        String telNum = smsArr[0].getOriginatingAddress(); // 건쪽의 연락처
        String msg = smsArr[0].getMessageBody(); // 문자열의 내용
//        SmsManager smsMgr = SmsManager.getDefault();
//        smsMgr.sendTextMessage("010-1234-5678", "010-2341-5455", msg, null, null);

        Toast.makeText(context, "phoneNum : " + telNum + "\n" + "content : " + msg,
                Toast.LENGTH_SHORT).show();

    }
}
