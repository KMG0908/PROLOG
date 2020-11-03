package com.example.prolog.prolog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
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
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.prolog.prolog.R.drawable.noti;

public class NotiActivity extends AppCompatActivity{
    String url = LoginActivity.basic_url + "/php/alertData.php";
    public GettingPHP gPHP;
    String[] email;
    int[] checkedLog;
    int[] detectedLog;
    int[] danger1;
    int[] danger2;
    String[] date;
    private static String beforeDate = "";
    String id = "";

    private static Thread thread;
    private static Thread alertThread;

    private static int alertFlag = 0;
    private static String loginID = "";
    public static String ID_login = "";

    public int cLog = -1;
    public int dLog = -1;
    private static String d = "";
    public int da1 = 0;
    public int da2 = 0;

    boolean threadFlag = true;
    boolean flag = true;

    private static boolean alertThreadFlag = false;

    private static boolean checkedFlag = false;

    public static final String PREFS_NAME = "MyPrefsFile1";
    public CheckBox dontShowAgain;
    private DrawerLayout mDrawerLayout;

    public static NotiActivity notiActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);

        notiActivity = NotiActivity.this;

        LoginActivity loginActivity = (LoginActivity) LoginActivity.loginActivity;
        loginActivity.finish();

        ImageButton nextButton = (ImageButton)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(da1 != 0 || da2 != 0){
                    Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                    intent.putExtra("danger1", da1);
                    intent.putExtra("danger2", da2);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(NotiActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("정보가 없습니다.");
                    alert.show();
                }
            }
        });

        if(!d.equals("")){
            TextView recent = (TextView)findViewById(R.id.recent);
            TextView textTitle = (TextView) findViewById(R.id.texttitle);
            TextView subText = (TextView)findViewById(R.id.subtext);

            recent.setText("최근 검사일: " + d);
            textTitle.setText("위험합니다");
            subText.setText("전체 로그 " + cLog + "개 중\n" + dLog + "개의 위험이 발견되었습니다.\n확인이 필요합니다.");
        }

        if(thread != null){
            if(thread.isAlive()) threadFlag = false;
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                if(intent.hasExtra("loginEmail")){
                    id = intent.getExtras().getString("loginEmail");
                    loginID = id;
                }
                else id = loginID;

                while(!thread.isInterrupted()){
                    gPHP = new GettingPHP();
                    gPHP.execute(url);
                    SystemClock.sleep(1000 * 30);
                    alertFlag = 1;
                }
            }
        });

        if(threadFlag) {
            thread.setDaemon(true);
            thread.start();
            Log.w("스레드 시작", "◎");
        }

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
                Intent intent = getIntent();
                if(intent.hasExtra("loginEmail")){
                    ID_login = intent.getExtras().getString("loginEmail");
                    ID_login = ID_login.replace("@", "");
                    String i[] = ID_login.split("\\.");
                    ID_login = i[0];
                    Set set = new Set();
                    set.setEmail(ID_login);
                    Log.w("ID_login", ID_login);
                }
                switch (id) {
                    case R.id.dashboard:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.summary:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent summary = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                SummaryActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(summary); // 다음 화면으로 넘어간다
                        break;

                    case R.id.country:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent country = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                CountryActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(country); // 다음 화면으로 넘어간다
                        break;

                    case R.id.ip:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent ip = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                IpActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(ip); // 다음 화면으로 넘어간다
                        break;
                    case R.id.time:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent time = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                TimeActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(time); // 다음 화면으로 넘어간다
                        break;
                    case R.id.attack:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent attack = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                AttackActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(attack); // 다음 화면으로 넘어간다
                        break;
                    case R.id.table:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent table = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                TableActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(table); // 다음 화면으로 넘어간다
                        break;
                    case R.id.report:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent report = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                ReportActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(report); // 다음 화면으로 넘어간다
                        break;
                    case R.id.help:
                        //Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        Intent help = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                HelpActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(help); // 다음 화면으로 넘어간다
                        break;
                }

                return true;
            }
        });
    }

    protected void onResume(){
        super.onResume();

        Log.w("시작", "!!");

        if(alertThread != null) {
            if (alertThread.isAlive()) {
                alertThread.interrupt();
                Log.w("종료", "★");
            }
        }
    }

    protected void onStop(){
        super.onStop();

        Log.w("정지", "ㅁㅁ");
    }

    protected void repeatAlert(){
        /*if(alertThread != null) {
            if (alertThread.isAlive()) {
                alertThread.interrupt();
                Log.w("alertThread", "종료");
                flag = false;
                SystemClock.sleep(1000 * 30);
            }
        }*/

        alertThread = new Thread(new Runnable() {
            @Override
            public void run() {
                alertThreadFlag = true;

                if(flag) SystemClock.sleep(1000 * 30);

                while(!alertThread.isInterrupted()){
                    PendingIntent pendingIntent = PendingIntent.getActivity(NotiActivity.this, 0,
                            new Intent(getApplicationContext(), NotiActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(NotiActivity.this)
                                    .setSmallIcon(noti)
                                    .setContentTitle("위험합니다.")
                                    .setContentText("전체 로그 " + cLog + "개 중 " + dLog + "개의 위험이 발견되었습니다.")
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true)
                                    .setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());

                    Log.w("반복 알림", "☆");

                    SystemClock.sleep(1000 * 30);
                }
            }
        });

        alertThread.setDaemon(true);
        alertThread.start();
    }

    class GettingPHP extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while ( true ) {
                            String line = br.readLine();
                            if ( line == null )
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                JSONArray results = jObject.getJSONArray("results");
                email = new String[results.length()];
                checkedLog = new int[results.length()];
                detectedLog = new int[results.length()];
                danger1 = new int[results.length()];
                danger2 = new int[results.length()];
                date = new String[results.length()];

                for ( int i = 0; i < results.length(); ++i ) {
                    JSONObject temp = results.getJSONObject(i);
                    email[i] = temp.get("Email").toString();
                    String c = temp.get("CheckedLog").toString();
                    checkedLog[i] = Integer.parseInt(c);
                    String d = temp.get("DetectedLog").toString();
                    detectedLog[i] = Integer.parseInt(d);
                    String d1 = temp.get("Danger1").toString();
                    danger1[i] = Integer.parseInt(d1);
                    String d2 = temp.get("Danger2").toString();
                    danger2[i] = Integer.parseInt(d2);
                    date[i] = temp.get("Date").toString();
                }

                TextView recent = (TextView)findViewById(R.id.recent);
                TextView textTitle = (TextView) findViewById(R.id.texttitle);
                TextView subText = (TextView)findViewById(R.id.subtext);

                for(int i=0; i<email.length; i++){
                    if(email[i].equals(id)){
                        recent.setText("최근 검사일: " + date[i]);
                        if(danger2[i] != 0){
                            textTitle.setText("위험 2단계");
                        }
                        else if(danger1[i] != 0){
                            textTitle.setText("위험 1단계");
                        }
                        subText.setText("전체 로그 " + checkedLog[i] + "개 중\n" + detectedLog[i] + "개의 위험이 발견되었습니다.\n확인이 필요합니다.");

                        da1 = danger1[i];
                        da2 = danger2[i];
                        Set set = new Set();
                        set.setDa1(danger1[i]);
                        set.setDa2(danger2[i]);
                        set.setCLog(checkedLog[i]);
                        set.setDLog(detectedLog[i]);

                        PendingIntent pendingIntent = PendingIntent.getActivity(NotiActivity.this, 0,
                                new Intent(getApplicationContext(), NotiActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(NotiActivity.this)
                                        .setSmallIcon(noti)
                                        .setContentTitle("위험합니다.")
                                        .setContentText("전체 로그 " + checkedLog[i] + "개 중 " + detectedLog[i] + "개의 위험이 발견되었습니다.")
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setContentIntent(pendingIntent);

                        if(alertFlag != 0) {
                            if (!beforeDate.equals(date[i])) {
                                d = date[i];
                                cLog = checkedLog[i];
                                dLog = detectedLog[i];

                                if(alertThread != null) {
                                    if (alertThread.isAlive()) {
                                        alertThread.interrupt();
                                        Log.w("alertThread", "종료2");
                                        //flag = false;
                                        SystemClock.sleep(1000 * 30);
                                    }
                                }

                                Log.w("처음 알림", "☆");
                                NotificationManager notificationManager =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.notify(0, builder.build());

                                if(!checkedFlag) repeatAlert();
                            }
                        }

                        beforeDate = date[i];
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void onDestroy(){
        super.onDestroy();

        if(thread != null){
            if(thread.isAlive()){
                thread.interrupt();
                SystemClock.sleep(1000 * 3);
                Log.w("thread", "end");
            }
        }

        if(alertThread != null){
            if(alertThread.isAlive()){
                alertThread.interrupt();
                SystemClock.sleep(1000 * 3);
                Log.w("alertThread", "end");
            }
        }

        Log.w("파괴", "destory");
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
}
