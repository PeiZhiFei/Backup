package com.runkun.lbsq.busi.test2;/*
package project.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import library.util.L;

public class MyExceptionDialog implements UncaughtExceptionHandler {
    private static MyExceptionDialog myException = new MyExceptionDialog();
    private Context context;
    private final DateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
    // 用来存储设备信息和异常信息
    private final Map<String, String> infos = new HashMap<String, String>();

    // String path = FileUtil.getRootPath() + "log/";

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private MyExceptionDialog() {
    }


    private void fin(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.zoom_enter,
                R.anim.zoom_exit);
        ((Activity) context).finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    */
/**
 * @return
 * @notice 同步方法，以免单例多线程环境下出现异常
 * @param ex
 * @return
 * @notice 保存错误信息到文件中，返回文件名称,便于将文件传送到服务器
 *//*

    public synchronized static MyExceptionDialog getInstance() {
        return myException;
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(myException);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        showDialog();
        String s = saveCrashInfo2File(ex);
        if (Tools.isNetworkAvailable(context)) {
            updateException(s);
        } else {

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            fin(context);
                        }
                    }, 1000);
                    Looper.loop();
                }
            }.start();

        }
        L.l(s);
    }

    private void showDialog() {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = new Toast(context.getApplicationContext());
                toast.setView(LayoutInflater.from(
                        context.getApplicationContext()).inflate(
                        R.layout.dialog_error, null));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                // TS
                // .toast(context.getApplicationContext(), "这样的bug都被你发现了");
                // Tools.dialogError(context);
                Looper.loop();
            }
        }.start();
    }

    */
/**
 * @param ex
 * @return
 * @notice 保存错误信息到文件中，返回文件名称,便于将文件传送到服务器
 *//*

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        String time = formatter.format(new Date());
        sb.append("\n" + time + "----");
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);

        return sb.toString();
    }
}
*/
