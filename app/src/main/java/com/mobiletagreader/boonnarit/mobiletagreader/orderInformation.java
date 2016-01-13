package com.mobiletagreader.boonnarit.mobiletagreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by boonnarit on 25/12/2558.
 */
public class orderInformation extends AppCompatActivity {

    String orderid;
    String tagid;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderinformation);

        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        //Toast.makeText(this,orderid, Toast.LENGTH_LONG).show();
/*
        Button button = (Button) findViewById(R.id.b_send);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(orderInformation.this, sending.class);
                intent.putExtra("orderid", orderid);
                intent.putExtra("tagid", "ee721a4b");
                //ee721a4b
                startActivity(intent);}

        });*/


        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is enabled.", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getTagInfo(intent);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient okHttpClient = new OkHttpClient();
                //Toast.makeText(getApplicationContext(), tagid, Toast.LENGTH_LONG).show();
                RequestBody formBody = new FormEncodingBuilder()
                        .add("order", orderid + "")
                        .add("tag", tagid + "" )
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url("http://128.199.173.160:8080/api/check_send").post(formBody).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                //Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(orderInformation.this, sending.class);
                intent.putExtra("status", string);
                startActivity(intent);

            }
        }.execute();

    }

    private void getTagInfo(Intent intent) {
        Tag tag = intent.getParcelableExtra(mNfcAdapter.EXTRA_TAG);
        String temp = bytesToHexString(tag.getId());
        final String temp2 = temp.substring(2);
        tagid = temp2;
        //Toast.makeText(this, temp2 ,Toast.LENGTH_SHORT).show();

    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onResume() {


        Intent intent = new Intent(this, orderInformation.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

        super.onResume();
    }

    @Override
    protected void onPause() {

        mNfcAdapter.disableForegroundDispatch(this);

        super.onPause();
    }

}
