package com.example.prolog.prolog;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.javainetlocator.InetAddressLocator;
import net.sf.javainetlocator.InetAddressLocatorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class ReportActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile1";
    public CheckBox dontShowAgain;
    private DrawerLayout mDrawerLayout;

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

    private int[] AttackyValues = {0, 0, 0, 0, 0, 0};
    private String[] AttackxValues = {"SQL Injection", "Directory Listing", "LFI & RFI", "Scanning", "Web CGI", "Pattern Block"};
    private int[] CountryyValues;
    private String[] CountryxValues;
    private int[] IPyValues;
    private String[] IPxValues;
    private int[] dateValue;
    private String[] dateArr;
    private int[] timeValue;
    private String[] timeArr;

    int checkedLog;
    int detectedLog;
    int da1;
    int da2;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new SendPostRequest().execute();
        setContentView(R.layout.activity_report);

        Set set = new Set();
        checkedLog = set.getCLog();
        Log.w("checkedLog", Integer.toString(checkedLog));
        detectedLog = set.getDLog();
        da1 = set.getDa1();
        da2 = set.getDa2();
        Log.w("danger2", Integer.toString(da2));


       /* Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setDataAndType(Uri.fromFile(new File("C:\\Android examples\\Prolog\\app\\src\\main\\res\\drawable\\PRO.pdf")), "application/pdf");
        startActivity(i);*/

        /*File file = new File("/storage/emulated/0/KaKaoTalkDownload/분석REPORT.pdf");

        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(ReportActivity.this,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }
        }*/



        /*try {
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/


        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("C:\\Users\\kmg\\workspace\\basicbootstrap\\src\\main\\webapp\\resources\\보고서.pdf"));
        startActivity(intent);*/

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
                                getApplicationContext(), // 현재 화면의 제어권자
                                AttackActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(attack); // 다음 화면으로 넘어간다
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
                    if(Integer.parseInt(AttackLevel[i]) == 6){
                        AttackyValues[0]++;
                    }
                    else if(Integer.parseInt(AttackLevel[i]) == 5){
                        AttackyValues[1]++;
                    }
                    else if(Integer.parseInt(AttackLevel[i]) == 4){
                        AttackyValues[2]++;
                    }
                    else if(Integer.parseInt(AttackLevel[i]) == 3){
                        AttackyValues[3]++;
                    }
                    else if(Integer.parseInt(AttackLevel[i]) == 2){
                        AttackyValues[4]++;
                    }
                    else if(Integer.parseInt(AttackLevel[i]) == 1){
                        AttackyValues[5]++;
                    }
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

                int countryCount = 1;

                for(int i=0; i<Country.length; i++){
                    int c = 0;
                    for(int j=0; j<i; j++){
                        if(Country[i].equals(Country[j])){
                            break;
                        }
                        else c++;

                        if(j==i-1){
                            if(c==j+1){
                                countryCount++;
                            }
                        }
                    }
                }

                CountryyValues = new int[countryCount];
                CountryxValues = new String[countryCount];

                CountryxValues[0] = Country[0];
                int k = 1;
                for(int i=0; i<Country.length; i++){
                    int c = 0;
                    for(int j=0; j<i; j++){
                        if(Country[i].equals(Country[j])){
                            break;
                        }
                        else c++;

                        if(j == i - 1){
                            if(c == j + 1){
                                CountryxValues[k++] = Country[i];
                            }
                        }
                    }
                }

                for(int i=0; i<Country.length; i++){
                    for(int j=0; j<CountryxValues.length; j++){
                        if(CountryxValues[j].equals(Country[i])){
                            CountryyValues[j]++;
                        }
                    }
                }


                /*Locale[] availableLocales = Locale.getAvailableLocales();
                for (Locale locale : availableLocales) {
                    String code = locale.getCountry();
                    String name = locale.getDisplayCountry(Locale.ENGLISH);

                    Log.w("name", name);
                }*/

                int ClientIPCount = 1;

                for(int i=0; i<ClientIP.length; i++){
                    int c = 0;
                    for(int j=0; j<i; j++){
                        if(ClientIP[i].equals(ClientIP[j])){
                            break;
                        }
                        else c++;

                        if(j==i-1){
                            if(c==j+1){
                                ClientIPCount++;
                            }
                        }
                    }
                }

                //yValues = new int[ClientIPCount];
                //xValues = new String[ClientIPCount];
                int[] ClientIPValue = new int[ClientIPCount];
                String[] ClientIPArr = new String[ClientIPCount];

                ClientIPArr[0] = ClientIP[0];
                k = 1;
                for(int i=0; i<ClientIP.length; i++){
                    int c = 0;
                    for(int j=0; j<i; j++){
                        if(ClientIP[i].equals(ClientIP[j])){
                            break;
                        }
                        else c++;

                        if(j == i - 1){
                            if(c == j + 1){
                                ClientIPArr[k++] = ClientIP[i];
                            }
                        }
                    }
                }

                for(int i=0; i<ClientIP.length; i++){
                    for(int j=0; j<ClientIPArr.length; j++){
                        if(ClientIPArr[j].equals(ClientIP[i])){
                            ClientIPValue[j]++;
                        }
                    }
                }

                for(int i=0; i<ClientIPArr.length; i++){
                    for(int j=i+1; j<ClientIPArr.length; j++){
                        if(ClientIPValue[i] < ClientIPValue[j]){
                            int temp = ClientIPValue[i];
                            ClientIPValue[i] = ClientIPValue[j];
                            ClientIPValue[j] = temp;

                            String temp_s = ClientIPArr[i];
                            ClientIPArr[i] = ClientIPArr[j];
                            ClientIPArr[j] = temp_s;
                        }
                    }
                }

                int count = 6;
                if(ClientIPArr.length <= 6){
                    count = ClientIPArr.length;
                }

                IPyValues = new int[count];
                IPxValues = new String[count];

                for(int i=0; i<count; i++){
                    IPyValues[i] = ClientIPValue[i];
                    IPxValues[i] = ClientIPArr[i];
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
                k = 1;
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
                progressDialog = ProgressDialog.show(ReportActivity.this, null, "PDF를 생성하는 중입니다.\n잠시만 기다려주세요.");

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        // 1. 필요한 작업
                        print();
                        handler.sendEmptyMessage(0);
                    }
                });
                thread.start();

                //print();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            progressDialog.dismiss(); // 다이얼로그 삭제
            // 2. 이후 처리
            /*File file = new File("/storage/emulated/0/Download/로그분석REPORT.pdf");

            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e) {
                    Toast.makeText(ReportActivity.this,
                            "No Application Available to View PDF",
                            Toast.LENGTH_SHORT).show();
                }

                finish();
            }*/
        }
    };


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

    public void print(){
        try {
            File f = new File("/storage/emulated/0/Download/로그분석REPORT.pdf");
            if(f.exists())
            {
                f.delete();
                Log.w("파일", "있음");
            }
            else {
                Log.w("파일", "없음");
            }

            Document document = new Document();

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(f));

            MyFooter event = new MyFooter();
            writer.setPageEvent(event);

            document.open();
            InputStream is = getAssets().open("fonts/malgun.ttf");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            BaseFont objBaseFont = BaseFont.createFont("malgun.ttf", BaseFont.IDENTITY_H, true, false, buffer, null);
            //BaseFont objBaseFont = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(objBaseFont, 10);
            Font font1 = new Font(objBaseFont,10,Font.BOLD);
            Font subtext = new Font(objBaseFont,6,Font.NORMAL);
            Font subject = new Font(objBaseFont,14,Font.NORMAL);
            Font busubject = new Font(objBaseFont,12,Font.NORMAL);
            Font content = new Font(objBaseFont,12,Font.BOLD);
            Font result = new Font(objBaseFont,11,Font.NORMAL);
            Font result2 =new Font(objBaseFont,12,Font.NORMAL);
            Font menual =new Font(objBaseFont,8,Font.NORMAL);
            Font font2 = new Font(objBaseFont,20,Font.BOLD);
            Font spacing = new Font(objBaseFont, 8, Font.NORMAL);
            Font font3 = new Font(objBaseFont, 12, Font.BOLD);
            Font font4 = new Font(objBaseFont, 5, Font.NORMAL);
            Font font5 = new Font(objBaseFont, 10, Font.NORMAL);
            font5.setColor(0, 122, 204);
            Font spacing2 = new Font(objBaseFont, 10, Font.NORMAL);
            Font spacing3 = new Font(objBaseFont, 12, Font.NORMAL);
            Font spacing4 = new Font(objBaseFont, 2, Font.NORMAL);
            //Chunk chunk = new Chunk("로그 분석 보고서\n\n", font);
            //Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            //chapter.setNumberDepth(0);
            Paragraph p1 = new Paragraph("\nPRO|LOG\n\n\n", font);
            p1.setAlignment(Element.ALIGN_CENTER);
            Chapter chapter = new Chapter(p1, 1);
            chapter.setNumberDepth(0);
            //chapter.add(p1);
            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(160);
            table.setLockedWidth(true);
            PdfPCell cell;
            cell = new PdfPCell(new Phrase("담당",font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("팀장",font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("사장",font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
            table.addCell("\n\n\n");
            table.addCell("\n\n\n");
            table.addCell("\n\n\n");
            table.setHorizontalAlignment(Element.ALIGN_RIGHT);

            chapter.add(table);

            Paragraph p2 = new Paragraph("\n\n\n로그 분석 보고서\n", font2);
            p2.setAlignment(Element.ALIGN_CENTER);
            chapter.add(p2);

            Paragraph pa1 = new Paragraph("\n\n", font4);
            chapter.add(pa1);

            Paragraph p3 = new Paragraph("────────────────────────────────────────────────────", font5);
            p3.setAlignment(Element.ALIGN_CENTER);
            chapter.add(p3);


            Paragraph pa3 = new Paragraph("총괄 보고서\n\n\n\n", font);
            pa3.setAlignment(Element.ALIGN_CENTER);
            chapter.add(pa3);

            java.util.Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String t = sdf.format(d);

            Paragraph p4 = new Paragraph(t + "\n\n\n\n\n\n\n",font1);
            p4.setAlignment(Element.ALIGN_CENTER);
            chapter.add(p4);

            PdfPTable table2 = new PdfPTable(1);
            table2.setTotalWidth(200);
            table2.setLockedWidth(true);

            //Paragraph ph = new Paragraph("<목차>\n\n       1. 로그 분석 결과\n\n       2. 로그 세부 분석 사항\n\n          A. 로그 현황 분석\n\n          B. 차트 세부 분석\n", font);
            Paragraph ph = new Paragraph("목차\n", font3);
            ph.add(new Paragraph("\n", spacing));
            ph.add(new Paragraph("      1.  로그 분석 결과\n", font));
            ph.add(new Paragraph("\n", spacing));
            ph.add(new Paragraph("      2.  로그 세부 분석 사항\n", font));
            ph.add(new Paragraph("\n", spacing));
            ph.add(new Paragraph("           A.  로그 현황 분석\n", font));
            ph.add(new Paragraph("\n", spacing));
            ph.add(new Paragraph("           B.  차트 세부 분석\n", font));
            //ph.add(new Paragraph("      1. 로그 분석 결과\n\n      2. 로그 세부 분석 사항\n\n          A. 로그 현황 분석\n\n          B. 차트 세부 분석\n", font));
            cell = new PdfPCell(ph);
            cell.setPadding(8);
            table2.addCell(cell);
            table2.setHorizontalAlignment(Element.ALIGN_CENTER);
            chapter.add(table2);

            //Paragraph p5 = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n이 문서는 사용자의 로그를 기반으로 분석한 자료입니다. 참고용으로 사용하시길 바랍니다.",font);
            //p5.setAlignment(Element.ALIGN_CENTER);
            //chapter.add(p5);


            //////////////////////////////////////////////1페이지 끝.

            //Chunk chunk2 = new Chunk("로그 분석 보고서\n\n", font);
            //Chapter chapter2 = new Chapter(new Paragraph(chunk2), 1);
            //chapter2.setNumberDepth(0);
            Paragraph p6 = new Paragraph("\n  1. 로그 분석 결과\n",subject);
            p6.add(new Paragraph("\n\n", spacing2));
            Chapter chapter2 = new Chapter(p6, 1);
            chapter2.setNumberDepth(0);
            //chapter2.add(p6);


            PdfPTable table3 = new PdfPTable(1);
            table3.setTotalWidth(470);
            table3.setLockedWidth(true);
            //Phrase summery = new Phrase("전체 로그 중 총 " + detectedLog + "개의 위험이 탐지되었습니다.\n (탐지 로그/전체 로그 : " + detectedLog + "/" + checkedLog + ")\n\n 탐지 된 " + detectedLog + "개의 위험 중 심각한 위험이 " + dangerousLog + "개 발견되었습니다.\n\n 전문가의 조치가 즉시 필요합니다.\n\n PRO|LOG 홈페이지의 고객센터에서 도움을 받으시는 것을 권고합니다.",content);
            Paragraph summery = new Paragraph("전체 로그 중 총 " + detectedLog + "개의 위험이 탐지되었습니다.\n",content);
            summery.add(new Paragraph("\n", spacing));
            summery.add(new Paragraph("(탐지 로그/전체 로그 : " + detectedLog + "/" + checkedLog + ")\n", content));
            summery.add(new Paragraph("\n", spacing));
            summery.add(new Paragraph("탐지된 " + detectedLog + "개의 위험 중 심각한 위험이 " + da2 + "개 발견되었습니다.\n", content));
            summery.add(new Paragraph("\n", spacing));
            summery.add(new Paragraph("전문가의 조치가 즉시 필요합니다.\n", content));
            summery.add(new Paragraph("\n", spacing));
            summery.add(new Paragraph("PRO|LOG 홈페이지의 고객센터에서 도움을 받으시는 것을 권고합니다.\n", content));

            cell = new PdfPCell(summery);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);
            chapter2.add(table3);

            Paragraph p7 = new Paragraph("\n\n\n", spacing2);
            p7.add(new Paragraph("  2. 로그 분석 세부 사항\n", subject));
            p7.add(new Paragraph("\n", spacing3));
            chapter2.add(p7);
            Paragraph p8 = new Paragraph("      A. 로그 현황 분석\n",busubject);
            p8.add(new Paragraph("\n", spacing3));
            chapter2.add(p8);

            PdfPTable table4 = new PdfPTable(3);
            table4.setWidthPercentage(100);
            table4.getDefaultCell().setUseAscender(true);
            table4.getDefaultCell().setUseDescender(true);
            table4.setTotalWidth(470);
            table4.setLockedWidth(true);

            cell = new PdfPCell(new Phrase("로그 현황", font1));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);

            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("전체 로그",font1));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell);
	        /*Paragraph number1=new Paragraph();
	        number1.add("탐지 로그¹");*/
            cell = new PdfPCell(new Phrase("탐지 로그¹",font1));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell);
/*	        Paragraph number2=new Paragraph();
	        number2.add("위험 로그 a²");*/
            cell = new PdfPCell(new Phrase("위험 로그²",font1));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell);
            table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(new Phrase("" + checkedLog, font));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("" + detectedLog, font));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell);
            cell = new PdfPCell(new Phrase("" + da2, font));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell);

            chapter2.add(table4);

            String str;
            if(dateArr[0].equals(dateArr[dateArr.length - 1])){
                str = "\n        ▶   로그 수집 기간 : " + dateArr[0] + "\n        ▶   로그 출처\n             -IP : 125.141.149.11\n             -PORT : 8889\n             -운영체제 : Microsoft Internet Information Services 6.0\n\n\n";
            }
            else{
                str = "\n        ▶   로그 수집 기간 : " + dateArr[0] + " ~ " + dateArr[dateArr.length - 1] + "\n        ▶   로그 출처\n             -IP : 125.141.149.11\n             -PORT : 8889\n             -운영체제 : Microsoft Internet Information Services 6.0\n\n\n";
            }

            Paragraph p9 = new Paragraph(str, font);
            chapter2.add(p9);

            float[] columnWidths = {5.8f, 0.3f, 1.7f, 3.5f};
            PdfPTable table5 = new PdfPTable(columnWidths);
            table5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5.setTotalWidth(530);
            table5.setLockedWidth(true);

            String str1 = "√ 로그 분석 결과 공격이 많이 들어온 국가는 ";
            int max1 = 4;
            double sum = 0;
            if(CountryxValues.length < 4) max1 = CountryxValues.length;

            for(int i=0; i<max1; i++){
                sum += CountryyValues[i];
            }

            Double[] CountryRate = {0.00, 0.00, 0.00, 0.00};
            for(int i=0; i<max1; i++){
                CountryRate[i] = CountryyValues[i] / sum * 100.0;
                Log.w("CountryRate[i]", Double.toString(CountryRate[i]));
                Log.w("CountryyValues[i]", Integer.toString(CountryyValues[i]));
            }

            for(int i=0; i<max1; i++){
                Locale loc = new Locale("", CountryxValues[i]);
                Log.w("CountryxValues[i]", loc.getCountry());
                if(i != max1 - 1){
                    str1 += loc.getDisplayCountry() + "(" + CountryRate[i] + "%), ";
                }
                else str1 += loc.getDisplayCountry() + "(" + CountryRate[i] + "%) 순입니다.\n\n";
            }

            String str2 = "√ 로그 분석 결과 공격이 많이 들어온 IP는 ";
            int max2 = 4;
            if(IPxValues.length < 4) max2 = IPxValues.length;

            sum = 0;
            for(int i=0; i<max2; i++){
                sum += IPyValues[i];
            }

            Double[] IPRate = {0.00, 0.00, 0.00, 0.00};
            for(int i=0; i<max2; i++){
                IPRate[i] = IPyValues[i] / sum * 100.0;
            }

            for(int i=0; i<max2; i++){
                String c;
                try{
                    Locale locale = InetAddressLocator.getLocale(IPxValues[i]);
                    c = locale.getCountry();
                    Log.w("c", c);
                }catch(InetAddressLocatorException e){
                    c = "LOCAL";
                }

                String countryName;
                if(!c.equals("LOCAL")){
                    Locale loc = new Locale("", c);
                    countryName = loc.getDisplayCountry();
                    Log.w("countryName", countryName);
                    if(countryName.equals("")) countryName = "대한민국";
                }
                else countryName = "LOCAL";

                if(i != max2 - 1){
                    str2 += IPxValues[i] + "(" + countryName + "), ";
                }
                else str2 +=IPxValues[i] + "(" + countryName + ") 순입니다.\n";
            }

            //Phrase res = new Phrase("\n -로그 분석 결과 공격이 많이 들어온 국가는 대한민국(80.54%), 미국(14.07%), 인도네시아(5.39%)순 입니다.\n\n -로그 분석 결과 공격이 많이 들어온 IP는 124.2.44.105(한국), 121.11.222.33(한국), 221.39.150.195(미국)순 입니다.\n\n",result);
            Paragraph res = new Paragraph(str1 + str2, result);
            //res.setIndentationLeft(8);
            res.setLeading(0, 2);
            cell=new PdfPCell();
            cell.setRowspan(9);
            cell.setPaddingLeft(5);
            cell.setPaddingRight(5);
            cell.addElement(res);
            //cell.setPadding(8);
            table5.addCell(cell);

            cell=new PdfPCell();
            cell.setBorderWidth(0);
            cell.setRowspan(9);
            table5.addCell(cell);

            Paragraph name = new Paragraph("공격 빈도가 높은 국가 및  IP", font1);
            name.setAlignment(Element.ALIGN_CENTER);
            cell =new PdfPCell(name);
            cell.setColspan(2);
            cell.setPadding(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell.addElement(name);
            table5.addCell(cell);

            Paragraph country= new Paragraph("국가", font1);
            country.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(country);
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //cell.addElement(country);
            table5.addCell(cell);

            String co[] = new String[4];
            for(int i=0; i<max1; i++){
                int j = i+1;
                Locale loc = new Locale("", CountryxValues[i]);
                co[i] = "  " + j + ". " + loc.getDisplayCountry() + " (" + CountryRate[i] + "%)";
            }
            for(int i=CountryxValues.length; i<4; i++){
                co[i] = " ";
            }

            String cl[] = new String[4];
            for(int i=0; i<max2; i++){
                int j = i+1;
                cl[i] = "  " + j + ". " + IPxValues[i] + " (" + IPRate[i] + "%)";
            }
            for(int i=IPxValues.length; i<4; i++){
                cl[i] = " ";
            }

            Paragraph country1 = new Paragraph(co[0], font);
            country1.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(country1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setPaddingLeft(10);
            //cell.addElement(country1);
            table5.addCell(cell);

            Paragraph country2 = new Paragraph(co[1], font);
            country2.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(country2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setPaddingLeft(10);
            //cell.addElement(country2);
            table5.addCell(cell);

            Paragraph country3 = new Paragraph(co[2], font);
            country3.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(country3);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setPaddingLeft(10);
            //cell.addElement(country3);
            table5.addCell(cell);

            Paragraph country4 = new Paragraph(co[3],font);
            country4.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(country4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setPaddingLeft(10);
            //cell.addElement(country4);
            table5.addCell(cell);

            Paragraph IP= new Paragraph("IP",font1);
            IP.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(IP);
            cell.setRowspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //cell.addElement(IP);
            table5.addCell(cell);

            Paragraph IP1 = new Paragraph(cl[0], font);
            IP1.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(IP1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setPaddingLeft(10);
            //cell.addElement(IP1);
            table5.addCell(cell);

            Paragraph IP2 = new Paragraph(cl[1], font);
            IP2.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(IP2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setPaddingLeft(10);
            //cell.addElement(IP2);
            table5.addCell(cell);

            Paragraph IP3 = new Paragraph(cl[2], font);
            IP3.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(IP3);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setPaddingLeft(10);
            //cell.addElement(IP3);
            table5.addCell(cell);

            Paragraph IP4 = new Paragraph(cl[3], font);
            IP4.setAlignment(Element.ALIGN_CENTER);
            cell = new PdfPCell(IP4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(5);
            cell.setPaddingLeft(10);
            //cell.addElement(IP4);
            table5.addCell(cell);

            chapter2.add(table5);

            //Paragraph line= new Paragraph("\n──────────────────\n ¹ 탐지 로그 : 전체 로그 중 위험 요소가 탐지 된 로그\n ² 위험 로그 : 탐지 로그 중 위험도가 높은 로그 (위험 2단계)\n",menual);
            //chapter2.add(line);

            //Paragraph page2 = new Paragraph("\n-2-",font);
            //page2.setAlignment(Element.ALIGN_CENTER);
            //chapter2.add(page2);
            ////////////////////////////////////////2페이지 끝

            //Chunk chunk3 = new Chunk("로그 분석 보고서\n\n\n", font);
            //Chapter chapter3 = new Chapter(new Paragraph(chunk3), 1);
            //chapter3.setNumberDepth(0);

            Paragraph p10 = new Paragraph("\n      B. 차트 세부 분석\n\n",busubject);
            Chapter chapter3 = new Chapter(p10, 1);
            chapter3.setNumberDepth(0);
            //chapter3.add(p10);

            Paragraph p13 = new Paragraph("\n",font);
            chapter3.add(p13);

            float[] columnWidths2 = {4,0.3f,4};
            PdfPTable table6 = new PdfPTable(columnWidths2);
            table6.setHorizontalAlignment(Element.ALIGN_CENTER);
            table6.setTotalWidth(500);
            table6.setLockedWidth(true);

            /*Image image = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"\\resources\\1.png");
            Image image2 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"\\resources\\2.png");
            System.out.println(request.getSession().getServletContext().getRealPath("/")+"\\resources\\2.png");

            image.scaleAbsolute(230, 150);
            image2.scaleAbsolute(230, 150);

            cell = new PdfPCell(image,false);
            cell.setBorderWidth(0);
            table6.addCell(cell);

            cell =new PdfPCell();
            cell.setBorderWidth(0);
            table6.addCell(cell);

            cell = new PdfPCell(image2,false);
            cell.setBorderWidth(0);
            table6.addCell(cell);*/

            //chapter3.add(table6);

            //Paragraph p11 = new Paragraph("\n\n\n",busubject);
            //chapter3.add(p11);

            /*int attackV[] = new int[6];
            try{
                for(int i=0; i<6; i++){
                    int j = i+1;
                    rs = stmt.executeQuery("select * from table3 where AttackLevel=\""+j+"\"");
                    rs.last();
                    attackV[i] = rs.getRow();
                }
            }catch(Exception e){
                e.printStackTrace();
            }*/


            float[] columnWidths3 = {1.15f,1.5f,0.95f,3.7f,2,1};
            PdfPTable table7 = new PdfPTable(columnWidths3);
            table7.setHorizontalAlignment(Element.ALIGN_CENTER);
            table7.setTotalWidth(500);
            table7.setLockedWidth(true);

            Paragraph level= new Paragraph("예상피해\n",font1);
            level.add(new Paragraph("\n", spacing4));
            level.add(new Paragraph("레벨", font1));
            level.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(level);
            cell.setPadding(5);
            //cell.addElement(level);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph attack = new Paragraph("공격 유형",font1);
            attack.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(attack);
            cell.setPadding(5);
            //cell.addElement(attack);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risklevel = new Paragraph("위험도",font1);
            risklevel.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(risklevel);
            cell.setPadding(5);
            //cell.addElement(risklevel);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risk = new Paragraph("예상 피해",font1);
            risk.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(risk);
            cell.setPadding(5);
            //cell.addElement(risk);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph danger = new Paragraph("위험 단계",font1);
            danger.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(danger);
            cell.setPadding(5);
            //cell.addElement(danger);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph fre = new Paragraph("빈도수",font1);
            fre.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(fre);
            cell.setPadding(5);
            //cell.addElement(fre);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph level1 = new Paragraph("상",font1);
            level1.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(level1);
            cell.setPadding(5);
            //cell.addElement(level1);
            cell.setRowspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph attack1 = new Paragraph("SQL Injection", font);
            attack1.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(attack1);
            cell.setPadding(5);
            //cell.addElement(attack1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risklevel1 = new Paragraph("6",font);
            risklevel1.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(risklevel1);
            cell.setPadding(5);
            //cell.addElement(risklevel1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risk1 = new Paragraph("개인(DB)정보 노출,\n",font);
            risk1.add(new Paragraph("\n", spacing4));
            risk1.add(new Paragraph("2차 공격 발생 가능성", font));
            risk1.setAlignment(Element.ALIGN_LEFT);
            cell=new PdfPCell(risk1);
            cell.setPadding(5);
            //cell.addElement(risk1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPaddingLeft(4);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph danger1 = new Paragraph("위험2단계¹",font);
            danger1.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(danger1);
            cell.setPadding(5);
            //cell.addElement(danger1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph fre1 = new Paragraph("" + AttackyValues[0], font);//변수
            fre1.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(fre1);
            cell.setPadding(5);
            //cell.addElement(fre1);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph attack2 = new Paragraph("Directory\n",font);
            attack2.add(new Paragraph("\n", spacing4));
            attack2.add(new Paragraph("Listing", font));
            attack2.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(attack2);
            cell.setPadding(5);
            //cell.addElement(attack2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risklevel2 = new Paragraph("5",font);
            risklevel2.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(risklevel2);
            cell.setPadding(5);
            //cell.addElement(risklevel2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risk2 = new Paragraph("개인 정보 탈취,\n",font);
            risk2.add(new Paragraph("\n", spacing4));
            risk2.add(new Paragraph("웹 서버 공격", font));
            risk2.setAlignment(Element.ALIGN_LEFT);
            cell=new PdfPCell(risk2);
            cell.setPadding(5);
            //cell.addElement(risk2);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingLeft(4);
            table7.addCell(cell);

            Paragraph danger2 = new Paragraph("위험2단계",font);
            danger2.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(danger2);
            cell.setPadding(5);
            //cell.addElement(danger2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph fre2 = new Paragraph("" + AttackyValues[1],font);//변수
            fre2.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(fre2);
            cell.setPadding(5);
            //cell.addElement(fre2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph attack3 = new Paragraph("LFI&RFI",font);
            attack3.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(attack3);
            cell.setPadding(5);
            //cell.addElement(attack3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risklevel3 = new Paragraph("4",font);
            risklevel3.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(risklevel3);
            cell.setPadding(5);
            //cell.addElement(risklevel3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risk3 = new Paragraph("공격 컴퓨터에 악성 코드 발생\n",font);
            risk3.setAlignment(Element.ALIGN_LEFT);
            cell=new PdfPCell(risk3);
            cell.setPadding(5);
            //cell.addElement(risk3);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPaddingLeft(4);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph danger3 = new Paragraph("위험2단계",font);
            danger3.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(danger3);
            cell.setPadding(5);
            //cell.addElement(danger3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph fre3 = new Paragraph("" + AttackyValues[2],font);//변수
            fre3.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(fre3);
            cell.setPadding(5);
            //cell.addElement(fre3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph level2 = new Paragraph("중",font1);
            level2.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(level2);
            cell.setPadding(5);
            // cell.addElement(level2);
            cell.setRowspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph attack4 = new Paragraph("Scanning",font);
            attack4.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(attack4);
            cell.setPadding(5);
            //cell.addElement(attack4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risklevel4 = new Paragraph("3",font);
            risklevel4.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(risklevel4);
            cell.setPadding(5);
            //cell.addElement(risklevel4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risk4 = new Paragraph("개인 정보 수집,\n",font);
            risk4.add(new Paragraph("\n", spacing4));
            risk4.add(new Paragraph("추가 공격 가능성", font));
            risk4.setAlignment(Element.ALIGN_LEFT);
            cell=new PdfPCell(risk4);
            cell.setPadding(5);
            //cell.addElement(risk4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPaddingLeft(4);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph danger4 = new Paragraph("위험1단계²",font);
            danger4.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(danger4);
            cell.setPadding(5);
            //cell.addElement(danger4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph fre4 = new Paragraph("" + AttackyValues[3],font);//변수
            fre4.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(fre4);
            cell.setPadding(5);
            //cell.addElement(fre4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph attack5 = new Paragraph("Web CGI",font);
            attack5.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(attack5);
            cell.setPadding(5);
            //cell.addElement(attack5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risklevel5 = new Paragraph("2",font);
            risklevel5.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(risklevel5);
            cell.setPadding(5);
            //cell.addElement(risklevel5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risk5 = new Paragraph("웹 쿠키 정보 노출,\n",font);
            risk5.add(new Paragraph("\n", spacing4));
            risk5.add(new Paragraph("거짓 페이지 생성 후 개인 정보 탈취", font));
            risk5.setAlignment(Element.ALIGN_LEFT);
            cell=new PdfPCell(risk5);
            cell.setPadding(5);
            //cell.addElement(risk5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPaddingLeft(4);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph danger5 = new Paragraph("위험1단계",font);
            danger5.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(danger5);
            cell.setPadding(5);
            //cell.addElement(danger5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph fre5 = new Paragraph("" + AttackyValues[4],font);//변수
            fre5.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(fre5);
            cell.setPadding(5);
            //cell.addElement(fre5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);


            Paragraph level3 = new Paragraph("하\n",font1);
            level3.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(level3);
            cell.setPadding(5);
            //cell.addElement(level3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph attack6 = new Paragraph("Pattern Block\n",font);
            attack6.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(attack6);
            cell.setPadding(5);
            //cell.addElement(attack6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risklevel6 = new Paragraph("1",font);
            risklevel6.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(risklevel6);
            cell.setPadding(5);
            //cell.addElement(risklevel6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph risk6 = new Paragraph("시스템 과부하 및 서비스 성능 저하\n",font);
            risk6.setAlignment(Element.ALIGN_LEFT);
            cell=new PdfPCell(risk6);
            cell.setPadding(5);
            //cell.addElement(risk6);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPaddingLeft(4);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph danger6 = new Paragraph("위험1단계",font);
            danger6.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(danger6);
            cell.setPadding(5);
            //cell.addElement(danger6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);

            Paragraph fre6 = new Paragraph("" + AttackyValues[5],font);//변수
            fre6.setAlignment(Element.ALIGN_CENTER);
            cell=new PdfPCell(fre6);
            cell.setPadding(5);
            //cell.addElement(fre6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table7.addCell(cell);
            chapter3.add(table7);


            Paragraph p12 = new Paragraph("\n",font);
            chapter3.add(p12);

            PdfPTable table8 = new PdfPTable(1);
            table8.setTotalWidth(500);
            table8.setLockedWidth(true);
            table8.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);


            int top = 0, middle = 0, bottom = 0;
            top = AttackyValues[0] + AttackyValues[1] + AttackyValues[2];
            middle = AttackyValues[3] + AttackyValues[4];
            bottom = AttackyValues[5];
           /* try{
                rs = stmt.executeQuery("select * from table3 where AttackLevel=\"1\"");
                rs.last();
                bottom = rs.getRow();
                rs = stmt.executeQuery("select * from table3 where AttackLevel=\"2\" or AttackLevel=\"3\"");
                rs.last();
                middle = rs.getRow();
                rs = stmt.executeQuery("select * from table3 where AttackLevel=\"4\" or AttackLevel=\"5\" or AttackLevel=\"6\"");
                rs.last();
                top = rs.getRow();
            }catch(Exception e){
                e.printStackTrace();
            }*/

            int account = top + middle + bottom;
            double topP = 0, middleP = 0, bottomP = 0;
            topP = top * 100.0 / account;
            topP = Math.round(topP * 100d) / 100d;
            middleP = middle * 100.0 / account;
            middleP = Math.round(middleP * 100d) / 100d;
            bottomP = bottom * 100.0 / account;
            bottomP = Math.round(bottomP * 100d) / 100d;
            topP = Double.parseDouble(String.format("%.1f", topP));
            middleP = Double.parseDouble(String.format("%.1f", middleP));
            bottomP = Double.parseDouble(String.format("%.1f", bottomP));

            int riskNum1 = bottom + middle;

            Paragraph p14=new Paragraph("   √  전체 로그 " + checkedLog + "개 중 위험 2단계 " + da2 + "개, 위험 1단계 " + da1 + "개가 발견되었습니다.\n\n   √  예상 피해 정도는 상 " + topP + "%, 중 " + middleP + "% 그리고 하 " + bottomP + "% 입니다.\n",font);
            cell=new PdfPCell(p14);
            cell.setPadding(5);
            cell.setPaddingLeft(6);
            //cell.addElement(p14);
            table8.addCell(cell);
            chapter3.add(table8);

            //Paragraph line2= new Paragraph("\n\n\n\n\n\n\n\n──────────────────\n ¹ 위험 2단계 : 위험 로그 중 위험도가 4 이상인 로그\n ² 위험 1단계 : 위험 로그 중 위험도가 4 미만인 로그\n",menual);
            //chapter3.add(line2);

            //Paragraph page3 = new Paragraph("\n-3-",font);
            //page3.setAlignment(Element.ALIGN_CENTER);
            //chapter3.add(page3);


            document.add(chapter);
            document.add(chapter2);
            document.add(chapter3);

            document.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.w("파일", "오류");
        }

        File file = new File("/storage/emulated/0/Download/로그분석REPORT.pdf");

        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(ReportActivity.this,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    }

    class MyFooter extends PdfPageEventHelper {
        public void onEndPage(PdfWriter writer, Document document) {
            try{
                InputStream is = getAssets().open("fonts/malgun.ttf");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                BaseFont objBaseFont = BaseFont.createFont("malgun.ttf", BaseFont.IDENTITY_H, true, false, buffer, null);
                //BaseFont objBaseFont = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font font = new Font(objBaseFont, 10);
                Font font2 = new Font(Font.FontFamily.UNDEFINED, 10, Font.ITALIC);
                Font menual = new Font(objBaseFont, 8);

                PdfContentByte cb = writer.getDirectContent();
                Phrase header = new Phrase("로그 분석 보고서", font);
                Phrase footer = new Phrase("" + writer.getPageNumber(), font2);

                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                        header,
                        document.left() + document.leftMargin(),
                        document.top(), 0);
                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                        footer,
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 10, 0);

                if(writer.getPageNumber() == 1){
                    Phrase footnotes = new Phrase("이 문서는 사용자의 로그를 기반으로 분석한 자료입니다. 참고용으로 사용하시길 바랍니다.", font);
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                            footnotes,
                            (document.right() - document.left()) / 2 + document.leftMargin(),
                            document.bottom() + 10, 0);
                }
                if(writer.getPageNumber() == 2){
                    Phrase footnotes = new Phrase("¹ 탐지 로그 : 전체 로그 중 위험 요소가 탐지 된 로그", menual);
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                            footnotes,
                            120,
                            document.bottom() + 23, 0);

                    footnotes = new Phrase("² 위험 로그 : 탐지 로그 중 위험도가 높은 로그 (위험 2단계)", menual);
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                            footnotes,
                            132,
                            document.bottom() + 10, 0);
                }
                if(writer.getPageNumber() == 3){
                    Phrase footnotes = new Phrase("¹ 위험 2단계 : 위험 로그 중 위험도가 4 이상인 로그", menual);
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                            footnotes,
                            120,
                            document.bottom() + 23, 0);

                    footnotes = new Phrase("² 위험 1단계 : 위험 로그 중 위험도가 4 미만인 로그", menual);
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                            footnotes,
                            120,
                            document.bottom() + 10, 0);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}



