package com.runkun.lbsq.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BaseAcitivity;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.view.PromptDialog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import feifei.project.util.L;
import feifei.project.util.ToastUtil;

public class UpdateManager
{

    private static final int GET_VERSION = 0;
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    private final String savePath = Environment.getExternalStorageDirectory()
            .getPath() + "/download";

    private ProgressBar progressBar;
    private TextView progressTextView;
    private Dialog downloadDialog;

    private boolean cancelUpdate = false;
    private int progress;
    private Map<String, String> versionMap;

    private final Context context;

    public UpdateManager(Context context)
    {
        this.context = context;
        ((BaseAcitivity) context).dialogInit();
    }

    private final Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case GET_VERSION:
                    int versionCode = getVersionCode(context);
                    int serverVersionCode = Integer.valueOf(versionMap
                            .get("version"));
                    if (serverVersionCode > versionCode)
                    {
                        showNoticeDialog(versionMap.get("info"),
                                versionMap.get("must").equals("true") ? true
                                        : false);
                    } else
                    {
                        // ToastUtil.toast(context, "已是最新版无需升级");
                        ((CancelUpdate) context).cancelUpdate();
                    }
                    break;
                case DOWNLOAD:
                    progressBar.setProgress(progress);
                    progressTextView.setText("正在下载……" + progress);
                    break;
                case DOWNLOAD_FINISH:
                    // todo
                    SharedPreferences share = context.getSharedPreferences(MyConstant.FILE_NAME,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = share.edit();
                    editor.remove(MyConstant.KEY_MEMBERID);
                    editor.remove(MyConstant.KEY_ADDCON);
                    editor.remove(MyConstant.KEY_ADDCONTACT);
                    editor.remove(MyConstant.KEY_ADDMOBILE);
                    editor.remove(MyConstant.KEY_ADDREMARK);
                    editor.apply();
                    installApk();
                    break;
                case -3:
                    ToastUtil.toast (context, String.valueOf (msg.obj));
                    break;
            }
        }
    };

    public void checkUpdate()
    {
        ((BaseAcitivity) context).dialogProgress((Activity) context,
                Tools.getStr(context, R.string.LOADING));
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    InputStream is = new URL(MyConstant.APP_VERSION_FILE_PATH)
                            .openStream();
                    versionMap = getServerVersionMap(is);
                    if (null != is)
                    {
                        is.close();
                    }
                    handler.sendEmptyMessage(GET_VERSION);
                    ((BaseAcitivity) context).dialogDismiss();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    L.l (e.getMessage ());
                    Tools.dialog((Activity) context, "当前网络不稳定，请重试", false, new onButtonClick()
                    {
                        @Override
                        public void buttonClick()
                        {
                            ((Activity) context).finish();
                        }
                    });
                    ((BaseAcitivity) context).dialogDismiss();
                }
            }
        }).start();
    }

    private int getVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
            Tools.toast(context, e.getMessage());
            ((BaseAcitivity) context).dialogDismiss();
        }
        return versionCode;
    }

    private Map<String, String> getServerVersionMap(InputStream is)
            throws Exception
    {
        Map<String, String> versionMap = new HashMap<String, String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++)
        {
            Node childNode = childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element childElement = (Element) childNode;
                if ("version".equals(childElement.getNodeName()))
                {
                    versionMap.put("version", childElement.getFirstChild()
                            .getNodeValue());
                } else if (("name".equals(childElement.getNodeName())))
                {
                    versionMap.put("name", childElement.getFirstChild()
                            .getNodeValue());
                } else if (("url".equals(childElement.getNodeName())))
                {
                    versionMap.put("url", childElement.getFirstChild()
                            .getNodeValue());
                } else if (("must".equals(childElement.getNodeName())))
                {
                    versionMap.put("must", childElement.getFirstChild()
                            .getNodeValue());
                } else if (("info".equals(childElement.getNodeName())))
                {
                    versionMap.put("info", childElement.getFirstChild()
                            .getNodeValue());
                }
            }
        }
        return versionMap;
    }

    private void showNoticeDialog(String info, final boolean must)
    {
        final PromptDialog pDialog = PromptDialog.create(context, "软件更新",
                info, PromptDialog.TYPE_CONFIRM_CANCEL);
        pDialog.setCancelable(false);
        pDialog.setConfirmButton("更新", new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showDownloadDialog(must);
                pDialog.dismiss();
            }

        }).setCancelButton(must ? "退出" : "稍后更新", new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (must)
                {
                    ((Activity) context).finish();
                } else
                {
                    // context.startActivity(new Intent(context,
                    // MainActivity.class));
                    ((CancelUpdate) context).cancelUpdate();
                }
                pDialog.dismiss();
            }

        }).show();


//        AlertDialog.Builder builder = new Builder(context);
//        builder.setTitle("软件更新");
//        builder.setMessage(info);
//        builder.setCancelable(false);
//        builder.setPositiveButton("更新", new OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.dismiss();
//                showDownloadDialog(must);
//            }
//        });
//        builder.setNegativeButton(must ? "退出" : "稍后更新", new OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.dismiss();
//                if (must)
//                {
//                    ((Activity) context).finish();
//                } else
//                {
//                    // context.startActivity(new Intent(context,
//                    // MainActivity.class));
//                    ((CancelUpdate) context).cancelUpdate();
//                }
//
//            }
//        });
//        Dialog noticeDialog = builder.create();
//        noticeDialog.show();
    }

    private void showDownloadDialog(final boolean must)
    {
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("正在下载");
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.update_progress, null);
        progressTextView = (TextView) v.findViewById(R.id.update_text);
        progressBar = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                cancelUpdate = true;
                if (must)
                {
                    ((Activity) context).finish();
                } else
                {
                    ((CancelUpdate) context).cancelUpdate();
                }
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        new DownloadApkThread().start();
    }

    private class DownloadApkThread extends Thread
    {

        @Override
        public void run()
        {
            try
            {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED))
                {
                    URL url = new URL(versionMap.get("url"));
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    File fileDir = new File(savePath);
                    if (!fileDir.exists())
                    {
                        fileDir.mkdirs();
                    }
                    File apkFile = new File(savePath, versionMap.get("name"));
                    if (!apkFile.exists())
                    {
                        apkFile.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        handler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            handler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);
                    fos.close();
                    is.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = -3;
                msg.obj = "下载出现异常:" + e.getMessage();
                handler.sendMessage(msg);
            }
            downloadDialog.dismiss();
        }

    }

    private void installApk()
    {
        File apkfile = new File(savePath, versionMap.get("name"));
        if (!apkfile.exists())
        {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(apkfile),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public interface CancelUpdate
    {
        void cancelUpdate();
    }
}