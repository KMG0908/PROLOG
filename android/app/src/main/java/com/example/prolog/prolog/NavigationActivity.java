package com.example.prolog.prolog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class NavigationActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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
                                DashboardActivity.class); // 다음 넘어갈 클래스 지정
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
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
