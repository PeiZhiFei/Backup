package com.runkun.lbsq.activity;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.tenddata.TCAgent;

public class MyApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
//        L.setDEBUG(false);
        TCAgent.init(this);

//        MyExceptionDialog.getInstance().init();
        CrashReport.initCrashReport(this, "900008095", false);
//        TCAgent.setReportUncaughtExceptions(true);
    }

}
