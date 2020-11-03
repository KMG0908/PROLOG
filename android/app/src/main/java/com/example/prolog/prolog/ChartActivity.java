package com.example.prolog.prolog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile1";
    public CheckBox dontShowAgain;
    private DrawerLayout mDrawerLayout;

    PieChart mChart;
    int danger1 = 0;
    int danger2 = 0;
    int safe = 100;

    // we're going to display pie chart for school attendance
    private int[] yValues = {0, 0, 100};
    private String[] xValues = {"위험2", "위험1", "안전"};

    // colors for different sections in pieChart
    public static  final int[] MY_COLORS = {
            Color.rgb(219,64,79), Color.rgb(235,149,024), Color.rgb(19,160, 132)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        NotiActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
                finish();
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra("danger1")){
            danger1 = intent.getExtras().getInt("danger1");
        }
        if(intent.hasExtra("danger2")){
            danger2 = intent.getExtras().getInt("danger2");
        }

        if(danger1 != 0 || danger2 != 0){
            safe = 0;
        }

        yValues[0] = danger2;
        yValues[1] = danger1;
        yValues[2] = safe;

        mChart = (PieChart) findViewById(R.id.chart1);

        //   mChart.setUsePercentValues(true);
        mChart.setDescription("");

        mChart.setRotationEnabled(true);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(ChartActivity.this,
                        xValues[e.getXIndex()] + " is " + e.getVal() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // setting sample Data for Pie Chart
        setDataForPieChart();

    }


    public void setDataForPieChart() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        int flag = yValues.length;

        if(yValues[2] == 0){
            flag -= 1;
        }

        for (int i = 0; i < flag; i++)
            yVals1.add(new Entry(yValues[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < flag; i++)
            xVals.add(xValues[i]);

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        mChart.setHoleColor(Color.rgb(61,74,89));
        mChart.setCenterText("PRO|LOG");
        mChart.setCenterTextSize(25);
        mChart.setCenterTextColor(Color.WHITE);



        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateXY(1400, 1400);


        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        l.setTextColor(Color.WHITE);
    }


    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            /*case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;*/
            case R.id.logout:
                AlertDialog.Builder logout_alert = new AlertDialog.Builder(this);
                logout_alert.setTitle("설정");
                logout_alert.setMessage("로그아웃 하시겠습니까?");
                logout_alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NotiActivity notiActivity = (NotiActivity) NotiActivity.notiActivity;
                        notiActivity.finish();

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

            /*case R.id.settings:
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
                break;*/
        }

        return super.onOptionsItemSelected(item);
    }





}