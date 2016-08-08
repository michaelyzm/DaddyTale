package com.michaelyzm.daddytale;

import android.app.Application;
import android.content.Context;

import com.michaelyzm.daddytale.db.DatabaseHelper;

/**
 * Created by zhoyu on 8/8/2016.
 */
public class DaddyTaleApplication extends Application {

    public static Context ApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationContext = getApplicationContext();
        DatabaseHelper.getInstance().createAndInitDatabase();
    }
}
