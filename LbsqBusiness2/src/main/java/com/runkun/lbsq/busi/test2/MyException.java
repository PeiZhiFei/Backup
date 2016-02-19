package com.runkun.lbsq.busi.test2;/*
package project.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import feifei.library.R;

*/
/**
 * 显示崩溃界面用toast，上传到服务器，并重新启动，3秒内全部完成
 * MyException.getInstance().init(this);
 * 在基类里面需要这样写才能有界面
 * MyException.getInstance().setContext(this);
 * 捕捉全局异常
 *//*

public class MyException implements UncaughtExceptionHandler
{
    private static MyException myException = new MyException();
    private Context context;
    private final DateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
    // 用来存储设备信息和异常信息
    private final Map<String, String> infos = new HashMap<String, String>();

    // String path = FileUtil.getRootPath() + "log/";

    public Context getContext()
    {
        return context;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    private MyException()
    {
    }

    //这里重新启动自己
    private void fin(Context context)
    {
        // TODO: 这里需要传入一个首类
        Intent intent = new Intent(context, MyWebView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.zoom_enter,
                R.anim.zoom_exit);
        ((Activity) context).finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public synchronized static MyException getInstance()
    {
        // if (myException == null) {
        // myException = new MyException();
        // }
        return myException;
    }

    public void init()
    {
        Thread.setDefaultUncaughtExceptionHandler(myException);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        showDialog();
        collectCrash(context);
        String s = saveCrashInfo2File(ex);
        if (NetworkUtil.isNetworkAvailable(context))
        {
            updateException(s);
        } else
        {
            //没网的时候延迟1秒关闭，这里用到了多个线程
            new Thread()
            {
                @Override
                public void run()
                {
                    Looper.prepare();
                    new Handler().postDelayed(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            fin(context);
                        }
                    }, 1000);
                    Looper.loop();
                }
            }.start();
        }
    }

    // TODO: 这里需要自定义一个错误的友好界面,这里用的是toast，不是dialog
    private void showDialog()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                Toast toast = new Toast(context.getApplicationContext());
                toast.setView(LayoutInflater.from(
                        context.getApplicationContext()).inflate(
                        R.layout.toast_layout, null));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                // TS.toast(context.getApplicationContext(), "这样的bug都被你发现了");
                Looper.loop();
            }
        }.start();
    }

    //保存错误信息到文件中，返回文件名称,便于将文件传送到服务器
    private String saveCrashInfo2File(Throwable ex)
    {
        StringBuffer sb = new StringBuffer();
        String time = formatter.format(new Date());
        sb.append("\n" + time + "----");
        for (Map.Entry<String, String> entry : infos.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        // sb.append(result);
        //todo 这里加了设备信息
        sb.append(result + mDeviceCrashInfo);
        //todo 这个是发送邮件给自己
        // SendUtil.sendMail(result);
        // try {
        // String fileName = "exception.log";
        // if (Environment.getExternalStorageState().equals(
        // Environment.MEDIA_MOUNTED)) {
        // File dir = new File(path);
        // if (!dir.exists()) {
        // dir.mkdirs();
        // }
        // FileOutputStream fos = new FileOutputStream(path + fileName,
        // true);
        // fos.write(sb.toString().getBytes());
        // fos.close();
        // }
        // return fileName;
        // } catch (Exception e) {
        // LogUtil.log("写文件的时候发生了错误" + e);
        // }
        // return null;
        return sb.toString();
    }

    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";

    //搜集设备的信息
    public void collectCrash(Context ctx)
    {
        try
        {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null)
            {
                mDeviceCrashInfo.put(VERSION_NAME,
                        pi.versionName == null ? "not set" : pi.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, "" + pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e)
        {
            L.l("Error while collect package info");
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), "" + field.get(null));
                L.l(field.getName() + " : " + field.get(null));
            } catch (Exception e)
            {
                L.l("Error while collect crash info");
            }
        }
    }
}
*/
