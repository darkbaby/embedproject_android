package com.mobiletagreader.boonnarit.mobiletagreader;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static java.lang.Thread.sleep;

/**
 * Created by stdisd01 on 25/6/2558.
 */
public class informationActivity extends ActionBarActivity{
    String username;
    String password;
    ListView listView;
    informationAdapter adapter;

    final ArrayList<String> orderid = new ArrayList<String>();
    final ArrayList<String> datetime = new ArrayList<String>();
    final ArrayList<String> status = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        username = intent.getStringExtra("userName");
        password = intent.getStringExtra("passWord");
    }

    @Override
    protected void onResume() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formBody = new FormEncodingBuilder()
                        .add("username", username)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url("http://128.199.173.160:8080/api/check_order").post(formBody).build();
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
                try {
                    JSONArray Jarray = new JSONArray(string);
                    for (int i = 0; i < Jarray.length();i++ ){

                        JSONObject object = Jarray.getJSONObject(i);
                        orderid.add(object.getInt("order")+"");
                        if (object.has("datetime")){
                            datetime.add(object.getString("datetime"));
                        }
                        else {
                            datetime.add(" ");
                        }
                        status.add(object.getString("status"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

        adapter = new informationAdapter(getApplicationContext(), orderid, datetime, status);
        listView = (ListView)findViewById(R.id.listViewInformation);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(informationActivity.this, orderInformation.class);
                intent.putExtra("orderid", orderid.get(position));
                startActivity(intent);}
        });
        super.onResume();
    }
}