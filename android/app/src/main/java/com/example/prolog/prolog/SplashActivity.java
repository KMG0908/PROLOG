package com.example.prolog.prolog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 황두호 on 2017-10-05.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
