package org.androidtown.ui.pager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JoinwithPhoneNumber extends Activity {

    String host_number;
    String device_ID;


    private RequestQueue requestQueue;
    private StringRequest request;
    public String urlStr = "http://iblind7.godo.co.kr/coolsms/example_euckr_sendsms_01.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_with_phone_number);

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        host_number = manager.getLine1Number();
        device_ID = manager.getDeviceId();

        if(host_number.startsWith("+82")) {
            host_number=host_number.replace("+82", "0");
        }

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(host_number);

        requestQueue = Volley.newRequestQueue(this);



    }

/*

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Intent intent = new Intent(getApplicationContext(), WaitforSMS.class);
            startActivity(intent);
            //finish();

        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            Toast.makeText(getApplicationContext(),
                    "인터넷에 연결되지 않았습니다.\n인터넷에 연결 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG).show();

        }
    };
*/


    public void onButton1Clicked(View v) {

        // 인터넷 연결 체크
        ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectManager.getActiveNetworkInfo();

        if ( activeNetwork != null)
        {
            if ( activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            {
                // connected to wifi
                Toast.makeText(getApplicationContext(), activeNetwork.getTypeName(), Toast.LENGTH_LONG).show();

                AlertDialog dialog = createDialogBox();
                dialog.show();
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                // connected to mobile
                Toast.makeText(getApplicationContext(), activeNetwork.getTypeName(), Toast.LENGTH_LONG).show();

                AlertDialog dialog = createDialogBox();
                dialog.show();
            }
        }
        else
        {
            // not connected to internet
            Toast.makeText(getApplicationContext(),
                    "인터넷에 연결되지 않았습니다.\n인터넷에 연결후 재시도해주세요.",
                    Toast.LENGTH_LONG).show();
        }


    }



    private AlertDialog createDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(host_number);
        builder.setMessage("이 전화번호로 SMS 인증번호를 발송합니다.");


        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "확인 클릭", Toast.LENGTH_SHORT).show();

                //requestSMSAuth();
                request = new StringRequest(Request.Method.POST,
                        urlStr,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(getApplicationContext(), WaitforSMS.class);
                                startActivity(intent);
                                //finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                /*Toast.makeText(getApplicationContext(),
                                        "인터넷에 연결되지 않았습니다.\n인터넷에 연결 후 다시 시도해주세요.",
                                        Toast.LENGTH_LONG).show(); */
                                Intent intent = new Intent(getApplicationContext(), WaitforSMS.class);
                                startActivity(intent);
                                //finish();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("phoneNo", host_number);

                        //return super.getParams();
                        return hashMap;
                    }
                };
                requestQueue.add(request);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "취소 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();

        return dialog;

    }






}
