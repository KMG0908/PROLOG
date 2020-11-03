package com.example.prolog.prolog;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    public static String basic_url = "http://192.168.43.10:80";

    String url = basic_url + "/php/loginData.php";
    public GettingPHP gPHP;
    String[] email;
    String[] passwd;
    boolean flag = false;

    public static LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gPHP = new GettingPHP();
        gPHP.execute(url);

        loginActivity = LoginActivity.this;
        Button loginButton = (Button)findViewById(R.id.loginButton);

        final TextView idText = (TextView) findViewById(R.id.idText);
        final TextView passwordText = (TextView) findViewById(R.id.passwordText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(idText.getText().toString().equals("") && passwordText.getText().toString().equals(""))){
                    for(int i=0; i<email.length; i++){
                        if(email[i].equals(idText.getText().toString())){
                            if(passwd[i].equals(passwordText.getText().toString())) flag = true;
                            break;
                        }
                    }

                    if(flag){
                        Intent intent = new Intent(getApplicationContext(), NotiActivity.class);
                        intent.putExtra("loginEmail", idText.getText().toString());
                        intent.putExtra("loginFlag", 0);
                        startActivity(intent);
                    }
                    else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();     //닫기
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        alert.setMessage("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
                        alert.show();
                    }
                }
            }
        });

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
                passwd = new String[results.length()];

                for ( int i = 0; i < results.length(); ++i ) {
                    JSONObject temp = results.getJSONObject(i);
                    email[i] = temp.get("Email").toString();
                    passwd[i] = temp.get("Passwd").toString();
                    Log.w("email", email[i]);
                    Log.w("passwd", passwd[i]);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
