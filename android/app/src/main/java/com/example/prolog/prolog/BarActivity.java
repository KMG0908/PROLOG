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
import android.widget.Button;
import android.widget.CheckBox;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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

public class BarActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PREFS_NAME = "MyPrefsFile1";
    public CheckBox dontShowAgain;
    private DrawerLayout mDrawerLayout;

    String ID = "";
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

    private Button bt_tab1, bt_tab2;

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY = new ArrayList<>();
    ArrayList<String> BarEntryLabels = new ArrayList<String>();
    BarDataSet Bardataset ;
    BarData BARDATA ;

    int risk[] = {0, 0, 0};
    String demage[] = {"예상피해 상", "예상피해 중", "예상피해 하"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        new SendPostRequest().execute();
        chart = (BarChart) findViewById(R.id.bar);

        bt_tab1 = (Button)findViewById(R.id.bt_tab1);
        bt_tab2 = (Button)findViewById(R.id.bt_tab2);

        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);

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
                        Intent time = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                TimeActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(time); // 다음 화면으로 넘어간다
                        finish();
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

    public void setDataForBarChart() {
        double riskRate[] = {0, 0, 0};
        double sum = 0;
        for(int i=0; i<risk.length; i++){
            sum += risk[i];
        }

        for(int i=0; i<risk.length; i++){
            riskRate[i] = risk[i] / sum * 100.0;
        }

        for(int i=0; i<risk.length; i++){
            BARENTRY.add(new BarEntry(risk[i], i));
            BarEntryLabels.add(demage[i]);
        }

        Bardataset = new BarDataSet(BARENTRY, "Projects");
        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        Bardataset.setValueTextColor(Color.WHITE);
        Bardataset.setValueTextSize(20);
        /*Bardataset.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return value + "%";
            }
        });*/

        BARDATA = new BarData(BarEntryLabels, Bardataset);

        /*chart.getAxisLeft().setAxisMaxValue(100f);
        chart.getAxisLeft().setAxisMinValue(0f);
        chart.getAxisRight().setAxisMaxValue(100f);
        chart.getAxisRight().setAxisMinValue(0f);*/
        /*chart.getAxisLeft().setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return "" + ((int)value);
            }
        });
        chart.getAxisRight().setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return "" + ((int)value);
            }
        });*/

        chart.setData(BARDATA);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getAxisRight().setTextColor(Color.WHITE);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.setDescription("");
        /*chart.setDescription("단위: %");
        chart.setDescriptionColor(Color.WHITE);*/

        chart.animateY(3000);

        Legend l = chart.getLegend();
        l.setEnabled(false);
    }

    public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(2f, 0));
        BARENTRY.add(new BarEntry(4f, 1));
        BARENTRY.add(new BarEntry(6f, 2));
        BARENTRY.add(new BarEntry(8f, 3));
        BARENTRY.add(new BarEntry(7f, 4));
        BARENTRY.add(new BarEntry(3f, 5));

    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("January");
        BarEntryLabels.add("February");
        BarEntryLabels.add("March");
        BarEntryLabels.add("April");
        BarEntryLabels.add("May");
        BarEntryLabels.add("June");

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
                    if(Integer.parseInt(AttackLevel[i]) == 6 || Integer.parseInt(AttackLevel[i]) == 5 || Integer.parseInt(AttackLevel[i]) ==4){
                        risk[0]++;
                    }
                    if(Integer.parseInt(AttackLevel[i]) == 3 || Integer.parseInt(AttackLevel[i]) == 2){
                        risk[1]++;
                    }
                    if(Integer.parseInt(AttackLevel[i]) == 1){
                        risk[2]++;
                    }
                    Problem[i] = temp.get("Problem").toString();
                    Country[i] = temp.get("Country").toString();
                    Date[i] = temp.get("Date").toString();
                    Time[i] = temp.get("Time").toString();
                    ClientIP[i] = temp.get("ClientIP").toString();
                    ServerIP[i] = temp.get("ServerIP").toString();
                    Port[i] = temp.get("Port").toString();
                }


                //dialog.dismiss();
                Log.w("파싱 끝", "☆");
                setDataForBarChart();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_tab1 :
                bt_tab1.setBackgroundResource(R.color.click);
                bt_tab2.setBackgroundResource(R.color.push);
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        SummaryActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
                finish();
                overridePendingTransition(0,0);
                break;

            case R.id.bt_tab2 :
                bt_tab2.setBackgroundResource(R.color.click);
                bt_tab1.setBackgroundResource(R.color.push);
                Intent intent2 = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        BarActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent2); // 다음 화면으로 넘어간다
                finish();
                overridePendingTransition(0,0);
                break;
        }
    }
}