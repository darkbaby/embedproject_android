package com.mobiletagreader.boonnarit.mobiletagreader;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Created by boonnarit on 28/12/2558.
 */
public class sending extends AppCompatActivity {
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        Intent intent = getIntent();
        status = intent.getStringExtra("status");

        if (!status.equals("fail")) {
            TextView textstatus = (TextView) findViewById(R.id.textStatus);
            textstatus.setText("Sented");
            ImageView status = (ImageView) findViewById(R.id.imageStatus);
            status.setImageResource(R.drawable.status_active);
        }

    }
}
