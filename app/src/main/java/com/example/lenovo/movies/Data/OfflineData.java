package com.example.lenovo.movies.Data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lenovo on 11/8/2016.
 */

public class OfflineData {
    Context context;
    private SharedPreferences.Editor set;
    private SharedPreferences get;
    String de = "";
    public OfflineData(Context context){
        this.context = context;
        set = context.getSharedPreferences("save",context.MODE_PRIVATE).edit();
        get = context.getSharedPreferences("save",Context.MODE_PRIVATE);
    }

    public void setJson(String s){
        set.putString("JSON",s);
        set.apply();
    }

    public String getJson(){
        return get.getString("JSON",de);
    }

    public void setDetails(boolean b){
        set.putBoolean("open",b);
        set.apply();
    }

    public boolean getDetails(){
        return get.getBoolean("open",false);
    }

}
