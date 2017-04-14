package org.androidtown.ui.pager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by jeny on 2016-09-06.
 */
public class WaitforSMS extends Activity {

    String host_number;
    Button button;

    EditText editText_smsNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_for_sms);

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        host_number = manager.getLine1Number();

        TextView textView_phoneNo = (TextView) findViewById(R.id.phoneNo);
        textView_phoneNo.setText(host_number);

        editText_smsNo = (EditText) findViewById(R.id.sms_no);
        editText_smsNo.addTextChangedListener(textWatcher);

        button = (Button) findViewById(R.id.button2);

    }

    public void onButton1Clicked(View v) {

        Toast.makeText(getApplicationContext(), "aa", Toast.LENGTH_SHORT).show();
    }


    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {

            //Toast.makeText(getApplicationContext(), "afterTextChanged", Toast.LENGTH_LONG).show();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            //Toast.makeText(getApplicationContext(), "beforeTextChanged", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //Toast.makeText(getApplicationContext(), "onTextChanged", Toast.LENGTH_LONG).show();

            if(editText_smsNo.getText().length() == 0)
            {
                Toast.makeText(getApplicationContext(), "SMS Empty", Toast.LENGTH_LONG).show();
                button.setEnabled(false);
                //button.setBackgroundColor(Color.GRAY);
            }
            else
            {
                button.setEnabled(true);
                //button.setBackgroundColor(Color.GREEN);
            }


        }
    };


    // SMS 수신하면 EditText 에 표시해주는 BroadcastReceiver
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            {
                //Toast.makeText(getApplicationContext(), "방송수신자 수신", Toast.LENGTH_LONG).show();

                Bundle bundle = intent.getExtras();
                Object[] objects = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[objects.length];

                int smsCount = objects.length;
                for(int i = 0; i < smsCount; i++)
                {
                    // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        // API 23 이상이면
                        String format = bundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) objects[i], format);
                    }
                    else
                    {
                        messages[i] = SmsMessage.createFromPdu((byte[]) objects[i]);
                    }
                }

                // SMS 메시지 확인
                String contents = messages[0].getMessageBody().toString();

                if (contents.contains("비더레이서"))
                {
                    contents = contents.replaceAll("[^0-9]", "");
                    editText_smsNo.setText(contents);

                    Toast.makeText(getApplicationContext(), "비더레이서 문자 수신", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "기타 문자 수신", Toast.LENGTH_LONG).show();
                }

            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}
