package com.runkun.lbsq.busi.activity;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;


public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        CrashReport.initCrashReport(this, "900007967", false);
    }
}
