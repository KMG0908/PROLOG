package com.example.prolog.prolog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class TimeActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile1";
    public CheckBox dontShowAgain;
    private DrawerLayout mDrawerLayout;

    LineChart lineChart;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();

    String loginID = NotiActivity.ID_login;

    String[] AttackLevel;
    String[] Problem;
    String[] Country;
    String[] Date;
    String[] Time;
    String[] ClientIP;
    String[] ServerIP;
    String[] Port;
    private final String urlPath = LoginActivity.basic_url + "/php/info.php";

    private int[] timeValue;
    private String[] timeArr;

    private int[] dateValue;
    private String[] dateArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        new SendPostRequest().execute();

        lineChart = (LineChart) findViewById(R.id.time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.dashboard:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent dash = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                NotiActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(dash); // 다음 화면으로 넘어간다
                        finish();
                        break;

                    case R.id.summary:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent summary = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                SummaryActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(summary); // 다음 화면으로 넘어간다
                        finish();
                        break;

                    case R.id.country:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent country = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                CountryActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(country); // 다음 화면으로 넘어간다
                        finish();
                        break;

                    case R.id.ip:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent ip = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                IpActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(ip); // 다음 화면으로 넘어간다
                        finish();
                        break;
                    case R.id.time:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.attack:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent attack = new Intent(
                                getApplicationContext(),
                                AttackActivity.class);
                        startActivity(attack);
                        finish();
                        break;
                    case R.id.table:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent table = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                TableActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(table); // 다음 화면으로 넘어간다
                        finish();
                        break;
                    case R.id.report:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent report = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                ReportActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(report); // 다음 화면으로 넘어간다
                        finish();
                        break;
                    case R.id.help:
                       //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent help = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                HelpActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(help); // 다음 화면으로 넘어간다
                        finish();
                        break;

                }

                return true;
            }
        });


    }

    public void setDataForLineChart() {
        LineDataSet dataset = new LineDataSet(entries, "시간");

        LineData data = new LineData(labels, dataset);
        dataset.setCircleColor(R.color.colorPrimary);//
        dataset.setColor(Color.rgb(173, 116, 96));
        dataset.setValueTextColor(Color.WHITE);
        dataset.setValueTextSize(11f);
        dataset.setDrawCubic(false);
        dataset.setDrawFilled(false);

        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.setDescription(" ");

        lineChart.setData(data);
        lineChart.animateY(2000);

        YAxis Y =lineChart.getAxisLeft();
        Y.setTextColor(Color.WHITE);

        YAxis Y2 = lineChart.getAxisRight();
        Y2.setTextColor(Color.WHITE);

        XAxis X = lineChart.getXAxis();
        X.setTextColor(Color.WHITE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.logout:
                AlertDialog.Builder logout_alert = new AlertDialog.Builder(this);
                logout_alert.setTitle("설정");
                logout_alert.setMessage("로그아웃 하시겠습니까?");
                logout_alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent j = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(j);
                        finish();
                    }
                });
                logout_alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                logout_alert.show();
                break;

            case R.id.settings:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                LayoutInflater adbInflater = LayoutInflater.from(this);
                View eulaLayout = adbInflater.inflate(R.layout.activity_setting, null);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                String skipMessage = settings.getString("skipMessage", "NOT checked");

                dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
                alert.setView(eulaLayout);
                alert.setTitle("알림 설정");
                alert.setMessage("알림을 반복하시겠습니까?");

                alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String checkBoxResult = "NOT checked";

                        if (dontShowAgain.isChecked()) {
                            checkBoxResult = "checked";
                        }

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();

                        editor.putString("skipMessage", checkBoxResult);
                        dontShowAgain.setChecked(settings.getBoolean("check1", false));
                        editor.commit();
                        // Do what you want to do on "OK" action

                        return;
                    }
                });

                dontShowAgain.setChecked(settings.getBoolean("check1", false));
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String checkBoxResult = "NOT checked";

                        if (dontShowAgain.isChecked()) {
                            checkBoxResult = "checked";
                        }

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();

                        editor.putString("skipMessage", checkBoxResult);
                        editor.commit();

                        // Do what you want to do on "CANCEL" action

                        return;
                    }
                });
                alert.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(urlPath); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                Intent intent = getIntent();
                if(intent.hasExtra("loginEmail")){
                    loginID = intent.getExtras().getString("loginEmail");
                }
                String dbName = loginID + "alert";
                postDataParams.put("dbName", dbName);
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
            //dialog.show(AttackActivity.this, "Loading", "데이터를 불러오는 중입니다.");
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
                    String t[] = Time[i].split(":");
                    Time[i] = t[0];
                    ClientIP[i] = temp.get("ClientIP").toString();
                    ServerIP[i] = temp.get("ServerIP").toString();
                    Port[i] = temp.get("Port").toString();
                }

                int timeCount = 1, dateCount = 1;

                for(int i=0; i<Time.length; i++){
                    int c = 0;
                    for(int j=0; j<i; j++){
                        if(Time[i].equals(Time[j])){
                            break;
                        }
                        else c++;

                        if(j==i-1){
                            if(c==j+1){
                                timeCount++;
                            }
                        }
                    }
                }

                for(int i=0; i<Date.length; i++){
                    int c = 0;
                    for(int j=0; j<i; j++){
                        if(Date[i].equals(Date[j])){
                            break;
                        }
                        else c++;

                        if(j==i-1){
                            if(c==j+1){
                                dateCount++;
                            }
                        }
                    }
                }

                timeValue = new int[timeCount];
                timeArr = new String[timeCount];

                dateValue = new int[dateCount];
                dateArr = new String[dateCount];

                dateArr[0] = Date[0];
                int k = 1;
                for(int i=0; i<Date.length; i++){
                    int c = 0;
                    for(int j=0; j<i; j++){
                        if(Date[i].equals(Date[j])){
                            break;
                        }
                        else c++;

                        if(j == i - 1){
                            if(c == j + 1){
                                dateArr[k++] = Date[i];
                            }
                        }
                    }
                }

                for(int i=0; i<Date.length; i++){
                    for(int j=0; j<dateArr.length; j++){
                        if(dateArr[j].equals(Date[i])){
                            dateValue[j]++;
                        }
                    }
                }

                String max = dateArr[0];
                for(int i=0; i<dateArr.length; i++){
                    for(int j=i+1; j<dateArr.length; j++){
                        if(dateValue[i] < dateValue[j]){
                            max = dateArr[j];
                        }
                    }
                }

                timeArr[0] = Time[0];
                k = 1;
                for(int i=0; i<Time.length; i++){
                    int c = 0;
                    for(int j=0; j<i; j++){
                        if(Time[i].equals(Time[j])){
                            break;
                        }
                        else c++;

                        if(j == i - 1){
                            if(c == j + 1){
                                timeArr[k++] = Time[i];
                            }
                        }
                    }
                }

                for(int i=0; i<Time.length; i++){
                    if(!Date[i].equals(max)) continue;
                    for(int j=0; j<timeArr.length; j++){
                        if(timeArr[j].equals(Time[i])){
                            timeValue[j]++;
                        }
                    }
                }


                for(int i=0; i<timeArr.length; i++){
                    for(int j=i+1; j<timeArr.length; j++){
                        if(Integer.parseInt(timeArr[i]) > Integer.parseInt(timeArr[j])){
                            int temp = timeValue[i];
                            timeValue[i] = timeValue[j];
                            timeValue[j] = temp;

                            String temp_s = timeArr[i];
                            timeArr[i] = timeArr[j];
                            timeArr[j] = temp_s;
                        }
                    }
                }
                //dialog.dismiss();
                Log.w("파싱 끝", "☆");
                TextView textView = (TextView) findViewById(R.id.date);
                textView.setText("날짜: " + max);
                for(int i=0; i<timeCount; i++){
                    entries.add(new Entry(timeValue[i], i));
                    labels.add(timeArr[i] + "시");
                }
                setDataForLineChart();

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
