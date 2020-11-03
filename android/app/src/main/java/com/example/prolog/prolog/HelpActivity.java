package com.example.prolog.prolog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile1";
    public CheckBox dontShowAgain;
    private DrawerLayout mDrawerLayout;

    String loginID = NotiActivity.ID_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ImageButton kisa_internet = (ImageButton) findViewById(R.id.kisa_internet);
        ImageButton kisa_phone = (ImageButton) findViewById(R.id.kisa_phone);
        ImageButton kisa_email = (ImageButton) findViewById(R.id.kisa_mail);

        ImageButton mili_internet = (ImageButton) findViewById(R.id.mili_internet);
        ImageButton mili_phone = (ImageButton) findViewById(R.id.mili_phone);
        ImageButton mili_email = (ImageButton) findViewById(R.id.mili_mail);

        ImageButton police_internet = (ImageButton) findViewById(R.id.police_internet);
        ImageButton police_phone = (ImageButton) findViewById(R.id.police_phone);
        ImageButton police_email = (ImageButton) findViewById(R.id.police_mail);

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
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
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

        kisa_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.kisa.or.kr/main.jsp");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
                //finish();
            }

        });

        kisa_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:118"));
                startActivity(myIntent);
                //finish();
            }

        });
        kisa_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "wseol@kisa.or.kr");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }

        });

        mili_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.mnd.go.kr/mbshome/mbs/mnd/subview.jsp?id=mnd_030501000000");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
                //finish();
            }

        });

        mili_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:1577-9090"));
                startActivity(myIntent);
                //finish();
            }

        });
        mili_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "military@gmail.com");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }

        });

        police_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://cyber.go.kr/crime/sub1.jsp?mid=010101");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
                //finish();
            }

        });

        police_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:182"));
                startActivity(myIntent);
                //finish();
            }

        });
        police_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "police@gmail.com");
                startActivity(Intent.createChooser(intent, "Send Email"));
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

}
