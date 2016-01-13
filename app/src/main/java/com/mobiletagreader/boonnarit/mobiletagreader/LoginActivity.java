package com.mobiletagreader.boonnarit.mobiletagreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    EditText username;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.login_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = (EditText) findViewById(R.id.username);
                pass = (EditText) findViewById(R.id.password);

                final String t1 = username.getText().toString();
                final String t2 = pass.getText().toString();

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("username", t1)
                                .add("pass", t2)
                                .build();
                        Request.Builder builder = new Request.Builder();
                        Request request = builder.url("http://128.199.173.160:8080/api/check_postman").post(formBody).build();
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
                        if(string.equals(0)){

                        }else {
                        Intent intent = new Intent(LoginActivity.this, informationActivity.class);
                        intent.putExtra("userName", username.getText().toString());
                        intent.putExtra("passWord", pass.getText().toString());
                        startActivity(intent);}
                    }
                }.execute();
            }
        });

    }




}

