package com.runkun.lbsq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.SplashActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;

public class MyExceptionDialog implements UncaughtExceptionHandler
{
    private static MyExceptionDialog myException = new MyExceptionDialog();
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

    private MyExceptionDialog()
    {
    }

    private void updateException(final String s2)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("member_id", ConfigUtil
                        .readString (context, MyConstant.KEY_MEMBERID, "-2")));
                params.add(new BasicNameValuePair("text", "11版本3.8" + s2));

                String s = null;
                try
                {
                    HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
                            params);
                    // URL使用基本URL即可，其中不需要加参数
                    HttpPost httpPost = new HttpPost(MyConstant.API_BASE_URL
                            + "mobileinfo");
                    // 将请求体内容加入请求中
                    httpPost.setEntity(requestHttpEntity);
                    // 需要客户端对象来发送请求
                    HttpClient httpClient = new DefaultHttpClient();
                    // 请求超时
                    httpClient.getParams().setParameter(
                            CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
                    // 读取超时
                    httpClient.getParams().setParameter(
                            CoreConnectionPNames.SO_TIMEOUT, 3000);
                    // 发送请求
                    HttpResponse response = httpClient.execute(httpPost);
                    // 显示响应
                    if (null == response)
                    {
                        return;
                    }
                    HttpEntity httpEntity = response.getEntity();
                    InputStream inputStream = httpEntity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String result = "";
                    String line = "";
                    while (null != (line = reader.readLine()))
                    {
                        result += line;
                    }
                    s = result;
                    JSONObject jsonData = new JSONObject(s);
                    String code = jsonData.getString("code");
                    if (code.equals("200"))
                    {
                    } else if (code.equals("202"))
                    {
                    } else if (code.equals("404"))
                    {
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    fin(context);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    fin(context);
                }
                fin(context);
            }

        }.start();

    }

    private void fin(Context context)
    {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.zoom_enter,
                R.anim.zoom_exit);
        ((Activity) context).finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * @return
     * @notice 同步方法，以免单例多线程环境下出现异常
     */
    public synchronized static MyExceptionDialog getInstance()
    {
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
        String s = saveCrashInfo2File(ex);
        if (Tools.isNetworkAvailable(context))
        {
            updateException(s);
        } else
        {

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
        L.l (s);
    }

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
                        R.layout.dialog_error, null));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                // ToastUtil
                // .toast(context.getApplicationContext(), "这样的bug都被你发现了");
                // Tools.dialogError(context);
                Looper.loop();
            }
        }.start();
    }

    /**
     * @param ex
     * @return
     * @notice 保存错误信息到文件中，返回文件名称,便于将文件传送到服务器
     */
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
        sb.append(result);

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
}
