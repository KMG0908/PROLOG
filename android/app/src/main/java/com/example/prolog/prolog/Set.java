package com.example.prolog.prolog;

/**
 * Created by kmg on 2018-10-31.
 */

public class Set {
    private static int cLog, dLog, da1, da2;
    private static String email;

    public void setCLog(int cLog){
        this.cLog = cLog;
    }

    public void setDLog(int dLog){
        this.dLog = dLog;
    }

    public void setDa1(int da1){
        this.da1 = da1;
    }

    public void setDa2(int da2){
        this.da2 = da2;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public int getCLog(){
        return cLog;
    }

    public int getDLog(){
        return dLog;
    }

    public int getDa1(){
        return da1;
    }

    public int getDa2(){
        return da2;
    }

    public String getEmail(){
        return email;
    }
}
