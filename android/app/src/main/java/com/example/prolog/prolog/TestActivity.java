package com.example.prolog.prolog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by kmg on 2017-11-25.
 */

public class TestActivity extends AppCompatActivity {
    Button bn;
    TextView Tresult;

    String[] AttackLevel;
    String[] Problem;
    String[] Country;
    String[] Date;
    String[] Time;
    String[] ClientIP;
    String[] ServerIP;
    String[] Port;

    //접속할주소
    private final String urlPath = LoginActivity.basic_url + "/php/info.php";
    private final String TAG = "PHPTEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        bn = (Button)findViewById(R.id.btn);
        Tresult = (TextView)findViewById(R.id._result);

        bn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new SendPostRequest().execute();
            }

        });
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(urlPath); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("dbName", "sdm6067naveralert");
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String str) {
            Toast.makeText(getApplicationContext(), str,
                    Toast.LENGTH_LONG).show();
            Tresult.setText(str);
            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                JSONArray results = jObject.getJSONArray("results");

                AttackLevel = new String[results.length()];
                Problem = new String[results.length()];
                Country = new String[results.length()];
                Date = new String[results.length()];
                Time = new String[results.length()];
                ClientIP = new String[results.length()];
                ServerIP = new String[results.length()];
                Port = new String[results.length()];

                for ( int i = 0; i < results.length(); ++i ) {
                    JSONObject temp = results.getJSONObject(i);
                    AttackLevel[i] = temp.get("AttackLevel").toString();
                    Problem[i] = temp.get("Problem").toString();
                    Country[i] = temp.get("Country").toString();
                    Date[i] = temp.get("Date").toString();
                    Time[i] = temp.get("Time").toString();
                    ClientIP[i] = temp.get("ClientIP").toString();
                    ServerIP[i] = temp.get("ServerIP").toString();
                    Port[i] = temp.get("Port").toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
