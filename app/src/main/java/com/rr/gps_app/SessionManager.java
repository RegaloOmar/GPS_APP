package com.rr.gps_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager
{
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USER = "USER";
    public static final String IDCANAL = "IDCANAL";
    public static final String DESTINO = "DESTINO";

    public SessionManager(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String user, String idCanal, String destino)
    {
        editor.putBoolean(LOGIN,true);
        editor.putString(USER,user);
        editor.putString(IDCANAL,idCanal);
        editor.putString(DESTINO,destino);
        editor.apply();
    }

    public boolean isLoggin()
    {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin()
    {
        if (!this.isLoggin())
        {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String,String> getUSerDetail()
    {
        HashMap<String, String> user = new HashMap<>();
        user.put(USER, sharedPreferences.getString(USER,null));
        user.put(IDCANAL,sharedPreferences.getString(IDCANAL,null));
        user.put(DESTINO,sharedPreferences.getString(DESTINO,null));
        return user;
    }

    public void logout()
    {
        editor.clear().apply();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
        ((MainActivity) context).finish();
    }
}
